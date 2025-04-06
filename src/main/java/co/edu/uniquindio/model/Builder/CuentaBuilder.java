package co.edu.uniquindio.model.Builder;
import co.edu.uniquindio.model.Cuenta;

public class CuentaBuilder {
    private String idCuenta;
    private String banco;
    private String numeroCuenta;
    private String tipoCuenta;
    
    public CuentaBuilder idCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
        return this;
    }
    public CuentaBuilder banco(String banco) {
        this.banco = banco;
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
    public Cuenta build() {
        return new Cuenta(idCuenta, banco, numeroCuenta, tipoCuenta);
    }

}
