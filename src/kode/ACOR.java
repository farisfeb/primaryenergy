package kode;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Nylon on 6/4/2017.
 */
public class ACOR {
    private double q;
    private double xi;
    private int m;
    private int archiveSize;
    private double[][] s;
    private double[] fitnessSolusi;
    private double[] omega;
    private double[] prob;
    private double[] probCum;
    private double[] sbest;
    private double fitnessSbest;
    private int dimensi;
    private double[] sMin;
    private double[] sMax;
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

    public ACOR(double q, double xi, int m, int archiveSize, int minKonvergen, int maxIterasi,
                double[] sMin, double[] sMax, int[] tahunLatih, int[] tahunUji) {
        this.q = q;
        this.xi = xi;
        this.m = m;
        this.archiveSize = archiveSize;
        fitnessIterasi = new ArrayList<>();
        this.minKonvergen = minKonvergen;
        isKonvergen = false;
        this.maxIterasi = maxIterasi;
        this.sMin = sMin;
        this.sMax = sMax;
        this.tahunLatih = tahunLatih;
        this.tahunUji = tahunUji;
        dimensi = sMin.length;
        db.openDatabase("dataenergi");
        dataLatih = db.selectData(tahunLatih[0], tahunLatih[1]);
        dataUji = db.selectData(tahunUji[0], tahunUji[1]);
    }

    public void inisialisasiACOR(){
        startCpuTime = getCpuTime();
        inisialisasiSolusi();
        sortSolusi(s, fitnessSolusi);
        updateSbest();
        hitungOmega();
        hitungProb();
        System.out.println("Inisialisasi ACOR berhasil");
    }

    public void iterasiACOR(){
        while(isKonvergen != true && maxIterasi != iterasi) {
            iterasi++;
            System.out.println("ITERASI " + iterasi);
            buatSemutBaru();
            updateSbest();
            isKonvergen(minKonvergen);
        }
        taskCpuTime = getCpuTime() - startCpuTime;
    }

