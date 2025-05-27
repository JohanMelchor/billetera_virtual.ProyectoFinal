package co.edu.uniquindio.strategy;

import co.edu.uniquindio.Util.TransaccionConstantes;
import co.edu.uniquindio.mapping.dto.TransaccionDto;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.Node;

public class GastosVsIngresosStrategy extends EstadisticaStrategyBase {

    @Override
    public Node generarGrafico(DatosEstadisticas datos) {
        double totalIngresos = 0.0;
        double totalGastos = 0.0;
        
        for (TransaccionDto transaccion : datos.getTransacciones()) {
            double monto = Double.parseDouble(transaccion.monto());
            String tipo = transaccion.tipoTransaccion();
            
            if (esIngreso(tipo)) {
                totalIngresos += monto;
            } else if (esGasto(tipo)) {
                totalGastos += monto;
            }
            // Las transferencias no se cuentan como ingreso ni gasto
        }
        
        // Crear datos para el gráfico de barras
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Gastos vs Ingresos");
        xAxis.setLabel("Tipo");
        yAxis.setLabel("Monto Total");
        
        // Crear serie de datos
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monto");
        
        series.getData().add(new XYChart.Data<>("Ingresos", totalIngresos));
        series.getData().add(new XYChart.Data<>("Gastos", totalGastos));
        
        barChart.getData().add(series);
        
        return barChart;
    }
    
    private boolean esIngreso(String tipo) {
        return tipo.equals(TransaccionConstantes.TIPO_DEPOSITO_CUENTA) ||
               tipo.equals(TransaccionConstantes.TIPO_DEPOSITO_PRESUPUESTO) ||
               tipo.equals(TransaccionConstantes.TIPO_AJUSTE_POSITIVO) ||
               tipo.equals(TransaccionConstantes.TIPO_DEPOSITO_INICIAL) ||
               tipo.equals(TransaccionConstantes.TIPO_BONIFICACION);
    }
    
    private boolean esGasto(String tipo) {
        return tipo.equals(TransaccionConstantes.TIPO_RETIRO_CUENTA) ||
               tipo.equals(TransaccionConstantes.TIPO_RETIRO_PRESUPUESTO) ||
               tipo.equals(TransaccionConstantes.TIPO_AJUSTE_NEGATIVO) ||
               tipo.equals(TransaccionConstantes.TIPO_PENALIZACION);
    }
    
    @Override
    public String getNombre() {
        return "Gastos vs Ingresos";
    }
    
    @Override
    public String getDescripcion() {
        return "Comparación entre el total de ingresos y gastos del sistema";
    }

}
