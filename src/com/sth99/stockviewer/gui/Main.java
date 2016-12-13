package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.data.StockCodeData;
import com.sth99.stockviewer.data.StockCodeStorager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("STOCK VIEWER");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }

    static Consumer<Object> c = System.out::println;

    public static void main(String[] args) {
        launch(args);
    }
}
