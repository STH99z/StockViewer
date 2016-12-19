package com.sth99.stockviewer.gui.component;

/**
 * Created by STH99 on 2016/12/19.
 */
public class ChartNotLocatedException extends RuntimeException {
    public ChartNotLocatedException() {
        super("图表还没有被定位");
    }
}
