package co.edu.uniquindio.controller;

import co.edu.uniquindio.factory.ModelFactory;
import co.edu.uniquindio.mapping.dto.CategoriaDto;

import java.util.List;

public class CategoriaController {
    ModelFactory modelFactory;
    
    public CategoriaController() {
        modelFactory = ModelFactory.getInstance();
    }
    
    public List<CategoriaDto> obtenerCategorias() {
        return modelFactory.obtenerCategorias();
    }
    
    public boolean agregarCategoria(CategoriaDto categoriaDto) {
        return modelFactory.agregarCategoria(categoriaDto);
    }
    
    public boolean actualizarCategoria(CategoriaDto categoriaDto) {
        return modelFactory.actualizarCategoria(categoriaDto);
    }
    
    public boolean eliminarCategoria(String idCategoria) {
        return modelFactory.eliminarCategoria(idCategoria);
    }
}