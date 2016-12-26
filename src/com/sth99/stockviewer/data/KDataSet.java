package com.sth99.stockviewer.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 股票的K线数据
 * Created by STH99 on 2016/12/15.
 */
public class KDataSet extends WebData {
    private static final String dataFilterPat = "(\\d{2})(\\d{2})(\\d{2}) ([0-9.]+) ([0-9.]+) ([0-9.]+) ([0-9.]+) (\\d+)\\\\n\\\\";
    static final String[] dataFilterTag = {"", "y", "m", "d", "open", "close", "highest", "lowest", "???"};
    static final int maxAge = 43200;

    static final Pattern dataFilter = Pattern.compile(dataFilterPat);

    StockCodeData code;
    KDataTimeLength timeLength;
    ArrayList<KData> KDataList;
    int lastFindIndex = -1;

    /**
     * 创建一个5日分时数据
     *
     * @param code
     * @throws MalformedURLException
     */
    public KDataSet(StockCodeData code) throws MalformedURLException, IOException {
        this(code, KDataTimeLength.daily);
    }

    /**
     * 创建一个K线数据
     *
     * @param code       股票代码
     * @param timeLength 时间长度
     * @throws MalformedURLException
     */
    public KDataSet(StockCodeData code, KDataTimeLength timeLength) throws MalformedURLException, IOException {
        super(timeLength.toUrlPrePart() + code.getFullCode() + ".js?maxage=" + maxAge);
        this.code = code;
        this.timeLength = timeLength;
        processData();
    }

    private void processData() throws IOException {
        Matcher matcher = dataFilter.matcher(getData());
        KDataList = new ArrayList<>();
        while (matcher.find()) {
            short[] s = new short[3];
            double[] d = new double[4];
            for (int i = 0; i < s.length; i++) {
                s[i] = Short.valueOf(matcher.group(i + 1));
            }
            for (int i = 0; i < d.length; i++) {
                d[i] = Double.valueOf(matcher.group(i + 1 + s.length));
            }
            KDataList.add(new KData(s[0], s[1], s[2], d[0], d[1], d[2], d[3]));
        }
    }

    private int findIndex(Date date) {
        for (int i = 0; i < KDataList.size(); i++) {
            if (KDataList.get(i).isSameDate(date)) {
                lastFindIndex = i;
                return lastFindIndex;
            }
        }
        lastFindIndex = -1;
        return lastFindIndex;
    }

    public ArrayList<KData> getKDataList() {
        return KDataList;
    }
}

