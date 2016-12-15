package com.sth99.stockviewer.data;

import java.io.Serializable;

/**
 * 最基础的数据类，想到什么通用的功能在往上加
 * Created by STH99 on 2016/12/8.
 */
public abstract class Data implements Serializable{
    public abstract boolean equals(Data data);
    public abstract String toString();
}
