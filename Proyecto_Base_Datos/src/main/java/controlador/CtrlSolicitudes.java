package controlador;

import modelo.PuestosDAO;
import modelo.SolicitudesDAO;
import vista.VistaPuestos;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

public class CtrlSolicitudes implements ActionListener {

    private final VistaPuestos vista;
    private final SolicitudesDAO solicitudesModelo;
    private final PuestosDAO puestosModelo;

    public CtrlSolicitudes(VistaPuestos vista, SolicitudesDAO solicitudesModelo, PuestosDAO puestosModelo) {
        this.vista = vista;
        this.solicitudesModelo = solicitudesModelo;
        this.puestosModelo = puestosModelo;

        // Registrar el botón con el controlador
        this.vista.btnPostularse.addActionListener(this);

        // Cargar los puestos al iniciar
        cargarPuestos();
    }

    private void cargarPuestos() {
        DefaultTableModel modeloTabla = (DefaultTableModel) vista.tablaPuestos.getModel();
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos

        System.out.println("Cargando puestos desde la base de datos...");
        List<String[]> puestos = puestosModelo.obtenerPuestosDisponibles();

        if (puestos.isEmpty()) {
            System.out.println("No se encontraron puestos en la base de datos.");
            JOptionPane.showMessageDialog(vista, "No hay puestos disponibles en la base de datos.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        System.out.println("Se encontraron " + puestos.size() + " puestos.");
        for (String[] puesto : puestos) {
            modeloTabla.addRow(puesto);
            System.out.println("Puesto cargado en la tabla: " + String.join(", ", puesto)); // Debugging
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnPostularse) {
            registrarSolicitud();
        }
    }

    private void registrarSolicitud() {
        try {
            int idPostulante = Integer.parseInt(vista.txtIDPostulante.getText());

            int selectedRow = vista.tablaPuestos.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(vista, "Por favor, seleccione un puesto de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idPuesto = Integer.parseInt(vista.tablaPuestos.getValueAt(selectedRow, 0).toString());

            if (solicitudesModelo.registrarSolicitud(idPostulante, idPuesto)) {
                JOptionPane.showMessageDialog(vista, "Solicitud registrada correctamente. Estado inicial: Pendiente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar la solicitud.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese un ID de postulante válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
