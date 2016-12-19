package com.sth99.stockviewer.util;

import java.util.Objects;

/**
 * Created by STH99 on 2016/12/8.
 */
public class Tuple<A, B> {
    public A a;
    public B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
