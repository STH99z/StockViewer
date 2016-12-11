package com.sth99.stockviewer.data;

/**
 * Created by STH99 on 2016/12/8.
 * 股票代码存储类
 */
public class StockCodeData extends Data {
    String name;
    String code;
    int numCode;
    StockBelong belong;

    public static final int CODE_LENGTH = 6;

    public StockCodeData(String name, String code, StockBelong belong) {
        this.name = name;
        this.code = code;
        this.belong = belong;
        this.numCode = Integer.parseInt(code);
    }

    public StockCodeData(String name, String fullCode) {
        this(name, fullCode.substring(2), StockBelong.valueOf(fullCode.substring(0, 2)));
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getNumCode() {
        return numCode;
    }

    public String getFullCode() {
        return getBelong().toString() + getCode();
    }

    public StockBelong getBelong() {
        return belong;
    }

    @Override
    public boolean equals(Data data) {
        if (!(data instanceof StockCodeData))
            return false;
        StockCodeData d = (StockCodeData) data;
        return getName() == d.getName() &&
                getFullCode() == d.getFullCode();
    }

    @Override
    public String toString() {
        return "StockCodeData{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", belong=" + belong +
                '}';
    }
}

enum StockBelong {
    sh, sz
}