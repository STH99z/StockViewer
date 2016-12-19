package com.sth99.stockviewer.gui.component;

import com.sth99.stockviewer.data.KData;
import com.sth99.stockviewer.data.KDataSet;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * Created by STH99 on 2016/12/19.
 */
public class KChart extends Chart implements IDrawable {
    ArrayList<KData> kDataList;
    ArrayList<Candle> candles;
    private double xStart, xEnd, yStart, yEnd;

    public KChart(ArrayList<KData> kDataList) {
        this.kDataList = kDataList;
        candles = new ArrayList<>(kDataList.size());
        xStart = 0d;
        xEnd = (double) kDataList.size();
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
        drawCheck();
        for (Candle candle : candles) {
            candle.draw(g2d, system);
        }
    }


}
