package co.edu.uniquindio.factory;

import co.edu.uniquindio.mapping.dto.*;
import co.edu.uniquindio.mapping.mappers.*;
import co.edu.uniquindio.model.*;
import co.edu.uniquindio.service.*;
import co.edu.uniquindio.Util.DataUtil;
import co.edu.uniquindio.Util.TransaccionConstantes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ModelFactory implements IModelFactoryServices {
    private static ModelFactory modelFactory;
    private BilleteraVirtual billeteraVirtual;
    private IUsuarioMapping usuarioMapping;
    private ICuentaMapping cuentaMapping;
    private ITransaccionMapping transaccionMapping;
    private IPresupuestoMapping presupuestoMapping;
    private ICategoriaMapping categoriaMapping;

    private ModelFactory(){
         billeteraVirtual = new BilleteraVirtual();
        usuarioMapping = new UsuarioMappingImpl();
        cuentaMapping = new CuentaMappingImpl();
        transaccionMapping = new TransaccionMappingImpl();
        presupuestoMapping = new PresupuestoMappingImpl();
        categoriaMapping = new CategoriaMappingImpl();

        billeteraVirtual = DataUtil.inicializarDatos();
    }

    public static ModelFactory getInstance(){
        if(modelFactory == null){
            modelFactory = new ModelFactory();
        }
        return modelFactory;
    }

    // Métodos para Usuario
    @Override
    public List<UsuarioDto> obtenerUsuarios() {
        return usuarioMapping.getUsuarioDto(billeteraVirtual.getListaUsuarios());
    }

    @Override
    public boolean agregarUsuario(UsuarioDto usuarioDto) {
        return billeteraVirtual.crearUsuario(usuarioMapping.usuarioDtoToUsuario(usuarioDto));
    }

    @Override
    public boolean eliminarUsuario(String idUsuario) {
        return billeteraVirtual.eliminarUsuario(idUsuario);
    }

    @Override
    public boolean actualizarUsuario(UsuarioDto usuarioDto) {
        return billeteraVirtual.actualizarUsuario(usuarioMapping.usuarioDtoToUsuario(usuarioDto));
    }

    // Métodos para Cuenta
    @Override
    public List<CuentaDto> obtenerCuentas() {
        return cuentaMapping.getCuentaDto(billeteraVirtual.getListaCuentas());
    }

    @Override
    public List<CuentaDto> obtenerCuentasPorUsuario(String idUsuario) {
        return cuentaMapping.getCuentaDto(billeteraVirtual.obtenerCuentasPorUsuario(idUsuario));
    }

    @Override
    public boolean agregarCuenta(CuentaDto cuentaDto) {
        return billeteraVirtual.crearCuenta(cuentaMapping.cuentaDtoToCuenta(cuentaDto));
    }

    @Override
    public boolean eliminarCuenta(String idCuenta) {
        return billeteraVirtual.eliminarCuenta(idCuenta);
    }

    @Override
    public boolean actualizarCuenta(CuentaDto cuentaDto) {
        return billeteraVirtual.actualizarCuenta(cuentaMapping.cuentaDtoToCuenta(cuentaDto));
    }

    // Métodos para Transacción
    @Override
    public List<TransaccionDto> obtenerTransacciones() {
        return transaccionMapping.getTransaccionDto(billeteraVirtual.getListaTransacciones());
    }

    @Override
    public List<TransaccionDto> obtenerTransaccionesPorUsuario(String idUsuario) {
        return transaccionMapping.getTransaccionDto(billeteraVirtual.obtenerTransaccionesPorUsuario(idUsuario));
    }

    @Override
    public List<TransaccionDto> obtenerTransaccionesPorCuenta(String idCuenta) {
        return transaccionMapping.getTransaccionDto(billeteraVirtual.obtenerTransaccionesPorCuenta(idCuenta));
    }

    @Override
    public boolean agregarTransaccion(TransaccionDto transaccionDto) {
        return billeteraVirtual.crearTransaccion(transaccionMapping.transaccionDtoToTransaccion(transaccionDto));
    }

    @Override
    public boolean realizarDeposito(String idCuenta, Double monto, String descripcion, String idCategoria) {
        Cuenta cuenta = billeteraVirtual.buscarCuentaPorId(idCuenta);
        if (cuenta == null) return false;
        
        Categoria categoria = null;
        if(idCategoria != null && !idCategoria.isEmpty()) {
            categoria = billeteraVirtual.buscarCategoriaPorId(idCategoria);
        }
        
        // Crear transacción de depósito
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransaccionConstantes.TIPO_DEPOSITO, // Corregido: Tipo DEPOSITO
            monto,
            descripcion,
            null,
            cuenta, // En un depósito, la cuenta es el destino
            categoria
        );
        
        // Aumentar saldo en la cuenta
        if (cuenta.getPresupuesto() != null) {
            cuenta.getPresupuesto().aumentarSaldo(monto);
            return billeteraVirtual.crearTransaccion(transaccion);
        }
        
        return false;
    }

    @Override
    public boolean realizarRetiro(String idCuenta, Double monto, String descripcion, String idCategoria) {
        Cuenta cuenta = billeteraVirtual.buscarCuentaPorId(idCuenta);
        if (cuenta == null || cuenta.getPresupuesto() == null) return false;
        
        // Verificar si hay suficiente saldo
        if (cuenta.getPresupuesto().getSaldo() < monto) return false;
        
        Categoria categoria = null;
        if(idCategoria != null && !idCategoria.isEmpty()) {
            categoria = billeteraVirtual.buscarCategoriaPorId(idCategoria);
        }
        
        // Crear transacción de retiro
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransaccionConstantes.TIPO_RETIRO,
            monto,
            descripcion,
            cuenta, // En un retiro, la cuenta es el origen
            null,
            categoria
        );
        
        // Disminuir saldo en la cuenta
        if (cuenta.getPresupuesto().reducirSaldo(monto)) {
            return billeteraVirtual.crearTransaccion(transaccion);
        }
        
        return false;
    }

    @Override
    public boolean realizarTransferencia(String idCuentaOrigen, String idCuentaDestino, Double monto, String descripcion, String idCategoria) {
        Cuenta cuentaOrigen = billeteraVirtual.buscarCuentaPorId(idCuentaOrigen);
        Cuenta cuentaDestino = billeteraVirtual.buscarCuentaPorId(idCuentaDestino);
        
        if (cuentaOrigen == null || cuentaDestino == null || 
            cuentaOrigen.getPresupuesto() == null || cuentaDestino.getPresupuesto() == null) {
            return false;
        }
        
        // Verificar si hay suficiente saldo
        if (cuentaOrigen.getPresupuesto().getSaldo() < monto) return false;
        
        Categoria categoria = null;
        if(idCategoria != null && !idCategoria.isEmpty()) {
            categoria = billeteraVirtual.buscarCategoriaPorId(idCategoria);
        }
        
        // Crear transacción de transferencia
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransaccionConstantes.TIPO_TRANSFERENCIA,
            monto,
            descripcion,
            cuentaOrigen,
            cuentaDestino,
            categoria
        );
        
        // Transferir fondos
        if (cuentaOrigen.getPresupuesto().reducirSaldo(monto)) {
            cuentaDestino.getPresupuesto().aumentarSaldo(monto);
            return billeteraVirtual.crearTransaccion(transaccion);
        }
        
        return false;
    }

    // Métodos para Presupuesto
    @Override
    public List<PresupuestoDto> obtenerPresupuestos() {
        return presupuestoMapping.getPresupuestoDto(billeteraVirtual.getListaPresupuestos());
    }

    @Override
    public List<PresupuestoDto> obtenerPresupuestosPorUsuario(String idUsuario) {
        return presupuestoMapping.getPresupuestoDto(billeteraVirtual.obtenerPresupuestosPorUsuario(idUsuario));
    }

    @Override
    public boolean agregarPresupuesto(PresupuestoDto presupuestoDto) {
        return billeteraVirtual.crearPresupuesto(presupuestoMapping.presupuestoDtoToPresupuesto(presupuestoDto));
    }

    @Override
    public boolean eliminarPresupuesto(String idPresupuesto) {
        return billeteraVirtual.eliminarPresupuesto(idPresupuesto);
    }

    @Override
    public boolean actualizarPresupuesto(PresupuestoDto presupuestoDto) {
        return billeteraVirtual.actualizarPresupuesto(presupuestoMapping.presupuestoDtoToPresupuesto(presupuestoDto));
    }

    // Métodos para Categoría
    @Override
    public List<CategoriaDto> obtenerCategorias() {
        return categoriaMapping.getCategoriaDto(billeteraVirtual.getListaCategorias());
    }

    @Override
    public boolean agregarCategoria(CategoriaDto categoriaDto) {
        return billeteraVirtual.crearCategoria(categoriaMapping.categoriaDtoToCategoria(categoriaDto));
    }

    @Override
    public boolean eliminarCategoria(String idCategoria) {
        return billeteraVirtual.eliminarCategoria(idCategoria);
    }

    @Override
    public boolean actualizarCategoria(CategoriaDto categoriaDto) {
        return billeteraVirtual.actualizarCategoria(categoriaMapping.categoriaDtoToCategoria(categoriaDto));
    }

    // Métodos auxiliares para buscar entidades
    public Usuario buscarUsuarioPorId(String idUsuario) {
        return billeteraVirtual.buscarUsuarioPorId(idUsuario);
    }

    public Cuenta buscarCuentaPorId(String idCuenta) {
        return billeteraVirtual.buscarCuentaPorId(idCuenta);
    }

    public Categoria buscarCategoriaPorId(String idCategoria) {
        return billeteraVirtual.buscarCategoriaPorId(idCategoria);
    }

    public UsuarioDto buscarUsuarioDtoPorId(String idUsuario) {
        Usuario usuario = billeteraVirtual.buscarUsuarioPorId(idUsuario);
        return (usuario != null) ? usuarioMapping.usuarioToUsuarioDto(usuario) : null;
    }

    public boolean validarCredenciales(String idUsuario, String password) {
        return billeteraVirtual.validarCredenciales(idUsuario, password);
    }

    public boolean validarCredencialesAdmin(String idAdmin, String password) {
        for (Administrador admin : billeteraVirtual.getListaAdministradores()) {
            if(admin.getIdAdmin().equals(idAdmin) && admin.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean esAdministrador(String idUsuario) {
        return billeteraVirtual.getListaAdministradores().stream()
                .anyMatch(admin -> admin.getIdAdmin().equals(idUsuario));
    }

    
}