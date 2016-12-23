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
    daily("daily"),//日K
    weekly("weekly"),//周K
    monthly("monthly");//月K

    KDataTimeLength(String toUrlTag) {
        this.toUrlTag = toUrlTag;
    }

    private String toUrlTag;

    public String toUrlTag() {
        return toUrlTag;
    }

    public String toUrlPrePart() {
        switch (this) {
            case daily:
                return "http://data.gtimg.cn/flashdata/hushen/latest/daily/";
            case weekly:
                return "http://data.gtimg.cn/flashdata/hushen/latest/weekly/";
            case monthly:
                return "http://data.gtimg.cn/flashdata/hushen/monthly/";
        }
        return "";
    }
}