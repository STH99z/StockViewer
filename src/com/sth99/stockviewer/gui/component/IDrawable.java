package com.sth99.stockviewer.gui.component;

import javafx.scene.canvas.GraphicsContext;

/**
 * 表示可以被画出来
 * Created by STH99 on 2016/12/18.
 */
public interface IDrawable {
    void draw(GraphicsContext g2d, CoordinateSystem system);
}
