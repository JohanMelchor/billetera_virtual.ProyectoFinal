package co.edu.uniquindio.service;

import co.edu.uniquindio.model.Cuenta;

public interface ICuentaServices {
    boolean crearCuenta(Cuenta cuenta);
    boolean eliminarCuenta(String idCuenta);
    boolean actualizarCuenta(Cuenta cuenta);
}