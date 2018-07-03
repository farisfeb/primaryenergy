package gui;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import kode.ACOR;

/**
 * Created by Nylon on 7/19/2017.
 */
public class PrediksiACOR {
    @FXML LineChart<String, Double> fitnessLineChart, mapeLineChart;
    @FXML Label  q, xi, archiveSize, m, minKonvergen, maxIterasi, minTahunLatih, maxTahunLatih,
            minTahunUji, maxTahunUji, xMinIntersep, xMaxIntersep, xMinKoef, xMaxKoef;
    @FXML TextField nilaiRegresi;

    boolean prosesClicked = false;
    private MainApp mainApp;
    ACOR acor;

    public PrediksiACOR() {
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        double[] sMin = {-100, -10, -10, -10, -10, -10};
        double[] sMax = {100, 10, 10, 10, 10, 10};
        int[] tahunLatih = {1967, 2006};
        int[] tahunUji = {2007, 2016};
        acor = new ACOR(0.001, 1, 120, 300, 500, 2000,
                sMin, sMax, tahunLatih, tahunUji);
        showParameter();
    }

    public void showParameter() {
        q.setText(Double.toString(acor.getQ()));
        xi.setText(Double.toString(acor.getXi()));
        archiveSize.setText(Integer.toString(acor.getArchiveSize()));
        m.setText(Integer.toString(acor.getM()));
        minKonvergen.setText(Integer.toString(acor.getMinKonvergen()));
        maxIterasi.setText(Integer.toString(acor.getMaxIterasi()));
        minTahunLatih.setText(Integer.toString(acor.getTahunLatih()[0]));
        maxTahunLatih.setText(Integer.toString(acor.getTahunLatih()[1]));
        minTahunUji.setText(Integer.toString(acor.getTahunUji()[0]));
        maxTahunUji.setText(Integer.toString(acor.getTahunUji()[1]));
        xMinIntersep.setText(Double.toString(acor.getsMin()[0]));
        xMaxIntersep.setText(Double.toString(acor.getsMax()[0]));
        xMinKoef.setText(Double.toString(acor.getsMin()[1]));
        xMaxKoef.setText(Double.toString(acor.getsMax()[1]));
    }

    public void showFitness(double[] fitnessIterasi ){
        fitnessLineChart.getData().clear();
        fitnessLineChart.setCreateSymbols(false);
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        for (int i = 0; i < fitnessIterasi.length; i++) {
            series.getData().add(new XYChart.Data<>(Integer.toString(i), fitnessIterasi[i]));
        }
        fitnessLineChart.getData().add(series);
    }

    public void showPrediksi(double[][] mapeArray){
        mapeLineChart.getData().clear();
        XYChart.Series<String, Double> series1 = new XYChart.Series<>();
        XYChart.Series<String, Double> series2 = new XYChart.Series<>();

        for (int i = 0; i < mapeArray.length; i++) {
            series1.getData().add(new XYChart.Data<>(Integer.toString((int)mapeArray[i][0]), mapeArray[i][1]));
            series2.getData().add(new XYChart.Data<>(Integer.toString((int)mapeArray[i][0]), mapeArray[i][2]));
        }
        series1.setName("Nilai aktual");
        series2.setName("Nilai prediksi");

        mapeLineChart.getData().addAll(series1, series2);

        showNilaiRegresi(acor.getSbest());
    }

    public void showNilaiRegresi(double[] gbest){
        String b0 = Double.toString(mainApp.round(gbest[0], 4));
        String b1 = Double.toString(mainApp.round(gbest[1], 4));
        String b2 = Double.toString(mainApp.round(gbest[2], 4));
        String b3 = Double.toString(mainApp.round(gbest[3], 4));
        String b4 = Double.toString(mainApp.round(gbest[4], 4));
        String b5 = Double.toString(mainApp.round(gbest[5], 4));
        nilaiRegresi.setText(b0 + " + " + b1 + "X1 + " + b2 + "X2  + " + b3 + "X3  + "
                + b4 + "X4  + " + b5 + "X5");
    }

    @FXML
    public void handleProses(){
        acor = new ACOR(acor.getQ(), acor.getXi(), acor.getM(), acor.getArchiveSize(), acor.getMinKonvergen(), acor.getMaxIterasi(),
                acor.getsMin(), acor.getsMax(), acor.getTahunLatih(), acor.getTahunUji());
        acor.inisialisasiACOR();
        acor.iterasiACOR();
        acor.saveRegresi();
        showPrediksi(acor.hitungPrediksi());
        showFitness(acor.getFitnessIterasi());
        prosesClicked = true;
    }

    @FXML
    private void handleRefreshParameter() {
        showParameter();
    }

    @FXML
    private void handleUbahParameter() {
       mainApp.showUbahParameterACOR(acor);
    }

    @FXML
    private void handleDetailPrediksi() {
        if (prosesClicked == true) {
            mainApp.showDetailPrediksi(acor.hitungPrediksi(), acor.hitungMAPE(), acor.getWaktuKomputasi());
        } else {
            errorMessage();
        }
    }

    @FXML
    private void handleDetailFitness() {
        if (prosesClicked == true) {
            double[] temp = acor.getFitnessIterasi();
            mainApp.showDetailFitness(temp);
        } else {
            errorMessage();
        }
    }

    private void errorMessage(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Klik proses dahulu");
        alert.setHeaderText(null);
        alert.setContentText("Jalankan proses terlebih dahulu");
        alert.showAndWait();
    }
}