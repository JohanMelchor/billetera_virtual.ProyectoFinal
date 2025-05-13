package co.edu.uniquindio.service;

import co.edu.uniquindio.model.Categoria;

public interface ICategoriaServices {
    boolean crearCategoria(Categoria categoria);
    boolean eliminarCategoria(String idCategoria);
    boolean actualizarCategoria(Categoria categoria);
}