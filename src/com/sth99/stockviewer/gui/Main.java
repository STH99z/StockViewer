package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.data.StockCodeStorager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("STOCK VIEWER");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
//        StockCodeStorager stockCodeStorager = new StockCodeStorager();
//        stockCodeStorager.showFastIndexer();
    }
}
