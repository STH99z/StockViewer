package com.sth99.stockviewer.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by STH99 on 2016/12/22.
 */
public class RegisterStageController implements Initializable, ControlledStage {
    private StageController stageController;
    @FXML
    private BorderPane mainPane;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordAgainField;
    @FXML
    private Button RegisterButton;
    @FXML
    private Button BackButton;
    @FXML
    private Label tipLabel;

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
