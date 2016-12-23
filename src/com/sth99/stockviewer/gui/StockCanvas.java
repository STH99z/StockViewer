package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.gui.component.CoordinateSystem;
import com.sth99.stockviewer.gui.component.Candle;
import com.sth99.stockviewer.gui.component.IDrawable;
import com.sth99.stockviewer.gui.component.Rectangle;
import com.sth99.stockviewer.util.MathUtil;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;

/**
 * Created by STH99 on 2016/12/17.
 */
public class StockCanvas extends Canvas {
    static double margin = 50d;

    double canvasWidth, canvasHeight;
    GraphicsContext g2d;
    CoordinateSystem coordinateSystem;
    Color backgroundColor = MathUtil.getColor(240, 247, 252);
    Color backgroundLineColor = MathUtil.getColor(217, 230, 239);
    Color backgroundTextColor = MathUtil.getColor(130, 162, 196);
    Color textColor = MathUtil.getColor(133, 161, 239);

    public void setCoordinateSystem(CoordinateSystem coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
        this.coordinateSystem.setColors(backgroundColor, backgroundLineColor, backgroundTextColor);
    }

    public void setCoordinateSystem(double margin) {
        setCoordinateSystem(new CoordinateSystem(margin, margin, canvasWidth - margin, canvasHeight - margin));
    }

    public void updateCoordinateSystem() {
        setCoordinateSystem(margin);
    }

    public StockCanvas(double width, double height) {
        super(width, height);
        canvasWidth = width;
        canvasHeight = height;
        Rectangle graphArea = new Rectangle(margin, margin, canvasWidth - margin, canvasHeight - margin);
//        System.out.println(graphArea);
        setCoordinateSystem(new CoordinateSystem(graphArea));
    }

    public void setCanvasWidth(double width) {
        setWidth(width);
        canvasWidth = width;
    }

    public void setCanvasHeight(double height) {
        setHeight(height);
        canvasHeight = height;
    }

    public GraphicsContext getG2d() {
        GraphicsContext g2dTemp;
        g2dTemp = this.getGraphicsContext2D();
        g2dTemp.setFontSmoothingType(null);
        g2dTemp.setGlobalBlendMode(null);
        return g2dTemp;
    }

    public void clear() {
        g2d = getG2d();
        g2d.setFill(Color.WHITE);
        g2d.fillRect(0d, 0d, getWidth(), getHeight());
        g2d.setFill(textColor);
        g2d.setFont(Font.font("Consolas", 12));
        g2d.setFontSmoothingType(null);
    }

    public void drawObject(IDrawable drawable) {
        drawable.draw(g2d, coordinateSystem);
    }

}
