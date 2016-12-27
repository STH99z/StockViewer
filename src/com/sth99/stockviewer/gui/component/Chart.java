package com.sth99.stockviewer.gui.component;

import javafx.scene.canvas.GraphicsContext;

/**
 * 图表的基类
 * Created by STH99 on 2016/12/19.
 */
public abstract class Chart implements IDrawable {
    private boolean chartLocated = false;

    public void locateChart(CoordinateSystem system, double xStart, double xEnd, double yStart, double yEnd) {
        system.locateSystem(xStart, xEnd, yStart, yEnd);
        chartLocated = true;
    }

    void drawCheck() throws ChartNotLocatedException {
        if (!chartLocated)
            throw new ChartNotLocatedException();
    }

    abstract void drawChartBackground(GraphicsContext g2d, CoordinateSystem system);
}
