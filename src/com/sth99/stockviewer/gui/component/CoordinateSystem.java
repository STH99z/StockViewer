package com.sth99.stockviewer.gui.component;

import javafx.scene.canvas.GraphicsContext;

/**
 * 简单的坐标系统
 * Created by STH99 on 2016/12/17.
 */
public class CoordinateSystem {
    Rectangle graphArea;
    /**
     * 图像上零点的坐标，多数情况应该在graphArea中
     */
    Point zeroPoint;
    /**
     * xy轴的缩放
     */
    Point scaleXY;

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
        scaleXY = new Point(graphArea.getAbsWidth() / (xEnd - xStart), graphArea.getAbsHeight() / (yEnd - yStart));
        zeroPoint = new Point(graphArea.left - xStart * scaleXY.x, graphArea.bottom + yStart * scaleXY.y);
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

    public void drawSystem(GraphicsContext g2d) {
        g2d.strokeRect(graphArea.left, graphArea.top, graphArea.getAbsWidth(), graphArea.getHeight());
        g2d.strokeLine(zeroPoint.x - 3, zeroPoint.y, zeroPoint.x + 3, zeroPoint.y);
        g2d.strokeLine(zeroPoint.x, zeroPoint.y - 3, zeroPoint.x, zeroPoint.y + 3);
        g2d.strokeLine(zeroPoint.x - 2, zeroPoint.y - 2, zeroPoint.x + 2, zeroPoint.y + 2);
        g2d.strokeLine(zeroPoint.x + 2, zeroPoint.y - 2, zeroPoint.x - 2, zeroPoint.y + 2);
        g2d.fillText(graphArea.toString(), graphArea.left, graphArea.top + 18d);
        g2d.fillText(zeroPoint.toString(), graphArea.left, graphArea.top + 36d);
    }
}
