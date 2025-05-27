package co.edu.uniquindio.strategy;

import co.edu.uniquindio.factory.EstadisticaStrategyFactory;
import co.edu.uniquindio.service.IEstadisticaStrategy;
import javafx.scene.Node;

public class EstadisticaProcessor {
    private final DatosEstadisticas datos;
    
    public EstadisticaProcessor(DatosEstadisticas datos) {
        this.datos = datos;
    }
    
    public Node generarEstadistica(String nombreEstadistica) {
        try {
            IEstadisticaStrategy strategy = EstadisticaStrategyFactory.getStrategy(nombreEstadistica);
            return strategy.generarGrafico(datos);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de estadística no válido: " + nombreEstadistica, e);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar estadística: " + e.getMessage(), e);
        }
    }
    
    public String[] getEstadisticasDisponibles() {
        return EstadisticaStrategyFactory.getNombresDisponibles();
    }
    
    public String getDescripcion(String nombreEstadistica) {
        try {
            IEstadisticaStrategy strategy = EstadisticaStrategyFactory.getStrategy(nombreEstadistica);
            return strategy.getDescripcion();
        } catch (Exception e) {
            return "Descripción no disponible";
        }
    }
}
