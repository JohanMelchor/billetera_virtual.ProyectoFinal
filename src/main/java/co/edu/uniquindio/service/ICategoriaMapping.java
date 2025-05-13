package co.edu.uniquindio.service;

import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.model.Categoria;

import java.util.List;

public interface ICategoriaMapping {
    List<CategoriaDto> getCategoriaDto(List<Categoria> listaCategorias);
    CategoriaDto categoriaToCategoriaDto(Categoria categoria);
    Categoria categoriaDtoToCategoria(CategoriaDto categoriaDto);
}