package co.edu.uniquindio.controller;

import co.edu.uniquindio.factory.ModelFactory;
import co.edu.uniquindio.mapping.dto.UsuarioDto;

import java.util.List;

public class UsuarioController {
    ModelFactory modelFactory;
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

    public boolean eliminarUsuario(UsuarioDto usuarioDto){
        return modelFactory.eliminarUsuario(usuarioDto);
    }
}

