package co.edu.uniquindio.service;

import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.model.Presupuesto;
import co.edu.uniquindio.state.ResultadoEstado;
import co.edu.uniquindio.state.TipoEstadoCuenta;

public interface IEstadoCuenta {
    // Operaciones de saldo
    ResultadoEstado puedeReducirSaldo(Cuenta cuenta, Double monto);
    ResultadoEstado puedeAumentarSaldo(Cuenta cuenta, Double monto);
    
    // Operaciones de presupuesto
    ResultadoEstado puedeAgregarPresupuesto(Cuenta cuenta, Presupuesto presupuesto);
    ResultadoEstado puedeEliminarPresupuesto(Cuenta cuenta, String idPresupuesto);
    
    // Operaciones específicas de transacciones
    ResultadoEstado puedeRecibirTransferencia(Cuenta cuenta, Double monto);
    ResultadoEstado puedeEnviarTransferencia(Cuenta cuenta, Double monto);
    ResultadoEstado puedeRetirar(Cuenta cuenta, Double monto);
    ResultadoEstado puedeDepositar(Cuenta cuenta, Double monto);
    
    // Información del estado
    TipoEstadoCuenta getTipo();
    String getDescripcion();
    String getMensajeEstado();
    
    // Transiciones de estado
    boolean puedeTransicionarA(TipoEstadoCuenta nuevoEstado);
}
