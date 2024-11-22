package modelo;

import com.mycompany.proyecto_base_datos.ConexionProyecto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModeloSolicitudes {

    public List<String[]> obtenerSolicitudesConFiltro(String filtro) {
        List<String[]> solicitudes = new ArrayList<>();
        Connection conexion = ConexionProyecto.obtenerConexion();

        if (conexion == null) {
            System.err.println("La conexión a la base de datos no está establecida.");
            return solicitudes;
        }

        String query = """
            SELECT 
                Puestos.Nombre_Puesto AS Puesto,
                CONCAT(Postulantes.Nombre, ' ', Postulantes.Apellido_1, ' ', Postulantes.Apellido_2) AS Nombre,
                Postulantes.Pretension_Salarial AS SalarioDeseado,
                Puestos.Area_Puesto AS AreaLaboral,
                Solicitudes.Estado AS Estado,
                Postulantes.ID_Postulante AS ID_Postulante,
                Puestos.ID_Puesto AS ID_Puesto
            FROM Solicitudes
            INNER JOIN Postulantes ON Solicitudes.ID_Postulante = Postulantes.ID_Postulante
            INNER JOIN Puestos ON Solicitudes.ID_Puesto = Puestos.ID_Puesto
            %s;
        """;

        String orden = switch (filtro) {
            case "Salario Deseado" -> "ORDER BY Postulantes.Pretension_Salarial DESC";
            case "Puesto de Postulante" -> "ORDER BY Puestos.ID_Puesto ASC";
            default -> "";
        };

        query = query.formatted(orden);

        try (PreparedStatement pstmt = conexion.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                solicitudes.add(new String[]{
                    rs.getString("Puesto"),
                    rs.getString("Nombre"),
                    rs.getString("SalarioDeseado"),
                    rs.getString("AreaLaboral"),
                    rs.getString("Estado"),
                    rs.getString("ID_Postulante"),
                    rs.getString("ID_Puesto")
                });
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes filtradas:");
            e.printStackTrace();
        }
        return solicitudes;
    }

    public boolean actualizarEstado(int idPostulante, int idPuesto, String nuevoEstado) {
        Connection conexion = ConexionProyecto.obtenerConexion();

        if (conexion == null) {
            System.err.println("La conexión a la base de datos no está establecida.");
            return false;
        }

        String query = """
            UPDATE Solicitudes
            SET Estado = ?
            WHERE ID_Postulante = ? AND ID_Puesto = ?;
        """;

        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idPostulante);
            pstmt.setInt(3, idPuesto);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado:");
            e.printStackTrace();
            return false;
        }
    }
}
