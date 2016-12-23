package com.sth99.stockviewer.data;

import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by STH99 on 2016/12/23.
 */
public class FundamentalData extends WebData {
    public static final int DATA_COUNT = 32;
    static final String dataFilterPat = "var hq_str_\\w{2}\\d{6}=\\\"(\\S+)\\\"";
    static final Pattern dataFilter = Pattern.compile(dataFilterPat);
    public static final String[] dataHeader = {
            "股票名字",
            "今日开盘价",
            "昨日收盘价",
            "当前价格",
            "今日最高价",
            "今日最低价",
            "竞买价",      //即“买一”报价
            "竞卖价",      //即“卖一”报价
            "成交的股票数", //由于股票交易以一百股为基本单位，所以在使用时，通常把该值除以一百
            "成交金额",     //单位为“元”，为了一目了然，通常以“万元”为成交金额的单位，所以通常把该值除以一万
            "买一申请",     //x股 即(int+1)x/100手；
            "买一报价",
            "买二",
            "买二",
            "买三",
            "买三",
            "买四",
            "买四",
            "买五",
            "买五",
            "卖一申报3100股，即31手；",
            "卖一报价",
            "卖二",
            "卖二",
            "卖三",
            "卖三",
            "卖四",
            "卖四",
            "卖五",
            "卖五",
            "日期",
            "时间"};

    StockCodeData code;
    String[] values;

    public String[] getValues() {
        return values;
    }

    public FundamentalData(StockCodeData code) throws MalformedURLException {
        super("http://hq.sinajs.cn/list=" + code.getFullCode());
        this.code = code;
        this.setCharsetName("GBK");
        processData();
    }

    public void processData() {
        Matcher matcher = dataFilter.matcher(getData());
        matcher.find();
        String toSplit = matcher.group(1);
        values = toSplit.split(",");
        if (values.length != DATA_COUNT - 1)
            System.out.println("Data process failed. " + values.length + " Data:" + getData());
    }
}
