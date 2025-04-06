package co.edu.uniquindio.model;

import co.edu.uniquindio.model.Builder.AdministradorBuilder;

public class Administrador {
    private String idAdministrador;
    private String nombre;
    private String correo;
    private String telefono;

    public Administrador() {
    }

    public Administrador(String idAdministrador, String nombre, String correo, String telefono) {
        this.idAdministrador = idAdministrador;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    public static AdministradorBuilder builder(){
        return new AdministradorBuilder();
    }

    public String getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(String idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
