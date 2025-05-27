package co.edu.uniquindio.factory;

import java.util.LinkedHashMap;
import java.util.Map;

import co.edu.uniquindio.service.IEstadisticaStrategy;
import co.edu.uniquindio.strategy.CategoriasMasUsadasStrategy;
import co.edu.uniquindio.strategy.DistribucionMontosStrategy;
import co.edu.uniquindio.strategy.EvolucionPorDiasStrategy;
import co.edu.uniquindio.strategy.GastosVsIngresosStrategy;
import co.edu.uniquindio.strategy.SaldoPorUsuarioStrategy;
import co.edu.uniquindio.strategy.TransaccionesPorCategoriaStrategy;

public class EstadisticaStrategyFactory {
    private static final Map<String, IEstadisticaStrategy> strategies = new LinkedHashMap<>();
    
    static {
        strategies.put("Transacciones por Categoría", new TransaccionesPorCategoriaStrategy());
        strategies.put("Saldo por Usuario", new SaldoPorUsuarioStrategy());
        strategies.put("Gastos vs Ingresos", new GastosVsIngresosStrategy());
        strategies.put("Categorías Más Usadas", new CategoriasMasUsadasStrategy());
        strategies.put("Evolución por Días", new EvolucionPorDiasStrategy());
        strategies.put("Distribución de Montos", new DistribucionMontosStrategy());
    }
    
    public static IEstadisticaStrategy getStrategy(String nombre) {
        IEstadisticaStrategy strategy = strategies.get(nombre);
        if (strategy == null) {
            throw new IllegalArgumentException("Estrategia de estadística no encontrada: " + nombre);
        }
        return strategy;
    }
    
    public static boolean existeStrategy(String nombre) {
        return strategies.containsKey(nombre);
    }
    
    public static String[] getNombresDisponibles() {
        return strategies.keySet().toArray(new String[0]);
    }
    
    public static Map<String, IEstadisticaStrategy> getAllStrategies() {
        return new LinkedHashMap<>(strategies);
    }
}
