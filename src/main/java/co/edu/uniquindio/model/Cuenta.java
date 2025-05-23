package co.edu.uniquindio.model;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.model.Builder.CuentaBuilder;

public class Cuenta {
    private String idCuenta;
    private String nombreBanco;
    private String numeroCuenta;
    private String tipoCuenta;
    private Usuario usuario;
    private List<Presupuesto> presupuestos;
    private Double saldoTotal;

    public Cuenta() {
        this.presupuestos = new ArrayList<>();
    }

    public Cuenta(String idCuenta, String nombreBanco, String numeroCuenta, String tipoCuenta, Usuario usuario,
            List<Presupuesto> presupuestos, Double saldoTotal) {
        this.idCuenta = idCuenta;
        this.nombreBanco = nombreBanco;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.usuario = usuario;
        this.presupuestos = presupuestos;
        this.saldoTotal = saldoTotal;
    }

    public String getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Presupuesto> getPresupuestos() {
        return presupuestos;
    }

    public void setPresupuestos(List<Presupuesto> presupuestos) {
        this.presupuestos = presupuestos;
    }

    public Double getSaldoTotal() {
        return saldoTotal;
    }

    
    void setSaldoTotal(Double saldoTotal) {
        this.saldoTotal = saldoTotal;
    }


    public static CuentaBuilder builder() {
        return new CuentaBuilder();
    }

    public boolean agregarPresupuesto(Presupuesto presupuesto) {
        // Validar que el saldo total sea suficiente
        if (saldoTotal >= presupuesto.getMontoAsignado()) {
            presupuestos.add(presupuesto);
            saldoTotal -= presupuesto.getMontoAsignado(); // Restar del saldo total
            return true;
        }
        return false;
    }

    public boolean eliminarPresupuesto(String idPresupuesto) {
        Presupuesto presupuesto = buscarPresupuestoPorId(idPresupuesto);
        if (presupuesto != null) {
            saldoTotal += presupuesto.getSaldo(); // Devolver saldo a la cuenta
            return presupuestos.remove(presupuesto);
        }
        return false;
    }

    public Presupuesto buscarPresupuestoPorId(String idPresupuesto) {
        return presupuestos.stream()
                .filter(p -> p.getIdPresupuesto().equals(idPresupuesto))
                .findFirst()
                .orElse(null);
    }

    public boolean reducirSaldoTotal(Double monto) {
        if (saldoTotal >= monto) {
            saldoTotal -= monto;
            return true;
        }
        return false;
    }

    public void aumentarSaldoTotal(Double monto) {
        saldoTotal += monto;
    }

}
