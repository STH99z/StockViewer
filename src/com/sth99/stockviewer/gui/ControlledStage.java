package com.sth99.stockviewer.gui;

import javafx.beans.value.ChangeListener;
import javafx.stage.Stage;

/**
 * Created by CatScan on 2016/6/23.
 * http://blog.csdn.net/nthack5730/article/details/51901593
 */
public interface ControlledStage {
    public void setStageController(StageController stageController);

    public void setShowingChangeListener(Stage stage);
}
