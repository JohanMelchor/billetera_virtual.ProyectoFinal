package co.edu.uniquindio.model;

import co.edu.uniquindio.service.*;
import co.edu.uniquindio.Util.TransaccionConstantes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BilleteraVirtual implements IUsuarioServices,IAdministradorServices, ICuentaServices, ITransaccionServices, IPresupuestoServices, ICategoriaServices {
    private ArrayList<Usuario>listaUsuarios= new ArrayList<>();
    private ArrayList<Cuenta>listaCuentas= new ArrayList<>();
    private ArrayList<Transaccion>listaTransacciones= new ArrayList<>();
    private ArrayList<Presupuesto>listaPresupuestos= new ArrayList<>();
    private ArrayList<Categoria>listaCategorias= new ArrayList<>();
    private ArrayList<Administrador>listaAdministradores= new ArrayList<>();
    private ArrayList<Reporte>listaReportes= new ArrayList<>();

    public BilleteraVirtual(){
        this.listaUsuarios = new ArrayList<>();
        this.listaCuentas = new ArrayList<>();
        this.listaTransacciones = new ArrayList<>();
        this.listaPresupuestos = new ArrayList<>();
        this.listaCategorias = new ArrayList<>();
        this.listaAdministradores = new ArrayList<>();
        this.listaReportes = new ArrayList<>();
    }

    public ArrayList<Cuenta> getListaCuentas() {
        return listaCuentas;
    }

    public ArrayList<Reporte> getListaReportes() {
        return listaReportes;
    }

    public ArrayList<Administrador> getListaAdministradores() {
        return listaAdministradores;
    }

    public ArrayList<Categoria> getListaCategorias() {
        return listaCategorias;
    }

    public ArrayList<Presupuesto> getListaPresupuestos() {
        return listaPresupuestos;
    }

    public void setListaTransacciones(ArrayList<Transaccion> listaTransacciones) {
        this.listaTransacciones = listaTransacciones;
    }

    public ArrayList<Transaccion> getListaTransacciones() {
        return listaTransacciones;
    }

    public void setListaCuentas(ArrayList<Cuenta> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    // Métodos para Usuario
    @Override
    public boolean crearUsuario(Usuario usuario) {
        if (buscarUsuarioPorId(usuario.getIdUsuario()) == null) {
            listaUsuarios.add(usuario);
            return true;
        }
        return false;
    }
    

    @Override
    public boolean eliminarUsuario(String idUsuario) {
        Usuario usuarioEliminado = buscarUsuarioPorId(idUsuario);
        if(usuarioEliminado != null){
            listaUsuarios.remove(usuarioEliminado);
            return true;
        }
        return false;
    }

    public Usuario buscarUsuarioPorId(String idUsuario) {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getIdUsuario().equals(idUsuario)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean validarCredenciales(String idUsuario, String password) {
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        if(usuario == null) return false;
            return usuario.getPassword().equals(password);
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        
        Usuario usuarioExistente = buscarUsuarioPorId(usuario.getIdUsuario());
        if(usuarioExistente != null){
            // Actualizar solo los campos permitidos
            usuarioExistente.setNombreCompleto(usuario.getNombreCompleto());
            usuarioExistente.setCorreo(usuario.getCorreo());
            usuarioExistente.setTelefono(usuario.getTelefono());
            usuarioExistente.setDireccion(usuario.getDireccion());
            usuarioExistente.setPassword(usuario.getPassword());

            return true;
        }
        return false;
    }

    // Métodos para Cuenta
    @Override
    public boolean crearCuenta(Cuenta cuenta) {
        Cuenta cuentaExistente = buscarCuenta(cuenta);
        if(cuentaExistente == null) {
            listaCuentas.add(cuenta);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarCuenta(String idCuenta) {
        Cuenta cuentaEliminar = buscarCuentaPorId(idCuenta);
        if(cuentaEliminar != null) {
            listaCuentas.remove(cuentaEliminar);
            return true;
        }
        return false;
    }

    @Override
    public boolean actualizarCuenta(Cuenta cuenta) {
        Cuenta cuentaActualizar = buscarCuentaPorId(cuenta.getIdCuenta());
        if(cuentaActualizar != null) {
            cuentaActualizar.setNombreBanco(cuenta.getNombreBanco());
            cuentaActualizar.setNumeroCuenta(cuenta.getNumeroCuenta());
            cuentaActualizar.setTipoCuenta(cuenta.getTipoCuenta());
            return true;
        }
        return false;
    }

    public Cuenta buscarCuenta(Cuenta cuenta) {
        for(Cuenta c : listaCuentas) {
            if(c.getIdCuenta().equals(cuenta.getIdCuenta())) {
                return c;
            }
        }
        return null;
    }

    public Cuenta buscarCuentaPorId(String idCuenta) {
        for(Cuenta cuenta : listaCuentas) {
            if(cuenta.getIdCuenta().equals(idCuenta)) {
                return cuenta;
            }
        }
        return null;
    }

    public List<Cuenta> obtenerCuentasPorUsuario(String idUsuario) {
        List<Cuenta> cuentasUsuario = new ArrayList<>();
        for(Cuenta cuenta : listaCuentas) {
            if(cuenta.getUsuario().getIdUsuario().equals(idUsuario)) {
                cuentasUsuario.add(cuenta);
            }
        }
        return cuentasUsuario;
    }

    // Métodos para Transacción
    @Override
    public boolean crearTransaccion(Transaccion transaccion) {
        listaTransacciones.add(transaccion);
        return true;
    }

    @Override
    public boolean retirarCuenta(String idCuenta, Double monto, String descripcion, String idCategoria) {
        Cuenta cuenta = buscarCuentaPorId(idCuenta);
        if (cuenta == null || !cuenta.reducirSaldoTotal(monto)) {
            return false;
        }

        Categoria categoria = buscarCategoriaPorId(idCategoria);
        
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            "RETIRO_CUENTA",
            monto,
            descripcion,
            cuenta,
            null,
            categoria
        );
        
        return crearTransaccion(transaccion);
    }

    @Override
    public boolean retirarPresupuesto(String idCuenta, String idPresupuesto, Double monto, 
                                    String descripcion, String idCategoria) {
        Cuenta cuenta = buscarCuentaPorId(idCuenta);
        if (cuenta == null) return false;

        Presupuesto presupuesto = cuenta.buscarPresupuestoPorId(idPresupuesto);
        if (presupuesto == null || !presupuesto.reducirSaldo(monto)) {
            return false;
        }

        Categoria categoria = buscarCategoriaPorId(idCategoria);
        
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            "RETIRO_PRESUPUESTO",
            monto,
            descripcion + " (Presupuesto: " + presupuesto.getNombre() + ")",
            cuenta,
            null,
            categoria
        );
        
        return crearTransaccion(transaccion);
    }

    @Override
    public boolean realizarTransferencia(String idCuentaOrigen, String idCuentaDestino, 
                                    Double monto, String descripcion, String idCategoria) {
        // Validar que no sea la misma cuenta
        if (idCuentaOrigen.equals(idCuentaDestino)) {
            return false;
        }

        Cuenta cuentaOrigen = buscarCuentaPorId(idCuentaOrigen);
        Cuenta cuentaDestino = buscarCuentaPorId(idCuentaDestino);
        
        // Validar cuentas y saldo suficiente
        if (cuentaOrigen == null || cuentaDestino == null || 
            !cuentaOrigen.reducirSaldoTotal(monto)) {
            return false;
        }
        
        // Realizar transferencia
        cuentaDestino.aumentarSaldoTotal(monto);
        
        // Registrar transacción
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransaccionConstantes.TIPO_TRANSFERENCIA,
            monto,
            descripcion,
            cuentaOrigen,
            cuentaDestino,
            buscarCategoriaPorId(idCategoria)
        );
        
        return crearTransaccion(transaccion);
    }

    public List<Transaccion> obtenerTransaccionesPorUsuario(String idUsuario) {
        List<Transaccion> transaccionesUsuario = new ArrayList<>();
        List<Cuenta> cuentasUsuario = obtenerCuentasPorUsuario(idUsuario);
        
        for(Transaccion transaccion : listaTransacciones) {
            // Verificar si la cuenta de origen o destino pertenece al usuario
            boolean esOrigen = transaccion.getCuentaOrigen() != null && 
                              cuentasUsuario.contains(transaccion.getCuentaOrigen());
            boolean esDestino = transaccion.getCuentaDestino() != null && 
                               cuentasUsuario.contains(transaccion.getCuentaDestino());
            
            if(esOrigen || esDestino) {
                transaccionesUsuario.add(transaccion);
            }
        }
        return transaccionesUsuario;
    }

    public List<Transaccion> obtenerTransaccionesPorCuenta(String idCuenta) {
        List<Transaccion> transaccionesCuenta = new ArrayList<>();
        for(Transaccion transaccion : listaTransacciones) {
            boolean esOrigen = transaccion.getCuentaOrigen() != null && 
                              transaccion.getCuentaOrigen().getIdCuenta().equals(idCuenta);
            boolean esDestino = transaccion.getCuentaDestino() != null && 
                               transaccion.getCuentaDestino().getIdCuenta().equals(idCuenta);
            
            if(esOrigen || esDestino) {
                transaccionesCuenta.add(transaccion);
            }
        }
        return transaccionesCuenta;
    }

    // Métodos para Presupuesto
    @Override
    public boolean crearPresupuesto(Presupuesto presupuesto) {
        Presupuesto presupuestoExistente = buscarPresupuesto(presupuesto);
        if(presupuestoExistente == null) {
            listaPresupuestos.add(presupuesto);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarPresupuesto(String idPresupuesto) {
        Presupuesto presupuestoEliminar = buscarPresupuestoPorId(idPresupuesto);
        if(presupuestoEliminar != null) {
            listaPresupuestos.remove(presupuestoEliminar);
            return true;
        }
        return false;
    }

    @Override
    public boolean actualizarPresupuesto(Presupuesto presupuesto) {
        Presupuesto presupuestoActualizar = buscarPresupuestoPorId(presupuesto.getIdPresupuesto());
        if(presupuestoActualizar != null) {
            presupuestoActualizar.setNombre(presupuesto.getNombre());
            presupuestoActualizar.setMontoAsignado(presupuesto.getMontoAsignado());
            presupuestoActualizar.setMontoGastado(presupuesto.getMontoGastado());
            presupuestoActualizar.setCategoria(presupuesto.getCategoria());
            return true;
        }
        return false;
    }

    public Presupuesto buscarPresupuesto(Presupuesto presupuesto) {
        for(Presupuesto p : listaPresupuestos) {
            if(p.getIdPresupuesto().equals(presupuesto.getIdPresupuesto())) {
                return p;
            }
        }
        return null;
    }

    public Presupuesto buscarPresupuestoPorId(String idPresupuesto) {
        for(Presupuesto presupuesto : listaPresupuestos) {
            if(presupuesto.getIdPresupuesto().equals(idPresupuesto)) {
                return presupuesto;
            }
        }
        return null;
    }

    public List<Presupuesto> obtenerPresupuestosPorUsuario(String idUsuario) {
        List<Presupuesto> presupuestosUsuario = new ArrayList<>();
        for(Presupuesto presupuesto : listaPresupuestos) {
            if(presupuesto.getUsuario().getIdUsuario().equals(idUsuario)) {
                presupuestosUsuario.add(presupuesto);
            }
        }
        return presupuestosUsuario;
    }

    // Métodos para Categoría
    @Override
    public boolean crearCategoria(Categoria categoria) {
        Categoria categoriaExistente = buscarCategoria(categoria);
        if(categoriaExistente == null) {
            listaCategorias.add(categoria);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarCategoria(String idCategoria) {
        Categoria categoriaEliminar = buscarCategoriaPorId(idCategoria);
        if(categoriaEliminar != null) {
            listaCategorias.remove(categoriaEliminar);
            return true;
        }
        return false;
    }

    @Override
    public boolean actualizarCategoria(Categoria categoria) {
        Categoria categoriaActualizar = buscarCategoriaPorId(categoria.getIdCategoria());
        if(categoriaActualizar != null) {
            categoriaActualizar.setNombre(categoria.getNombre());
            categoriaActualizar.setDescripcion(categoria.getDescripcion());
            return true;
        }
        return false;
    }

    public Categoria buscarCategoria(Categoria categoria) {
        for(Categoria c : listaCategorias) {
            if(c.getIdCategoria().equals(categoria.getIdCategoria())) {
                return c;
            }
        }
        return null;
    }

    public Categoria buscarCategoriaPorId(String idCategoria) {
        for(Categoria categoria : listaCategorias) {
            if(categoria.getIdCategoria().equals(idCategoria)) {
                return categoria;
            }
        }
        return null;
    }

    @Override
    public boolean crearAdministrador(Administrador administrador) {
        if(buscarAdministradorPorId(administrador.getIdAdmin()) != null) {
            return false;
        }
        listaAdministradores.add(administrador);
        return true;
    }

    @Override
    public Administrador buscarAdministradorPorId(String idAdmin) {
        return listaAdministradores.stream()
            .filter(a -> a.getIdAdmin().equals(idAdmin))
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean depositoCuenta(String idCuenta, Double monto, String descripcion, String idCategoria) {
        Cuenta cuenta = buscarCuentaPorId(idCuenta);
        if (cuenta == null) return false;
        
        cuenta.aumentarSaldoTotal(monto);
        
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransaccionConstantes.TIPO_DEPOSITO_CUENTA,
            monto,
            descripcion,
            null,  // No hay cuenta origen en depósito
            cuenta,
            buscarCategoriaPorId(idCategoria)
        );
        
        return crearTransaccion(transaccion);
    }

    @Override
    public boolean depositoPresupuesto(String idCuenta, String idPresupuesto, Double monto, 
                                         String descripcion, String idCategoria) {
        Cuenta cuenta = buscarCuentaPorId(idCuenta);
        if (cuenta == null) return false;
        
        Presupuesto presupuesto = cuenta.buscarPresupuestoPorId(idPresupuesto);
        if (presupuesto == null) return false;
        
        // Validar que la cuenta tenga saldo suficiente
        if (cuenta.getSaldoTotal() < monto) return false;
        
        // Realizar operaciones
        cuenta.reducirSaldoTotal(monto);  // Restar de la cuenta
        presupuesto.aumentarSaldo(monto); // Añadir al presupuesto
        
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransaccionConstantes.TIPO_DEPOSITO_PRESUPUESTO,
            monto,
            descripcion + " (Presupuesto: " + presupuesto.getNombre() + ")",
            cuenta,  // Origen: la cuenta
            null,    // Destino: el presupuesto (no es una cuenta)
            buscarCategoriaPorId(idCategoria)
        );

        return crearTransaccion(transaccion);
    }

}