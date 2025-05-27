package co.edu.uniquindio.strategy;

import java.util.LinkedHashMap;
import java.util.Map;

import co.edu.uniquindio.mapping.dto.TransaccionDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.Node;

public class DistribucionMontosStrategy extends EstadisticaStrategyBase {
    @Override
    public Node generarGrafico(DatosEstadisticas datos) {
        // Categorizar montos en rangos
        Map<String, Integer> rangoMontos = new LinkedHashMap<>();
        rangoMontos.put("$0 - $50", 0);
        rangoMontos.put("$50 - $100", 0);
        rangoMontos.put("$100 - $500", 0);
        rangoMontos.put("$500 - $1000", 0);
        rangoMontos.put("$1000+", 0);
        
        for (TransaccionDto transaccion : datos.getTransacciones()) {
            double monto = Double.parseDouble(transaccion.monto());
            
            if (monto <= 50) {
                rangoMontos.merge("$0 - $50", 1, Integer::sum);
            } else if (monto <= 100) {
                rangoMontos.merge("$50 - $100", 1, Integer::sum);
            } else if (monto <= 500) {
                rangoMontos.merge("$100 - $500", 1, Integer::sum);
            } else if (monto <= 1000) {
                rangoMontos.merge("$500 - $1000", 1, Integer::sum);
            } else {
                rangoMontos.merge("$1000+", 1, Integer::sum);
            }
        }

        // Crear gráfico de pastel
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : rangoMontos.entrySet()) {
            if (entry.getValue() > 0) { // Solo mostrar rangos con datos
                pieChartData.add(new PieChart.Data(
                    entry.getKey() + " (" + entry.getValue() + ")", 
                    entry.getValue()));
            }
        }
        
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Distribución de Transacciones por Monto");
        pieChart.setLabelsVisible(true);
        
        return pieChart;
    }
    
    @Override
    public String getNombre() {
        return "Distribución de Montos";
    }
    
    @Override
    public String getDescripcion() {
        return "Distribución de transacciones agrupadas por rangos de montos";
    }
}
