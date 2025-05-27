package co.edu.uniquindio.strategy;

import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.mapping.dto.TransaccionDto;
import co.edu.uniquindio.service.IEstadisticaStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public abstract class EstadisticaStrategyBase implements IEstadisticaStrategy {

    protected String obtenerNombreCategoria(String idCategoria, DatosEstadisticas datos) {
        if (idCategoria == null || idCategoria.isEmpty()) {
            return "Sin Categoría";
        }
        
        for (CategoriaDto categoria : datos.getCategorias()) {
            if (categoria.idCategoria().equals(idCategoria)) {
                return categoria.nombre();
            }
        }
        return "Desconocida";
    }
    
    protected Map<String, Integer> contarTransaccionesPorCategoria(DatosEstadisticas datos) {
        Map<String, Integer> contador = new HashMap<>();
        
        for (TransaccionDto transaccion : datos.getTransacciones()) {
            String nombreCategoria = obtenerNombreCategoria(transaccion.idCategoria(), datos);
            contador.merge(nombreCategoria, 1, Integer::sum);
        }
        
        return contador;
    }
}
