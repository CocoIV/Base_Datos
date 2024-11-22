package controlador;

import modelo.Postulantes;
import modelo.PostulantesDAO;
import modelo.PuestosDAO;
import modelo.SolicitudesDAO;
import vista.VistaPuestos;
import vista.VistaPostulantes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CtrlPostulantes implements ActionListener {

    private final VistaPostulantes vistaPostulantes;
    private final PostulantesDAO modeloPostulantes;
    private final VistaPuestos vistaPuestos;
    private final PuestosDAO modeloPuestos;
    private final SolicitudesDAO modeloSolicitudes;

    public CtrlPostulantes(VistaPostulantes vistaPostulantes, PostulantesDAO modeloPostulantes,
                           VistaPuestos vistaPuestos, PuestosDAO modeloPuestos) {
        this.vistaPostulantes = vistaPostulantes;
        this.modeloPostulantes = modeloPostulantes;
        this.vistaPuestos = vistaPuestos;
        this.modeloPuestos = modeloPuestos;
        this.modeloSolicitudes = new SolicitudesDAO(); // Modelo para registrar solicitudes

        this.vistaPostulantes.btnRegistrarse.addActionListener(this);
        this.vistaPostulantes.btnPostularse.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaPostulantes.btnRegistrarse) {
            registrarPostulante();
        } else if (e.getSource() == vistaPostulantes.btnPostularse) {
            abrirVistaPuestos();
        }
    }

    private void registrarPostulante() {
        try {
            // Validar campos obligatorios
            if (vistaPostulantes.txtNombre.getText().isEmpty()
                    || vistaPostulantes.txtApellido_1.getText().isEmpty()
                    || vistaPostulantes.txtApellido_2.getText().isEmpty()
                    || vistaPostulantes.txtCedula.getText().isEmpty()
                    || vistaPostulantes.txtPretencion_Salarial.getText().isEmpty()
                    || vistaPostulantes.Contrasena.getText().isEmpty()
                    || vistaPostulantes.Provincia.getSelectedItem() == null
                    || vistaPostulantes.txtCanton.getText().isEmpty()
                    || vistaPostulantes.txtDistrito.getText().isEmpty()) {
                JOptionPane.showMessageDialog(vistaPostulantes, "Complete todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Registro del lugar
            int idLugar = modeloPostulantes.registrarLugar(
                    vistaPostulantes.Provincia.getSelectedItem().toString(),
                    vistaPostulantes.txtCanton.getText(),
                    vistaPostulantes.txtDistrito.getText()
            );

            if (idLugar == -1) {
                JOptionPane.showMessageDialog(vistaPostulantes, "Error al registrar el lugar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear el postulante con los datos ingresados
            Postulantes postulante = new Postulantes();
            postulante.setNombre(vistaPostulantes.txtNombre.getText());
            postulante.setApellido_1(vistaPostulantes.txtApellido_1.getText());
            postulante.setApellido_2(vistaPostulantes.txtApellido_2.getText());
            postulante.setCedula(vistaPostulantes.txtCedula.getText());
            postulante.setContrasena(vistaPostulantes.Contrasena.getText());
            postulante.setPretension_Salarial(Double.parseDouble(vistaPostulantes.txtPretencion_Salarial.getText()));
            postulante.setAtestados(vistaPostulantes.txtAtestados.getText());
            postulante.setExperiencia(vistaPostulantes.txtExperiencia.getText());
            postulante.setArea(vistaPostulantes.Area.getSelectedItem().toString());
            postulante.setID_Lugar(idLugar);

            // Registrar el postulante
            int idPostulante = modeloPostulantes.registrarPostulante(postulante);

            if (idPostulante != -1) {
                JOptionPane.showMessageDialog(vistaPostulantes, "Postulante registrado correctamente. Su ID es: " + idPostulante, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vistaPostulantes, "Error al registrar el postulante.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaPostulantes, "Error en los valores numéricos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaPostulantes, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirVistaPuestos() {
        try {
            DefaultTableModel modeloTabla = (DefaultTableModel) vistaPuestos.tablaPuestos.getModel();
            modeloTabla.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos

            List<String[]> puestos = modeloPuestos.obtenerPuestosDisponibles();
            if (puestos.isEmpty()) {
                JOptionPane.showMessageDialog(vistaPostulantes, "No hay puestos disponibles en la base de datos.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (String[] puesto : puestos) {
                modeloTabla.addRow(puesto);
            }

            // Añadir funcionalidad al botón "Postularse"
            vistaPuestos.btnPostularse.addActionListener(e -> postularse());

            vistaPuestos.setVisible(true);
            vistaPostulantes.setVisible(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaPostulantes, "Error al abrir la vista de puestos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void postularse() {
        try {
            // Obtener ID_Postulante ingresado por el usuario
            int idPostulante = Integer.parseInt(vistaPuestos.txtIDPostulante.getText());

            // Obtener el ID_Puesto seleccionado en la tabla
            int selectedRow = vistaPuestos.tablaPuestos.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(vistaPuestos, "Seleccione un puesto de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idPuesto = Integer.parseInt(vistaPuestos.tablaPuestos.getValueAt(selectedRow, 0).toString());

            // Registrar la solicitud en la base de datos
            if (modeloSolicitudes.registrarSolicitud(idPostulante, idPuesto)) {
                JOptionPane.showMessageDialog(vistaPuestos, "Postulación registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vistaPuestos, "Error al registrar la postulación.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaPuestos, "Ingrese un ID de postulante válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
