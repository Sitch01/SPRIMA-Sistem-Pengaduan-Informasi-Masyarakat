/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/sprima_projek"; 
        String user = "root";
        String pass = ""; 
        return DriverManager.getConnection(url, user, pass);
    }

//    public static com.sun.jdi.connect.spi.Connection getKoneksi() {   
//    }
}
