/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author ACER
 */
public class Config {
    
    private static final String URL = "jdbc:mysql://localhost:3306/medica"; // Sesuaikan nama database Anda
    private static final String USER = "root"; // Sesuaikan dengan username MySQL Anda
    private static final String PASSWORD = ""; // Sesuaikan dengan password MySQL Anda

    public static Connection getConnection() throws SQLException {
        try {
            // Memuat driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC tidak ditemukan!");
            e.printStackTrace();
        }

        // Kembali koneksi ke database
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}