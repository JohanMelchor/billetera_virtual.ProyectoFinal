package co.edu.uniquindio.model;

public class Administrador {
    private String idAdmin;
    private String nombre;
    private String correo;
    private String password;

    public Administrador(String idAdmin, String nombre, String correo, String password) {
        this.idAdmin = idAdmin;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}