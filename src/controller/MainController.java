package controller;

import java.lang.reflect.InvocationTargetException;

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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.transform.Scale;
import model.ModelData;


public class MainController {

    /*Récupération des éléments placés sur l'interface graphique sous forme FXML*/
    @FXML
    private LineChart LineChart;

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

    private XYChart.Series series = new XYChart.Series<>();

    /*Permet de connaitre dans le "platform.runlater" la valeur i de la boucle for*/
    private int iNumber = 0;

    /*Les données de ModelData sont listé dans une liste observable*/
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

                /*Pour le moment, remplis toutes les 5 secondes le tableau et le graphique avec une valeur calculé prédéfinie*/
                if(i%5 == 0 && i!= 0){
                    iNumber = i;
                    tabView.getItems().removeAll(getData());

                    /*Obligatoire, sinon ne s'effectue pas dans le bon thread*/
                    Platform.runLater(
                            () -> {
                                series.getData().add(new XYChart.Data(iNumber, iNumber*5.0));
                            }
                    );
                    dataList.add(new ModelData(i, i*5.0));
                    tabView.getItems().addAll(getData());
                }
                try {
                    /*Permet le traitement seconde par seconde*/
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            /*Une fois le temps écoulé, on peut afficher le bouton d'impression*/
            /*A retirer pour correspondre au besoin client*/
            printBtn.setDisable(false);
        }

    }

    @FXML
    private void initialize() {

        /*Retire le rond rouge sous le graphe*/
        LineChart.setLegendVisible(false);

        LineChart.getData().add(series);
        prog.setProgress(0.0);
        progInd.setProgress(0.0);
        printBtn.setDisable(true);

        /*Définit dans quel colonne doit se trouver les différentes données de ModelData*/
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
