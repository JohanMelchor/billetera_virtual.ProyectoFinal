package co.edu.uniquindio.controller;

import co.edu.uniquindio.factory.ModelFactory;
import co.edu.uniquindio.mapping.dto.CuentaDto;
import co.edu.uniquindio.state.TipoEstadoCuenta;

import java.util.List;

public class CuentaController {
    ModelFactory modelFactory;
    
    public CuentaController() {
        modelFactory = ModelFactory.getInstance();
    }
    
    public List<CuentaDto> obtenerCuentas() {
        return modelFactory.obtenerCuentas();
    }
    
    public List<CuentaDto> obtenerCuentasPorUsuario(String idUsuario) {
        return modelFactory.obtenerCuentasPorUsuario(idUsuario);
    }
    
    public boolean agregarCuenta(CuentaDto cuentaDto) {
        return modelFactory.agregarCuenta(cuentaDto);
    }
    
    public boolean actualizarCuenta(CuentaDto cuentaDto) {
        return modelFactory.actualizarCuenta(cuentaDto);
    }
    
    public boolean eliminarCuenta(String idCuenta) {
        return modelFactory.eliminarCuenta(idCuenta);
    }

    public List<CuentaDto> obtenerTodasCuentas() {
        return modelFactory.obtenerTodasCuentas();
    }

    public boolean cambiarEstadoCuenta(String idCuenta, TipoEstadoCuenta nuevoEstado) {
        return modelFactory.cambiarEstadoCuenta(idCuenta, nuevoEstado);
    }
}