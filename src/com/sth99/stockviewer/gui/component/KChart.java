package com.sth99.stockviewer.gui.component;

import com.sth99.stockviewer.data.KData;
import com.sth99.stockviewer.data.KDataSet;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * Created by STH99 on 2016/12/19.
 */
public class KChart extends Chart {
    ArrayList<KData> kDataList;
    ArrayList<Candle> candles;
    private double xStart, xEnd, yStart, yEnd;

    public KChart(ArrayList<KData> kDataList) {
        this.kDataList = kDataList;
        candles = new ArrayList<>(kDataList.size());
        xStart = -1;
        xEnd = kDataList.size();
        yStart = kDataList.get(0).lowest;
        yEnd = kDataList.get(0).highest;
        for (int i = 0; i < kDataList.size(); i++) {
            candles.add(new Candle(kDataList.get(i), (double) i));//fixme 时间的添加
            //统计处理
            if (kDataList.get(i).lowest < yStart)
                yStart = kDataList.get(i).lowest;
            if (kDataList.get(i).highest > yEnd)
                yEnd = kDataList.get(i).highest;
        }
    }

    public KChart(KDataSet kDataSet) {
        this(kDataSet.getKDataList());
    }

    @Override
    public void draw(GraphicsContext g2d, CoordinateSystem system) {
        locateChart(system, xStart, xEnd, yStart, yEnd);
        system.draw(g2d, null);
        drawChartBackground(g2d, system);
        drawCheck();
        for (Candle candle : candles) {
            candle.draw(g2d, system);
        }
    }

    @Override
    void drawChartBackground(GraphicsContext g2d, CoordinateSystem system) {
        for (double x = system.graphArea.left; x <= system.graphArea.right; x += 80) {
            g2d.setFill(system.backgroundLineColor);
            g2d.fillRect((int) x, (int) system.graphArea.top, 1, (int) system.graphArea.getAbsHeight());
            g2d.setFill(system.backgroundTextColor);
            KData kData = null;
            try {
                kData = kDataList.get((int) system.retriveX(x));
                String text = String.format("%02d/%02d/%02d", kData.year, kData.month, kData.day);
                g2d.fillText(text, x, system.graphArea.bottom + 12);
            } catch (IndexOutOfBoundsException e) {
                int ei=(int) system.retriveX(x);
                if(ei!=-1)
                    System.out.println();
                else{
                    kData = kDataList.get(0);
                    String text = String.format("%02d/%02d/%02d", kData.year, kData.month, kData.day);
                    g2d.fillText(text, x, system.graphArea.bottom + 12);
                }
            }
        }
        for (double y = system.graphArea.top; y <= system.graphArea.bottom; y += 20) {
            g2d.setFill(system.backgroundLineColor);
            g2d.fillRect((int) system.graphArea.left, (int) y, (int) system.graphArea.getAbsWidth(), 1);
            g2d.setFill(system.backgroundTextColor);
            g2d.fillText(String.format("%.2f", system.retriveY(y)), system.graphArea.left - 35, y + 6);
        }
    }
    //TODO labels and mouseLocateLine
}
