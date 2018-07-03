package gui;/**
 * Created by Nylon on 7/19/2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kode.ACOR;
import kode.Database;
import kode.PSO;
import kode.PSOACOR;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    public MainApp(){
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Prediksi Konsumsi Energi Primer Indonesia");
        this.primaryStage.getIcons().add(new Image("file:resources/code.png"));

        initRootLayout();
        showPrediksiPSOACOR();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fxml/Root.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            Root controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPrediksiPSOACOR() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fxml/PrediksiPSOACOR.fxml"));
            AnchorPane prediksi = (AnchorPane) loader.load();

            rootLayout.setCenter(prediksi);

            PrediksiPSOACOR controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPrediksiPSO() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fxml/PrediksiPSO.fxml"));
            AnchorPane prediksi = (AnchorPane) loader.load();

            rootLayout.setCenter(prediksi);

            PrediksiPSO controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPrediksiACOR() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fxml/PrediksiACOR.fxml"));
            AnchorPane prediksi = (AnchorPane) loader.load();

            rootLayout.setCenter(prediksi);

            PrediksiACOR controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showUbahParameterPSOACOR(PSOACOR psoacor) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fxml/UbahParameterPSOACOR.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ubah Parameter PSOACOR");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            UbahParameterPSOACOR controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setParameter(psoacor);

            dialogStage.getIcons().add(new Image("file:resources/edit.png"));
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showUbahParameterPSO(PSO pso) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fxml/UbahParameterPSO.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ubah Parameter PSO");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            UbahParameterPSO controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setParameter(pso);

            dialogStage.getIcons().add(new Image("file:resources/edit.png"));
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showUbahParameterACOR(ACOR acor) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fxml/UbahParameterACOR.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ubah Parameter ACOR");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            UbahParameterACOR controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setParameter(acor);

            dialogStage.getIcons().add(new Image("file:resources/edit.png"));
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showDetailPrediksi(double[][] dataPrediksi, double mape, long waktuKomputasi) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fxml/DetailPrediksi.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Detail Prediksi");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.getIcons().add(new Image("file:resources/bar-chart.png"));

            DetailPrediksi controller = loader.getController();
            controller.showData(dataPrediksi);
            controller.showMape(mape);
            controller.showWaktuKomputasi(waktuKomputasi);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDetailFitness(double[] dataFitness) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fxml/DetailFitness.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Detail Fitness");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.getIcons().add(new Image("file:resources/bar-chart.png"));

            DetailFitness controller = loader.getController();
            controller.showData(dataFitness);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLihatData() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fxml/LihatData.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Lihat Data Konsumsi Energi");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.getIcons().add(new Image("file:resources/list.png"));

            LihatData controller = loader.getController();
            Database db = new Database();
            db.openDatabase("dataenergi");
            controller.showData(db.selectData(1967, 2016));
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLihatRegresi() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fxml/LihatRegresi.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Lihat Regresi Terbaik");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.getIcons().add(new Image("file:resources/list.png"));

            LihatRegresi controller = loader.getController();
            Database db = new Database();
            db.openDatabase("dataenergi");
            controller.showDataRegresi(db.selectRegresi(50));
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
