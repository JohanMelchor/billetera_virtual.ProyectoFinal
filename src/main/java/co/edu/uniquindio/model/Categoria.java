package co.edu.uniquindio.model;

import co.edu.uniquindio.model.Builder.CategoriaBuilder;

public class Categoria {
    private String idCategoria;
    private String nombreCategoria;
    private String descripcionCategoria;
    
    public Categoria() {
    }

    public Categoria(String idCategoria, String nombreCategoria, String descripcionCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.descripcionCategoria = descripcionCategoria;
    }

    public static CategoriaBuilder builder() {
        return new CategoriaBuilder();
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }
    public String getNombreCategoria() {
        return nombreCategoria;
    }
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }
    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

}
