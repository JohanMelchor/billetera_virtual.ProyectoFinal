package co.edu.uniquindio.state;

import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.service.IEstadoCuenta;

public abstract class EstadoCuentaBase implements IEstadoCuenta {
    protected boolean validarMontoPositivo(Double monto) {
        return monto != null && monto > 0;
    }
    
    protected boolean validarSaldoSuficiente(Cuenta cuenta, Double monto) {
        return cuenta.getSaldoTotal() >= monto;
    }
    
    @Override
    public String getMensajeEstado() {
        return "Cuenta " + getTipo().getDescripcion().toLowerCase();
    }
}
