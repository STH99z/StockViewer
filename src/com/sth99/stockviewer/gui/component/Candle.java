package com.sth99.stockviewer.gui.component;

import com.sth99.stockviewer.data.KData;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * K线图中一个单一一个蜡烛
 * Created by STH99 on 2016/12/17.
 */
public class Candle implements IDrawable {
    double open, close, highest, lowest;
    double x;
    static Color strokeColor = Color.color(0d, 0d, 0d);
    static Color riseColor = Color.color(.8d, 0d, 0d);
    static Color dropColor = Color.color(0d, .8d, 0d);

    public Candle(double open, double close, double highest, double lowest, double x) {
        this.open = open;
        this.close = close;
        this.highest = highest;
        this.lowest = lowest;
        this.x = x;
    }

    public Candle(KData kData, double x) {
        this(kData.open, kData.close, kData.highest, kData.lowest, x);
    }

    public static void setStrikeColor(Color c) {
        strokeColor = c;
    }

    public static void setFillColor(Color rise, Color drop) {
        riseColor = rise;
        dropColor = drop;
    }

    @Override
    public void draw(GraphicsContext g2d, CoordinateSystem system) {
        double o, c, h, l, x;
        x = system.locateX(this.x);
        o = system.locateY(open);
        c = system.locateY(close);
        h = system.locateY(highest);
        l = system.locateY(lowest);
        g2d.setStroke(strokeColor);
        g2d.strokeLine(x, h, x, l);
        System.out.println(open + " " + close);
        if (open > close) {
            g2d.setFill(riseColor);
            g2d.fillRect(x - 3, o, 5, c - o);
        } else if (open < close) {
            g2d.setFill(dropColor);
            g2d.fillRect(x - 3, c, 5, o - c);
        }
    }
}
