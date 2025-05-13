package co.edu.uniquindio.model.Builder;

import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.model.Usuario;

public class CuentaBuilder {
    protected String idCuenta;
    protected String nombreBanco;
    protected String numeroCuenta;
    protected String tipoCuenta;
    protected Usuario usuario;

    public CuentaBuilder idCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
        return this;
    }

    public CuentaBuilder nombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
        return this;
    }

    public CuentaBuilder numeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
        return this;
    }

    public CuentaBuilder tipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public CuentaBuilder usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public Cuenta build() {
        return new Cuenta(idCuenta, nombreBanco, numeroCuenta, tipoCuenta, usuario);
    }
}