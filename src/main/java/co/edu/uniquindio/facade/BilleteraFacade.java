package co.edu.uniquindio.facade;

import co.edu.uniquindio.controller.UsuarioController;
import co.edu.uniquindio.controller.CuentaController;
import co.edu.uniquindio.controller.PresupuestoController;
import co.edu.uniquindio.controller.ReporteController;
import co.edu.uniquindio.controller.TransaccionController;
import co.edu.uniquindio.controller.CategoriaController;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.state.TipoEstadoCuenta;
import co.edu.uniquindio.mapping.dto.CuentaDto;
import co.edu.uniquindio.mapping.dto.PresupuestoDto;
import co.edu.uniquindio.mapping.dto.TransaccionDto;
import co.edu.uniquindio.mapping.dto.CategoriaDto;
import java.util.List;


public class BilleteraFacade {
    private final CategoriaController categoriaController = new CategoriaController();
    private final CuentaController cuentaController = new CuentaController();
    private final PresupuestoController presupuestoController = new PresupuestoController();
    private final ReporteController reporteController = new ReporteController();
    private final TransaccionController transaccionController = new TransaccionController();
    private final UsuarioController usuarioController = new UsuarioController();
    
    public BilleteraFacade() {

    }

    // Métodos de la clase TransaccionController
    public List<TransaccionDto> obtenerTransacciones() {
        return transaccionController.obtenerTransacciones();
    }
    
    public List<TransaccionDto> obtenerTransaccionesPorUsuario(String idUsuario) {
        return transaccionController.obtenerTransaccionesPorUsuario(idUsuario);
    }
    
    public List<TransaccionDto> obtenerTransaccionesPorCuenta(String idCuenta) {
        return transaccionController.obtenerTransaccionesPorCuenta(idCuenta);
    }
    
    public boolean agregarTransaccion(TransaccionDto transaccionDto) {
        return transaccionController.agregarTransaccion(transaccionDto);
    }
    
    public boolean depositoCuenta(String idCuenta, Double monto, String descripcion, String idCategoria) {
        return transaccionController.depositoCuenta(idCuenta, monto, descripcion, idCategoria);
    }

    public boolean depositoPresupuesto(String idCuenta, String idPresupuesto, Double monto, 
                                            String descripcion, String idCategoria) {
        return transaccionController.depositoPresupuesto(idCuenta, idPresupuesto, monto, descripcion, idCategoria);
    }
    
    public boolean retiroPorCuenta(String idCuenta, Double monto, String descripcion, String idCategoria) {
        return transaccionController.retiroPorCuenta(idCuenta, monto, descripcion, idCategoria);
    }
    
    public boolean retiroPorPresupuesto(String idCuenta, String idPresupuesto, Double monto, 
                                      String descripcion, String idCategoria) {
        return transaccionController.retiroPorPresupuesto(idCuenta, idPresupuesto, monto, descripcion, idCategoria);
    }
    
    public boolean realizarTransferencia(String idCuentaOrigen, String idCuentaDestino, 
                                  Double monto, String descripcion, String idCategoria) {
        return transaccionController.realizarTransferencia(idCuentaOrigen, idCuentaDestino, 
                                            monto, descripcion, idCategoria);
    }

    public List<TransaccionDto> obtenerTodasTransacciones() {
        return transaccionController.obtenerTodasTransacciones();
    }
    // Métodos de la clase ReporteController
    public boolean generarReporteUsuario(String idUsuario, String rutaArchivo) {
        return reporteController.generarReporteUsuario(idUsuario, rutaArchivo);
    }

    public boolean generarReporteAdmin(String rutaArchivo) {
        return reporteController.generarReporteAdmin(rutaArchivo);
    }

    // Métodos de la clase PresupuestoController
     public List<PresupuestoDto> obtenerPresupuestos() {
        return presupuestoController.obtenerPresupuestos();
    }
    
    public List<PresupuestoDto> obtenerPresupuestosPorUsuario(String idUsuario) {
        return presupuestoController.obtenerPresupuestosPorUsuario(idUsuario);
    }
    
    public boolean agregarPresupuesto(PresupuestoDto presupuestoDto) {
        return presupuestoController.agregarPresupuesto(presupuestoDto);
    }
    
    public boolean actualizarPresupuesto(PresupuestoDto presupuestoDto) {
        return presupuestoController.actualizarPresupuesto(presupuestoDto);
    }
    
