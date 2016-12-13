package com.sth99.stockviewer.util;

/**
 * Created by STH99 on 2016/12/11.
 */
public class MathAdv {
    public static int pow(int base, int times) {
        int result = 1;
        while (times-- > 0)
            result *= base;
        return result;
    }
}
