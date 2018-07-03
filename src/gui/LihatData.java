package gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Created by Nylon on 8/5/2017.
 */
public class LihatData {
    @FXML private TableView<double[]> tabelLihatData;
    @FXML private TableColumn<double[], Double> kolomTahun;
    @FXML private TableColumn<double[], Double> kolomEnergiPrimer;
    @FXML private TableColumn<double[], Double> kolomGNI;
    @FXML private TableColumn<double[], Double> kolomGDP;
    @FXML private TableColumn<double[], Double> kolomPopulasi;
    @FXML private TableColumn<double[], Double> kolomImpor;
    @FXML private TableColumn<double[], Double> kolomEkspor;

    public void showData(double[][] dbData) {
        ObservableList<double[]> data = FXCollections.observableArrayList();
        for (double[] row : dbData) {
            data.add(row);
        }
        tabelLihatData.setItems(data);
        kolomTahun.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper((int)param.getValue()[0])
        );

        kolomEnergiPrimer.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(param.getValue()[1])
        );

        kolomGNI.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(param.getValue()[2])
        );

        kolomGDP.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(param.getValue()[3])
        );

        kolomPopulasi.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(param.getValue()[4])
        );

        kolomImpor.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(param.getValue()[5])
        );

        kolomEkspor.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(param.getValue()[6])
        );
    }
}