    public boolean eliminarPresupuesto(String idPresupuesto) {
        return presupuestoController.eliminarPresupuesto(idPresupuesto);
    }

    public boolean agregarPresupuestoACuenta(String idCuenta, PresupuestoDto presupuestoDto) {
        return presupuestoController.agregarPresupuestoACuenta(idCuenta, presupuestoDto);
    }

    public List<PresupuestoDto> obtenerPresupuestosPorCuenta(String idCuenta) {
        return presupuestoController.obtenerPresupuestosPorCuenta(idCuenta);
    }

    public List<PresupuestoDto> obtenerTodosPresupuestos() {
        return presupuestoController.obtenerTodosPresupuestos();
    }

    // Métodos de la clase CuentaController
    public List<CuentaDto> obtenerCuentas() {
        return cuentaController.obtenerCuentas();
    }
    
    public List<CuentaDto> obtenerCuentasPorUsuario(String idUsuario) {
        return cuentaController.obtenerCuentasPorUsuario(idUsuario);
    }
    
    public boolean agregarCuenta(CuentaDto cuentaDto) {
        return cuentaController.agregarCuenta(cuentaDto);
    }
    
    public boolean actualizarCuenta(CuentaDto cuentaDto) {
        return cuentaController.actualizarCuenta(cuentaDto);
    }
    
    public boolean eliminarCuenta(String idCuenta) {
        return cuentaController.eliminarCuenta(idCuenta);
    }

    public List<CuentaDto> obtenerTodasCuentas() {
        return cuentaController.obtenerTodasCuentas();
    }

    public boolean cambiarEstadoCuenta(String idCuenta, TipoEstadoCuenta nuevoEstado) {
        return cuentaController.cambiarEstadoCuenta(idCuenta, nuevoEstado);
    }

    // Métodos de la clase CategoriaController
    public List<CategoriaDto> obtenerCategorias() {
        return categoriaController.obtenerCategorias();
    }
    
    public boolean agregarCategoria(CategoriaDto categoriaDto) {
        return categoriaController.agregarCategoria(categoriaDto);
    }
    
    public boolean actualizarCategoria(CategoriaDto categoriaDto) {
        return categoriaController.actualizarCategoria(categoriaDto);
    }
    
    public boolean eliminarCategoria(String idCategoria) {
        return categoriaController.eliminarCategoria(idCategoria);
    }

    public CategoriaDto buscarCategoriaPorId(String idCategoria) {
        return categoriaController.buscarCategoriaPorId(idCategoria);
    }

    //Metodos de UsuarioController
    public List<UsuarioDto> obtenerUsuarios(){
        return usuarioController.obtenerUsuarios();
    }

    public boolean agregarUsuario(UsuarioDto usuarioDto){
        return usuarioController.agregarUsuario(usuarioDto);
    }

    public boolean actualizarUsuario(UsuarioDto usuarioDto){
        return usuarioController.actualizarUsuario(usuarioDto);
    }

    public boolean eliminarUsuario(String idUsuario){
        return usuarioController.eliminarUsuario(idUsuario);
    }

    public boolean registrarUsuario(UsuarioDto usuarioDto) {
        return usuarioController.agregarUsuario(usuarioDto);
    }

    public UsuarioDto buscarUsuarioPorId(String idUsuario) {
        return usuarioController.buscarUsuarioPorId(idUsuario);
    }

    public boolean validarCredenciales(String idUsuario, String password) {
        return usuarioController.validarCredenciales(idUsuario, password);
    }

    public boolean validarCredencialesAdmin(String idAdmin, String password) {
        return usuarioController.validarCredencialesAdmin(idAdmin, password);
    }

    public boolean esAdministrador(String idUsuario) {
        return usuarioController.esAdministrador(idUsuario);
    }

    public String obtenerNombreAdmin(String idAdmin) {
        return usuarioController.obtenerNombreAdmin(idAdmin);
    }

    public boolean generarReporteUsuarioFormato(String idUsuario, String rutaArchivo, String formato) {
        return reporteController.generarReporteConAdapter(idUsuario, rutaArchivo, formato);
    }

    public boolean generarReporteAdminFormato(String rutaArchivo, String formato) {
        return reporteController.generarReporteConAdapter(null, rutaArchivo, formato);
    }

    public boolean generarReporteAuto(String idUsuario, String rutaArchivo) {
        return reporteController.generarReporteInteligente(idUsuario, rutaArchivo);
    }

    public String[] getFormatosDisponibles() {
        return reporteController.obtenerTiposReporte();
    }

}
