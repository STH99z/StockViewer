package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.user.UserStorager;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by STH99 on 2016/12/22.
 */
public class RegisterStageController implements Initializable, ControlledStage {
    public static final String NAME_DUPLICATE = "用户名已存在";
    public static final String PASSWORD_NOT_SAME = "两次输入的密码不一致";
    public static final String REGISTER_OK = "注册成功";
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
    private Button registerButton;
    @FXML
    private Button backButton;
    @FXML
    private Label tipLabel;

    @Override
    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    private EventHandler<ActionEvent> backButtonHandler = event -> {
        stageController.setStage(MainApp.loginViewID, MainApp.registerViewID);
    };

    private EventHandler<ActionEvent> registerButtonHandler = event -> {
        UserStorager userStorager = UserStorager.get();
        if (userStorager.nameExist(userNameField.getText())) {
            tipLabel.setTextFill(Color.RED);
            tipLabel.setText(NAME_DUPLICATE);
            return;
        }
        if (!passwordField.getText().equals(passwordAgainField.getText())) {
            tipLabel.setTextFill(Color.RED);
            tipLabel.setText(PASSWORD_NOT_SAME);
            passwordField.setText("");
            passwordAgainField.setText("");
            return;
        }
        userStorager.register(userNameField.getText(), passwordField.getText());
        tipLabel.setTextFill(Color.BLACK);
        tipLabel.setText(REGISTER_OK);
        try {
            userStorager.saveToFile("users.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    private ChangeListener<String> userNameChangeListener = (observable, oldValue, newValue) -> {
        if (UserStorager.get().nameExist(userNameField.getText())) {
            tipLabel.setTextFill(Color.RED);
            tipLabel.setText(NAME_DUPLICATE);
            return;
        }
        tipLabel.setText("");
    };

    private ChangeListener<Boolean> stageShowListener = (observable, oldValue, newValue) -> {
        if (newValue == true) {
            userNameField.setText("");
            passwordField.setText("");
            passwordAgainField.setText("");
            tipLabel.setText("");
        }
    };

    @Override
    public void setShowingChangeListener(Stage stage) {
        stage.showingProperty().addListener(stageShowListener);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            backButton.setOnAction(backButtonHandler);
            registerButton.setOnAction(registerButtonHandler);
            registerButton.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) registerButton.fire();
            });
            passwordAgainField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) registerButton.fire();
            });
            userNameField.textProperty().addListener(userNameChangeListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
