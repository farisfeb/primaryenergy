package gui;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import kode.PSO;

/**
 * Created by Nylon on 7/19/2017.
 */
public class PrediksiPSO {
    @FXML LineChart<String, Double> fitnessLineChart, mapeLineChart;
    @FXML Label w, k, c1, c2, popSize, minKonvergen, maxIterasi, minTahunLatih, maxTahunLatih,
            minTahunUji, maxTahunUji, xMinIntersep, xMaxIntersep, xMinKoef, xMaxKoef;
    @FXML TextField nilaiRegresi;

    boolean prosesClicked = false;
    private MainApp mainApp;
    PSO pso;

    public PrediksiPSO() {
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        double[] xMin = {-100, -10, -10, -10, -10, -10};
        double[] xMax = {100, 10, 10, 10, 10, 10};
        double[] c1c2 = {1.496, 1.496};
        int[] tahunLatih = {1967, 2006};
        int[] tahunUji = {2007, 2016};
        pso = new PSO(0.7298, 0.6, 130, 100,2000,
                c1c2, xMin, xMax, tahunLatih, tahunUji);
        showParameter();
    }

    public void showParameter() {
        w.setText(Double.toString(pso.getW()));
        k.setText(Double.toString(pso.getK()));
        c1.setText(Double.toString(pso.getC1c2()[0]));
        c2.setText(Double.toString(pso.getC1c2()[1]));
        popSize.setText(Integer.toString(pso.getPopSize()));
        minKonvergen.setText(Integer.toString(pso.getMinKonvergen()));
        maxIterasi.setText(Integer.toString(pso.getMaxIterasi()));
        minTahunLatih.setText(Integer.toString(pso.getTahunLatih()[0]));
        maxTahunLatih.setText(Integer.toString(pso.getTahunLatih()[1]));
        minTahunUji.setText(Integer.toString(pso.getTahunUji()[0]));
        maxTahunUji.setText(Integer.toString(pso.getTahunUji()[1]));
        xMinIntersep.setText(Double.toString(pso.getxMin()[0]));
        xMaxIntersep.setText(Double.toString(pso.getxMax()[0]));
        xMinKoef.setText(Double.toString(pso.getxMin()[1]));
        xMaxKoef.setText(Double.toString(pso.getxMax()[1]));
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

        showNilaiRegresi(pso.getGbest());
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
        pso = new PSO(pso.getW(), pso.getK(), pso.getPopSize(), pso.getMinKonvergen(), pso.getMaxIterasi(),
                pso.getC1c2(), pso.getxMin(), pso.getxMax(), pso.getTahunLatih(), pso.getTahunUji());
        pso.inisialisasiPSO();
        pso.iterasiPSO();
        pso.saveRegresi();
        showPrediksi(pso.hitungPrediksi());
        showFitness(pso.getFitnessIterasi());
        prosesClicked = true;
    }

    @FXML
    private void handleRefreshParameter() {
        showParameter();
    }

    @FXML
    private void handleUbahParameter() {
       mainApp.showUbahParameterPSO(pso);
    }

    @FXML
    private void handleDetailPrediksi() {
        if (prosesClicked == true) {
            mainApp.showDetailPrediksi(pso.hitungPrediksi(), pso.hitungMAPE(), pso.getWaktuKomputasi());
        } else {
            errorMessage();
        }
    }

    @FXML
    private void handleDetailFitness() {
        if (prosesClicked == true) {
            double[] temp = pso.getFitnessIterasi();
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