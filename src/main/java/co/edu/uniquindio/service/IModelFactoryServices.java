package co.edu.uniquindio.service;

import co.edu.uniquindio.mapping.dto.*;

import java.util.List;

public interface IModelFactoryServices {
    // Usuario
    List<UsuarioDto> obtenerUsuarios();
    boolean agregarUsuario(UsuarioDto usuarioDto);
    boolean eliminarUsuario(String idUsuario);
    boolean actualizarUsuario(UsuarioDto usuarioDto);
    
    // Cuenta
    List<CuentaDto> obtenerCuentas();
    List<CuentaDto> obtenerCuentasPorUsuario(String idUsuario);
    boolean agregarCuenta(CuentaDto cuentaDto);
    boolean eliminarCuenta(String idCuenta);
    boolean actualizarCuenta(CuentaDto cuentaDto);
    
    // Transacción
    List<TransaccionDto> obtenerTransacciones();
    List<TransaccionDto> obtenerTransaccionesPorUsuario(String idUsuario);
    List<TransaccionDto> obtenerTransaccionesPorCuenta(String idCuenta);
    boolean agregarTransaccion(TransaccionDto transaccionDto);
    boolean realizarDeposito(String idCuenta, Double monto, String descripcion, String idCategoria);
    boolean realizarRetiro(String idCuenta, Double monto, String descripcion, String idCategoria);
    boolean realizarTransferencia(String idCuentaOrigen, String idCuentaDestino, Double monto, String descripcion, String idCategoria);
    
    // Presupuesto
    List<PresupuestoDto> obtenerPresupuestos();
    List<PresupuestoDto> obtenerPresupuestosPorUsuario(String idUsuario);
    boolean agregarPresupuesto(PresupuestoDto presupuestoDto);
    boolean eliminarPresupuesto(String idPresupuesto);
    boolean actualizarPresupuesto(PresupuestoDto presupuestoDto);
    
    // Categoría
    List<CategoriaDto> obtenerCategorias();
    boolean agregarCategoria(CategoriaDto categoriaDto);
    boolean eliminarCategoria(String idCategoria);
    boolean actualizarCategoria(CategoriaDto categoriaDto);

}