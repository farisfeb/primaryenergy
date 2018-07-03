package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * Created by Nylon on 7/19/2017.
 */
public class Root {

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML private void handleExit() {
        System.exit(0);
    }
    @FXML private void handlePrediksiPSOACOR() {
        mainApp.showPrediksiPSOACOR();
    }
    @FXML private void handlePrediksiPSO() {
        mainApp.showPrediksiPSO();
    }
    @FXML private void handlePrediksiACOR() {
        mainApp.showPrediksiACOR();
    }
    @FXML private void handleLihatData() {
        mainApp.showLihatData();
    }
    @FXML private void handleLihatRegresi() {
        mainApp.showLihatRegresi();
    }
    @FXML private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Prediksi Kebutuhan Energi Primer Indonesia");
        alert.setHeaderText("About");
        alert.setContentText("Faris Febrianto (135150201111221)\n" +
                "Pembimbing 1 : Candra Dewi, S.Kom., M.Sc\n" +
                "Pembimbing 2 : Bayu Rahayudi, S.T., M.T");
        alert.showAndWait();
    }
}
