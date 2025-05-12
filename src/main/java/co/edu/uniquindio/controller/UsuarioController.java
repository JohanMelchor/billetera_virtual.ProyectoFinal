package co.edu.uniquindio.controller;

import co.edu.uniquindio.factory.ModelFactory;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.model.Usuario;


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

    public boolean eliminarUsuario(String idUsuario){
        return modelFactory.eliminarUsuario(idUsuario);
    }

    public Usuario usuarioExist(String correo, String contrasenia) {
        return modelFactory.usuarioExist(correo, contrasenia);
    }
}

