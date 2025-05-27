package co.edu.uniquindio.state;

import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.model.Presupuesto;

public class CuentaSuspendidaState extends EstadoCuentaBase{
    private static final double LIMITE_OPERACION = 1000.0; // Límite durante suspensión
    
    @Override
    public ResultadoEstado puedeReducirSaldo(Cuenta cuenta, Double monto) {
        if (!validarMontoPositivo(monto)) {
            return ResultadoEstado.denegar("Operación denegada", "El monto debe ser mayor a cero");
        }
        
        if (monto > LIMITE_OPERACION) {
            return ResultadoEstado.denegar("Monto excede el límite", 
                String.format("En cuenta suspendida, el límite de retiro es $%.2f", LIMITE_OPERACION));
        }
        
        if (!validarSaldoSuficiente(cuenta, monto)) {
            return ResultadoEstado.denegar("Saldo insuficiente", 
                String.format("Saldo actual: $%.2f, Monto requerido: $%.2f", 
                    cuenta.getSaldoTotal(), monto));
        }
        
        return ResultadoEstado.permitir("Retiro permitido (cuenta suspendida)");
    }
    
    @Override
    public ResultadoEstado puedeAumentarSaldo(Cuenta cuenta, Double monto) {
        if (!validarMontoPositivo(monto)) {
            return ResultadoEstado.denegar("Operación denegada", "El monto debe ser mayor a cero");
        }
        
        // Los depósitos están permitidos sin límite durante suspensión
        return ResultadoEstado.permitir("Depósito permitido (cuenta suspendida)");
    }
    
    @Override
    public ResultadoEstado puedeAgregarPresupuesto(Cuenta cuenta, Presupuesto presupuesto) {
        return ResultadoEstado.denegar("Cuenta suspendida", 
            "No se pueden crear presupuestos durante la suspensión");
    }
    
    @Override
    public ResultadoEstado puedeEliminarPresupuesto(Cuenta cuenta, String idPresupuesto) {
        return ResultadoEstado.permitir("Eliminación de presupuesto permitida");
    }
    
    @Override
    public ResultadoEstado puedeRecibirTransferencia(Cuenta cuenta, Double monto) {
        return puedeAumentarSaldo(cuenta, monto);
    }
    
    @Override
    public ResultadoEstado puedeEnviarTransferencia(Cuenta cuenta, Double monto) {
        return puedeReducirSaldo(cuenta, monto);
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
        return TipoEstadoCuenta.SUSPENDIDA;
    }
    
    @Override
    public String getDescripcion() {
        return String.format("Cuenta suspendida - Operaciones limitadas a $%.2f", LIMITE_OPERACION);
    }
    
    @Override
    public String getMensajeEstado() {
        return String.format("⚠️ Cuenta temporalmente suspendida. Límite de operaciones: $%.2f", LIMITE_OPERACION);
    }
    
    @Override
    public boolean puedeTransicionarA(TipoEstadoCuenta nuevoEstado) {
        // Una cuenta suspendida puede volver a activa o ser bloqueada
        return nuevoEstado == TipoEstadoCuenta.ACTIVA || nuevoEstado == TipoEstadoCuenta.BLOQUEADA;
    }
}
