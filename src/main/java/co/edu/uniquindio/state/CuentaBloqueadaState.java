package co.edu.uniquindio.state;

import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.model.Presupuesto;

public class CuentaBloqueadaState extends EstadoCuentaBase{
    @Override
    public ResultadoEstado puedeReducirSaldo(Cuenta cuenta, Double monto) {
        return ResultadoEstado.denegar("Cuenta bloqueada", 
            "No se pueden realizar retiros desde una cuenta bloqueada");
    }
    
    @Override
    public ResultadoEstado puedeAumentarSaldo(Cuenta cuenta, Double monto) {
        return ResultadoEstado.denegar("Cuenta bloqueada", 
            "No se pueden realizar depósitos a una cuenta bloqueada");
    }
    
    @Override
    public ResultadoEstado puedeAgregarPresupuesto(Cuenta cuenta, Presupuesto presupuesto) {
        return ResultadoEstado.denegar("Cuenta bloqueada", 
            "No se pueden crear presupuestos en una cuenta bloqueada");
    }
    
    @Override
    public ResultadoEstado puedeEliminarPresupuesto(Cuenta cuenta, String idPresupuesto) {
        return ResultadoEstado.denegar("Cuenta bloqueada", 
            "No se pueden eliminar presupuestos de una cuenta bloqueada");
    }
    
    @Override
    public ResultadoEstado puedeRecibirTransferencia(Cuenta cuenta, Double monto) {
        return ResultadoEstado.denegar("Cuenta bloqueada", 
            "No se pueden recibir transferencias en una cuenta bloqueada");
    }
    
    @Override
    public ResultadoEstado puedeEnviarTransferencia(Cuenta cuenta, Double monto) {
        return ResultadoEstado.denegar("Cuenta bloqueada", 
            "No se pueden enviar transferencias desde una cuenta bloqueada");
    }
    
    @Override
    public ResultadoEstado puedeRetirar(Cuenta cuenta, Double monto) {
        return puedeReducirSaldo(cuenta, monto);
    }
    
    @Override
    public ResultadoEstado puedeDepositar(Cuenta cuenta, Double monto) {
        return puedeAumentarSaldo(cuenta, monto);
    }
    
    @Override
    public TipoEstadoCuenta getTipo() {
        return TipoEstadoCuenta.BLOQUEADA;
    }
    
    @Override
    public String getDescripcion() {
        return "Cuenta bloqueada - Solo consultas permitidas";
    }
    
    @Override
    public String getMensajeEstado() {
        return "⚠️ Esta cuenta está bloqueada. Contacte al administrador para más información.";
    }
    
    @Override
    public boolean puedeTransicionarA(TipoEstadoCuenta nuevoEstado) {
        // Una cuenta bloqueada puede volver a activa o ser cerrada
        return nuevoEstado == TipoEstadoCuenta.ACTIVA || nuevoEstado == TipoEstadoCuenta.CERRADA;
    }
}


