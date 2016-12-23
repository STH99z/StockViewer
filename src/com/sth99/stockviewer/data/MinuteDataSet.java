package com.sth99.stockviewer.data;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日股票价格数据
 * Created by STH99 on 2016/12/23.
 */
public class MinuteDataSet extends WebData {
    private static final String dateFilterPat = "date:(\\d{2})(\\d{2})(\\d{2})\\\\n\\\\";
    private static final String dataFilterPat = "(\\d{2})(\\d{2}) ([0-9.]+) (\\d+)\\\\n\\\\";
    static final Pattern dateFilter = Pattern.compile(dateFilterPat);
    static final Pattern dataFilter = Pattern.compile(dataFilterPat);

    public short year, month, day;
    StockCodeData code;
    ArrayList<MinuteData> minuteDataList;

    public MinuteDataSet(StockCodeData code) throws MalformedURLException {
        super("http://data.gtimg.cn/flashdata/hushen/minute/" + code.getFullCode() + ".js");
        this.code = code;
        processData();
    }

    private void processData() {
        Matcher dateMatcher = dateFilter.matcher(getData());
        if (dateMatcher.find()) {
            year = Short.valueOf(dateMatcher.group(1));
            month = Short.valueOf(dateMatcher.group(2));
            day = Short.valueOf(dateMatcher.group(3));
        }
        Matcher matcher = dataFilter.matcher(getData());
        minuteDataList = new ArrayList<>();
        while (matcher.find()) {
            short[] s = new short[2];
            double[] d = new double[2];
            for (int i = 0; i < s.length; i++) {
                s[i] = Short.valueOf(matcher.group(i + 1));
            }
            for (int i = 0; i < d.length; i++) {
                d[i] = Double.valueOf(matcher.group(i + 1 + s.length));
            }
            minuteDataList.add(new MinuteData(s[0], s[1], d[0], d[1]));
        }
    }

    public ArrayList<MinuteData> getMinuteDataList() {
        return minuteDataList;
    }
}
