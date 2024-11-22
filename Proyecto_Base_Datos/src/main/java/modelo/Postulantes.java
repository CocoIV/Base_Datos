package modelo;

public class Postulantes {
    private String Cedula;
    private String Nombre;
    private String Apellido_1;
    private String Apellido_2;
    private String OtrasSenas;
    private String Atestados;
    private String Experiencia;
    private double Pretension_Salarial;
    private String Contrasena;
    private String Correo;
    private String Area;
    private int ID_Lugar;

    // Getters y Setters
    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String cedula) {
        this.Cedula = cedula;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getApellido_1() {
        return Apellido_1;
    }

    public void setApellido_1(String apellido_1) {
        this.Apellido_1 = apellido_1;
    }

    public String getApellido_2() {
        return Apellido_2;
    }

    public void setApellido_2(String apellido_2) {
        this.Apellido_2 = apellido_2;
    }

    public String getOtrasSenas() {
        return OtrasSenas;
    }

    public void setOtrasSenas(String otrasSenas) {
        this.OtrasSenas = otrasSenas;
    }

    public String getAtestados() {
        return Atestados;
    }

    public void setAtestados(String atestados) {
        this.Atestados = atestados;
    }

    public String getExperiencia() {
        return Experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.Experiencia = experiencia;
    }

    public double getPretension_Salarial() {
        return Pretension_Salarial;
    }

    public void setPretension_Salarial(double pretension_Salarial) {
        this.Pretension_Salarial = pretension_Salarial;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String contrasena) {
        this.Contrasena = contrasena;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        this.Correo = correo;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        this.Area = area;
    }

    public int getID_Lugar() {
        return ID_Lugar;
    }

    public void setID_Lugar(int id_lugar) {
        this.ID_Lugar = id_lugar;
    }
}
