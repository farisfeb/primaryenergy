package gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Nylon on 7/20/2017.
 */
public class DetailFitness {

    @FXML private TableView<double[]> tabelFitness;
    @FXML private TableColumn<double[], Double> kolomIterasi;
    @FXML private TableColumn<double[], Double> kolomFitness;

    public void showData(double[] dataArray) {
        double[][] tempData = new double[dataArray.length][2];
        for (int i = 0; i < dataArray.length; i++) {
            tempData[i][0] = i;
            tempData[i][1] = dataArray[i];
        }

        ObservableList<double[]> data = FXCollections.observableArrayList();
        for (double[] row : tempData) {
            data.add(row);
        }
        tabelFitness.setItems(data);

        kolomIterasi.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper((int)param.getValue()[0])
        );

        kolomFitness.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(round(param.getValue()[1], 2))
        );
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }
}
