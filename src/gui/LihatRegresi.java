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
 * Created by Nylon on 8/5/2017.
 */
public class LihatRegresi {
    @FXML private TableView<double[]> tabelLihatRegresi;
    @FXML private TableColumn<double[], Double> kolomMetode;
    @FXML private TableColumn<double[], Double> kolomb0;
    @FXML private TableColumn<double[], Double> kolomb1;
    @FXML private TableColumn<double[], Double> kolomb2;
    @FXML private TableColumn<double[], Double> kolomb3;
    @FXML private TableColumn<double[], Double> kolomb4;
    @FXML private TableColumn<double[], Double> kolomb5;
    @FXML private TableColumn<double[], Double> kolomMape;

    public void showDataRegresi(double[][] dbData) {
        ObservableList<double[]> data = FXCollections.observableArrayList();
        for (double[] row : dbData) {
            data.add(row);
        }
        tabelLihatRegresi.setItems(data);
        kolomMetode.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper((int) param.getValue()[0])
        );

        kolomb0.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(round(param.getValue()[1],5))
        );

        kolomb1.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(round(param.getValue()[2],5))
        );

        kolomb2.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(round(param.getValue()[3],5))
        );

        kolomb3.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(round(param.getValue()[4],5))
        );

        kolomb4.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(round(param.getValue()[5],5))
        );

        kolomb5.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(round(param.getValue()[6],5))
        );

        kolomMape.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(round(param.getValue()[7], 2))
        );
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }
}
