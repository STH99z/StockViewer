package com.sth99.stockviewer.gui;

import com.sth99.stockviewer.data.*;
import com.sth99.stockviewer.gui.component.KChart;
import com.sth99.stockviewer.gui.component.MinuteChart;
import com.sth99.stockviewer.user.UserStorager;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
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
    private MenuItem uidMenu, userNameMenu, menuSwapUser;
    @FXML
    private CheckMenuItem menuDefaultSave;
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
    private ListView<StockCodeData> stockListView, stockListViewSelf;
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
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label
            labelBasicName,
            labelBasicStat,
            labelBasicCount,
            labelBasicAnalyzed;
    @FXML
    private Label
            labelGraphName,
            labelGraphStat,
            labelGraphCount,
            labelGraphAnalyzed;
    @FXML
    private Label
            labelLocalName,
            labelLocalStat,
            labelLocalCount,
            labelLocalAnalyzed;
    @FXML
    private Button buttonSaveToLocal, buttonDeleteLocal;
    @FXML
    private Button buttonAddToSelfList, buttonRemoveFromSelfList;
    private Label[] labelBasicGroup = new Label[4];
    private Label[] labelGraphGroup = new Label[4];
    private Label[] labelLocalGroup = new Label[4];


    private static final String TODO_STRING = "" +
            "菜单处理，换色处理，用户信息存储（自选股，颜色）\n" +
            "自选股票存储\n";

    StockCodeStorager stockCodeStorager;
    StockCodeData currentStockCode;
    KDataSet[] kDataSet = new KDataSet[3];
    MinuteDataSet minuteDataSet;
    FundamentalData fundamentalData;
    double frameWidth, frameHeight;

    private void displayException(Exception e) {
        if (e instanceof FileNotFoundException)
            consoleTextArea.appendText("网络文件未找到\n");
    }

    private void updateGraphData() {
        int okCount = 0;
        int dataCount = 0;
        String graphOK = "";
        String[] graphName = {"分时", "日K", "周K", "月K"};
        if (minuteDataSet.getMinuteDataList().size() > 0) {
            okCount++;
            dataCount += minuteDataSet.getMinuteDataList().size();
            graphOK += graphName[0] + " ";
        }
        for (int i = 0; i < kDataSet.length; i++) {
            if (kDataSet[i].getKDataList().size() > 0) {
                okCount++;
                dataCount += kDataSet[i].getKDataList().size();
                graphOK += graphName[i + 1];
            }
        }
        setLabelText(labelGraphName, graphOK, okCount == 4);
        if (okCount == 4)
            setLabelText(labelGraphStat, "完整", true);
        else if (okCount > 0)
            setLabelText(labelGraphStat, "残缺", false);
        else
            setLabelText(labelGraphStat, "完全失败", false);
        setLabelText(labelGraphCount, dataCount + "", dataCount > 0);
        setLabelText(labelGraphAnalyzed, dataCount + "", dataCount > 0);
    }

    private void updateBasicDataFailed() {
        boolean suc = false;
        setLabelText(labelBasicName, currentStockCode.getName() + " " + currentStockCode.getFullCode(), suc);
        setLabelText(labelBasicStat, "网络文件未找到", suc);
        setLabelText(labelBasicCount, "0", suc);
        setLabelText(labelBasicAnalyzed, "未解析", suc);
    }

    private void updateBasicDataSuccess() {
        boolean suc = true;
        setLabelText(labelBasicName, currentStockCode.getName() + " " + currentStockCode.getFullCode(), suc);
        setLabelText(labelBasicStat, "获取数据成功", suc);
        setLabelText(labelBasicCount, fundamentalData.getValues().length + "", suc);
        setLabelText(labelBasicAnalyzed, FundamentalData.DATA_COUNT + "", suc);
    }

    private void saveAllData() {
        if (minuteDataSet.getMinuteDataList().size() > 0) {
            try {
                minuteDataSet.saveData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < kDataSet.length; i++) {
            try {
                kDataSet[i].saveData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        updateLocalData();
    }

    private void deleteAllData() {
        if (minuteDataSet != null && minuteDataSet.fileExist()) {
            minuteDataSet.deleteFile();
        }
        for (int i = 0; i < kDataSet.length; i++) {
            if (kDataSet[i] != null && kDataSet[i].fileExist()) {
                kDataSet[i].deleteFile();
            }
        }
        updateLocalData();
    }

    private void updateLocalData() {
        int okCount = 0;
        int kOkCount = 0;
        int mOkCount = 0;
        String graphOK = "";
        String[] graphName = {"分时", "日K", "周K", "月K"};
        if (minuteDataSet != null && minuteDataSet.fileExist()) {
            okCount++;
            mOkCount++;
            graphOK += graphName[0] + " ";
        }
        for (int i = 0; i < kDataSet.length; i++) {
            if (kDataSet[i] != null && kDataSet[i].fileExist()) {
                okCount++;
                kOkCount++;
                graphOK += graphName[i + 1];
            }
        }
        setLabelText(labelLocalName, "存在" + okCount + "个", okCount == 4);
        if (okCount > 0)
            buttonDeleteLocal.setDisable(false);
        else
            buttonDeleteLocal.setDisable(true);
        if (okCount == 4)
            setLabelText(labelLocalStat, "完整", true);
        else if (okCount > 0)
            setLabelText(labelLocalStat, "残缺", false);
        else
            setLabelText(labelLocalStat, "完全失败", false);
        if (mOkCount == 1)
            setLabelText(labelLocalCount, "完整", true);
        else
            setLabelText(labelLocalCount, "不存在", false);
        if (kOkCount == 3)
            setLabelText(labelLocalAnalyzed, "完整", true);
        else if (kOkCount > 0)
            setLabelText(labelLocalAnalyzed, "残缺（" + kOkCount + "）个", false);
        else
            setLabelText(labelLocalAnalyzed, "不存在", false);
    }

    private void setLabelText(Label label, String text, boolean OKstate) {
        label.setText(text);
        if (OKstate)
            label.setTextFill(Color.DARKBLUE);
        else
            label.setTextFill(Color.DARKRED);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializer();
        postInitializer();
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
        progressBar.setPrefWidth(frameWidth);
        updateCanvas();
    }

    private void retriveStockData(StockCodeData stockCodeData) {
        currentStockCode = stockCodeData;
        progressBar.setProgress(0d);
        try {
            minuteDataSet = new MinuteDataSet(stockCodeData);
            progressBar.setProgress(0.2d);
        } catch (Exception e) {
            consoleTextArea.appendText(e.getMessage());
            displayException(e);
        }
        for (int i = 0; i < 3; i++) {
            try {
                kDataSet[i] = new KDataSet(stockCodeData, KDataTimeLength.values()[i]);
                progressBar.setProgress(progressBar.getProgress() + 0.2d);
            } catch (Exception e) {
                consoleTextArea.appendText(e.getMessage());
                displayException(e);
            }
        }
        updateGraphData();
        try {
            fundamentalData = new FundamentalData(stockCodeData);
            updateBasicDataSuccess();
            progressBar.setProgress(progressBar.getProgress() + 0.2d);
        } catch (Exception e) {
            consoleTextArea.appendText(e.getMessage());
            updateBasicDataFailed();
            displayException(e);
        }
        if (menuDefaultSave.isSelected())
            saveAllData();
    }

    private void updateCodeListView() {
        String text = stockListField.getText();
        stockListView.setItems(FXCollections.observableArrayList(stockCodeStorager.getStockListFrom(text)));
        stockListViewSelf.setItems(FXCollections.observableArrayList(UserStorager.get().getCurrentUser().getSelfSelected()));
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

    private void addToSelfList() {
        StockCodeData selectedItem = stockListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && !stockListViewSelf.getItems().contains(selectedItem)) {
            stockListViewSelf.getItems().add(selectedItem);
        }
    }

    private void removeFromSelfList() {
        StockCodeData selectedItem = stockListViewSelf.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            int i = stockListViewSelf.getSelectionModel().getSelectedIndex();
            stockListViewSelf.getItems().remove(i);
        }
    }

    private void swapUser() {
        stageController.setStage(MainApp.loginViewID, MainApp.mainViewID);
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
        retriveStockData(newValue);
        updateLocalData();
        updateDetailPane();
        updateConsoleArea(newValue);
        updateCanvas();
    };
    private ChangeListener<StockCodeData> selfCodeListChangeListener = (observable, oldValue, newValue) -> {
        ArrayList<StockCodeData> selfSelected = UserStorager.get().getCurrentUser().getSelfSelected();
        selfSelected.clear();
        for (StockCodeData stockCodeData : stockListViewSelf.getItems()) {
            selfSelected.add(stockCodeData);
        }
        try {
            UserStorager.get().getCurrentUser().saveSelfList();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        updateSelfList();
    };

    private void updateSelfList() {
        ObservableList<StockCodeData> items = stockListViewSelf.getItems();
        items.clear();
        for (StockCodeData stockCodeData : UserStorager.get().getCurrentUser().getSelfSelected()) {
            items.add(stockCodeData);
        }
    }

    @Override
    public void setShowingChangeListener(Stage stage) {
        stage.showingProperty().addListener(stageShowingChangeListener);
    }

    /**
     * 后初始化器，包含网络操作，这个时候先显示界面。后初始化放在与initialize不同的线程。
     */
    public void postInitializer() {
        stockCodeStorager = new StockCodeStorager();
        stockListField.textProperty().addListener(codeFieldChangeListener);
        stockListView.getSelectionModel().selectedItemProperty().addListener(codeListSelectListener);
        stockListViewSelf.getSelectionModel().selectedItemProperty().addListener(codeListSelectListener);
        stockListViewSelf.getSelectionModel().selectedItemProperty().addListener(selfCodeListChangeListener);
        currentStockCode = new StockCodeData("", "sh600000");
        stockListField.setText("600");
        stockListView.getSelectionModel().select(0);
        buttonSaveToLocal.setOnAction(event -> saveAllData());
        buttonDeleteLocal.setOnAction(event -> deleteAllData());
        buttonAddToSelfList.setOnAction(event -> addToSelfList());
        buttonRemoveFromSelfList.setOnAction(event -> removeFromSelfList());
        menuSwapUser.setOnAction(event -> swapUser());
    }

    /**
     * 初始化器，在initialize中调用（同线程）。
     */
    private void initializer() {
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
    }

    private void bottomAreaInitialize() {
        consoleTextArea = new TextArea(TODO_STRING);
        consoleTextArea.appendText("\n");
        consoleTextField = new TextField("console in");
        consolePane.getChildren().addAll(consoleTextArea, consoleTextField);
        createAbsAnchor(consolePane, consoleTextArea, 0d, 0d, 0d, 23d);
        consolePane.setBottomAnchor(consoleTextField, 0d);
        consolePane.setLeftAnchor(consoleTextField, 0d);
        consolePane.setRightAnchor(consoleTextField, 0d);

        labelBasicGroup[0] = labelBasicName;
        labelBasicGroup[1] = labelBasicStat;
        labelBasicGroup[2] = labelBasicCount;
        labelBasicGroup[3] = labelBasicAnalyzed;
        labelGraphGroup[0] = labelGraphName;
        labelGraphGroup[1] = labelGraphStat;
        labelGraphGroup[2] = labelGraphCount;
        labelGraphGroup[3] = labelGraphAnalyzed;
        labelLocalGroup[0] = labelLocalName;
        labelLocalGroup[1] = labelLocalStat;
        labelLocalGroup[2] = labelLocalCount;
        labelLocalGroup[3] = labelLocalAnalyzed;

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
