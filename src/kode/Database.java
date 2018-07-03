package kode; /**
 * Created by Nylon on 6/21/2017.
 */
import java.sql.*;

public class Database {
    Connection c = null;
    Statement stmt = null;
    double[][] dbData;

    void createDatabase(String filename){
        String url = "jdbc:sqlite:" + filename + ".db";
        try (Connection c = DriverManager.getConnection(url)) {
            if (c != null) {
                System.out.println("kode.Database " + filename + " berhasil dibuat");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void openDatabase(String filename){
        String url = "jdbc:sqlite:" + filename + ".db";
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            c.setAutoCommit(false);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
//        System.out.println("Database " + filename + " berhasil dibuka");
    }

    void createTable(){
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE ENERGI " +
                    "(YEAR INT PRIMARY KEY NOT NULL," +
                    " PRIMARYENERGI REAL, " +
                    " GNI REAL, " +
                    " GDP REAL, " +
                    " POPULASI REAL, " +
                    " IMPOR REAL, " +
                    " EKSPOR REAL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
        }catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Tabel berhasil dibuat");
    }

    void insertData(int year, double energi, double gni, double gdp, double pop, double impor, double ekspor){
        try {
            stmt = c.createStatement();
            String sql;
            sql= "INSERT INTO ENERGI (YEAR,PRIMARYENERGI,GNI,GDP,POPULASI,IMPOR,EKSPOR) " +
                    "VALUES ("+year+", "+energi+", "+gni+", "+gdp+", "+pop+", "+impor+", "+ekspor+");";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Data berhasil disimpan");
    }

    public double[][] selectData(int minYear, int maxYear){
        try {
            stmt = c.createStatement();
            if(maxYear >= minYear) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM ENERGI WHERE YEAR>=" + minYear +
                        " AND YEAR<=" + maxYear + " ORDER BY YEAR ASC;");
                int row = maxYear - minYear + 1;
                int column = rs.getMetaData().getColumnCount();
                dbData = new double[row][column];
                while (rs.next()) {
                    for (int d = 0; d < column; d++) {
                        dbData[rs.getRow() - 1][d] = rs.getDouble(d+1);
                    }
                }
                rs.close();
            } else if(maxYear < minYear){
                ResultSet rs1 = stmt.executeQuery("SELECT * FROM ENERGI " +
                        "WHERE YEAR >=" +minYear+ " ORDER BY YEAR ASC;");
                int row = (2016 - minYear) + (maxYear - 1967) + 2;
                int column = rs1.getMetaData().getColumnCount();
                dbData = new double[row][column];
                int rowIndex = 0;
                while (rs1.next()) {
                    for (int d = 0; d < column; d++) {
                        dbData[rowIndex][d] = rs1.getDouble(d+1);
                    }
                    rowIndex++;
                }

                ResultSet rs2 = stmt.executeQuery("SELECT * FROM ENERGI " +
                        "WHERE YEAR >= 1967 AND YEAR<=" +maxYear+ " ORDER BY YEAR ASC;");
                while (rs2.next()) {
                    for (int d = 0; d < column; d++) {
                        dbData[rowIndex][d] = rs2.getDouble(d+1);
                    }
                    rowIndex++;
                }
                rs1.close();
                rs2.close();
            }
            stmt.close();
            c.commit();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return dbData;
    }

    void printdbData(){
        System.out.printf("%7s %7s %7s %7s %7s %7s %7s%n", "Tahun", "Energi", "GNI",
                "GDP", "Pop", "Impor", "Ekspor");
        for (int i = 0; i < dbData.length; i++) {
            for (int d = 0; d < dbData[0].length; d++) {
                System.out.printf("%7.2f ", dbData[i][d]);
            }
            System.out.println();
        }
    }

    void updateData(){
        try {
            stmt = c.createStatement();
            String sql = "UPDATE ENERGI set GNI=8.52 where YEAR=1985;";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Update data berhasil");
    }

    void deleteData(int tahun){
        try {
            stmt = c.createStatement();
            String sql = "DELETE FROM ENERGI where YEAR="+tahun+";";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Delete data berhasil");
    }

    public void saveRegresi(double[] gbest, double mape, int metode){
        try {
            stmt = c.createStatement();
            String sql = "INSERT INTO bobotRegresi (metode,b0,b1,b2,b3,b4,b5,mape) " +
                    "VALUES ("+metode+", "+gbest[0]+", "+gbest[1]+", "+gbest[2]+"," +
                    ""+gbest[3]+", "+gbest[4]+", "+gbest[5]+", "+mape+");";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Pemodelan regresi berhasil disimpan");
    }

    public double[][] selectRegresi(int limit){
        double[][] dataRegresi = new double[limit][8];
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM bobotRegresi " +
                    "ORDER BY mape ASC " +
                    "LIMIT "+limit+";" );
            while (rs.next()) {
                for (int d = 0; d < dataRegresi[0].length; d++) {
                    dataRegresi[rs.getRow() - 1][d] = rs.getDouble(d+1);
                }
            }
            rs.close();
            stmt.close();
            c.commit();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
//        printBobotRegresi(dataRegresi);
        return dataRegresi;
    }

    void printBobotRegresi(double[][] data){
        System.out.printf("%7s %7s %7s %7s %7s %7s %7s %7s%n", "metode", "b0",
                "b1", "b2", "b3", "b4" , "b5", "mape");
        for (int i = 0; i < data.length; i++) {
            for (int d = 0; d < data[0].length; d++) {
                System.out.printf("%7.2f ", data[i][d]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Database db = new Database();
//        db.createDatabase("dataenergi");
//        db.createTable();
//        db.insertData(2017, 1, 1, 1, 1, 1, 1);
//        db.updateData();
//        db.deleteData(2017);

        db.openDatabase("dataenergi");
        db.selectData(1990, 1989);
        db.printdbData();

//        db.openDatabase("dataenergi");
//        double[] gbest = {-100, -10, -10, -10, -10, -10};
//        db.saveRegresi(gbest, 11111, 1);
//        db.selectRegresi();
    }
}
