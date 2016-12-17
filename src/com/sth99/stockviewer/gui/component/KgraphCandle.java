package com.sth99.stockviewer.gui.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * K线图中一个单一一个蜡烛
 * Created by STH99 on 2016/12/17.
 */
public class KgraphCandle {
    double open, close, highest, lowest;
    static Color stroke, fill;

    public KgraphCandle(double open, double close, double highest, double lowest) {
        this.open = open;
        this.close = close;
        this.highest = highest;
        this.lowest = lowest;
    }

    public static void setStrikeColor(Color c) {
        stroke = c;
    }

    public static void setFillColor(Color c) {
        fill = c;
    }

    public void draw(GraphicsContext g2d, CoordinateSystem system, double x) {
        double o, c, h, l;
        x = system.locateX(x);
        o = system.locateY(open);
        c = system.locateY(close);
        h = system.locateY(highest);
        l = system.locateY(lowest);
        g2d.strokeLine(x, h, x, l);
        //fixme 方向颜色判断
        g2d.fillRect(x - 3, c, 5, o - c);
    }
}
