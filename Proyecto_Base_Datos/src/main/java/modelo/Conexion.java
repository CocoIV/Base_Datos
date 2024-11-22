
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author diego
 */
public class Conexion {

    Connection connection = null;

    String user = "Diego";
    String pass = "12345";
    String db = "Empresa";
    String ip = "localhost";
    String port = "1433";

    public Connection obtenerConexion() {
        try {
            String ConnectionQuery = "jdbc:sqlserver://localhost:1433;database=Empresa;user=Diego;password=12345;encrypt=true;trustServerCertificate=true;";
            connection = DriverManager.getConnection(ConnectionQuery);
            JOptionPane.showMessageDialog(null, "Conexion Exitosa a la Base de Datos");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return connection;
    }
}
