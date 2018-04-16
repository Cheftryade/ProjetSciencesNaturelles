package controller;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;

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
import jssc.SerialPort;
import static jssc.SerialPort.MASK_RXCHAR;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;
import java.lang.Math;
import java.text.DecimalFormat;
import model.Impression;

public class MainController1{

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
    double dp=0;
    double tmp=0;
    double rh=0;
    double maxHA=0;
    double minHA=1000;
    
    SerialPort serialPort;
    Thread readThread;
    Impression imp;

    public ObservableList<ModelData> getData() {
        return dataList;
    }
    
     SerialPort port = null;
    ObservableList<String> portList;
    
    //Label labelValue;
    final int NUM_OF_POINT = 50;
    //XYChart.Series series;
     
    /*private String detectPort(){
         
        portList = FXCollections.observableArrayList();
 
        String[] serialPortNames = SerialPortList.getPortNames();
        for(String name: serialPortNames){
            
            portList.add(name);
        }
        return portList.get(1);
    }*/
    public boolean connectPort() {
        
        portList = FXCollections.observableArrayList();
        //Balayage des listes des ports connectés
        String[] serialPortNames = SerialPortList.getPortNames();
        for(String name: serialPortNames){
            //if (name=="COM6")
            portList.add(name);
        }
        
        boolean success = false;
        SerialPort serialPort = new SerialPort(portList.get(2));
        //2 est pour COM6 Pour le pc de la fac
        try {
            
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_1200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            //proprietés de l'adaptateur USB
            
                   //System.out.println("slt4");
                    try {
                       
                        byte[] b=new byte[8];
                        // 5000 ms est pour l'attente de port
                        //96 octect pour la detection de toute les donnes
                                b = serialPort.readBytes(96,5000);
                                //b = serialPort.readBytes();
                        //int value = b[0] & 0xff;    //convert to int
                        //extraction des valeurs
                        
                        String st = new String (b);
                        String[] sa=st.split("DP  =");
                        String[] sa1=sa[1].split("RH  =");
                        String[] sa2=sa1[1].split("TMP =");
                        System.out.println("aa="+sa1[0]);
                        dp = Double.parseDouble(sa1[0].trim());
                        System.out.println("bb="+sa2[0]);
                        rh = Double.parseDouble(sa2[0].trim());
                        System.out.println("cc="+sa2[1].split("C")[0]);
                        tmp=Double.parseDouble(sa2[1].split("C")[0].trim()); 
                        System.out.println("dd="+st);
                        
                        //Platform.runLater(() -> {
                            //labelValue.setText(st);
                            //shiftSeriesData((float)value * 5/255); //in 5V scale
                        //});
                        
                    } catch (SerialPortException ex) {
                        Logger.getLogger(MainController1.class.getName())
                                .log(Level.SEVERE, null, ex);
                    } catch (SerialPortTimeoutException ex) {
                Logger.getLogger(MainController1.class.getName()).log(Level.SEVERE, null, ex);
            }
            port = serialPort;
            success = true;
            
        } catch (SerialPortException ex) {
            Logger.getLogger(MainController1.class.getName())
                    .log(Level.SEVERE, null, ex);
            System.out.println("SerialPortException: " + ex.toString());
        }
        try {
            serialPort.closePort();
        } catch (SerialPortException ex) {
            Logger.getLogger(MainController1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
    
    public void disconnectPort(){
        
        //System.out.println("disconnectArduino()");
        if(port != null){
            try {
                port.removeEventListener();
                
                if(port.isOpened()){
                    port.closePort();
                }
                
            } catch (SerialPortException ex) {
                Logger.getLogger(MainController1.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
    

    class bg_Thread implements Runnable {
        
        //SimpleRead();
        @Override
        public void run() {
            
            
           /* try {
            Thread.sleep(2000000000);
        } catch (InterruptedException e) {System.out.println(e);}*/
            double temps = WelcomeController.getTemps();
            int intervaltemps = (int)WelcomeController.getIntervalTemps();
            String nom=WelcomeController.getName();
            
            
            for(int i = 0; i < temps+1; i+=intervaltemps){
                prog.setProgress(i/temps);
                progInd.setProgress(i/temps);
                //connectPort(detectPort());
                
                 
                
                    iNumber = i;
                    
                    tabView.getItems().removeAll(getData());
                    
                    Platform.runLater(
                            () -> {
                               
                       
                        try{
                        connectPort();}
                        catch(Exception e)
                        {
                            
                        disconnectPort();
                        e.printStackTrace();
                        imp.prob(nom,iNumber);
                        }//rh tmp dp
                        
                        
                                double HA=(6.112*Math.exp((17.67* tmp)/(tmp+243.5))* rh * 2.1674)/(273.15+tmp);
                                if (HA>maxHA)
                                    {   //yAxis.setLowerBound((int) HA-1);
                                        yAxis.setUpperBound((int) HA+1);
                                        maxHA=HA;
                                    }
                                if (HA<minHA)
                                {
                                        yAxis.setLowerBound((int) HA-1);
                                        //yAxis.setUpperBound((int) HA+1);
                                        minHA=HA;
                                
                                }
                                if (iNumber==0)
                                {
                                    try{
                                imp.create(nom, "plante", rh, tmp, dp, HA);
                                    }
                                    catch (FileNotFoundException e ){e.printStackTrace();}
                                    catch (UnsupportedEncodingException e ){e.printStackTrace();}
                                }else {
                                
                                
                                        imp.add(nom, rh, tmp, dp, HA,iNumber);
                                    
                                
                                
                                }
                                series.getData().add(new XYChart.Data(iNumber, HA));
                                java.text.DecimalFormat df =new DecimalFormat(".###");
                                
                                dataList.add(new ModelData(iNumber, Double.parseDouble(df.format(HA).replace(',', '.')))); //(6.112*Math.exp((17.67* tmp)/(tmp+243.5))* rh * 2.1674)/(273.15+tmp)));
                                tabView.getItems().addAll(getData());
                            }
                    );
                    
                    
                //}
                try {
                    Thread.sleep(intervaltemps*1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    imp.prob(nom,iNumber);
                    e.printStackTrace();
                }
            }
            /*if(temps%intervaltemps!=0)
            {
            
            
            }*/
            imp.fin(nom);
            printBtn.setDisable(false);
        }

    }

    @FXML
    private void initialize() {
        LineChart.setLegendVisible(false);
        LineChart.getData().add(series);
        imp=new Impression();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(6);
        //yAxis.setUpperBound(15);
        yAxis.setTickUnit(1);
        prog.setProgress(0.0);
        progInd.setProgress(0.0);
        printBtn.setDisable(true);
        columnTime.setCellValueFactory(new PropertyValueFactory<>("Temps"));
        columnTempOne.setCellValueFactory(new PropertyValueFactory<>("Valeur Humidité Absolue"));
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
