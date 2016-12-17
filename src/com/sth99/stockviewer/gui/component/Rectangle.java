package com.sth99.stockviewer.gui.component;

/**
 * 简单的长方体模型
 * Created by STH99 on 2016/12/17.
 */
public class Rectangle {
    public double top, left, right, bottom;

    public Point getTopLeft() {
        return new Point(left, top);
    }

    public Point getTopRight() {
        return new Point(right, top);
    }

    public Point getBottomLeft() {
        return new Point(left, bottom);
    }

    public Point getBottomRight() {
        return new Point(right, bottom);
    }

    public Rectangle(double top, double left, double right, double bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    public Rectangle(Point topLeft, Point size) {
        this(topLeft.y, topLeft.x, topLeft.x + size.x, topLeft.y + size.y);
    }

    public Rectangle(Point topLeft, Point rightBottom, boolean nothing) {
        this(topLeft.y, topLeft.x, rightBottom.x, rightBottom.y);
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "top=" + top +
                ", left=" + left +
                ", right=" + right +
                ", bottom=" + bottom +
                '}';
    }

    public boolean isReverse() {
        return right < left || bottom < top;
    }

    public void flip() {
        double temp = left;
        left = right;
        right = temp;
        temp = bottom;
        bottom = top;
        top = temp;
    }

    public Point getCenter() {
        return new Point((left + right) / 2d, (top + bottom) / 2d);
    }

    public double getWidth() {
        return right - left;
    }

    public double getHeight() {
        return bottom - top;
    }

    public double getAbsWidth() {
        return Math.abs(getWidth());
    }

    public double getAbsHeight() {
        return Math.abs(getHeight());
    }

    public boolean intersect(Rectangle r) {
        double halfWidthAdd = getAbsWidth() / 2d + r.getAbsWidth() / 2d;
        double halfHeightAdd = getAbsHeight() / 2d + r.getAbsHeight() / 2d;
        Point deltaCenter = getCenter().subtract(r.getCenter()).toAbsPoint();
        return deltaCenter.x < halfWidthAdd && deltaCenter.y < halfHeightAdd;
    }
}
