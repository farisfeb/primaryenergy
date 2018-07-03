package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import kode.PSOACOR;
import javafx.stage.Stage;

/**
 * Created by Nylon on 7/19/2017.
 */
public class UbahParameterPSOACOR {

    private PSOACOR psoacor;
    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML private TextField q, xi, w, k, c1, c2, popSize, m, minKonvergen,
            maxIterasi, minTahunLatih, maxTahunLatih, minTahunUji, maxTahunUji,
            xMinIntersep, xMaxIntersep, xMinKoef, xMaxKoef;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setParameter(PSOACOR psoacor) {
        this.psoacor = psoacor;
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

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            psoacor.setQ(Double.parseDouble(q.getText()));
            psoacor.setXi(Double.parseDouble(xi.getText()));
            psoacor.setW(Double.parseDouble(w.getText()));
            psoacor.setK(Double.parseDouble(k.getText()));
            psoacor.setPopSize(Integer.parseInt(popSize.getText()));
            psoacor.setM(Integer.parseInt(m.getText()));
            psoacor.setMinKonvergen(Integer.parseInt(minKonvergen.getText()));
            psoacor.setMaxIterasi(Integer.parseInt(maxIterasi.getText()));

            double c1 = Double.parseDouble(this.c1.getText());
            double c2 = Double.parseDouble(this.c2.getText());
            double[] c1c2 = {c1, c2};
            psoacor.setC1c2(c1c2);

            int[] tahunDataLatih = {Integer.parseInt(minTahunLatih.getText()),
                    Integer.parseInt(maxTahunLatih.getText())};
            psoacor.setTahunLatih(tahunDataLatih);
            int[] tahunDataUji = {Integer.parseInt(minTahunUji.getText()),
                    Integer.parseInt(maxTahunUji.getText())};
            psoacor.setTahunUji(tahunDataUji);

            double xMinKons = Double.parseDouble(this.xMinIntersep.getText());
            double xMaxKons = Double.parseDouble(this.xMaxIntersep.getText());
            double xMinKoef = Double.parseDouble(this.xMinKoef.getText());
            double xMaxKoef = Double.parseDouble(this.xMaxKoef.getText());
            double[] xMin = {xMinKons, xMinKoef, xMinKoef, xMinKoef, xMinKoef, xMinKoef};
            double[] xMax = {xMaxKons, xMaxKoef, xMaxKoef, xMaxKoef, xMaxKoef, xMaxKoef};
            psoacor.setxMin(xMin);
            psoacor.setxMax(xMax);

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (q.getText() == null || q.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (xi.getText() == null || xi.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (w.getText() == null || w.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (k.getText() == null || k.getText().length() == 0){
            errorMessage += "Input salah!\n";
        } else if (c1.getText() == null || c1.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (c2.getText() == null || c2.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (popSize.getText() == null || popSize.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (m.getText() == null || m.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (minKonvergen.getText() == null || minKonvergen.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (maxIterasi.getText() == null || maxIterasi.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (minTahunLatih.getText() == null || minTahunLatih.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (maxTahunLatih.getText() == null || maxTahunLatih.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (minTahunUji.getText() == null || minTahunUji.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (maxTahunUji.getText() == null || maxTahunUji.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (xMinIntersep.getText() == null || xMinIntersep.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (xMaxIntersep.getText() == null || xMaxIntersep.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (xMinKoef.getText() == null || xMinKoef.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (xMaxKoef.getText() == null || xMaxKoef.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else {
            try {
                Integer.parseInt(popSize.getText());
                Integer.parseInt(m.getText());
                Integer.parseInt(minTahunLatih.getText());
                Integer.parseInt(maxTahunLatih.getText());
                Integer.parseInt(minTahunUji.getText());
                Integer.parseInt(maxTahunUji.getText());
                Integer.parseInt(minKonvergen.getText());
                Integer.parseInt(maxIterasi.getText());

                Double.parseDouble(q.getText());
                Double.parseDouble(xi.getText());
                Double.parseDouble(w.getText());
                Double.parseDouble(k.getText());
                Double.parseDouble(c1.getText());
                Double.parseDouble(c2.getText());
                Double.parseDouble(xMinIntersep.getText());
                Double.parseDouble(xMaxIntersep.getText());
                Double.parseDouble(xMinKoef.getText());
                Double.parseDouble(xMaxKoef.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Input salah!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Input Salah");
            alert.setHeaderText("Harap perbaiki input yang salah!");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
