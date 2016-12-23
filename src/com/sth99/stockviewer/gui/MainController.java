package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.data.*;
import com.sth99.stockviewer.gui.component.KChart;
import com.sth99.stockviewer.gui.component.MinuteChart;
import com.sth99.stockviewer.user.UserStorager;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable, ControlledStage {
    public static final double LIST_PREF_WIDTH = 200d;
    public static final double LIST_SHRINK_HEIGHT = 225d;
    private static final double TITLEDPANE_HEADER_HEIGHT = 22d;
    public static final double CANVAS_SHRINK_WIDTH = 400d;
    public static final int CANVAS_SHRINK_HEIGHT = 254;
    public static final String UID_STIRNG = "UID:";
    public static final String USERNAME_STRING = "用户名:";
    public static final String TAB_TEXT[] = {
            "分时图",
            "日K线图",
            "周K线图",
            "月K线图"
    };
    private StageController stageController;
    @FXML
    private MenuItem uidMenu, userNameMenu;
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
    private TitledPane detailPane;
    @FXML
    private Label nameLabel, currentPriceLabel, yesterdayCloseLabel, todayOpenLabel,
            todayHighestLabel, todayLowestLabel, auctionBuyLabel, auctionSellLabel,
            turnVolumeLabel, turnPriceLabel, dateLabel, timeLabel;
    @FXML
    private VBox buySellVBox;
    private ArrayList<String> originalLabelString;
    private Label[] fundamentalDataLabel;
    @FXML
    private StockCanvas mainCanvas[];
    @FXML
    private TabPane mainTabPane;

    private static final String TODO_STRING = "" +
            "右侧部分完成\n" +
            "下侧部分用图表代替\n";

    StockCodeStorager stockCodeStorager;
    StockCodeData currentStockCode;
    KDataSet[] kDataSet = new KDataSet[3];
    MinuteDataSet minuteDataSet;
    FundamentalData fundamentalData;
    double frameWidth, frameHeight;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializer.run();
    }

    private void reLayout() {
        //check width and height has both changed
        if (frameWidth < 1200d)
            frameWidth = 1200d;
        if (frameHeight < 800d)
            frameHeight = 800d;
        stockListBorderPane.setPrefSize(LIST_PREF_WIDTH, (frameHeight - LIST_SHRINK_HEIGHT) / 2d - TITLEDPANE_HEADER_HEIGHT);
        selfChosenPane.setPrefSize(LIST_PREF_WIDTH, (frameHeight - LIST_SHRINK_HEIGHT) / 2d - TITLEDPANE_HEADER_HEIGHT - 2);
        detailPane.setPrefSize(LIST_PREF_WIDTH, frameHeight - LIST_SHRINK_HEIGHT);
        for (int i = 0; i < mainCanvas.length; i++) {
            mainCanvas[i].setCanvasWidth(frameWidth - CANVAS_SHRINK_WIDTH);
            mainCanvas[i].setCanvasHeight(frameHeight - CANVAS_SHRINK_HEIGHT);
            mainCanvas[i].updateCoordinateSystem();
        }
        updateCanvas();
    }

    private void updateCodeListView() {
        String text = stockListField.getText();
        stockListView.setItems(FXCollections.observableArrayList(stockCodeStorager.getStockListFrom(text)));
    }

    private void updateCurrentStockData(StockCodeData stockCodeData) {
        currentStockCode = stockCodeData;
        try {
            minuteDataSet = new MinuteDataSet(stockCodeData);
            for (int i = 0; i < 3; i++) {
                kDataSet[i] = new KDataSet(stockCodeData, KDataTimeLength.values()[i]);
            }
            fundamentalData = new FundamentalData(stockCodeData);
        } catch (MalformedURLException mue) {
            consoleTextArea.appendText(mue.getMessage() + "\n");
        }
    }

    private void updateConsoleArea(StockCodeData newValue) {
        if (currentStockCode == null)
            return;
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCanvas() {
        for (int i = 0; i < mainCanvas.length; i++) {
            mainCanvas[i].clear();
            if (i == 0) {
                if (minuteDataSet == null)
                    continue;
                MinuteChart minuteChart = new MinuteChart(minuteDataSet);
                mainCanvas[i].drawObject(minuteChart);
                continue;
            }
            if (kDataSet[i - 1] != null) {
                KChart kChart = new KChart(kDataSet[i - 1]);
                mainCanvas[i].drawObject(kChart);
            }
        }
    }

    private void updateDetailPane() {
        if (fundamentalData == null)
            return;
        //显示基本信息 名字Label不用显示"股票名称"四个字
        fundamentalDataLabel[0].setText(fundamentalData.getValues()[0]);
        for (int i = 1; i < FundamentalData.DATA_COUNT; i++) {
            fundamentalDataLabel[i].setText(originalLabelString.get(i) + " " + fundamentalData.getValues()[i]);
        }
    }

    private void updateMenu() {
        if (UserStorager.get().getCurrentUser() == null)
            return;
        uidMenu.setText(UID_STIRNG + UserStorager.get().getCurrentUser().getUid());
        userNameMenu.setText(USERNAME_STRING + UserStorager.get().getCurrentUser().getUserName());
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
        updateCurrentStockData(newValue);
        updateDetailPane();
        updateConsoleArea(newValue);
        updateCanvas();
    };
    private ChangeListener<Number> widthResizeListener = (observable, oldValue, newValue) -> {
        if (frameWidth == 1200d && newValue.doubleValue() < 1200)
            return;
        frameWidth = newValue.doubleValue();
        reLayout();
    };
    private ChangeListener<Number> heightResizeListener = (observable, oldValue, newValue) -> {
        if (frameHeight == 800d && newValue.doubleValue() < 800)
            return;
        frameHeight = newValue.doubleValue();
        reLayout();
    };
    private ChangeListener<Boolean> stageShowingChangeListener = (observable, oldValue, newValue) -> {
        if (newValue == false)
            return;
        updateMenu();
    };

    @Override
    public void setShowingChangeListener(Stage stage) {
        stage.showingProperty().addListener(stageShowingChangeListener);
    }

    /**
     * 后初始化器，包含网络操作，这个时候先显示界面。后初始化放在与initialize不同的线程。
     */
    public Runnable postInitializer = () -> {
        stockCodeStorager = new StockCodeStorager();
        stockListField.textProperty().addListener(codeFieldChangeListener);
        stockListView.getSelectionModel().selectedItemProperty().addListener(codeListSelectListener);
        currentStockCode = new StockCodeData("", "sh600000");
        stockListField.setText("600");
        stockListView.getSelectionModel().select(0);
    };
    /**
     * 初始化器，在initialize中调用（同线程）。
     */
    private Runnable initializer = () -> {
        //MainApp Pane Initialize
        frameWidth = 1200d;
        frameHeight = 800d;
        //Bottom Area Initialize
        bottomAreaInitialize();
        //Center Area Initialize
        centerAreaInitialize();
        //Right Area Initialize
        rightAreaInitialize();
        //MainApp Pane Listener
        addMainPaneListener();

        new Thread(postInitializer).start();
    };

    private void bottomAreaInitialize() {
        consoleTextArea = new TextArea(TODO_STRING);
        try {
            consoleTextArea.appendText(new String(new ColorData(100, 200, 100).toByteData()));
            consoleTextArea.appendText("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        consoleTextField = new TextField("console in");
        consolePane.getChildren().addAll(consoleTextArea, consoleTextField);
        createAbsAnchor(consolePane, consoleTextArea, 0d, 0d, 0d, 23d);
        consolePane.setBottomAnchor(consoleTextField, 0d);
        consolePane.setLeftAnchor(consoleTextField, 0d);
        consolePane.setRightAnchor(consoleTextField, 0d);
    }

    private void centerAreaInitialize() {
        mainCanvas = new StockCanvas[4];
        for (int i = 0; i < mainCanvas.length; i++) {
            Tab tab = new Tab(TAB_TEXT[i]);
            mainCanvas[i] = new StockCanvas(frameWidth - CANVAS_SHRINK_WIDTH, frameHeight - CANVAS_SHRINK_HEIGHT);
            AnchorPane anchorPane = new AnchorPane(mainCanvas[i]);
            createAbsAnchor(anchorPane, mainCanvas[i], 0d, 0d, 0d, 0d);
            tab.setContent(anchorPane);
            mainTabPane.getTabs().add(tab);
        }
    }

    private void addMainPaneListener() {
        mainPane.widthProperty().addListener(widthResizeListener);
        mainPane.heightProperty().addListener(heightResizeListener);
    }

    private void rightAreaInitialize() {
        int i = 0;
        String[] chineseNumber = {"一", "二", "三", "四", "五"};
        originalLabelString = new ArrayList<>(FundamentalData.DATA_COUNT);
        fundamentalDataLabel = new Label[FundamentalData.DATA_COUNT];
        fundamentalDataLabel[i++] = nameLabel;
        fundamentalDataLabel[i++] = yesterdayCloseLabel;
        fundamentalDataLabel[i++] = todayOpenLabel;
        fundamentalDataLabel[i++] = currentPriceLabel;
        fundamentalDataLabel[i++] = todayHighestLabel;
        fundamentalDataLabel[i++] = todayLowestLabel;
        fundamentalDataLabel[i++] = auctionBuyLabel;
        fundamentalDataLabel[i++] = auctionSellLabel;
        fundamentalDataLabel[i++] = turnVolumeLabel;
        fundamentalDataLabel[i++] = turnPriceLabel;
        for (int j = 0; j < 5; j++) {
            Label l1 = new Label("买" + chineseNumber[j] + "申请");
            Label l2 = new Label("买" + chineseNumber[j] + "报价");
            fundamentalDataLabel[i++] = l1;
            fundamentalDataLabel[i++] = l2;
            buySellVBox.getChildren().addAll(l1, l2);
        }
        for (int j = 0; j < 5; j++) {
            Label l1 = new Label("卖" + chineseNumber[j] + "申请");
            Label l2 = new Label("卖" + chineseNumber[j] + "报价");
            fundamentalDataLabel[i++] = l1;
            fundamentalDataLabel[i++] = l2;
            buySellVBox.getChildren().addAll(l1, l2);
        }
        fundamentalDataLabel[i++] = dateLabel;
        fundamentalDataLabel[i++] = timeLabel;
        for (int j = 0; j < FundamentalData.DATA_COUNT; j++) {
            originalLabelString.add(fundamentalDataLabel[j].getText());
        }
    }

    @Override
    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }
}
