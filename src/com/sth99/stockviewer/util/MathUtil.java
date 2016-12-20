package com.sth99.stockviewer.util;

import javafx.scene.paint.Color;

import java.util.Date;

/**
 * Created by STH99 on 2016/12/11.
 */
public class MathUtil {
    public static int pow(int base, int times) {
        int result = 1;
        while (times-- > 0)
            result *= base;
        return result;
    }

    public static double pow(double base, int times) {
        double result = 1d;
        while (times-- > 0)
            result *= base;
        return result;
    }

    public static String getYear(Date date) {
        return ((date.getYear() + 1900) + "").substring(2, 4);
    }

    public static short getYearShort(Date date) {
        return (short) ((date.getYear() + 1900) / 100);
    }

    public static String getMonth(Date date) {
        return ((date.getMonth() + 1) + "");
    }

    public static short getMonthShort(Date date) {
        return (short) (date.getMonth() + 1);
    }

    public static String getDay(Date date) {
        return ((date.getDate()) + "");
    }

    public static short getDayShort(Date date) {
        return ((short) date.getDate());
    }

    public static double limitMin(double value, double min) {
        return value > min ? value : min;
    }

    public static double limitMax(double value, double max) {
        return value < max ? value : max;
    }

    public static Color getColor(int r, int g, int b) {
        return Color.color(r / 255d, g / 255d, b / 255d);
    }

    public static Color getColor(double r, double g, double b) {
        return Color.color(r, g, b);
    }
}
