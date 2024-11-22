package modelo;

import com.mycompany.proyecto_base_datos.ConexionProyecto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PuestosDAO {

    private final ConexionProyecto conexion;

    public PuestosDAO() {
        this.conexion = new ConexionProyecto();
    }

    public List<String[]> obtenerPuestosDisponibles() {
        String sql = "SELECT ID_Puesto, Nombre_Puesto, Area_Puesto, Fecha_Cierre, Experiencia_Requerida FROM Puestos";
        List<String[]> puestos = new ArrayList<>();

        try {
            Connection conn = conexion.obtenerConexion();

            if (conn == null || conn.isClosed()) {
                System.err.println("Error: La conexión está cerrada.");
                return puestos;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String[] puesto = {
                        rs.getString("ID_Puesto"),
                        rs.getString("Nombre_Puesto"),
                        rs.getString("Area_Puesto"),
                        rs.getString("Fecha_Cierre"),
                        rs.getString("Experiencia_Requerida")
                    };
                    puestos.add(puesto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener puestos: " + e.getMessage());
        }

        return puestos;
    }
}
