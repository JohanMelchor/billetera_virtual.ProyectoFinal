package co.edu.uniquindio.service;

import co.edu.uniquindio.model.Usuario;

public interface IUsuarioServices {
    boolean crearUsuario(Usuario usuario);
    boolean eliminarUsuario(Usuario usuario);
    boolean actualizarUsuario(Usuario usuario);
}
