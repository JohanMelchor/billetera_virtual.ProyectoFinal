package co.edu.uniquindio.factory;

import co.edu.uniquindio.service.IEstadoCuenta;
import co.edu.uniquindio.state.CuentaActivaState;
import co.edu.uniquindio.state.CuentaBloqueadaState;
import co.edu.uniquindio.state.CuentaCerradaState;
import co.edu.uniquindio.state.CuentaSuspendidaState;
import co.edu.uniquindio.state.TipoEstadoCuenta;

public class EstadoCuentaFactory {
    public static IEstadoCuenta crearEstado(TipoEstadoCuenta tipo) {
        switch (tipo) {
            case ACTIVA:
                return new CuentaActivaState();
            case BLOQUEADA:
                return new CuentaBloqueadaState();
            case CERRADA:
                return new CuentaCerradaState();
            case SUSPENDIDA:
                return new CuentaSuspendidaState();
            default:
                return new CuentaActivaState(); // Estado por defecto
        }
    }
    
    public static TipoEstadoCuenta[] getEstadosDisponibles() {
        return TipoEstadoCuenta.values();
    }
}
