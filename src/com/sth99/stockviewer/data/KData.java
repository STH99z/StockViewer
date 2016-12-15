package com.sth99.stockviewer.data;

import java.net.MalformedURLException;

/**
 * 股票的K线数据
 * Created by STH99 on 2016/12/15.
 */
public class KData extends WebData {
    ////fixme 需要一个factory？
    static final int maxAge = 43200;

    /**
     * 创建一个5日分时数据
     * @param code
     * @throws MalformedURLException
     */
    public KData(StockCodeData code) throws MalformedURLException {
        super("http://data.gtimg.cn/flashdata/hushen/4day/sz/" + code.getFullCode() +
                ".js?maxage=" + maxAge);
    }

    /**
     * 创建一个K线数据
     * @param code 股票代码
     * @param timeLength 时间长度
     * @throws MalformedURLException
     */
    public KData(StockCodeData code, KDataTimeLength timeLength) throws MalformedURLException {
        super(timeLength.toUrlPrePart() + code.getFullCode() + ".js?maxage=" + maxAge);
    }
}

