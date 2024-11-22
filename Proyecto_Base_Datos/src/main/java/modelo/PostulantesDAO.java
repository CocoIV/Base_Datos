package modelo;

import com.mycompany.proyecto_base_datos.ConexionProyecto;
import java.sql.*;

public class PostulantesDAO {

    private final ConexionProyecto conexion;

    public PostulantesDAO() {
        this.conexion = new ConexionProyecto();
    }

    // Método para registrar un postulante
    public int registrarPostulante(Postulantes postulante) {
        String sqlInsert = "INSERT INTO Postulantes (ID_Postulante, Nombre, Apellido_1, Apellido_2, Cedula, Contrasena, Pretension_Salarial, Atestados, Experiencia, Area, ID_Lugar) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlMaxID = "SELECT MAX(ID_Postulante) AS MaxID FROM Postulantes";

        try {
            Connection conn = conexion.obtenerConexion();

            if (conn == null || conn.isClosed()) {
                System.err.println("Error: La conexión está cerrada.");
                return -1;
            }

            // Obtener el próximo ID
            int nextID = 1;
            try (PreparedStatement psMaxID = conn.prepareStatement(sqlMaxID); ResultSet rs = psMaxID.executeQuery()) {
                if (rs.next() && rs.getInt("MaxID") > 0) {
                    nextID = rs.getInt("MaxID") + 1;
                }
            }

            // Registrar el postulante
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, nextID);
                psInsert.setString(2, postulante.getNombre());
                psInsert.setString(3, postulante.getApellido_1());
                psInsert.setString(4, postulante.getApellido_2());
                psInsert.setString(5, postulante.getCedula());
                psInsert.setString(6, postulante.getContrasena());
                psInsert.setDouble(7, postulante.getPretension_Salarial());
                psInsert.setString(8, postulante.getAtestados());
                psInsert.setString(9, postulante.getExperiencia());
                psInsert.setString(10, postulante.getArea());
                psInsert.setInt(11, postulante.getID_Lugar());

                if (psInsert.executeUpdate() > 0) {
                    return nextID;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar postulante: " + e.getMessage());
        }

        return -1; // Retornar -1 si ocurre algún error
    }

    // Método para registrar un lugar y devolver su ID
    public int registrarLugar(String provincia, String canton, String distrito) {
        String sqlSelect = "SELECT ID_Lugar FROM Lugares WHERE Provincia = ? AND Canton = ? AND Distrito = ?";
        String sqlInsert = "INSERT INTO Lugares (ID_Lugar, Provincia, Canton, Distrito) VALUES (?, ?, ?, ?)";
        String sqlMaxID = "SELECT MAX(ID_Lugar) AS MaxID FROM Lugares";

        try {
            Connection conn = conexion.obtenerConexion();

            if (conn == null || conn.isClosed()) {
                System.err.println("Error: La conexión está cerrada.");
                return -1;
            }

            // Verificar si el lugar ya existe
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
                psSelect.setString(1, provincia);
                psSelect.setString(2, canton);
                psSelect.setString(3, distrito);

                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("ID_Lugar"); // Si el lugar ya existe, devolver su ID
                    }
                }
            }

            // Obtener el próximo ID para el lugar
            int nextID = 1; // Si no hay registros, el primer ID será 1
            try (PreparedStatement psMaxID = conn.prepareStatement(sqlMaxID); ResultSet rs = psMaxID.executeQuery()) {
                if (rs.next() && rs.getInt("MaxID") > 0) {
                    nextID = rs.getInt("MaxID") + 1;
                }
            }

            // Insertar el nuevo lugar con el ID generado
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, nextID);
                psInsert.setString(2, provincia);
                psInsert.setString(3, canton);
                psInsert.setString(4, distrito);

                int rowsAffected = psInsert.executeUpdate();
                if (rowsAffected > 0) {
                    return nextID; // Retornar el nuevo ID generado
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar el lugar: " + e.getMessage());
        }

        return -1; // Retornar -1 si ocurre algún error
    }
}
