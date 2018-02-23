package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/*Type de données récupérés et affichés sous forme de graphe / tableau*/
public class ModelData {

    private final SimpleIntegerProperty  time ;
    private final SimpleDoubleProperty dataValue ;
    public ModelData(int time, double dataValue) {
        this.time = new SimpleIntegerProperty(time) ;
        this.dataValue = new SimpleDoubleProperty(dataValue) ;
    }

    public int getTemps() {
        return time.get();
    }
    public double getValeur() {
        return dataValue.get();
    }
}
