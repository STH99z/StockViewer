package com.sth99.stockviewer.data;

import java.io.Serializable;

/**
 * Created by STH99 on 2016/12/8.
 */
public abstract class Data implements Serializable{
    public abstract boolean equals(Data data);
    public abstract String toString();
}
