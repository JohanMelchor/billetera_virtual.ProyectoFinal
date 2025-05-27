package co.edu.uniquindio.strategy;

import java.util.Map;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.Node;

public class CategoriasMasUsadasStrategy extends EstadisticaStrategyBase {
    @Override
    public Node generarGrafico(DatosEstadisticas datos) {
        Map<String, Integer> contadorCategorias = contarTransaccionesPorCategoria(datos);
        
        // Crear gráfico de barras horizontal
        CategoryAxis yAxis = new CategoryAxis();
        NumberAxis xAxis = new NumberAxis();
        BarChart<Number, String> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Categorías Más Usadas");
        xAxis.setLabel("Número de Transacciones");
        yAxis.setLabel("Categoría");
        
        XYChart.Series<Number, String> series = new XYChart.Series<>();
        series.setName("Uso de Categorías");
        
        // Ordenar por más usadas
        contadorCategorias.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10) // Solo las top 10
            .forEach(entry -> {
                series.getData().add(new XYChart.Data<>(entry.getValue(), entry.getKey()));
            });
        
        barChart.getData().add(series);
        
        return barChart;
    }
    
    @Override
    public String getNombre() {
        return "Categorías Más Usadas";
    }
    
    @Override
    public String getDescripcion() {
        return "Top 10 de categorías más utilizadas en las transacciones";
    }
}
