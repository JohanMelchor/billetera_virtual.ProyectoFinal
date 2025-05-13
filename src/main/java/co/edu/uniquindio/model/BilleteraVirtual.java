package co.edu.uniquindio.model;

import co.edu.uniquindio.service.*;
import co.edu.uniquindio.Util.TransaccionConstantes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BilleteraVirtual implements IUsuarioServices, ICuentaServices, ITransaccionServices, IPresupuestoServices, ICategoriaServices {
    private ArrayList<Usuario>listaUsuarios= new ArrayList<>();
    private ArrayList<Cuenta>listaCuentas= new ArrayList<>();
    private ArrayList<Transaccion>listaTransacciones= new ArrayList<>();
    private ArrayList<Presupuesto>listaPresupuestos= new ArrayList<>();
    private ArrayList<Categoria>listaCategorias= new ArrayList<>();
    private ArrayList<Administrador>listaAdministradores= new ArrayList<>();
    private ArrayList<Reporte>listaReportes= new ArrayList<>();

    public BilleteraVirtual(){}

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
       Usuario nuevoUsuario = buscarUsuario(usuario);
        if(nuevoUsuario == null){
            listaUsuarios.add(usuario);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarUsuario(String idUsuario) {
        Usuario usuarioEliminado = buscarUsuairoId(idUsuario);
        if(usuarioEliminado != null){
            listaUsuarios.remove(usuarioEliminado);
            return true;
        }
        return false;
    }

    private Usuario buscarUsuairoId(String idUsuario) {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getIdUsuario().equalsIgnoreCase(idUsuario)) {return usuario;}
        }
        return null;
    }

    private Usuario buscarUsuario(Usuario nuevUsuario) {
        for(Usuario usuario: listaUsuarios) {
            if(usuario.getIdUsuario().equalsIgnoreCase(nuevUsuario.getIdUsuario())) return usuario;
        }
        return null;
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        Usuario usuarioActualizado = buscarUsuario(usuario);
        if(usuarioActualizado != null){
            usuarioActualizado.setNombreCompleto(usuario.getNombreCompleto());
            usuarioActualizado.setCorreo(usuario.getCorreo());
            usuarioActualizado.setTelefono(usuario.getTelefono());
            usuarioActualizado.setDireccion(usuario.getDireccion());
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
    public boolean agregarFondos(String idCuenta, Double monto) {
        Cuenta cuenta = buscarCuentaPorId(idCuenta);
        if(cuenta != null && cuenta.getPresupuesto() != null) {
            cuenta.getPresupuesto().aumentarSaldo(monto);
            
            // Crear transacción de depósito
            Transaccion transaccion = new Transaccion(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                TransaccionConstantes.TIPO_DEPOSITO,
                monto,
                "Depósito de fondos",
                null,
                cuenta,
                null
            );
            listaTransacciones.add(transaccion);
            
            return true;
        }
        return false;
    }

    @Override
    public boolean retirarFondos(String idCuenta, Double monto) {
        Cuenta cuenta = buscarCuentaPorId(idCuenta);
        if(cuenta != null && cuenta.getPresupuesto() != null) {
            if(cuenta.getPresupuesto().reducirSaldo(monto)) {
                
                // Crear transacción de retiro
                Transaccion transaccion = new Transaccion(
                    UUID.randomUUID().toString(),
                    LocalDateTime.now(),
                    TransaccionConstantes.TIPO_RETIRO,
                    monto,
                    "Retiro de fondos",
                    cuenta,
                    null,
                    null
                );
                listaTransacciones.add(transaccion);
                
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean transferirFondos(String idCuentaOrigen, String idCuentaDestino, Double monto) {
        Cuenta cuentaOrigen = buscarCuentaPorId(idCuentaOrigen);
        Cuenta cuentaDestino = buscarCuentaPorId(idCuentaDestino);
        
        if(cuentaOrigen != null && cuentaDestino != null && 
           cuentaOrigen.getPresupuesto() != null && cuentaDestino.getPresupuesto() != null) {
            
            if(cuentaOrigen.getPresupuesto().reducirSaldo(monto)) {
                cuentaDestino.getPresupuesto().aumentarSaldo(monto);
                
                // Crear transacción de transferencia
                Transaccion transaccion = new Transaccion(
                    UUID.randomUUID().toString(),
                    LocalDateTime.now(),
                    TransaccionConstantes.TIPO_TRANSFERENCIA,
                    monto,
                    "Transferencia entre cuentas",
                    cuentaOrigen,
                    cuentaDestino,
                    null
                );
                listaTransacciones.add(transaccion);
                
                return true;
            }
        }
        return false;
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

    // Método para buscar usuario por id
    public Usuario buscarUsuarioPorId(String idUsuario) {
        for(Usuario usuario : listaUsuarios) {
            if(usuario.getIdUsuario().equals(idUsuario)) {
                return usuario;
            }
        }
        return null;
    }
}