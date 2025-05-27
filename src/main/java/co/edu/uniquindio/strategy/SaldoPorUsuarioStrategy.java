package co.edu.uniquindio.strategy;

import co.edu.uniquindio.mapping.dto.UsuarioDto;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.Node;

public class SaldoPorUsuarioStrategy extends EstadisticaStrategyBase{
    @Override
    public Node generarGrafico(DatosEstadisticas datos) {
        // Crear datos para el gráfico de barras
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Saldo por Usuario");
        xAxis.setLabel("Usuario");
        yAxis.setLabel("Saldo");
        
        // Crear serie de datos
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Saldo");
        
        for (UsuarioDto usuario : datos.getUsuarios()) {
            series.getData().add(new XYChart.Data<>(
                usuario.nombreCompleto(),
                Double.parseDouble(usuario.saldo())
            ));
        }
        
        barChart.getData().add(series);
        
        return barChart;
    }
    
    @Override
    public String getNombre() {
        return "Saldo por Usuario";
    }
    
    @Override
    public String getDescripcion() {
        return "Gráfico de barras que muestra el saldo actual de cada usuario";
    }
}
