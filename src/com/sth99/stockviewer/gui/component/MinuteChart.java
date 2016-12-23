package com.sth99.stockviewer.gui.component;

import com.sth99.stockviewer.data.KData;
import com.sth99.stockviewer.data.MinuteData;
import com.sth99.stockviewer.data.MinuteDataSet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * 日股票价格走势图
 * Created by STH99 on 2016/12/23.
 */
public class MinuteChart extends Chart implements IDrawable {
    ArrayList<MinuteData> minuteDataList;
    private double xStart, xEnd, yStart, yEnd;

    public MinuteChart(MinuteDataSet minuteDataSet) {
        this(minuteDataSet.getMinuteDataList());
    }

    public MinuteChart(ArrayList<MinuteData> minuteDataList) {
        this.minuteDataList = minuteDataList;
        xStart = -1;
        xEnd = minuteDataList.size();
        yStart = minuteDataList.get(0).price;
        yEnd = minuteDataList.get(0).price;
        for (int i = 0; i < minuteDataList.size(); i++) {
            //统计处理
            if (minuteDataList.get(i).price < yStart)
                yStart = minuteDataList.get(i).price;
            if (minuteDataList.get(i).price > yEnd)
                yEnd = minuteDataList.get(i).price;
        }
    }

    @Override
    public void draw(GraphicsContext g2d, CoordinateSystem system) {
        locateChart(system, xStart, xEnd, yStart, yEnd);
        system.draw(g2d, null);
        drawChartBackground(g2d, system);
        drawCheck();
        g2d.setFill(Color.GRAY);
        for (int i = 0; i < minuteDataList.size() - 1; i++) {
            double p1 = minuteDataList.get(i).price;
            double p2 = minuteDataList.get(i + 1).price;
            system.drawLine(g2d, i, p1, i + 1, p2);
        }
    }

    @Override
    void drawChartBackground(GraphicsContext g2d, CoordinateSystem system) {
        for (double x = system.graphArea.left; x <= system.graphArea.right; x += 40) {
            g2d.setFill(system.backgroundLineColor);
            g2d.fillRect((int) x, (int) system.graphArea.top, 1, (int) system.graphArea.getAbsHeight());
            g2d.setFill(system.backgroundTextColor);
            MinuteData minuteData = null;
            try {
                minuteData = minuteDataList.get((int) system.retriveX(x));
                String text = String.format("%02d:%02d", minuteData.hour, minuteData.minute);
                g2d.fillText(text, x, system.graphArea.bottom + 12);
            } catch (IndexOutOfBoundsException e) {
                int ei = (int) system.retriveX(x);
                if (ei != -1)
                    System.out.println();
                else {
                    minuteData = minuteDataList.get(0);
                    String text = String.format("%02d%02d", minuteData.hour, minuteData.minute);
                    g2d.fillText(text, x, system.graphArea.bottom + 12);
                }
            }
        }
        for (double y = yStart; y <= yEnd + 0.009d; y += 0.01d) {
            g2d.setFill(system.backgroundLineColor);
            g2d.fillRect((int) system.graphArea.left, (int) system.locateY(y), (int) system.graphArea.getAbsWidth(), 1);
            g2d.setFill(system.backgroundTextColor);
            g2d.fillText(String.format("%.2f", y), system.graphArea.left - 35, system.locateY(y) + 6);
        }
    }
}
