package co.edu.uniquindio.service;

import co.edu.uniquindio.model.Transaccion;

public interface ITransaccionServices {
    boolean crearTransaccion(Transaccion transaccion);
    boolean depositoCuenta(String idCuenta, Double monto, String descripcion, String idCategoria);
    boolean depositoPresupuesto(String idCuenta, String idPresupuesto, Double monto, String descripcion, String idCategoria);
    boolean retirarCuenta(String idCuenta, Double monto, String descripcion, String idCategoria);
    boolean retirarPresupuesto(String idCuenta, String idPresupuesto, Double monto, String descripcion, String idCategoria);
    boolean realizarTransferencia(String idCuentaOrigen, String idCuentaDestino, Double monto, String descripcion, String idCategoria);
}