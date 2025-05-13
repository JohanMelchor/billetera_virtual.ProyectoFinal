package co.edu.uniquindio.service;

import co.edu.uniquindio.model.Transaccion;

public interface ITransaccionServices {
    boolean crearTransaccion(Transaccion transaccion);
    boolean agregarFondos(String idCuenta, Double monto);
    boolean retirarFondos(String idCuenta, Double monto);
    boolean transferirFondos(String idCuentaOrigen, String idCuentaDestino, Double monto);
}