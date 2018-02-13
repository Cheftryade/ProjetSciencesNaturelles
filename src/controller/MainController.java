package controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterAttributes;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Scale;
import model.ModelData;


public class MainController {

    @FXML
    private HBox idHbox;

    @FXML
    private LineChart<?, ?> LineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    protected TableView<ModelData> tabView;

    @FXML private TableColumn<ModelData,Integer> columnTime;
    @FXML private TableColumn<ModelData,Double> columnTempOne;

    @FXML
    private ProgressBar prog;

    @FXML
    private ProgressIndicator progInd;

    @FXML
    private Button printBtn;

    private XYChart.Series series = new XYChart.Series<>();;

    private int iNumber = 0;

    private ObservableList<ModelData> dataList = FXCollections.observableArrayList();

    public ObservableList<ModelData> getData() {
        return dataList;
    }
    class bg_Thread implements Runnable {

        @Override
        public void run() {
            double temps = WelcomeController.getTemps();
            for(int i = 0; i < temps+1.0; i++){
                prog.setProgress(i/temps);
                progInd.setProgress(i/temps);
                if(i%5 == 0 && i!= 0){
                    iNumber = i;
                    tabView.getItems().removeAll(getData());
                    Platform.runLater(
                            () -> {
                                series.getData().add(new XYChart.Data(iNumber, iNumber*5.0));
                            }
                    );
                    dataList.add(new ModelData(i, i*5.0));
                    tabView.getItems().addAll(getData());
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            printBtn.setDisable(false);
        }

    }

    @FXML
    private void initialize() {
        LineChart.setLegendVisible(false);
        LineChart.getData().add(series);
        prog.setProgress(0.0);
        progInd.setProgress(0.0);
        printBtn.setDisable(true);
        columnTime.setCellValueFactory(new PropertyValueFactory<>("Temps"));
        columnTempOne.setCellValueFactory(new PropertyValueFactory<>("Valeur"));
        tabView.getItems().addAll(getData());

        Thread th = new Thread(new bg_Thread());
        th.start();
    }

    @FXML protected void handleSubmitButtonAction(ActionEvent event) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        printNode(LineChart);
    }

    public static void printNode(final Node node) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout
                = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        PrinterAttributes attr = printer.getPrinterAttributes();
        PrinterJob job = PrinterJob.createPrinterJob();
        double scaleX
                = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY
                = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY/2);
        node.getTransforms().add(scale);

        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            boolean success = job.printPage(pageLayout, node);
            job.printPage(pageLayout, node);
            if (success) {
                job.endJob();
            }
        }
        node.getTransforms().remove(scale);
    }
}
