package co.edu.uniquindio.model.Builder;
import co.edu.uniquindio.model.Categoria;

public class CategoriaBuilder {
    private String idCategoria;
    private String nombre;
    private String descripcion;

    public CategoriaBuilder IdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
        return this;
    }
    public CategoriaBuilder Nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public CategoriaBuilder Descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public Categoria build() {
        return new Categoria(idCategoria, nombre, descripcion);
    }
    

}
