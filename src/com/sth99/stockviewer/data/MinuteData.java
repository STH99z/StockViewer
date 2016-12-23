package com.sth99.stockviewer.data;

import com.sth99.stockviewer.util.MathUtil;

import java.util.Date;

/**
 * 每分钟的股票价格数据体
 * Created by STH99 on 2016/12/23.
 */
public class MinuteData extends Data {
    /**
     * 两位（十进制）的时间数据
     */
    public short hour, minute;
    /**
     * 股票价格
     */
    public double price, magicalNumber;

    public MinuteData(short hour, short minute, double price, double magicalNumber) {
        this.hour = hour;
        this.minute = minute;
        this.price = price;
        this.magicalNumber = magicalNumber;
    }

    @Override
    public String toString() {
        return "MinuteData{" +
                "hour=" + hour +
                ", minute=" + minute +
                ", price=" + price +
                ", magicalNumber=" + magicalNumber +
                '}';
    }

    @Override
    public boolean equals(Data o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MinuteData that = (MinuteData) o;

        if (hour != that.hour) return false;
        if (minute != that.minute) return false;
        if (Double.compare(that.price, price) != 0) return false;
        return Double.compare(that.magicalNumber, magicalNumber) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) hour;
        result = 31 * result + (int) minute;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(magicalNumber);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
