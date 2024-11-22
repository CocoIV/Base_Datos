/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_base_datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionProyecto {

    private static Connection connection = null;

    private static final String USER = "Diego";
    private static final String PASS = "12345";
    private static final String DB = "Empresa";
    private static final String IP = "localhost";
    private static final String PORT = "1433";

    public static Connection obtenerConexion() {
        try {
            // Si la conexión es nula o está cerrada, abrir una nueva
            if (connection == null || connection.isClosed()) {
                String connectionQuery = "jdbc:sqlserver://" + IP + ":" + PORT + ";database=" + DB + ";user=" + USER + ";password=" + PASS + ";encrypt=true;trustServerCertificate=true;";
                connection = DriverManager.getConnection(connectionQuery);
                JOptionPane.showMessageDialog(null, "Conexión exitosa a la base de datos.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.toString());
        }
        return connection;
    }
}

