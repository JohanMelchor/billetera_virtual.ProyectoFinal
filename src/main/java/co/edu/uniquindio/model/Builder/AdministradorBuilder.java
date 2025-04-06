package co.edu.uniquindio.model.Builder;

import co.edu.uniquindio.model.Administrador;

public class AdministradorBuilder {

    private String idAdministrador;
    private String nombre;
    private String correo;
    private String telefono;

    public AdministradorBuilder IdAdministrador(String idAdministrador) {
        this.idAdministrador = idAdministrador;
        return this;
    }

    public AdministradorBuilder Nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public AdministradorBuilder Correo(String correo) {
        this.correo = correo;
        return this;
    }

    public AdministradorBuilder Telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public Administrador build(){
        return new Administrador(idAdministrador, nombre, correo, telefono);
    }

}
