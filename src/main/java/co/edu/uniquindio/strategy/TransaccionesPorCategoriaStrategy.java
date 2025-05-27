package co.edu.uniquindio.strategy;

import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.Node;

public class TransaccionesPorCategoriaStrategy extends EstadisticaStrategyBase{
    @Override
    public Node generarGrafico(DatosEstadisticas datos) {
        Map<String, Integer> contadorCategoria = contarTransaccionesPorCategoria(datos);
        
        // Crear datos para el gráfico de pastel
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : contadorCategoria.entrySet()) {
            pieChartData.add(new PieChart.Data(
                entry.getKey() + " (" + entry.getValue() + ")", 
                entry.getValue()
            ));
        }
        
        // Crear gráfico de pastel
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Transacciones por Categoría");
        pieChart.setLabelsVisible(true);
        
        return pieChart;
    }
    
    @Override
    public String getNombre() {
        return "Transacciones por Categoría";
    }
    
    @Override
    public String getDescripcion() {
        return "Muestra la distribución de transacciones agrupadas por categoría";
    }
}
