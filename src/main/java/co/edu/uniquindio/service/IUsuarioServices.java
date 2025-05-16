package co.edu.uniquindio.service;

import co.edu.uniquindio.model.Usuario;

public interface IUsuarioServices {
    boolean crearUsuario(Usuario usuario);
    boolean eliminarUsuario(String idUsuario);
    boolean actualizarUsuario(Usuario usuario);
    Usuario buscarUsuarioPorId(String idUsuario);
}
