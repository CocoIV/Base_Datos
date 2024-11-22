package controlador;

import modelo.ModeloSolicitudes;
import vista.VistaBuscarPostulante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControladorSolicitudes {

    private final VistaBuscarPostulante vista;
    private final ModeloSolicitudes modelo;

    public ControladorSolicitudes(VistaBuscarPostulante vista, ModeloSolicitudes modelo) {
        this.vista = vista;
        this.modelo = modelo;

        // Configurar eventos para los botones
        this.vista.btnFiltro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltro();
            }
        });

        this.vista.btnmodificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarEstado();
            }
        });

        // Cargar todas las solicitudes al inicio
        cargarSolicitudes();
    }

    private void cargarSolicitudes() {
        try {
            // Obtener las solicitudes iniciales ordenadas por puesto
            List<String[]> solicitudes = modelo.obtenerSolicitudesConFiltro("Puesto de Postulante");
            actualizarTabla(solicitudes);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar las solicitudes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void aplicarFiltro() {
        try {
            String filtroSeleccionado = vista.Filtrosbox.getSelectedItem().toString();
            List<String[]> solicitudesFiltradas = modelo.obtenerSolicitudesConFiltro(filtroSeleccionado);

            if (solicitudesFiltradas == null || solicitudesFiltradas.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "No se encontraron resultados con el filtro seleccionado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            actualizarTabla(solicitudesFiltradas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al aplicar el filtro: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void modificarEstado() {
        try {
            int filaSeleccionada = vista.tablaEvaPostulantes.getSelectedRow();

            if (filaSeleccionada != -1) {
                // Obtener los valores de la fila seleccionada
                String idPostulante = vista.tablaEvaPostulantes.getValueAt(filaSeleccionada, 5).toString();
                String idPuesto = vista.tablaEvaPostulantes.getValueAt(filaSeleccionada, 6).toString();
                String nuevoEstado = vista.estadobox.getSelectedItem().toString();

                // Validar que los valores no estén vacíos y sean números válidos
                if (idPostulante.isEmpty() || idPuesto.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Los IDs no pueden estar vacíos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idPostulanteInt = Integer.parseInt(idPostulante);
                int idPuestoInt = Integer.parseInt(idPuesto);

                // Actualizar el estado en la base de datos
                if (modelo.actualizarEstado(idPostulanteInt, idPuestoInt, nuevoEstado)) {
                    JOptionPane.showMessageDialog(vista, "Estado actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarSolicitudes(); // Recargar la tabla después de actualizar
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al actualizar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Por favor selecciona una fila.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "IDs no válidos: deben ser números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al modificar el estado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void actualizarTabla(List<String[]> solicitudes) {
        try {
            DefaultTableModel tableModel = (DefaultTableModel) vista.tablaEvaPostulantes.getModel();
            tableModel.setRowCount(0); // Limpiar la tabla

            if (solicitudes == null || solicitudes.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "No se encontraron datos para mostrar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Agregar las solicitudes al modelo de la tabla
            for (String[] solicitud : solicitudes) {
                if (solicitud.length < 7) {
                    System.err.println("Datos incompletos para la fila: " + String.join(", ", solicitud));
                    continue; // Ignorar filas incompletas
                }

                tableModel.addRow(new Object[]{
                    solicitud[0], // Puesto
                    solicitud[1], // Nombre
                    solicitud[2], // Salario Deseado
                    solicitud[3], // Área Laboral
                    solicitud[4], // Estado
                    solicitud[5], // ID_Postulante (oculto)
                    solicitud[6] // ID_Puesto (oculto)
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al actualizar la tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
