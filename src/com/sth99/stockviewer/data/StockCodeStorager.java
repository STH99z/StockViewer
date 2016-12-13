package com.sth99.stockviewer.data;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sth99.stockviewer.util.MathAdv;

/**
 * Created by STH99 on 2016/12/8.
 */
public class StockCodeStorager {
    ArrayList<StockCodeData> codeList;

    public StockCodeStorager() {
        codeList = new ArrayList<>(4096);
        getWebData();
    }

    private void add(StockCodeData data) {
        codeList.add(data);
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

    public ArrayList<StockCodeData> getStockList() {
        return codeList;
    }

    public ArrayList<StockCodeData> getStockListFrom(int rangeStart, int rangeEnd) {
        ArrayList<StockCodeData> list = new ArrayList<>();
        int i = 0;
        while (i < codeList.size() && codeList.get(i).getNumCode() < rangeStart)
            i++;
        while (i < codeList.size() && codeList.get(i).getNumCode() < rangeEnd) {
            list.add(codeList.get(i));
            i++;
        }
        while (i < codeList.size() && codeList.get(i).getBelong() == StockBelong.sh)
            i++;
        while (i < codeList.size() && codeList.get(i).getNumCode() < rangeStart)
            i++;
        while (i < codeList.size() && codeList.get(i).getNumCode() < rangeEnd) {
            list.add(codeList.get(i));
            i++;
        }
        return list;
    }

    /**
     * 外部调用获取股票代码表
     * <p>
     * 用于股票代码查找
     * 传入“610”会返回610000到610999的StockCodeData
     *
     * @param rangeStart 开始的字符串
     * @return
     */
    public ArrayList<StockCodeData> getStockListFrom(String rangeStart) {
        if (rangeStart == null || rangeStart.equals(""))
            return getStockList();
        Pattern nonDigit = Pattern.compile("\\D");
        Matcher matcher = nonDigit.matcher(rangeStart);
        if (matcher.find())
            return null;
        if (rangeStart.length() == 6) {
            int code = Integer.parseInt(rangeStart);
            return getStockListFrom(code, code);
        }
        int start, end;
        int need = 6 - rangeStart.length();
        start = Integer.parseInt(rangeStart) * MathAdv.pow(10, need);
        end = start + MathAdv.pow(10, need) - 1;
        return getStockListFrom(start, end);
    }
}
