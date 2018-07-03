package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import kode.PSOACOR;

/**
 * Created by Nylon on 7/19/2017.
 */
public class PrediksiPSOACOR {
    @FXML LineChart<String, Double> fitnessLineChart, mapeLineChart;
    @FXML Label q, xi, w, k, c1, c2, popSize, m, minKonvergen, maxIterasi,
            minTahunLatih, maxTahunLatih, minTahunUji, maxTahunUji,
            xMinIntersep, xMaxIntersep, xMinKoef, xMaxKoef;
    @FXML TextField nilaiRegresi;

    boolean prosesClicked = false;
    private MainApp mainApp;
    PSOACOR psoacor;

    public PrediksiPSOACOR() {
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        double[] xMin = {-100, -10, -10, -10, -10, -10};
        double[] xMax = {100, 10, 10, 10, 10, 10};
        double[] c1c2 = {1.496, 1.496};
        int[] tahunLatih = {1967, 2006};
        int[] tahunUji = {2007, 2015};
        psoacor = new PSOACOR(c1c2,0.7298,0.6,0.007,1,10, 120, 100,
                2000, xMin, xMax, tahunLatih, tahunUji);
        showParameter();
    }

    public void showParameter() {
        q.setText(Double.toString(psoacor.getQ()));
        xi.setText(Double.toString(psoacor.getXi()));
        w.setText(Double.toString(psoacor.getW()));
        k.setText(Double.toString(psoacor.getK()));
        c1.setText(Double.toString(psoacor.getC1c2()[0]));
        c2.setText(Double.toString(psoacor.getC1c2()[1]));
        popSize.setText(Integer.toString(psoacor.getPopSize()));
        m.setText(Integer.toString(psoacor.getM()));
        minKonvergen.setText(Integer.toString(psoacor.getMinKonvergen()));
        maxIterasi.setText(Integer.toString(psoacor.getMaxIterasi()));
        minTahunLatih.setText(Integer.toString(psoacor.getTahunLatih()[0]));
        maxTahunLatih.setText(Integer.toString(psoacor.getTahunLatih()[1]));
        minTahunUji.setText(Integer.toString(psoacor.getTahunUji()[0]));
        maxTahunUji.setText(Integer.toString(psoacor.getTahunUji()[1]));
        xMinIntersep.setText(Double.toString(psoacor.getxMin()[0]));
        xMaxIntersep.setText(Double.toString(psoacor.getxMax()[0]));
        xMinKoef.setText(Double.toString(psoacor.getxMin()[1]));
        xMaxKoef.setText(Double.toString(psoacor.getxMax()[1]));
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

    public void showPrediksi(double[][] dataPrediksi){
        mapeLineChart.getData().clear();
        XYChart.Series<String, Double> series1 = new XYChart.Series<>();
        XYChart.Series<String, Double> series2 = new XYChart.Series<>();

        for (int i = 0; i < dataPrediksi.length; i++) {
            series1.getData().add(new XYChart.Data<>(Integer.toString((int)dataPrediksi[i][0]), dataPrediksi[i][1]));
            series2.getData().add(new XYChart.Data<>(Integer.toString((int)dataPrediksi[i][0]), dataPrediksi[i][2]));
        }
        series1.setName("Nilai aktual");
        series2.setName("Nilai prediksi");

        mapeLineChart.getData().addAll(series1, series2);

        showNilaiRegresi(psoacor.getGbest());
    }

    public void showNilaiRegresi(double[] gbest){
        String b0 = Double.toString(mainApp.round(gbest[0], 5));
        String b1 = Double.toString(mainApp.round(gbest[1], 5));
        String b2 = Double.toString(mainApp.round(gbest[2], 5));
        String b3 = Double.toString(mainApp.round(gbest[3], 5));
        String b4 = Double.toString(mainApp.round(gbest[4], 5));
        String b5 = Double.toString(mainApp.round(gbest[5], 5));
        nilaiRegresi.setText(b0 + " + " + b1 + "X1 + " + b2 + "X2 + " + b3 + "X3 + "
                + b4 + "X4 + " + b5 + "X5");
    }

    @FXML
    public void handleProses(){
        psoacor = new PSOACOR(psoacor.getC1c2(), psoacor.getW(), psoacor.getK(), psoacor.getQ(),
                psoacor.getXi(), psoacor.getM(), psoacor.getPopSize(), psoacor.getMinKonvergen(),
                psoacor.getMaxIterasi(), psoacor.getxMin(), psoacor.getxMax(), psoacor.getTahunLatih(),
                psoacor.getTahunUji());
        psoacor.inisialisasiPSOACOR();
        psoacor.iterasiPSOACOR();
        psoacor.saveRegresi();
        showPrediksi(psoacor.hitungPrediksi());
        showFitness(psoacor.getFitnessIterasi());
        System.out.println("Waktu Komputasi " + psoacor.getWaktuKomputasi() + "ms");
        System.out.println("Iterasi " + psoacor.getIterasi());
        System.out.println("IsKonvergen " + psoacor.isKonvergen());
        prosesClicked = true;
    }

    @FXML
    private void handleRefreshParameter() {
        showParameter();
    }

    @FXML
    private void handleUbahParameter() {
       mainApp.showUbahParameterPSOACOR(psoacor);
    }

    @FXML
    private void handleDetailPrediksi() {
        if (prosesClicked == true) {
            mainApp.showDetailPrediksi(psoacor.hitungPrediksi(), psoacor.hitungMAPE(), psoacor.getWaktuKomputasi());
        } else {
            errorMessage();
        }
    }

    @FXML
    private void handleDetailFitness() {
        if (prosesClicked == true) {
            double[] temp = psoacor.getFitnessIterasi();
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