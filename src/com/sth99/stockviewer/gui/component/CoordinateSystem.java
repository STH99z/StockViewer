package com.sth99.stockviewer.gui.component;

import javafx.scene.canvas.GraphicsContext;

/**
 * 简单的坐标系统
 * Created by STH99 on 2016/12/17.
 */
public class CoordinateSystem implements IDrawable {
    Rectangle graphArea;
    /**
     * 图像上零点的坐标，多数情况应该在graphArea中
     */
    Point zeroPoint;
    /**
     * xy轴的缩放
     */
    Point scaleXY;
    private static final boolean DEBUG = true;

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
     * 就是绘制一下这个坐标系，debug用。
     *
     * @param g2d
     * @param system 填写null就ok。只是为了和接口保持一致。
     */
    public void draw(GraphicsContext g2d, CoordinateSystem system) {
        drawFrame(g2d);
        drawZeroPoint(g2d);
        drawZeroPointLine(g2d);
        drawDebugText(g2d);
    }

    private void drawDebugText(GraphicsContext g2d) {
        if (!DEBUG)
            return;
        g2d.fillText(graphArea.toString(), graphArea.left, graphArea.top + 18d);
        g2d.fillText(zeroPoint.toString(), graphArea.left, graphArea.top + 36d);
        g2d.fillText(scaleXY.toString(), graphArea.left, graphArea.top + 54d);
    }

    private void drawZeroPoint(GraphicsContext g2d) {
        g2d.strokeOval(zeroPoint.x - 2, zeroPoint.y - 2, 4, 4);
//        g2d.strokeLine(zeroPoint.x - 3, zeroPoint.y, zeroPoint.x + 3, zeroPoint.y);
//        g2d.strokeLine(zeroPoint.x, zeroPoint.y - 3, zeroPoint.x, zeroPoint.y + 3);
//        g2d.strokeLine(zeroPoint.x - 2, zeroPoint.y - 2, zeroPoint.x + 2, zeroPoint.y + 2);
//        g2d.strokeLine(zeroPoint.x + 2, zeroPoint.y - 2, zeroPoint.x - 2, zeroPoint.y + 2);
    }

    private void drawZeroPointLine(GraphicsContext g2d) {
        g2d.strokeLine(graphArea.left, zeroPoint.y, graphArea.right, zeroPoint.y);
        g2d.strokeLine(zeroPoint.x, graphArea.top, zeroPoint.x, graphArea.bottom);
    }

    private void drawFrame(GraphicsContext g2d) {
        g2d.strokeRect(graphArea.left, graphArea.top, graphArea.getAbsWidth(), graphArea.getHeight());
    }

    /**
     * 确认3x2的2d衍射变换矩阵，用作逻辑值和显示值的映射
     * 每次绘制用户内容前调用
     *
     * @param g2d
     */
    public void applyTransform(GraphicsContext g2d) {
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
}
