package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.gui.ControlledStage;
import com.sth99.stockviewer.gui.StageController;
import com.sth99.stockviewer.user.User;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by STH99 on 2016/12/22.
 */
public class LoginStageController implements Initializable, ControlledStage {
    public static final String PASSWORD_WRONG = "密码不正确";
    public static final String USER_NOT_EXIST = "用户名不存在";
    private StageController stageController;
    @FXML
    private BorderPane mainPane;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label tipLabel;

    @Override
    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    private EventHandler<ActionEvent> loginButtonHandler = (event) -> {
        UserStorager userStorager = UserStorager.get();
        if (!userStorager.nameExist(userNameField.getText())) {
            tipLabel.setTextFill(Color.RED);
            tipLabel.setText(USER_NOT_EXIST);
            return;
        }
        User user = userStorager.get(userNameField.getText());
        if (!user.getPassword().equals(passwordField.getText())) {
            tipLabel.setTextFill(Color.RED);
            tipLabel.setText(PASSWORD_WRONG);
            return;
        }
        userStorager.setCurrentUser(user);
        stageController.setStage(MainApp.mainViewID, MainApp.loginViewID);
    };

    private EventHandler<ActionEvent> regiterButtonHandler = (event) -> {
        stageController.setStage(MainApp.registerViewID, MainApp.loginViewID);
    };

    private ChangeListener<Boolean> stageShowListener = (observable, oldValue, newValue) -> {
        if (newValue == true) {
            userNameField.setText("");
            passwordField.setText("");
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
            registerButton.setOnAction(regiterButtonHandler);
            loginButton.setOnAction(loginButtonHandler);
            loginButton.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) loginButton.fire();
            });
            passwordField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) loginButton.fire();
            });
            UserStorager.get().readFromFile("users.dat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
