package com.sth99.stockviewer.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {
    private StageController stageController;
    public static String mainViewID = "mainWindow";
    public static String mainViewRes = "mainWindow.fxml";
    public static String loginViewID = "loginWindow";
    public static String loginViewRes = "loginWindow.fxml";
    public static String registerViewID = "registerWindow";
    public static String registerViewRes = "registerWindow.fxml";


    @Override
    public void start(Stage primaryStage) throws Exception {
//        @Deprecated
//        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
//        primaryStage.setTitle("STOCK VIEWER");
//        primaryStage.setScene(new Scene(root, 1200, 800));
//        primaryStage.show();
//        new Thread(mainController.postInitializer).start();

        stageController = new StageController();

        stageController.setPrimaryStage("primaryStage", primaryStage);

        stageController.loadStage(loginViewID, loginViewRes);
        stageController.loadStage(mainViewID, mainViewRes);
        stageController.loadStage(registerViewID, registerViewRes);

        stageController.getStage(loginViewID).setTitle("登录");
        stageController.getStage(loginViewID).setResizable(false);
        stageController.getStage(registerViewID).setTitle("注册");
        stageController.getStage(registerViewID).setResizable(false);
        stageController.getStage(mainViewID).setTitle("STOCK VIEWER");


        stageController.setStage(loginViewID);
//        stageController.setStage(mainViewID);
    }

    public String relatedPath(String path) {
        return getClass().getResource(path).getFile();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
