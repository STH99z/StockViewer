package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.data.KData;
import com.sth99.stockviewer.data.KDataTimeLength;
import com.sth99.stockviewer.data.StockCodeData;
import com.sth99.stockviewer.data.StockCodeStorager;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TitledPane stockListPane;
    @FXML
    private Label stockListLable;
    @FXML
    private TextField stockListField;
    @FXML
    private ListView<StockCodeData> stockListView;
    @FXML
    private AnchorPane consolePane;
    private TextArea consoleTextArea;
    private TextField consoleTextField;

    StockCodeStorager stockCodeStorager;
    StockCodeData currentStockCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializer.run();
        new Thread(postInitializer).start();
    }

    private void updateCodeListView() {
        String text = stockListField.getText();
        stockListView.setItems(FXCollections.observableArrayList(stockCodeStorager.getStockListFrom(text)));
    }

    private void createAbsAnchor(AnchorPane pane, Node child, double up, double left, double right, double down) {
        pane.setTopAnchor(child, up);
        pane.setLeftAnchor(child, left);
        pane.setRightAnchor(child, right);
        pane.setBottomAnchor(child, down);
    }

    public void consoleOut(String text) {
        consoleTextArea.appendText("\t" + text);
    }

    private ChangeListener<String> codeFieldChangeListener = (observable, oldValue, newValue) -> updateCodeListView();
    private ChangeListener<StockCodeData> codeListSelectListener = (observable, oldValue, newValue) -> {
        currentStockCode = newValue;
        if (currentStockCode == null)
            return;
        try {
            KData kData = new KData(currentStockCode, KDataTimeLength.daily);
            consoleTextArea.clear();
            consoleTextArea.appendText(kData.getData());
            consoleTextArea.appendText(currentStockCode.getFullCode() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    /**
     * 初始化器，在initialize中调用（同线程）。
     */
    private Runnable initializer = () -> {
        consoleTextArea = new TextArea("test");
        consoleTextField = new TextField("console in");
        consolePane.getChildren().addAll(consoleTextArea, consoleTextField);
        createAbsAnchor(consolePane, consoleTextArea, 0d, 0d, 0d, 23d);
        consolePane.setBottomAnchor(consoleTextField, 0d);
        consolePane.setLeftAnchor(consoleTextField, 0d);
        consolePane.setRightAnchor(consoleTextField, 0d);
    };
    /**
     * 后初始化器，包含网络操作，这个时候先显示界面。后初始化放在与initialize不同的线程。
     */
    private Runnable postInitializer = () -> {
        stockCodeStorager = new StockCodeStorager();
        stockListField.textProperty().addListener(codeFieldChangeListener);
        stockListView.getSelectionModel().selectedItemProperty().addListener(codeListSelectListener);
        updateCodeListView();
    };
}
