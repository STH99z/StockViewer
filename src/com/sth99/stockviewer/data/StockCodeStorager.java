package com.sth99.stockviewer.data;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Created by STH99 on 2016/12/8.
 */
public class StockCodeStorager {
    ArrayList<StockCodeData> codeList;
    ArrayList<Tuple<Integer, Integer>> fastIndexer;
    private int lastCode = -1;

    public StockCodeStorager() {
        codeList = new ArrayList<>();
        fastIndexer = new ArrayList<>();
        getWebData();
    }

    private void add(StockCodeData data) {
        codeList.add(data);
        if (lastCode == -1) {
            lastCode = data.getNumCode();
            fastIndexer.add(new Tuple<>(lastCode, codeList.size() - 1));
        } else if (lastCode == data.getNumCode() - 1) {
            lastCode = data.getNumCode();
        } else if (data.getNumCode() % 1000 == 0) {
            //整1000
            lastCode = data.getNumCode();
            fastIndexer.add(new Tuple<>(lastCode, codeList.size() - 1));
        } else {
            //断开
            lastCode = data.getNumCode();
            fastIndexer.add(new Tuple<>(lastCode, codeList.size() - 1));
        }
    }

    private int pow10(int times) {
        int result = 1;
        for (int i = 0; i < times; i++) {
            result *= 10;
        }
        return result;
    }

    private int findStart(String indexString) {
        int end = indexString.length() - 1;
        int index = Integer.parseInt(indexString);
        int start = -1;
        int i = 0;
        index *= pow10(StockCodeData.CODE_LENGTH - indexString.length());
        while (fastIndexer.get(i).a < index) {
            start = fastIndexer.get(i).b;
            i++;
        }
        while (!codeList.get(start).getCode().substring(0, end).equals(indexString)) {
            start++;
        }
        return start;
    }

    private void getWebData() {
        try {
            WebData webData = new WebData("http://quote.eastmoney.com/stocklist.html");
            String pat = "<a target=\"_blank\" href=\"http://quote\\.eastmoney\\.com/(\\S+?)\\.html\">(\\S+?)\\((\\d+?)\\)</a>";
            webData.getData();
            Matcher m = webData.filterData(pat);
            while (m.find()) {
                StockCodeData stockCodeData = new StockCodeData(m.group(2), m.group(1));
                add(stockCodeData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFastIndexer() {
        for (Tuple<Integer, Integer> integerIntegerTuple : fastIndexer) {
            System.out.println(integerIntegerTuple);
        }
    }

    public ArrayList<StockCodeData> getStockCodeData(String indexString) {
        ArrayList<StockCodeData> list = new ArrayList<>();
        if (indexString.length() > 6) {
            return list;
        }
        int indexStart = findStart(indexString);
        while (true) {
            StockCodeData data = codeList.get(indexStart);
            list.add(data);
            if (!data.getCode().substring(0, StockCodeData.CODE_LENGTH - indexString.length()).equals(indexString))
                break;
            indexStart++;
        }
        return list;
    }
}
