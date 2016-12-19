package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.gui.component.CoordinateSystem;
import com.sth99.stockviewer.gui.component.Candle;
import com.sth99.stockviewer.gui.component.IDrawable;
import com.sth99.stockviewer.gui.component.Rectangle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by STH99 on 2016/12/17.
 */
public class StockCanvas extends Canvas {

    GraphicsContext g2d;
    double canvasWidth, canvasHeight;

    public void setCoordinateSystem(CoordinateSystem coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    CoordinateSystem coordinateSystem;
    static double margin = 50d;

    public StockCanvas(double width, double height) {
        super(width, height);
        canvasWidth = width;
        canvasHeight = height;
        Rectangle graphArea = new Rectangle(margin, margin, canvasWidth - margin, canvasHeight - margin);
        System.out.println(graphArea);
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
        return this.getGraphicsContext2D();
    }

    public void clear() {
        g2d = getG2d();
        g2d.setFill(getColor(200, 200, 200));
        g2d.fillRect(0d, 0d, getWidth(), getHeight());
        drawTest();
    }

    public void drawObject(IDrawable drawable) {
        drawable.draw(g2d, coordinateSystem);
    }

    private void drawTest() {
//        g2d.setFill(Color.GREEN);
//        g2d.setStroke(Color.BLUE);
//        g2d.setLineWidth(5);
//        g2d.strokeLine(40, 10, 10, 40);
//        g2d.fillOval(10, 60, 30, 30);
//        g2d.strokeOval(60, 60, 30, 30);
//        g2d.fillRoundRect(110, 60, 30, 30, 10, 10);
//        g2d.strokeRoundRect(160, 60, 30, 30, 10, 10);
//        g2d.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
//        g2d.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
//        g2d.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
//        g2d.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
//        g2d.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
//        g2d.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
//        g2d.fillPolygon(new double[]{10, 40, 10, 40},
//                new double[]{210, 210, 240, 240}, 4);
//        g2d.strokePolygon(new double[]{60, 90, 60, 90},
//                new double[]{210, 210, 240, 240}, 4);
//        g2d.strokePolyline(new double[]{110, 140, 110, 140},
//                new double[]{210, 210, 240, 240}, 4);

        g2d.setLineWidth(1d);
        g2d.setFill(getColor(0, 0, 0));
        g2d.setStroke(getColor(0, 0, 0));

        g2d.setFont(Font.font("Consolas", 18));
        g2d.fillText("w=" + canvasWidth + " h=" + canvasHeight, 1d, 19d);
    }

    private Color getColor(int r, int g, int b) {
        return Color.color(r / 255d, g / 255d, b / 255d);
    }

    private Color getColor(double r, double g, double b) {
        return Color.color(r, g, b);
    }
}
