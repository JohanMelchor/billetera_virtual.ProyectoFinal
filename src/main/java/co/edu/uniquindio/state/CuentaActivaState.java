package co.edu.uniquindio.state;

import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.model.Presupuesto;

public class CuentaActivaState extends EstadoCuentaBase {

    @Override
    public ResultadoEstado puedeReducirSaldo(Cuenta cuenta, Double monto) {
        if (!validarMontoPositivo(monto)) {
            return ResultadoEstado.denegar("Operación denegada", "El monto debe ser mayor a cero");
        }
        
        if (!validarSaldoSuficiente(cuenta, monto)) {
            return ResultadoEstado.denegar("Saldo insuficiente", 
                String.format("Saldo actual: $%.2f, Monto requerido: $%.2f", 
                    cuenta.getSaldoTotal(), monto));
        }
        
        return ResultadoEstado.permitir("Operación permitida");
    }
    
    @Override
    public ResultadoEstado puedeAumentarSaldo(Cuenta cuenta, Double monto) {
        if (!validarMontoPositivo(monto)) {
            return ResultadoEstado.denegar("Operación denegada", "El monto debe ser mayor a cero");
        }
        
        return ResultadoEstado.permitir("Depósito permitido");
    }
    
    @Override
    public ResultadoEstado puedeAgregarPresupuesto(Cuenta cuenta, Presupuesto presupuesto) {
        if (presupuesto == null || presupuesto.getMontoAsignado() <= 0) {
            return ResultadoEstado.denegar("Presupuesto inválido", "El presupuesto debe tener un monto válido");
        }
        
        if (!validarSaldoSuficiente(cuenta, presupuesto.getMontoAsignado())) {
            return ResultadoEstado.denegar("Saldo insuficiente para presupuesto", 
                String.format("Saldo disponible: $%.2f, Monto presupuesto: $%.2f", 
                    cuenta.getSaldoTotal(), presupuesto.getMontoAsignado()));
        }
        
        return ResultadoEstado.permitir("Presupuesto puede ser agregado");
    }
    
    @Override
    public ResultadoEstado puedeEliminarPresupuesto(Cuenta cuenta, String idPresupuesto) {
        return ResultadoEstado.permitir("Presupuesto puede ser eliminado");
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
        return TipoEstadoCuenta.ACTIVA;
    }
    
    @Override
    public String getDescripcion() {
        return "Cuenta activa - Todas las operaciones permitidas";
    }
    
    @Override
    public boolean puedeTransicionarA(TipoEstadoCuenta nuevoEstado) {
        // Una cuenta activa puede transicionar a cualquier estado
        return true;
    }

}
