package com.sth99.stockviewer.gui.component;

import com.sth99.stockviewer.data.KData;
import com.sth99.stockviewer.util.MathUtil;
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
    static Color riseColor = MathUtil.getColor(217, 26, 42);
    static Color dropColor = MathUtil.getColor(23, 166, 58);

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
        if (open < close) {
            g2d.setFill(riseColor);
        } else if (open > close) {
            g2d.setFill(dropColor);
        }
        system.fillRect(g2d, x - 0.4, open, x + 0.4, close);
        system.drawVLine(g2d, x, highest, lowest);
    }
}
