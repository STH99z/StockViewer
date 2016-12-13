package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.data.StockCodeData;
import com.sth99.stockviewer.data.StockCodeStorager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class Controller implements Initializable {
    @FXML
    private TitledPane paneStockList;
    @FXML
    private TextField codeField;
    @FXML
    private ListView codeListView;

    StockCodeStorager stockCodeStorager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread postInitThread = new Thread(postInitialize);
        postInitThread.start();
    }

    private synchronized void updateCodeListView() {
        String text = codeField.getText();
        codeListView.setItems(FXCollections.observableArrayList(stockCodeStorager.getStockListFrom(text)));
    }

    private Consumer<Object> o = System.out::println;

    private ChangeListener<String> codeFieldChangeListener = (observable, oldValue, newValue) -> updateCodeListView();

    private Runnable postInitialize = new Runnable() {
        @Override
        public void run() {
            stockCodeStorager = new StockCodeStorager();
            codeField.textProperty().addListener(codeFieldChangeListener);
            updateCodeListView();
        }
    };
}
