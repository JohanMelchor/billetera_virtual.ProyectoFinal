package co.edu.uniquindio.model;

import co.edu.uniquindio.model.Builder.CuentaBuilder;

public class Cuenta {
    private String idCuenta;
    private String banco;
    private String numeroCuenta;
    private String tipoCuenta;

    public Cuenta() {
    }

    public Cuenta(String idCuenta, String banco, String numeroCuenta, String tipoCuenta) {
        this.idCuenta = idCuenta;
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
    }
    public static CuentaBuilder builder(){
        return new CuentaBuilder();
    }
    
    public String getIdCuenta() {
        return idCuenta;
    }
    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }
    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    public String getNumeroCuenta() {
        return numeroCuenta;
    }
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
    public String getTipoCuenta() {
        return tipoCuenta;
    }
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

}
