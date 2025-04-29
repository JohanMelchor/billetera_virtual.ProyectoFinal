package co.edu.uniquindio.model.Builder;

import co.edu.uniquindio.model.Usuario;


public class UsuarioBuilder {
    protected String idUsuario;
    protected String nombreCompleto;
    protected String correo;
    protected String telefono;
    protected String direccion;
    protected Double saldo;

    public UsuarioBuilder idUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
        return this;
    }

    public UsuarioBuilder nombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        return this;
    }

        public UsuarioBuilder correo(String correo) {
            this.correo = correo;
            return this;
        }

        public UsuarioBuilder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public UsuarioBuilder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public UsuarioBuilder saldo(Double saldo) {
            this.saldo = saldo;
            return this;
        }

        public Usuario build() {
            return new Usuario(idUsuario, nombreCompleto, correo, telefono, direccion, saldo);
        }
}

