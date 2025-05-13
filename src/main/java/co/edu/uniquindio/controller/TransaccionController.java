package co.edu.uniquindio.controller;

import co.edu.uniquindio.factory.ModelFactory;
import co.edu.uniquindio.mapping.dto.TransaccionDto;

import java.util.List;

public class TransaccionController {
    ModelFactory modelFactory;
    
    public TransaccionController() {
        modelFactory = ModelFactory.getInstance();
    }
    
    public List<TransaccionDto> obtenerTransacciones() {
        return modelFactory.obtenerTransacciones();
    }
    
    public List<TransaccionDto> obtenerTransaccionesPorUsuario(String idUsuario) {
        return modelFactory.obtenerTransaccionesPorUsuario(idUsuario);
    }
    
    public List<TransaccionDto> obtenerTransaccionesPorCuenta(String idCuenta) {
        return modelFactory.obtenerTransaccionesPorCuenta(idCuenta);
    }
    
    public boolean agregarTransaccion(TransaccionDto transaccionDto) {
        return modelFactory.agregarTransaccion(transaccionDto);
    }
    
    public boolean realizarDeposito(String idCuenta, Double monto, String descripcion, String idCategoria) {
        return modelFactory.realizarDeposito(idCuenta, monto, descripcion, idCategoria);
    }
    
    public boolean realizarRetiro(String idCuenta, Double monto, String descripcion, String idCategoria) {
        return modelFactory.realizarRetiro(idCuenta, monto, descripcion, idCategoria);
    }
    
    public boolean realizarTransferencia(String idCuentaOrigen, String idCuentaDestino, Double monto, String descripcion, String idCategoria) {
        return modelFactory.realizarTransferencia(idCuentaOrigen, idCuentaDestino, monto, descripcion, idCategoria);
    }
}