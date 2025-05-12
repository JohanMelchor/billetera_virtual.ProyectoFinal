package co.edu.uniquindio.model;

import co.edu.uniquindio.model.Builder.UsuarioBuilder;

public class Usuario {
    private String nombreCompleto;
    private String idUsuario;
    private String correo;
    private String telefono;
    private String direccion;
    private Double saldo;
    private String tipoUsuario;
    private String contrasenia;

    public Usuario() {
    }

    public Usuario(String nombreCompleto, String idUsuario,String correo,
                   String telefono, String direccion, Double saldo,String tipoUsuario,String contrasenia) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.saldo = saldo;
        this.tipoUsuario = tipoUsuario;
        this.contrasenia = contrasenia;
    }

    public static UsuarioBuilder builder() {
        return new UsuarioBuilder();
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
