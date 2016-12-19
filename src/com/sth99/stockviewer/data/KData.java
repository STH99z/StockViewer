package com.sth99.stockviewer.data;

import java.util.Date;

import com.sth99.stockviewer.util.MathUtil;

/**
 * 单个K线图的数据体
 * Created by STH99 on 2016/12/19.
 */
public class KData extends Data {
    /**
     * 两位（十进制）的时间数据
     */
    public short year, month, day;
    /**
     * 4个关键值
     */
    public double open, close, highest, lowest;

    public KData(short year, short month, short day, double open, double close, double highest, double lowest) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.open = open;
        this.close = close;
        this.highest = highest;
        this.lowest = lowest;
    }

    public KData(Date date, double open, double close, double highest, double lowest) {
        this(MathUtil.getYearShort(date), MathUtil.getMonthShort(date), MathUtil.getDayShort(date),
                open, close, highest, lowest);
    }

    public boolean isSameDate(KData kData) {
        if (year != kData.year) return false;
        if (month != kData.month) return false;
        if (day != kData.day) return false;
        return true;
    }

    public boolean isSameDate(Date date) {
        return year == MathUtil.getYearShort(date) &&
                month == MathUtil.getMonthShort(date) &&
                day == MathUtil.getDayShort(date);
    }

    @Override
    public String toString() {
        return "KData{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", open=" + open +
                ", close=" + close +
                ", highest=" + highest +
                ", lowest=" + lowest +
                '}';
    }

    @Override
    public boolean equals(Data o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KData kData = (KData) o;

        if (year != kData.year) return false;
        if (month != kData.month) return false;
        if (day != kData.day) return false;
        if (Double.compare(kData.open, open) != 0) return false;
        if (Double.compare(kData.close, close) != 0) return false;
        if (Double.compare(kData.highest, highest) != 0) return false;
        return Double.compare(kData.lowest, lowest) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) year;
        result = 31 * result + (int) month;
        result = 31 * result + (int) day;
        temp = Double.doubleToLongBits(open);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(close);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(highest);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lowest);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