    public void inisialisasiSolusi(){
        s = new double[archiveSize][dimensi];
        fitnessSolusi = new double[archiveSize];
        double s[][] = {
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
//        this.s = s;
        System.out.println("Inisialisasi Solusi");
        for (int i = 0; i < archiveSize; i++) {
            for (int d = 0; d < dimensi; d++) {
                this.s[i][d] = sMin[d] + (rand.nextDouble() * (sMax[d] - sMin[d]));
                System.out.printf(" %5.2f  ", this.s[i][d]);
            }
            fitnessSolusi[i] = fitness.hitungFitness(this.s[i], dataLatih);
            System.out.printf("%5.1f%n", fitnessSolusi[i]);
        }
    }

    void sortSolusi(double[][] solusi, double[] fitness) {
        for (int i = 0; i < solusi.length; i++) {
            for(int j = i + 1; j < solusi.length; j++) {
                if (fitness[j] < fitness[i]) {
                    double tempFitness = fitness[i];
                    fitness[i] = fitness[j];
                    fitness[j] = tempFitness;

                    double[] tempSolusi = Arrays.copyOf(solusi[i], dimensi);
                    solusi[i] = Arrays.copyOf(solusi[j], dimensi);
                    solusi[j] = Arrays.copyOf(tempSolusi, dimensi);
                }
            }
        }
        System.out.println("Merge dan sort solusi");
        printSolusi(solusi, fitness);
    }

    void updateSbest(){
        sbest = Arrays.copyOf(s[0], dimensi);
        fitnessSbest = fitnessSolusi[0];
        fitnessIterasi.add(fitnessSbest);
        printSbest();
    }

    void hitungOmega(){
        omega = new double[archiveSize];
        System.out.println("Omega");
        for (int i = 0; i < archiveSize; i++) {
            omega[i] = 1 / (q * archiveSize * Math.sqrt(2 * Math.PI))
                    * Math.exp(-(Math.pow(i, 2) / 2 * Math.pow(q, 2)
                    * Math.pow(archiveSize, 2)));
            System.out.printf("%-5s%1d %5.10f%n", "omega ke-", i+1, omega[i]);
        }
    }

    void hitungProb () {
        double sumOmega = 0;
        for (int i = 0; i < omega.length; i++){
            sumOmega += omega[i];
        }

        System.out.println("Peluang P");
        prob = new double[archiveSize];
        probCum = new double[archiveSize];
        double tempSum = 0;
        for (int i = 0; i < archiveSize; i++){
            prob[i] = omega[i]/sumOmega;
            tempSum += prob[i];
            probCum[i] = tempSum;
            System.out.printf("%4s%1d %1s %1.9f %7s%1d %1s %1.9f%n",
                    "prob", i+1, "=", prob[i], "probCum", i+1, "=", probCum[i]);
        }
    }

    public double[] rouletteWheel(){
        double random = rand.nextDouble();
//        double random = 0.42;
        System.out.printf("%13s %1.5f%n","rand roulette", random);
        int rouletteIndex = 0;
        for (int i = 0; i < archiveSize; i++){
            if (random <= probCum[i]){
                rouletteIndex = i;
                break;
            }
        }
        double[] sTerpilih = Arrays.copyOf(s[rouletteIndex], dimensi);
        System.out.printf("%-9s%1d ", "sTerpilih", (rouletteIndex  + 1));
        for (int d = 0; d < dimensi; d++ ) {
            System.out.printf("%5.2f   ", sTerpilih [d]);
        }
        System.out.println();
        return sTerpilih;
    }

    public double[] hitungSigma(double[] seleksi){
        double[] selisih = new double[dimensi];
        double[] sigma = new double[dimensi];
        System.out.printf("%-12s ", "sigma");
        for (int d = 0; d < dimensi; d++) {
            for (int i = 0; i < archiveSize; i++) {
                selisih[d] += (Math.abs(s[i][d] - seleksi[d])) / (archiveSize - 1);
            }
            sigma[d] = xi * selisih[d];
            System.out.printf("%5.2f   ", sigma[d]);
        }
        System.out.println();
        return sigma;
    }

    double[][] mergeSolusi(double[][] solusi1, double[][] solusi2){
        double[][] mergeSolusi = new double[solusi1.length + solusi2.length][];
        int i;
        for(i = 0; i < solusi1.length; i++){
            mergeSolusi[i] = Arrays.copyOf(solusi1[i], dimensi);
        }

        for(int j = 0; j < solusi2.length; j++){
            mergeSolusi[i++] = Arrays.copyOf(solusi2[j], dimensi);
        }
        return mergeSolusi;
    }

    double[] mergeFitness(double[] fitness1, double[] fitness2){
        double[] mergeFitness = new double[fitness1.length + fitness2.length];
        int i;
        for(i = 0; i < fitness1.length; i++){
            mergeFitness[i] = fitness1[i];
        }

        for(int j = 0; j < fitness2.length; j++){
            mergeFitness[i++] = fitness2[j];
        }
        return mergeFitness;
    }

    void buatSemutBaru() {
        double[][] semutBaru = new double[this.m][dimensi];
        double[] fitnessSemutBaru = new double[this.m];
        System.out.println("Semut baru");
        for(int i = 0; i < this.m; i++){
            double[] sTerpilih = rouletteWheel();
            double[] sigma = hitungSigma(sTerpilih);
            for(int d = 0; d < dimensi; d++){
                double gauss = rand.nextGaussian() * sigma[d];
                semutBaru[i][d] = sTerpilih[d] + rand.nextGaussian() * sigma[d];
//                semutBaru[i][d] = sTerpilih[d] + 0.19 * sigma[d];

                //limit semut baru
                if (semutBaru[i][d] >= sMax[d]){
                    semutBaru[i][d] = sMax[d];
                }else if (semutBaru[i][d] <= sMin[d]){
                    semutBaru[i][d] = sMin[d];
                }
                System.out.printf(" %5.2f  ", semutBaru[i][d]);
            }
            fitnessSemutBaru[i] = fitness.hitungFitness(semutBaru[i], dataLatih);
            System.out.printf("%5.1f%n", fitnessSemutBaru[i]);
        }

        //Merge semut baru dengan archive solusi
        double[][] mergeSolusi = mergeSolusi(s, semutBaru);
        double[] mergeFitness = mergeFitness(fitnessSolusi, fitnessSemutBaru);

        //sort hasil merge
        sortSolusi(mergeSolusi, mergeFitness);

        //cut solusi terburuk
        for(int i = 0; i < archiveSize; i++){
            s[i] = Arrays.copyOf(mergeSolusi[i], dimensi);
            fitnessSolusi[i] = mergeFitness[i];
        }
        System.out.println("Solusi Baru");
        printSolusi(s, fitnessSolusi);
    }

    public void isKonvergen(int minKonvergen){
        if((iterasi-minKonvergen) >= 0){
            int counter = 0;
            for (int i = iterasi; i > (iterasi-minKonvergen); i--) {
                if (fitnessSbest == fitnessIterasi.get(i)){
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
        return fitness.hitungPrediksi(sbest, dataUji);
    }

    public double hitungMAPE(){
        return fitness.hitungMAPE(sbest, dataUji);
    }

    public long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.getCurrentThreadCpuTime();
    }

    public void saveRegresi(){
        Database db = new Database();
        db.openDatabase("dataenergi");
        db.saveRegresi(sbest, hitungMAPE(), 3);
    }

    void printSolusi(double[][] s, double[] fitness){
        for (int i = 0; i < s.length; i++) {
            System.out.print("s" + (i+1));
            for (int d = 0; d < dimensi; d++) {
                System.out.printf(" %5.2f   ", s[i][d]);
            }
            System.out.printf("%5.1f%n", fitness[i]);
        }
    }

    void printSbest(){
        System.out.println("Sbest");
        for (int d = 0; d < dimensi; d++) {
            System.out.printf("%5.5f   ", sbest[d]);
        }
        System.out.printf("%5.1f%n", fitnessSbest);
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
    public double[] getsMin() {
        return sMin;
    }
    public double[] getsMax() {
        return sMax;
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
    public int getArchiveSize() {
        return archiveSize;
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
    public double[] getSbest() {
        return sbest;
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
    public void setsMin(double[] sMin) {
        this.sMin = sMin;
    }
    public void setsMax(double[] sMax) {
        this.sMax = sMax;
    }
    public void setMinKonvergen(int minKonvergen) {
        this.minKonvergen = minKonvergen;
    }
    public void setMaxIterasi(int maxIterasi) {
        this.maxIterasi = maxIterasi;
    }
    public void setArchiveSize(int archiveSize) {
        this.archiveSize = archiveSize;
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
        double[] sMin = {-100, -10, -10, -10, -10, -10};
        double[] sMax = {100, 10, 10, 10, 10, 10};
        int[] tahunLatih = {1967, 2006};
        int[] tahunUji = {2007, 2016};
        ACOR acor = new ACOR(0.001, 1, 130, 300, 500, 2000,
                sMin, sMax, tahunLatih, tahunUji);
        acor.inisialisasiACOR();
        acor.iterasiACOR();
//        acor.printFitnessIterasi();
        acor.hitungPrediksi();
        acor.hitungMAPE();
//        acor.saveRegresi();
        System.out.println("Waktu Komputasi " + acor.getWaktuKomputasi() + "ms");
        System.out.println("Iterasi " + acor.getIterasi());
        System.out.println("IsKonvergen " + acor.isKonvergen());
    }
}
