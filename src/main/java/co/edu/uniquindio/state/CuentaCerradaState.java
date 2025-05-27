package co.edu.uniquindio.state;

import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.model.Presupuesto;

public class CuentaCerradaState extends EstadoCuentaBase {
    @Override
    public ResultadoEstado puedeReducirSaldo(Cuenta cuenta, Double monto) {
        return ResultadoEstado.denegar("Cuenta cerrada", 
            "No se pueden realizar operaciones en una cuenta cerrada");
    }
    
    @Override
    public ResultadoEstado puedeAumentarSaldo(Cuenta cuenta, Double monto) {
        return ResultadoEstado.denegar("Cuenta cerrada", 
            "No se pueden realizar operaciones en una cuenta cerrada");
    }
    
    @Override
    public ResultadoEstado puedeAgregarPresupuesto(Cuenta cuenta, Presupuesto presupuesto) {
        return ResultadoEstado.denegar("Cuenta cerrada", 
            "No se pueden crear presupuestos en una cuenta cerrada");
    }
    
    @Override
    public ResultadoEstado puedeEliminarPresupuesto(Cuenta cuenta, String idPresupuesto) {
        return ResultadoEstado.denegar("Cuenta cerrada", 
            "No se pueden eliminar presupuestos de una cuenta cerrada");
    }
    
    @Override
    public ResultadoEstado puedeRecibirTransferencia(Cuenta cuenta, Double monto) {
        return ResultadoEstado.denegar("Cuenta cerrada", 
            "No se pueden recibir transferencias en una cuenta cerrada");
    }
    
    @Override
    public ResultadoEstado puedeEnviarTransferencia(Cuenta cuenta, Double monto) {
        return ResultadoEstado.denegar("Cuenta cerrada", 
            "No se pueden enviar transferencias desde una cuenta cerrada");
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
        return TipoEstadoCuenta.CERRADA;
    }
    
    @Override
    public String getDescripcion() {
        return "Cuenta cerrada - Solo consultas históricas";
    }
    
    @Override
    public String getMensajeEstado() {
        return "❌ Esta cuenta está cerrada permanentemente. Solo se permiten consultas históricas.";
    }
    
    @Override
    public boolean puedeTransicionarA(TipoEstadoCuenta nuevoEstado) {
        // Una cuenta cerrada no puede cambiar a ningún otro estado (permanente)
        return false;
    }
}
