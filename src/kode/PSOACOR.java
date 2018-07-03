package kode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class PSOACOR {
    private double[] c1c2;
    private double w;
    private double k;
    private double q;
    private double xi;
    private int m;
    private int popSize;
    private double[][] v;
    private double[][] pbest;
    private double[] fitnessPbest;
    private double[][] x;
    private double[] fitnessPartikel;
    private double[][] xTemp;
    private double[] fitnessPartikelTemp;
    private double[][] semutBaru;
    private double[] fitnessSemutBaru;
    private double[] omega;
    private double[] prob;
    private double[] probCum;
    private double[] gbest;
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

    public PSOACOR(double[] c1c2, double w, double k, double q, double xi, int m, int popSize, int minKonvergen,
                   int maxIterasi, double[] xMin, double[] xMax, int[] tahunLatih, int[] tahunUji) {
        this.c1c2 = c1c2;
        this.w = w;
        this.k = k;
        this.q = q;
        this.xi = xi;
        this.m = m;
        this.popSize = popSize;
        fitnessIterasi = new ArrayList<>();
        this.minKonvergen = minKonvergen;
        isKonvergen = false;
        this.maxIterasi = maxIterasi;
        this.xMin = xMin;
        this.xMax = xMax;
        this.tahunLatih = tahunLatih;
        this.tahunUji = tahunUji;
        dimensi = xMin.length;
        db.openDatabase("dataenergi");
        dataLatih = db.selectData(tahunLatih[0], tahunLatih[1]);
        dataUji = db.selectData(tahunUji[0], tahunUji[1]);
    }

    public void inisialisasiPSOACOR(){
        startCpuTime = getCpuTime();
        inisialisasiKecepatan();
        inisialisasiPartikel();
        inisialisasiPbest();
        inisilisasiGbest();
        hitungOmega();
        hitungProb();
        System.out.println("Inisialisasi PSOACOR berhasil");
    }

    public void iterasiPSOACOR(){
        while(isKonvergen != true && maxIterasi != iterasi) {
            iterasi++;
            System.out.println("ITERASI " + iterasi);
            System.out.println("Tahap PSO");
            updateKecepatan();
            updatePosisi();
            updatePbestPSO();
            updateGbestPSO();
            System.out.println("Tahap ACOR");
            buatSemutBaru();
            replacePartikelPbestACOR();
            updateGbestACOR();
            isKonvergen(minKonvergen);
        }
        taskCpuTime = getCpuTime() - startCpuTime;
    }

    public void inisialisasiPartikel(){
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
        System.out.println("Inisialisasi pbest");
        printPartikel(pbest, fitnessPbest);
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
        System.out.println("Inisialisasi gbest");
        printGbest();
    }

    public void hitungOmega(){
        omega = new double[popSize];
        System.out.println("Omega");
        for (int i = 0; i < popSize; i++) {
            omega[i] = 1 / (q * popSize * Math.sqrt(2 * Math.PI)) *
                    Math.exp(-(Math.pow(i, 2) / 2 * Math.pow(q, 2) *
                            Math.pow(popSize, 2)));
            System.out.printf("%-5s%1d %5.10f%n", "omega ke-", i, omega[i]);
        }
    }

    public void hitungProb () {
        double sumOmega = 0;
        for (int i = 0; i < omega.length; i++){
            sumOmega += omega[i];
        }

        System.out.println("Peluang P");
        prob = new double[popSize];
        probCum = new double[popSize];
        double tempSum = 0;
        for (int i = 0; i < popSize; i++){
            prob[i] = omega[i]/sumOmega;
            tempSum += prob[i];
            probCum[i] = tempSum;
            System.out.printf("%4s%1d %1s %1.9f %7s%1d %1s %1.9f%n",
                    "prob", i+1, "=", prob[i], "probCum", i+1, "=", probCum[i]);
        }
    }

    public void updateKecepatan() {
        System.out.println("Update Kecepatan");
        for (int i = 0; i < popSize; i++) {
            System.out.print("v" + (i+1));
            for (int d = 0; d < dimensi; d++) {
                double r1 = rand.nextDouble();
                double r2 = rand.nextDouble();
//                double r1 = 0.27;
//                double r2 = 0.86;
                v[i][d] = w * v[i][d] + c1c2[0] * r1 * (pbest[i][d] - x[i][d])
                        + c1c2[1] * r2 * (gbest[d] - x[i][d]);

                //limit kecepatan
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
                //limit partikel baru
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

    public void updatePbestPSO() {
        for (int i = 0; i < popSize; i++) {
            double tempFitnessPbest = fitnessPartikel[i];
            if (tempFitnessPbest < fitnessPbest[i]) {
                pbest[i] = Arrays.copyOf(x[i], dimensi);
                fitnessPbest[i] = tempFitnessPbest;
            }
        }
        System.out.println("Update Pbest tahap PSO");
        printPartikel(pbest, fitnessPbest);
    }

    public void updateGbestPSO() {
        for (int i = 0; i < popSize; i++) {
            double tempFitnessGbest = fitnessPbest[i];
            if (tempFitnessGbest < fitnessGbest) {
                gbest = Arrays.copyOf(pbest[i], dimensi);
                fitnessGbest = tempFitnessGbest;
            }
        }
        System.out.println("Update gbest tahap PSO");
        printGbest();
    }

    public int cariPartikelTerburuk() {
        int indexTerburuk = 0;
        double tempFitnessTerburuk = fitnessPartikel[0];
        for (int i = 1; i < popSize; i++) {
            if (tempFitnessTerburuk < fitnessPartikel[i]){
                tempFitnessTerburuk = fitnessPartikel[i];
                indexTerburuk = i;
            }
        }
        return indexTerburuk;
    }

    public void urutPartikel() {
        xTemp = new double[popSize][dimensi];
        fitnessPartikelTemp = new double[popSize];
        for (int i = 0; i < popSize; i++) {
            xTemp[i] = Arrays.copyOf(x[i], dimensi);
            fitnessPartikelTemp[i] = fitnessPartikel[i];
        }

        for (int i = 0; i < xTemp.length; i++) {
            for(int j = i + 1; j < xTemp.length; j++) {
                if (fitnessPartikelTemp[j] < fitnessPartikelTemp[i]) {
                    double tempFitness = fitnessPartikelTemp[i];
                    fitnessPartikelTemp[i] = fitnessPartikelTemp[j];
                    fitnessPartikelTemp[j] = tempFitness;

                    double[] tempSolution = Arrays.copyOf(xTemp[i], dimensi);
                    xTemp[i] = Arrays.copyOf(xTemp[j], dimensi);
                    xTemp[j] = Arrays.copyOf(tempSolution, dimensi);
                }
            }
        }
        System.out.println("Partikel Terurut");
        printPartikel(xTemp, fitnessPartikelTemp);
    }

    public double[] rouletteWheel(){
        double random = rand.nextDouble();
//        double random = 0.42;
        System.out.printf("%13s %1.5f%n","rand roulette", random);
        int rouletteIndex = 0;
        for (int i = 0; i < popSize; i++){
            if (random <= probCum[i]){
                rouletteIndex = i;
                break;
            }
        }
        double[] xTerpilih = Arrays.copyOf(xTemp[rouletteIndex], dimensi);
        System.out.printf("%-9s%1d ", "xTerpilih", (rouletteIndex  + 1));
        for (int d = 0; d < dimensi; d++) {
            System.out.printf("%5.2f   ", xTerpilih[d]);
        }
        System.out.println();
        return xTerpilih;
    }

    public double[] hitungSigma(double[] seleksi){
        double[] selisih = new double[dimensi];
        double[] sigma = new double[dimensi];
        System.out.printf("%-11s ", "sigma");
        for (int d = 0; d < dimensi; d++) {
            for (int i = 0; i < popSize; i++){
                selisih[d] += (Math.abs(x[i][d] - seleksi[d])) / (popSize - 1);
            }
            sigma[d] = xi * selisih[d];
            System.out.printf("%5.2f   ", sigma[d]);
        }
        System.out.println();
        return sigma;
    }

    public void buatSemutBaru() {
        urutPartikel();
        semutBaru = new double[m][dimensi];
        fitnessSemutBaru = new double[m];
        System.out.println("Semut Baru");
        for(int i = 0; i < m; i++){
            double[] xTerpilih = rouletteWheel();
            double[] sigma = hitungSigma(xTerpilih);
            System.out.print("s" + (i+1));
            for(int d = 0; d < dimensi; d++){
                semutBaru[i][d] = xTerpilih[d] + rand.nextGaussian() * sigma[d];
//                semutBaru[i][d] = xTerpilih[d] + 0.87 * sigma[d];

                //limit semut baru
                if (semutBaru[i][d] >= xMax[d]){
                    semutBaru[i][d] = xMax[d];
                }else if (semutBaru[i][d] <= xMin[d]){
                    semutBaru[i][d] = xMin[d];
                }
                System.out.printf(" %5.2f  ", semutBaru[i][d]);
            }
            fitnessSemutBaru[i] = fitness.hitungFitness(semutBaru[i], dataLatih);
            System.out.printf("%5.1f%n", fitnessSemutBaru[i]);
        }
    }

    public void replacePartikelPbestACOR(){
        System.out.println("Replace partikel terburuk dan update pbest tahap ACOR");
        for(int i = 0; i < m; i++) {
            int xTerburuk = cariPartikelTerburuk();
            if (fitnessSemutBaru[i] < fitnessPartikel[xTerburuk]) {
                //replace partikel terburuk dengan semut baru
                x[xTerburuk] = Arrays.copyOf(semutBaru[i], dimensi);
                fitnessPartikel[xTerburuk] = fitnessSemutBaru[i];

                //update pbest tahap ACOR
                double tempFitnessPbest = fitnessSemutBaru[i];
                if (tempFitnessPbest < fitnessPbest[xTerburuk]) {
                    fitnessPbest[xTerburuk] = tempFitnessPbest;
                    pbest[xTerburuk] = Arrays.copyOf(x[xTerburuk], dimensi);
                }
            }
            printUpdateParPbestACOR(xTerburuk);
        }
    }

    public void updateGbestACOR(){
        for(int i = 0; i < m; i++) {
            if (fitnessSemutBaru[i] < fitnessGbest) {
                fitnessGbest = fitnessSemutBaru[i];
                gbest = Arrays.copyOf(semutBaru[i], dimensi);
            }
        }
        fitnessIterasi.add(fitnessGbest);
        System.out.println("Update gbest tahap ACOR");
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
        db.saveRegresi(gbest, hitungMAPE(), 1);
    }

    public void printPartikel(double[][] x, double[] fitness){
        for (int i = 0; i < x.length; i++) {
            System.out.print(i+1);
            for (int d = 0; d < dimensi; d++) {
                System.out.printf(" %5.2f  ", x[i][d]);
            }
            System.out.printf("%5.1f%n", fitness[i]);
        }
    }

    public void printUpdateParPbestACOR(int i){
        System.out.printf("%1s%-6d", "x", (i + 1));
        for (int d = 0; d < dimensi; d++) {
            System.out.printf(" %5.2f  ", x[i][d]);
        }
        System.out.printf("%5.1f%n", fitnessPartikel[i]);
        System.out.printf("%5s%-3d", "pbest", (i + 1));
        for (int d = 0; d < dimensi; d++) {
            System.out.printf(" %5.2f  ", pbest[i][d]);
        }
        System.out.printf("%5.1f%n", fitnessPbest[i]);
    }

    public void printGbest() {
        for (int d = 0; d < dimensi; d++) {
            System.out.printf("%5.5f   ", gbest[d]);
        }
        System.out.printf("%5.1f%n", fitnessGbest);
    }

    public void printFitnessIterasi() {
        System.out.println("Fitness Iterasi");
        for (int i = 0; i < fitnessIterasi.size(); i++) {
            System.out.printf("%5.4f%n", fitnessIterasi.get(i));
        }
    }

    public double getQ() {
        return q;
    }
    public double getXi() {
        return xi;
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
    public int getM() {
        return m;
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
    public void setQ(double q) {
        this.q = q;
    }
    public void setXi(double xi) {
        this.xi = xi;
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
    public void setM(int m) {
        this.m = m;
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
        PSOACOR psoacor = new PSOACOR(c1c2,0.7298,0.6,0.007,1,10, 120,
                100,2000, xMin, xMax, tahunLatih, tahunUji);
        psoacor.inisialisasiPSOACOR();
        psoacor.iterasiPSOACOR();
//        psoacor.printFitnessIterasi();
        psoacor.hitungPrediksi();
        psoacor.hitungMAPE();
//        psoacor.saveRegresi();
        System.out.println("Waktu Komputasi " + psoacor.getWaktuKomputasi() + "ms");
        System.out.println("Iterasi " + psoacor.getIterasi());
        System.out.println("IsKonvergen " + psoacor.isKonvergen());
    }
}
