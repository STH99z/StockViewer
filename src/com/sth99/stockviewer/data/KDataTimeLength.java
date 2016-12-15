package com.sth99.stockviewer.data;

/**
 * Created by STH99 on 2016/12/15.
 * 数据时长
 */
public enum KDataTimeLength {
//    样例
//
//    分时图
//    http://data.gtimg.cn/flashdata/hushen/minute/sz000001.js?maxage=110&0.28163905744440854
//    五天分时图
//    http://data.gtimg.cn/flashdata/hushen/4day/sz/sz000002.js?maxage=43200
//    日k
//    http://data.gtimg.cn/flashdata/hushen/latest/daily/sz000002.js?maxage=43201
//    周K
//    http://data.gtimg.cn/flashdata/hushen/latest/weekly/sz000002.js?maxage=43201
//    月k
//    http://data.gtimg.cn/flashdata/hushen/monthly/sz000002.js?maxage=43201

    minute("minute"),
    fourDay("4day"),
    daily("daily"),
    weekly("weekly"),
    monthly("monthly");

    KDataTimeLength(String toUrlTag) {
        this.toUrlTag = toUrlTag;
    }

    private String toUrlTag;

    public String toUrlTag() {
        return toUrlTag;
    }

    public String toUrlPrePart() {
        switch (this) {
            case minute:
                return "http://data.gtimg.cn/flashdata/hushen/minute/";
            case fourDay:
                return "http://data.gtimg.cn/flashdata/hushen/4day/";
            case daily:
                return "http://data.gtimg.cn/flashdata/hushen/latest/daily/";
            case monthly:
                return "http://data.gtimg.cn/flashdata/hushen/latest/weekly/";
            case weekly:
                return "http://data.gtimg.cn/flashdata/hushen/monthly/";
        }
        return "";
    }
}