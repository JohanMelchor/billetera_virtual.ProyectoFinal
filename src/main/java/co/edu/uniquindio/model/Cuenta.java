package co.edu.uniquindio.model;
import java.util.List;

import co.edu.uniquindio.factory.EstadoCuentaFactory;
import co.edu.uniquindio.model.Builder.CuentaBuilder;
import co.edu.uniquindio.service.IEstadoCuenta;
import co.edu.uniquindio.state.ResultadoEstado;
import co.edu.uniquindio.state.TipoEstadoCuenta;

public class Cuenta {
    private String idCuenta;
    private String nombreBanco;
    private String numeroCuenta;
    private String tipoCuenta;
    private Usuario usuario;
    private List<Presupuesto> presupuestos;
    private Double saldoTotal;
    private IEstadoCuenta estado;

    public Cuenta() {
        this.estado = EstadoCuentaFactory.crearEstado(TipoEstadoCuenta.ACTIVA);
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
        this.estado = EstadoCuentaFactory.crearEstado(TipoEstadoCuenta.ACTIVA);
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

    public IEstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(IEstadoCuenta estado) {
        this.estado = estado;
    }

    public TipoEstadoCuenta getTipoEstado() {
        return estado.getTipo();
    }

    public static CuentaBuilder builder() {
        return new CuentaBuilder();
    }


    public Presupuesto buscarPresupuestoPorId(String idPresupuesto) {
        return presupuestos.stream()
                .filter(p -> p.getIdPresupuesto().equals(idPresupuesto))
                .findFirst()
                .orElse(null);
    }

    public void cambiarEstado(TipoEstadoCuenta nuevoTipo) {
        if (estado.puedeTransicionarA(nuevoTipo)) {
            this.estado = EstadoCuentaFactory.crearEstado(nuevoTipo);
        } else {
            throw new IllegalStateException("No se puede cambiar de " + estado.getTipo() + " a " + nuevoTipo);
        }
    }

    public String getMensajeEstado() {
        return estado.getMensajeEstado();
    }

    public boolean reducirSaldoTotal(Double monto) {
        ResultadoEstado resultado = estado.puedeReducirSaldo(this, monto);
        if (resultado.isPermitido()) {
            saldoTotal -= monto;
            return true;
        }
        return false;
    }

    public boolean aumentarSaldoTotal(Double monto) {
        ResultadoEstado resultado = estado.puedeAumentarSaldo(this, monto);
        if (resultado.isPermitido()) {
            saldoTotal += monto;
            return true;
        }
        return false;
    }

     public boolean agregarPresupuesto(Presupuesto presupuesto) {
        ResultadoEstado resultado = estado.puedeAgregarPresupuesto(this, presupuesto);
        if (resultado.isPermitido()) {
            if (presupuesto == null || presupuesto.getMontoAsignado() <= 0) {
                return false;
            }
            
            double diferencia = this.saldoTotal - presupuesto.getMontoAsignado();
            if (diferencia >= -0.001) {
                this.saldoTotal -= presupuesto.getMontoAsignado();
                this.presupuestos.add(presupuesto);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarPresupuesto(String idPresupuesto) {
        ResultadoEstado resultado = estado.puedeEliminarPresupuesto(this, idPresupuesto);
        if (resultado.isPermitido()) {
            Presupuesto presupuesto = buscarPresupuestoPorId(idPresupuesto);
            if (presupuesto != null) {
                saldoTotal += presupuesto.getSaldo();
                return presupuestos.remove(presupuesto);
            }
        }
        return false;
    }

    public ResultadoEstado puedeRecibirTransferencia(Double monto) {
        return estado.puedeRecibirTransferencia(this, monto);
    }
    
    public ResultadoEstado puedeEnviarTransferencia(Double monto) {
        return estado.puedeEnviarTransferencia(this, monto);
    }
    
    public ResultadoEstado puedeRetirar(Double monto) {
        return estado.puedeRetirar(this, monto);
    }
    
    public ResultadoEstado puedeDepositar(Double monto) {
        return estado.puedeDepositar(this, monto);
    }


}
