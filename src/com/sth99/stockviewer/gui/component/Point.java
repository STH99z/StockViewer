package com.sth99.stockviewer.gui.component;

import com.sth99.stockviewer.util.MathUtil;

/**
 * 简单的点模型
 * Created by STH99 on 2016/12/17.
 */
public class Point {
    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0d, 0d);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public double getDistance(Point p) {
        return Math.sqrt(MathUtil.pow(x - p.x, 2) + MathUtil.pow(y - p.y, 2));
    }

    public Point translate(Point p) {
        this.x += p.x;
        this.y += p.y;
        return this;
    }

    public Point add(Point p) {
        return new Point(x + p.x, y + p.y);
    }

    public Point subtract(Point p) {
        return new Point(x - p.x, y - p.y);
    }

    public Point multiply(double scale) {
        return new Point(x * scale, y * scale);
    }

    public Point multiply2(double xScale, double yScale) {
        return new Point(x * xScale, y * yScale);
    }

    public Point divide(double scale) {
        return new Point(x / scale, y / scale);
    }

    public Point toAbsPoint() {
        return new Point(Math.abs(x), Math.abs(y));
    }
}
