package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.gui.ControlledStage;
import com.sth99.stockviewer.gui.StageController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by STH99 on 2016/12/22.
 */
public class LoginStageController implements Initializable, ControlledStage {
    private StageController stageController;
    @FXML
    private BorderPane mainPane;

    @Override
    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
