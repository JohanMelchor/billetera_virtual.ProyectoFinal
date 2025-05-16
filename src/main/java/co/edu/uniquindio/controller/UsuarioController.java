package co.edu.uniquindio.controller;

import co.edu.uniquindio.factory.ModelFactory;
import co.edu.uniquindio.mapping.dto.UsuarioDto;

import java.util.List;

public class UsuarioController {
    private ModelFactory modelFactory;

    public UsuarioController() {
        modelFactory=ModelFactory.getInstance();
    }

    public List<UsuarioDto> obtenerUsuarios(){return modelFactory.obtenerUsuarios();}

    public boolean agregarUsuario(UsuarioDto usuarioDto){
        return modelFactory.agregarUsuario(usuarioDto);
    }

    public boolean actualizarUsuario(UsuarioDto usuarioDto){
        return modelFactory.actualizarUsuario(usuarioDto);
    }

    public boolean eliminarUsuario(String idUsuario){
        return modelFactory.eliminarUsuario(idUsuario);
    }

    public boolean registrarUsuario(UsuarioDto usuarioDto) {
        return modelFactory.agregarUsuario(usuarioDto);
    }

    public UsuarioDto buscarUsuarioPorId(String idUsuario) {
        return modelFactory.buscarUsuarioDtoPorId(idUsuario);
    }

    public boolean validarCredenciales(String idUsuario, String password) {
        return modelFactory.validarCredenciales(idUsuario, password);
    }
}

