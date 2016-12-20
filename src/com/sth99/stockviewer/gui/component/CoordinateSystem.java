package com.sth99.stockviewer.gui.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 简单的坐标系统
 * Created by STH99 on 2016/12/17.
 */
public class CoordinateSystem implements IDrawable {
    private static final boolean DEBUG = true;

    Rectangle graphArea;
    /**
     * 图像上零点的坐标，多数情况应该在graphArea中
     */
    Point zeroPoint;
    /**
     * xy轴的缩放
     */
    Point scaleXY;
    Color backgroundColor;
    Color backgroundLineColor;
    Color backgroundTextColor;

    public CoordinateSystem(Rectangle graphArea) {
        this.graphArea = graphArea;
        locateSystem(new Point(graphArea.left, graphArea.top), new Point(1d, 1d));
    }

    public CoordinateSystem(double top, double left, double right, double bottom) {
        this(new Rectangle(top, left, right, bottom));
    }

    public void locateSystem(Point zeroPoint, Point scaleXY) {
        this.zeroPoint = zeroPoint;
        this.scaleXY = scaleXY;
    }

    public void locateSystem(double xStart, double xEnd, double yStart, double yEnd) {
        Point scaleXY = new Point(graphArea.getAbsWidth() / (xEnd - xStart), graphArea.getAbsHeight() / (yEnd - yStart));
        Point zeroPoint = new Point(graphArea.left - xStart * scaleXY.x, graphArea.bottom + yStart * scaleXY.y);
        locateSystem(zeroPoint, scaleXY);
    }

    private double reverseY(double y) {
        return graphArea.getAbsHeight() - y;
    }

    /**
     * 把基于数值的点转换为坐标内线性变换后的对应点
     *
     * @param point
     * @return
     */
    public Point locatePoint(Point point) {
        return new Point(locateX(point.x), locateY(point.y));
    }

    public double locateX(double x) {
        return zeroPoint.x + x * scaleXY.x;
    }

    public double locateY(double y) {
        return zeroPoint.y - y * scaleXY.y;
    }

    /**
     * 根据屏幕点坐标返回坐标实际值
     *
     * @param point 画布上的点坐标，左上角0,0
     * @return
     */
    public Point retrivePoint(Point point) {
        return new Point(retriveX(point.x), retriveY(point.y));
    }

    public double retriveX(double x) {
        return (x - zeroPoint.x) / scaleXY.x;
    }

    public double retriveY(double y) {
        return (zeroPoint.y - y) / scaleXY.y;
    }

    public void setColors(Color backgroundColor, Color backgroundLineColor, Color backgroundTextColor) {
        this.backgroundColor = backgroundColor;
        this.backgroundLineColor = backgroundLineColor;
        this.backgroundTextColor = backgroundTextColor;
    }

    /**
     * 就是绘制一下这个坐标系，debug用。
     *
     * @param g2d
     * @param system 填写null就ok。只是为了和接口保持一致。
     */
    public void draw(GraphicsContext g2d, CoordinateSystem system) {
        drawFrame(g2d);
        drawZeroPoint(g2d);
        drawDebugText(g2d);
    }

    private void drawDebugText(GraphicsContext g2d) {
        if (!DEBUG)
            return;
        g2d.fillText(graphArea.toString(), graphArea.left, graphArea.top + 12);
        g2d.fillText(zeroPoint.toString(), graphArea.left, graphArea.top + 24);
        g2d.fillText(scaleXY.toString(), graphArea.left, graphArea.top + 36);
    }

    private void drawZeroPoint(GraphicsContext g2d) {
        g2d.strokeOval(zeroPoint.x - 2, zeroPoint.y - 2, 4, 4);
    }

    private void drawFrame(GraphicsContext g2d) {
        g2d.setFill(backgroundColor);
        g2d.fillRect((int) graphArea.left, (int) graphArea.top, (int) graphArea.getAbsWidth(), (int) graphArea.getAbsHeight());
        g2d.setFill(backgroundLineColor);
        g2d.fillRect((int) graphArea.left, (int) graphArea.top, 1, (int) graphArea.getAbsHeight());
        g2d.fillRect((int) graphArea.left, (int) graphArea.top, (int) graphArea.getAbsWidth(), 1);
        g2d.fillRect((int) graphArea.right, (int) graphArea.top, 1, (int) graphArea.getAbsHeight());
        g2d.fillRect((int) graphArea.left, (int) graphArea.bottom, (int) graphArea.getAbsWidth(), 1);
        for (double x = graphArea.left; x < graphArea.right; x += 50) {
            g2d.setFill(backgroundLineColor);
            g2d.fillRect((int) x, (int) graphArea.top, 1, (int) graphArea.getAbsHeight());
            g2d.setFill(backgroundTextColor);
            g2d.fillText(String.format("%.2f", retriveX(x)), x, graphArea.bottom + 12);
        }
        for (double y = graphArea.top; y < graphArea.bottom; y += 20) {
            g2d.setFill(backgroundLineColor);
            g2d.fillRect((int) graphArea.left, (int) y, (int) graphArea.getAbsWidth(), 1);
            g2d.setFill(backgroundTextColor);
            g2d.fillText(String.format("%.2f", retriveY(y)), graphArea.left - 32, y + 6);
        }
        g2d.setFill(backgroundTextColor);
        //TODO add label drawing
    }

    /**
     * 确认3x2的2d衍射变换矩阵，用作逻辑值和显示值的映射
     * 每次绘制用户内容前调用
     *
     * @param g2d
     * @deprecated
     */
    void applyTransform(GraphicsContext g2d) {
        double[][] transform = new double[2][3];
        transform[0][0] = scaleXY.x;
        transform[1][1] = scaleXY.y;
        transform[0][2] = zeroPoint.x;
        transform[1][2] = -zeroPoint.y;
        g2d.transform(transform[0][0],
                transform[1][0],
                transform[0][1],
                transform[1][1],
                transform[0][2],
                transform[1][2]);
    }

    public void drawVLine(GraphicsContext g2d, double x, double y1, double y2) {
        int ix = (int) locateX(x);
        int iy1 = (int) locateY(y1);
        int iy2 = (int) locateY(y2);
        int m = iy1 < iy2 ? iy1 : iy2;
        int h = Math.abs(iy1 - iy2);
        g2d.fillRect(ix, m, 1, h);
    }

    public void drawHLine(GraphicsContext g2d, double y, double x1, double x2) {
        int iy = (int) locateY(y);
        int ix1 = (int) locateX(x1);
        int ix2 = (int) locateX(x2);
        int m = ix1 < ix2 ? ix1 : ix2;
        int w = Math.abs(ix1 - ix2);
        g2d.fillRect(m, iy, w, 1);
    }

    public void fillRect(GraphicsContext g2d, double x1, double y1, double x2, double y2) {
        int ix1 = (int) locateX(x1);
        int iy1 = (int) locateY(y1);
        int ix2 = (int) locateX(x2);
        int iy2 = (int) locateY(y2);
        int w = Math.abs(ix1 - ix2);
        int h = Math.abs(iy1 - iy2);
        int sx = ix1 < ix2 ? ix1 : ix2;
        int sy = iy1 < iy2 ? iy1 : iy2;
        g2d.fillRect(sx, sy, w, h);
    }
}
