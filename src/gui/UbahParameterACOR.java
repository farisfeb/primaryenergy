package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kode.ACOR;

/**
 * Created by Nylon on 7/19/2017.
 */
public class UbahParameterACOR {

    private ACOR acor;
    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML private TextField  q, xi, archiveSize, m, minKonvergen, maxIterasi,
            minTahunLatih, maxTahunLatih, minTahunUji, maxTahunUji,
            xMinIntersep, xMaxIntersep, xMinKoef, xMaxKoef;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setParameter(ACOR acor) {
        this.acor = acor;
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

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            acor.setQ(Double.parseDouble(q.getText()));
            acor.setXi(Double.parseDouble(xi.getText()));
            acor.setArchiveSize(Integer.parseInt(archiveSize.getText()));
            acor.setM(Integer.parseInt(m.getText()));
            acor.setMinKonvergen(Integer.parseInt(minKonvergen.getText()));
            acor.setMaxIterasi(Integer.parseInt(maxIterasi.getText()));

            int[] tahunDataLatih = {Integer.parseInt(minTahunLatih.getText()),
                    Integer.parseInt(maxTahunLatih.getText())};
            acor.setTahunLatih(tahunDataLatih);
            int[] tahunDataUji = {Integer.parseInt(minTahunUji.getText()),
                    Integer.parseInt(maxTahunUji.getText())};
            acor.setTahunUji(tahunDataUji);

            double xMinKons = Double.parseDouble(this.xMinIntersep.getText());
            double xMaxKons = Double.parseDouble(this.xMaxIntersep.getText());
            double xMinKoef = Double.parseDouble(this.xMinKoef.getText());
            double xMaxKoef = Double.parseDouble(this.xMaxKoef.getText());
            double[] xMin = {xMinKons, xMinKoef, xMinKoef, xMinKoef, xMinKoef, xMinKoef};
            double[] xMax = {xMaxKons, xMaxKoef, xMaxKoef, xMaxKoef, xMaxKoef, xMaxKoef};
            acor.setsMin(xMin);
            acor.setsMax(xMax);

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

        if (q.getText() == null || q.getText().length() == 0){
            errorMessage += "Input salah!\n";
        } else if (xi.getText() == null || xi.getText().length() == 0) {
            errorMessage += "Input salah!\n";
        } else if (archiveSize.getText() == null || archiveSize.getText().length() == 0) {
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
                Integer.parseInt(archiveSize.getText());
                Integer.parseInt(m.getText());
                Integer.parseInt(minTahunLatih.getText());
                Integer.parseInt(maxTahunLatih.getText());
                Integer.parseInt(minTahunUji.getText());
                Integer.parseInt(maxTahunUji.getText());
                Integer.parseInt(minKonvergen.getText());
                Integer.parseInt(maxIterasi.getText());

                Double.parseDouble(q.getText());
                Double.parseDouble(xi.getText());
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
