package co.edu.uniquindio.model.Builder;
import co.edu.uniquindio.model.Usuario;

public class UsuarioBuilder{
    private String idUsuario;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private String direccion;
    private Double saldo;

    public UsuarioBuilder IdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
        return this;
    }
    public UsuarioBuilder NombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        return this;
    }
    public UsuarioBuilder Correo(String correo) {
        this.correo = correo;
        return this;
    }
    public UsuarioBuilder Telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }
    public UsuarioBuilder Direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }
    public UsuarioBuilder Saldo(Double saldo) {
        this.saldo = saldo;
        return this;
    }
    public Usuario build() {
        return new Usuario(idUsuario, nombreCompleto, correo, telefono, direccion, saldo);
    }    

}