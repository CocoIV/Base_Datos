package modelo;

import com.mycompany.proyecto_base_datos.ConexionProyecto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SolicitudesDAO {

    private final ConexionProyecto conexion;

    public SolicitudesDAO() {
        this.conexion = new ConexionProyecto();
    }

    public boolean registrarSolicitud(int idPostulante, int idPuesto) {
        String sqlInsert = "INSERT INTO Solicitudes (ID_Solicitud, ID_Postulante, ID_Puesto, Estado) VALUES (?, ?, ?, ?)";
        String sqlMaxID = "SELECT MAX(ID_Solicitud) AS MaxID FROM Solicitudes";

        try (Connection conn = conexion.obtenerConexion()) {
            if (conn == null || conn.isClosed()) {
                System.err.println("Error: La conexión está cerrada.");
                return false;
            }

            // Calcular el próximo ID para la solicitud
            int nextID = 1;
            try (PreparedStatement psMaxID = conn.prepareStatement(sqlMaxID);
                 ResultSet rs = psMaxID.executeQuery()) {
                if (rs.next() && rs.getInt("MaxID") > 0) {
                    nextID = rs.getInt("MaxID") + 1;
                }
            }

            // Insertar la solicitud con el ID calculado
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, nextID);
                psInsert.setInt(2, idPostulante);
                psInsert.setInt(3, idPuesto);
                psInsert.setString(4, "Pendiente"); // Estado inicial

                return psInsert.executeUpdate() > 0; // Retorna true si se inserta una fila
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar la solicitud: " + e.getMessage());
            return false;
        }
    }
}
