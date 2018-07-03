package kode;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PSO {
    private double[] c1c2;
    private double w;
    private double k;
    private int popSize;
    private double[][] x;
    private double[][] v;
    private double[][] pbest;
    private double[] gbest;
    private double[] fitnessPartikel;
    private double[] fitnessPbest;
    private double fitnessGbest;
    private int dimensi;
    private double[] xMin;
    private double[] xMax;
    private double[] vMin;
    private double[] vMax;
    private int iterasi;
    private ArrayList<Double> fitnessIterasi;
    private int minKonvergen;
    private boolean isKonvergen;
    private int maxIterasi;
    private double[][] dataLatih;
    private double[][] dataUji;
    private int[] tahunLatih;
    private int[] tahunUji;
    private Database db = new Database();
    private Fitness fitness = new Fitness();
    private Random rand = new Random();
    private long startCpuTime;
    private long taskCpuTime;

    public PSO(double w, double k, int popSize, int minKonvergen, int maxIterasi,
               double[] c1c2, double[] xMin, double[] xMax, int[] tahunLatih, int[] tahunUji) {
        this.c1c2 = c1c2;
        this.w = w;
        this.k = k;
        this.popSize = popSize;
        fitnessIterasi = new ArrayList<>();
        this.minKonvergen = minKonvergen;
        isKonvergen = false;
        this.maxIterasi = maxIterasi;
        this.xMin = xMin;
        this.xMax = xMax;
        this.tahunLatih = tahunLatih;
        this.tahunUji = tahunUji;
        this.dimensi = xMin.length;
        db.openDatabase("dataenergi");
        dataLatih = db.selectData(tahunLatih[0], tahunLatih[1]);
        dataUji = db.selectData(tahunUji[0], tahunUji[1]);
    }

    public void inisialisasiPSO(){
        startCpuTime = getCpuTime();
        inisialisasiKecepatan();
        inisialisasiPartikel();
        inisialisasiPbest();
        inisilisasiGbest();
        System.out.println("Inisialisasi PSO berhasil");
    }

    public void iterasiPSO() {
        while(isKonvergen != true && maxIterasi != iterasi) {
            iterasi++;
            System.out.println("ITERASI " + iterasi);
            updateKecepatan();
            updatePosisi();
            updatePbest();
            updateGbest();
            isKonvergen(minKonvergen);
        }
        taskCpuTime = getCpuTime() - startCpuTime;
    }

    public void inisialisasiPartikel() {
        x = new double[popSize][dimensi];
        fitnessPartikel = new double[popSize];
        double x[][] = {
                {-63.27, -1.62, 1.33, -1.77, 3.39, 1.87},
                {-61.16, 0.63, 4.88, -4.03, -2.42, 0.74},
                {-41.39, 2.01, -5.27, 3.22, 3.16, -4.75},
                {-62.10, -3.80, -3.27, 3.58, 2.39, 2.01},
                {-57.70, 2.71, -0.58, -1.23, 1.54, 0.62},
                {-46.07, 2.26, 0.13, 1.58, 0.09, -4.25},
                {-59.50, 2.75, -4.46, -0.08, 3.44, 2.41},
                {-73.80, 0.34, 0.89, 2.71, -3.36, -1.40},
                {-75.73, -5.76, -2.61, 6.14, 0.56, 0.99},
                {-54.32, 9.39, -3.63, -3.73, 6.90, -1.27}};
//        this.x = x;
        System.out.println("Inisialisasi Partikel");
        for (int i = 0; i < popSize; i++) {
            System.out.print("x" + (i+1));
            for (int d = 0; d < dimensi; d++) {
                this.x[i][d] = xMin[d] + (rand.nextDouble() * (xMax[d] - xMin[d]));
                System.out.printf(" %5.2f  ", this.x[i][d]);
            }
            fitnessPartikel[i] = fitness.hitungFitness(this.x[i], dataLatih);
            System.out.printf("%5.1f%n", fitnessPartikel[i]);
        }
    }

    public void inisialisasiKecepatan() {
        System.out.println("Inisialisasi Kecepatan");
        velocityClamping();
        v = new double[popSize][dimensi];
        for (int i = 0; i < popSize; i++) {
            System.out.print("v" + (i+1));
            for (int d = 0; d < dimensi; d++) {
                v[i][d] = 0;
                System.out.printf(" %5.2f  ", v[i][d]);
            }
            System.out.println();
        }
    }

    public void velocityClamping() {
        vMin = new double[dimensi];
        vMax = new double[dimensi];
        for (int d = 0; d < dimensi; d++) {
            vMax[d] = k * ((xMax[d] - xMin[d]) / 2);
            vMin[d] = -vMax[d];
            System.out.println("vMax b" + d + " = " + vMax[d] + " vMin b" + d + " = " + vMin[d]);
        }
    }

    public void inisialisasiPbest() {
        pbest = new double[popSize][dimensi];
        fitnessPbest = new double[popSize];
        for (int i = 0; i < popSize; i++) {
            pbest[i] = Arrays.copyOf(x[i], dimensi);
            fitnessPbest[i] = fitnessPartikel[i];
        }
        System.out.println("Inisialisasi Pbest");
        printPbest(pbest, fitnessPbest);
    }

    public void inisilisasiGbest() {
        gbest = pbest[0];
        fitnessGbest = fitnessPbest[0];
        for (int i = 1; i < popSize; i++) {
            double[] tempGbest = pbest[i];
            double tempFitnessGbest = fitnessPbest[i];
            if (tempFitnessGbest < fitnessGbest) {
                gbest = tempGbest;
                fitnessGbest = tempFitnessGbest;
            }
        }
        fitnessIterasi.add(fitnessGbest);
        printGbest();
    }

    public void updateKecepatan() {
        System.out.println("Update Kecepatan");
        for (int i = 0; i < popSize; i++) {
            System.out.print("v" + (i+1));
            for (int d = 0; d < dimensi; d++) {
                double r1 = rand.nextDouble();
                double r2 = rand.nextDouble();
//                    double r1 = 0.27;
//                    double r2 = 0.86;
                v[i][d] = w * v[i][d] + c1c2[0] * r1 * (pbest[i][d] - x[i][d])
                        + c1c2[1] * r2 * (gbest[d] - x[i][d]);

                //velocity clamping
                if (v[i][d] >= vMax[d]){
                    v[i][d] = vMax[d];
                } else if (v[i][d] <= vMin[d]){
                    v[i][d] = vMin[d];
                }
                System.out.printf(" %5.2f   ", v[i][d]);
            }
            System.out.println();
        }
    }

    public void updatePosisi() {
        System.out.println("Update Posisi Partikel");
        for (int i = 0; i < popSize; i++) {
            System.out.print("x" + (i+1));
            for (int d = 0; d < dimensi; d++) {
                x[i][d] = x[i][d] + v[i][d];
                //limit posisi
                if (x[i][d] >= xMax[d]){
                    x[i][d] = xMax[d];
                } else if (x[i][d] <= xMin[d]){
                    x[i][d] = xMin[d];
                }
                System.out.printf(" %5.2f  ", x[i][d]);
            }
            fitnessPartikel[i] = fitness.hitungFitness(x[i], dataLatih);
            System.out.printf("%5.1f%n", fitnessPartikel[i]);
        }
    }

    public void updatePbest() {
        for (int i = 0; i < popSize; i++) {
            double tempPbestfitness = fitnessPartikel[i];
            if (tempPbestfitness < fitnessPbest[i]) {
                pbest[i] = Arrays.copyOf(x[i], dimensi);
                fitnessPbest[i] = tempPbestfitness;
            }
        }
        System.out.println("Update Pbest");
        printPbest(pbest, fitnessPbest);
    }

    public void updateGbest() {
        for (int i = 0; i < popSize; i++) {
            double tempGbestfitness = fitnessPbest[i];
            if (tempGbestfitness < fitnessGbest) {
                gbest = Arrays.copyOf(pbest[i], dimensi);
                fitnessGbest = tempGbestfitness;
            }
        }
        fitnessIterasi.add(fitnessGbest);
        printGbest();
    }

    public void isKonvergen(int minKonvergen){
        if((iterasi-minKonvergen) >= 0){
            int counter = 0;
            for (int i = iterasi; i > (iterasi-minKonvergen); i--) {
                if (fitnessGbest == fitnessIterasi.get(i)){
                    counter++;
                }
            }
            if (counter == minKonvergen){
                isKonvergen = true;
                System.out.println("counter = " + counter);
            }
        }
    }

    public double[][] hitungPrediksi(){
        System.out.println("Nilai data uji");
        return fitness.hitungPrediksi(gbest, dataUji);
    }

    public double hitungMAPE(){
        return fitness.hitungMAPE(gbest, dataUji);
    }

    public long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.getCurrentThreadCpuTime();
    }

    public void saveRegresi(){
        Database db = new Database();
        db.openDatabase("dataenergi");
        db.saveRegresi(gbest, hitungMAPE(), 2);
    }

    public void printPbest(double[][] x, double[] fitness) {
        for (int i = 0; i < x.length; i++) {
            System.out.print("x" + (i+1));
            for (int d = 0; d < dimensi; d++) {
                System.out.printf(" %5.2f   ", x[i][d]);
            }
            System.out.printf("%5.1f", fitness[i]);
            System.out.println();
        }
    }

    public void printGbest() {
        System.out.println("Gbest");
        for (int d = 0; d < dimensi; d++) {
            System.out.printf("%5.5f   ", gbest[d]);
        }
        System.out.printf("%5.1f", fitnessGbest);
        System.out.println();
    }

    public void printFitnessIterasi() {
        System.out.println("Fitness Iterasi");
        for (int i = 0; i < fitnessIterasi.size(); i++) {
            System.out.printf("%5.4f%n", fitnessIterasi.get(i));
        }
    }

    public double getW() {
        return w;
    }
    public double getK() {
        return k;
    }
    public double[] getxMin() {
        return xMin;
    }
    public double[] getxMax() {
        return xMax;
    }
    public double[] getFitnessIterasi() {
        double[] fitnessIterasi = new double[this.fitnessIterasi.size()];
        for (int i = 0; i < this.fitnessIterasi.size(); i++){
            fitnessIterasi[i] = this.fitnessIterasi.get(i);
        }
        return fitnessIterasi;
    }
    public int getMinKonvergen() {
        return minKonvergen;
    }
    public int getMaxIterasi() {
        return maxIterasi;
    }
    public double[] getC1c2() {
        return c1c2;
    }
    public int getPopSize() {
        return popSize;
    }
    public int[] getTahunLatih() {
        return tahunLatih;
    }
    public int[] getTahunUji() {
        return tahunUji;
    }
    public double[] getGbest() {
        return gbest;
    }
    public int getIterasi() {
        return iterasi;
    }
    public long getWaktuKomputasi() {
        return taskCpuTime/ 1000000;
    }
    public boolean isKonvergen() {
        return isKonvergen;
    }
    public void setK(double k) {
        this.k = k;
    }
    public void setW(double w) {
        this.w = w;
    }
    public void setxMin(double[] xMin) {
        this.xMin = xMin;
    }
    public void setxMax(double[] xMax) {
        this.xMax = xMax;
    }
    public void setMinKonvergen(int minKonvergen) {
        this.minKonvergen = minKonvergen;
    }
    public void setMaxIterasi(int maxIterasi) {
        this.maxIterasi = maxIterasi;
    }
    public void setC1c2(double[] c1c2) {
        this.c1c2 = c1c2;
    }
    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }
    public void setTahunLatih(int[] tahunLatih) {
        this.tahunLatih = tahunLatih;
    }
    public void setTahunUji(int[] tahunUji) {
        this.tahunUji = tahunUji;
    }

    public static void main(String[] args) {
        double[] xMin = {-100, -10, -10, -10, -10, -10};
        double[] xMax = {100, 10, 10, 10, 10, 10};
        double[] c1c2 = {1.496, 1.496};
        int[] tahunLatih = {1967, 2006};
        int[] tahunUji = {2007, 2016};
        PSO pso = new PSO(0.7298, 0.6, 130, 100,2000,
                c1c2, xMin, xMax, tahunLatih, tahunUji);
        pso.inisialisasiPSO();
        pso.iterasiPSO();
//        pso.printFitnessIterasi();
        pso.hitungPrediksi();
        pso.hitungMAPE();
//        pso.saveRegresi();
        System.out.println("Waktu Komputasi " + pso.getWaktuKomputasi() + "ms");
        System.out.println("Iterasi " + pso.getIterasi());
        System.out.println("IsKonvergen " + pso.isKonvergen());
    }
}
