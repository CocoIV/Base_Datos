package com.mycompany.proyecto_base_datos;

import controlador.ControladorSolicitudes;
import modelo.ModeloSolicitudes;
import vista.VistaBuscarPostulante;

public class Proyecto_Base_Datos {

    public static void main(String[] args) {
        // Verificar la conexión a la base de datos
        ConexionProyecto conexion = new ConexionProyecto();
        if (conexion.obtenerConexion() != null) {
            System.out.println("Conexión exitosa a la base de datos.");
        } else {
            System.err.println("Error al conectar a la base de datos.");
            return; // Detener la ejecución si no hay conexión
        }

        // Inicializar la vista y el modelo
        VistaBuscarPostulante vistaBuscarPostulante = new VistaBuscarPostulante();
        ModeloSolicitudes modeloSolicitudes = new ModeloSolicitudes();

        // Conectar la vista con el modelo usando el controlador
        ControladorSolicitudes controladorSolicitudes = new ControladorSolicitudes(vistaBuscarPostulante, modeloSolicitudes);

        // Mostrar directamente la VistaBuscarPostulante
        vistaBuscarPostulante.setVisible(true);

        // Mensaje de inicio
        System.out.println("VistaBuscarPostulante cargada con datos.");
    }
}