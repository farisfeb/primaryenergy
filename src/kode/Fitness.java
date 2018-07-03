package kode;

/**
 * Created by Nylon on 6/4/2017.
 */
public class Fitness {

    //fitness selisih kuadrat nilai aktual dg prediksi
    public double hitungFitness(double[] x, double[][] dataLatih) {
        double fitness = 0;
        for (int i = 0; i < dataLatih.length; i++) {
            double nilaiAktual = dataLatih[i][1];
            double nilaiPrediksi = x[0] + (x[1] * dataLatih[i][2]) +
                    (x[2] * dataLatih[i][3]) + (x[3] * dataLatih[i][4]) +
                    (x[4] * dataLatih[i][5]) + (x[5] * dataLatih[i][6]);
            fitness += Math.pow((nilaiAktual - nilaiPrediksi), 2);
        }
        return fitness;
    }

    public double[][] hitungPrediksi(double[] x, double[][] dataUji) {
        double[][] data = new double[dataUji.length][4];
        for (int i = 0; i < dataUji.length; i++) {
            data[i][0] = dataUji[i][0];//tahun
            data[i][1] = dataUji[i][1];//nilai aktual
            //nilai prediksi
            data[i][2] = x[0] + (x[1] * dataUji[i][2]) + (x[2] *
                    dataUji[i][3]) + (x[3] * dataUji[i][4]) +
                    (x[4] * dataUji[i][5]) + (x[5] * dataUji[i][6]);
            //selisih
            data[i][3] = Math.abs(data[i][1] - data[i][2]);
            System.out.printf("%4.0f %14s %5.2f %16s %5.2f %9s %5.2f%n",
                    data[i][0] , "nilai aktual =", data[i][1], "nilai prediksi =", data[i][2],
                    "selisih =", data[i][3]);
        }
        return data;
    }

    public double hitungMAPE(double[] x, double[][] dataUji) {
        double selisih = 0;
        for (int i = 0; i < dataUji.length; i++) {
            double nilaiAktual = dataUji[i][1];
            double nilaiPrediksi = x[0] + (x[1] * dataUji[i][2]) + (x[2] *
                    dataUji[i][3]) + (x[3] * dataUji[i][4]) +
                    (x[4] * dataUji[i][5]) + (x[5] * dataUji[i][6]);
            selisih += Math.abs(nilaiAktual - nilaiPrediksi) / nilaiAktual;
        }
        double mape = selisih / dataUji.length * 100;
        System.out.printf("%9s %5.2f%n", "MAPE(%) =", mape);
        return mape;
    }
}
