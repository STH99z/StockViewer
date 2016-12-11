package com.sth99.stockviewer.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TitledPane paneStockList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paneStockList.setText("waht?");
        ArrayList<Objects> list = new ArrayList<>();
        List<Objects> list2 = list;
    }
}
