package co.edu.uniquindio.model;

import co.edu.uniquindio.service.IUsuarioServices;

import java.util.ArrayList;

public class BilleteraVirtual implements IUsuarioServices {
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

    @Override
    public boolean crearUsuario(Usuario usuario) {
       Usuario NuevoUsuario=buscarUsuario(usuario);
        if(NuevoUsuario==null){
            listaUsuarios.add(usuario);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarUsuario(String idUsuario) {
        Usuario UsuarioEliminado=buscarUsuairoId(idUsuario);
        if(UsuarioEliminado!=null){
            listaUsuarios.remove(UsuarioEliminado);
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
        Usuario UsuarioActualizado=buscarUsuario(usuario);
        if(UsuarioActualizado!=null){
            UsuarioActualizado.setNombreCompleto(usuario.getNombreCompleto());
            UsuarioActualizado.setCorreo(usuario.getCorreo());
            UsuarioActualizado.setTelefono(usuario.getTelefono());
            UsuarioActualizado.setDireccion(usuario.getDireccion());
            return true;
        }
        return false;
    }

    @Override
    public Usuario usuarioExist(String correo, String contrasenia) {
        for (Usuario usuario : listaUsuarios) {
            if(usuario.getCorreo().equals(correo) && usuario.getContrasenia().equals(contrasenia)){
                return usuario;
            }
        }
        return null;
    }
}
