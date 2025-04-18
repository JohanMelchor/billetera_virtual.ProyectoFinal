package co.edu.uniquindio.service;

import co.edu.uniquindio.mapping.dto.UsuarioDto;

import java.util.List;

public interface IModelFactoryServices {
    List<UsuarioDto> obtenerUsuarios();
    boolean agregarUsuario(UsuarioDto usuarioDto);
    boolean eliminarUsuario(UsuarioDto usuarioDto);
    boolean actualizarUsuario(UsuarioDto usuarioDto);
}
