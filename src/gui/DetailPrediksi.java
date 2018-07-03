package gui;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Created by Nylon on 7/19/2017.
 */
public class DetailPrediksi {
    @FXML private TableView<double[]> tabelPrediksi;
    @FXML private TableColumn<double[], Double> kolomTahun;
    @FXML private TableColumn<double[], Double> kolomAktual;
    @FXML private TableColumn<double[], Double> kolomPrediksi;
    @FXML private TableColumn<double[], Double> kolomSelisih;
    @FXML private Label mape, waktuKomputasi;
    private MainApp mainApp = new MainApp();

    public void showData(double[][] dataArray) {
        ObservableList<double[]> data = FXCollections.observableArrayList();
        for (double[] row : dataArray) {
            data.add(row);
        }
        tabelPrediksi.setItems(data);
        kolomTahun.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper((int)param.getValue()[0])
        );

        kolomAktual.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(mainApp.round(param.getValue()[1], 2))
        );

        kolomPrediksi.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(mainApp.round(param.getValue()[2], 2))
        );
        kolomSelisih.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper(mainApp.round(param.getValue()[3], 2))
        );
    }

    public void showMape(double dataMape){
        mape.setText(Double.toString(mainApp.round(dataMape, 2)));
    }

    public void showWaktuKomputasi(long cpuTime){
        waktuKomputasi.setText(Long.toString(cpuTime));
    }
}
