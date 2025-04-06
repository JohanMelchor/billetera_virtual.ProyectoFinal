package co.edu.uniquindio.service;

import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.model.Usuario;

import java.util.List;

public interface IUsuarioMapping {
    List<UsuarioDto> getUsuarioDto(List<Usuario> listaUsuarios);
    UsuarioDto usuarioToUsuarioDto(Usuario usuario);
    Usuario usuarioDtoToUsuario(UsuarioDto usuarioDto);
}
