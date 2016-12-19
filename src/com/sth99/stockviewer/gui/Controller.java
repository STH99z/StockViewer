package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.data.KDataSet;
import com.sth99.stockviewer.data.KDataTimeLength;
import com.sth99.stockviewer.data.StockCodeData;
import com.sth99.stockviewer.data.StockCodeStorager;
import com.sth99.stockviewer.gui.component.KChart;
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
    private BorderPane mainPane;
    @FXML
    private TitledPane stockListPane;
    @FXML
    private BorderPane stockListBorderPane;
    @FXML
    private Label stockListLable;
    @FXML
    private TextField stockListField;
    @FXML
    private ListView<StockCodeData> stockListView;
    @FXML
    private BorderPane selfChosenPane;
    @FXML
    private AnchorPane consolePane;
    private TextArea consoleTextArea;
    private TextField consoleTextField;
    @FXML
    private AnchorPane mainAnchorPane;
    private StockCanvas mainCanvas;

    private static final double TITLEDPANE_HEADER_HEIGHT = 22d;

    StockCodeStorager stockCodeStorager;
    StockCodeData currentStockCode;
    KDataSet kDataSet;
    double frameWidth, frameHeight;
    private long lastPropertyCahngeTiming = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.controller = this;
        initializer.run();
    }

    private void reLayout() {
        //check width and height has both changed
        if (frameWidth < 1200d)
            frameWidth = 1200d;
        if (frameHeight < 800d)
            frameHeight = 800d;
        stockListBorderPane.setPrefSize(200d, (frameHeight - 200d) / 2d - TITLEDPANE_HEADER_HEIGHT);
        selfChosenPane.setPrefSize(200d, (frameHeight - 200d) / 2d - TITLEDPANE_HEADER_HEIGHT - 2);
        mainCanvas.setCanvasWidth(frameWidth - 400d);
        mainCanvas.setCanvasHeight(frameHeight - 200d);
        updateCanvas();
    }

    private void updateCodeListView() {
        String text = stockListField.getText();
        stockListView.setItems(FXCollections.observableArrayList(stockCodeStorager.getStockListFrom(text)));
    }

    private void updateConsoleArea(StockCodeData newValue) {
        currentStockCode = newValue;
        if (currentStockCode == null)
            return;
        try {
            kDataSet = new KDataSet(currentStockCode, KDataTimeLength.daily);
            consoleTextArea.clear();
            consoleTextArea.appendText(kDataSet.getKDataList().size() + "\n");
            consoleTextArea.appendText(currentStockCode.getFullCode() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCanvas() {
        mainCanvas.clear();
        if (kDataSet != null) {
            KChart kChart = new KChart(kDataSet);
            mainCanvas.drawObject(kChart);
            System.out.println("KCdraw called");
        }
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
        updateConsoleArea(newValue);
        updateCanvas();
    };
    private ChangeListener<Number> widthResizeListener = (observable, oldValue, newValue) -> {
        frameWidth = newValue.doubleValue();
        reLayout();
    };
    private ChangeListener<Number> heightResizeListener = (observable, oldValue, newValue) -> {
        frameHeight = newValue.doubleValue();
        reLayout();
    };

    /**
     * 初始化器，在initialize中调用（同线程）。
     */
    public Runnable initializer = () -> {
        //Main Pane Initialize
        frameWidth = 1200d;
        frameHeight = 800d;

        //Bottom Area Initialize
        consoleTextArea = new TextArea("test");
        consoleTextField = new TextField("console in");
        consolePane.getChildren().addAll(consoleTextArea, consoleTextField);
        createAbsAnchor(consolePane, consoleTextArea, 0d, 0d, 0d, 23d);
        consolePane.setBottomAnchor(consoleTextField, 0d);
        consolePane.setLeftAnchor(consoleTextField, 0d);
        consolePane.setRightAnchor(consoleTextField, 0d);

        //Center Area Initialize
        mainCanvas = new StockCanvas(frameWidth - 400d, frameHeight - 200d);
        mainAnchorPane.getChildren().add(mainCanvas);
        createAbsAnchor(mainAnchorPane, mainCanvas, 0d, 0d, 0d, 0d);

        //Main Pane Listener
        mainPane.widthProperty().addListener(widthResizeListener);
        mainPane.heightProperty().addListener(heightResizeListener);
    };
    /**
     * 后初始化器，包含网络操作，这个时候先显示界面。后初始化放在与initialize不同的线程。
     */
    public Runnable postInitializer = () -> {
        stockCodeStorager = new StockCodeStorager();
        stockListField.textProperty().addListener(codeFieldChangeListener);
        stockListView.getSelectionModel().selectedItemProperty().addListener(codeListSelectListener);
        updateCodeListView();
    };
}
