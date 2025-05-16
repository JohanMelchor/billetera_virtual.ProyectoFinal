package co.edu.uniquindio.service;

import co.edu.uniquindio.model.Administrador;

public interface IAdministradorServices {
    boolean crearAdministrador(Administrador administrador);
    Administrador buscarAdministradorPorId(String idAdministrador);
}
