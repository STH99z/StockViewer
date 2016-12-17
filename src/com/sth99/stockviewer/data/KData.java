package com.sth99.stockviewer.data;

import com.sth99.stockviewer.util.MathAdv;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 股票的K线数据
 * Created by STH99 on 2016/12/15.
 */
public class KData extends WebData {
    ////fixme 需要一个factory？
    private static final String dataFilterPat = "(\\d{2})(\\d{2})(\\d{2}) ([0-9.]+) ([0-9.]+) ([0-9.]+) ([0-9.]+) (\\d+)\\\\n\\\\";
    static final String[] dataFilterTag = {"", "y", "m", "d", "open", "close", "highest", "lowest", "???"};
    static final int GROUP_OPEN = 4;
    static final int GROUP_CLOSE = 5;
    static final int GROUP_HIGHEST = 6;
    static final int GROUP_LOWEST = 7;
    static final int maxAge = 43200;

    static final Pattern dataFilter = Pattern.compile(dataFilterPat);

    StockCodeData code;
    KDataTimeLength timeLength;
    ArrayList<String> filteredData;
    int lastFindIndex = -1;

    /**
     * 创建一个5日分时数据
     *
     * @param code
     * @throws MalformedURLException
     */
    public KData(StockCodeData code) throws MalformedURLException {
        this(code, KDataTimeLength.daily);
    }

    /**
     * 创建一个K线数据
     *
     * @param code       股票代码
     * @param timeLength 时间长度
     * @throws MalformedURLException
     */
    public KData(StockCodeData code, KDataTimeLength timeLength) throws MalformedURLException {
        super(timeLength.toUrlPrePart() + code.getFullCode() + ".js?maxage=" + maxAge);
        this.code = code;
        this.timeLength = timeLength;
        processData();
    }

    private void processData() {
        Matcher matcher = dataFilter.matcher(getData());
        filteredData = new ArrayList<>();
        while (matcher.find()) {
            filteredData.add(matcher.group(0));
        }
    }

    @Deprecated
    public String getFilteredData() {
        String s = "";
        for (String d : filteredData) {
            s += d + "\n";
        }
        return s;
    }

    private int findIndex(Date date) {
        for (int i = 0; i < filteredData.size(); i++) {
            if (isSameDate(date, filteredData.get(i))) {
                lastFindIndex = i;
                return lastFindIndex;
            }
        }
        lastFindIndex = -1;
        return lastFindIndex;
    }

    private boolean isSameDate(Date date, String dataStr) {
        String y = dataStr.substring(0, 2);
        String m = dataStr.substring(2, 4);
        String d = dataStr.substring(4, 6);
        return y.equals(MathAdv.getYear(date)) &&
                m.equals(MathAdv.getMonth(date)) &&
                d.equals(MathAdv.getDay(date));
    }

    public double getOpen(Date date) {
        return getElement(date, GROUP_OPEN);
    }

    public double getClose(Date date) {
        return getElement(date, GROUP_CLOSE);
    }

    public double getHighest(Date date) {
        return getElement(date, GROUP_HIGHEST);
    }

    public double getLowest(Date date) {
        return getElement(date, GROUP_LOWEST);
    }

    private double getElement(Date date, int index) {
        if (lastFindIndex == -1)
            return 0d;
        if (!isSameDate(date, filteredData.get(lastFindIndex))) {
            findIndex(date);
            if (lastFindIndex == -1)
                return 0d;
        }
        Matcher matcher = dataFilter.matcher(filteredData.get(lastFindIndex));
        matcher.find();
        return Double.parseDouble(matcher.group(index));
    }

}

