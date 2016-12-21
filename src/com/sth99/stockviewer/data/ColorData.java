package com.sth99.stockviewer.data;

import com.sth99.stockviewer.util.MathUtil;
import javafx.scene.paint.Color;

/**
 * 易保存的颜色数据
 * Created by STH99 on 2016/12/21.
 */
public class ColorData extends Data {
    int r, g, b, a;

    public Color getColor() {
        return MathUtil.getColor(r, g, b, a);
    }

    public ColorData(Color color) {
        this.r = (int) (color.getRed() * 255);
        this.g = (int) (color.getGreen() * 255);
        this.b = (int) (color.getBlue() * 255);
        this.a = (int) (color.getOpacity() * 255);
    }

    public ColorData(double r, double g, double b) {
        this.r = (int) (r * 255);
        this.g = (int) (g * 255);
        this.b = (int) (b * 255);
        this.a = 255;
    }

    public ColorData(double r, double g, double b, double a) {
        this.r = (int) (r * 255);
        this.g = (int) (g * 255);
        this.b = (int) (b * 255);
        this.a = (int) (a * 255);
    }

    public ColorData(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public ColorData(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }

    @Override
    public String toString() {
        return "ColorData{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }

    @Override
    public boolean equals(Data o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColorData colorData = (ColorData) o;

        if (r != colorData.r) return false;
        if (g != colorData.g) return false;
        if (b != colorData.b) return false;
        return a == colorData.a;
    }

    @Override
    public int hashCode() {
        int result = r;
        result = 31 * result + g;
        result = 31 * result + b;
        result = 31 * result + a;
        return result;
    }
}
