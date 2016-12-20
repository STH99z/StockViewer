package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.data.StockCodeData;
import com.sth99.stockviewer.data.StockCodeStorager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class Main extends Application {

    static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("STOCK VIEWER");
        primaryStage.setScene(new Scene(root, 1200, 800, false, SceneAntialiasing.DISABLED));
        new Thread(controller.postInitializer).start();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
