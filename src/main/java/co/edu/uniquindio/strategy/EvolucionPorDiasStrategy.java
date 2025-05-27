package co.edu.uniquindio.strategy;

import java.util.Map;
import java.util.TreeMap;

import co.edu.uniquindio.mapping.dto.TransaccionDto;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.Node;

public class EvolucionPorDiasStrategy extends EstadisticaStrategyBase {

    @Override
    public Node generarGrafico(DatosEstadisticas datos) {
        Map<String, Integer> transaccionesPorDia = new TreeMap<>();
        
        for (TransaccionDto transaccion : datos.getTransacciones()) {
            try {
                String fecha = transaccion.fecha();
                String dia = fecha.substring(0, 10); // YYYY-MM-DD
                transaccionesPorDia.merge(dia, 1, Integer::sum);
            } catch (Exception e) {
                // Ignorar fechas mal formateadas
            }
        }

        // Crear gráfico de líneas
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Evolución de Transacciones por Día");
        xAxis.setLabel("Fecha");
        yAxis.setLabel("Número de Transacciones");
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Transacciones Diarias");
        
        // Tomar solo los últimos 30 días o menos
        transaccionesPorDia.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .limit(30)
            .forEach(entry -> {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            });
        
        lineChart.getData().add(series);
        
        return lineChart;
    }
    
    @Override
    public String getNombre() {
        return "Evolución por Días";
    }
    
    @Override
    public String getDescripcion() {
        return "Línea de tiempo que muestra la evolución de transacciones por día";
    }

}
