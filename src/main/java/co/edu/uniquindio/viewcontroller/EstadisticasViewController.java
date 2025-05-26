package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.Util.TransaccionConstantes;
import co.edu.uniquindio.controller.*;
import co.edu.uniquindio.mapping.dto.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.*;

public class EstadisticasViewController {
    
    private UsuarioController usuarioController;
    private TransaccionController transaccionController;
    private CategoriaController categoriaController;
    private List<UsuarioDto> listaUsuarios;
    private List<TransaccionDto> listaTransacciones;
    private List<CategoriaDto> listaCategorias;
    
    @FXML
    private ComboBox<String> cbTipoEstadistica;
    
    @FXML
    private Button btnGenerarEstadistica;
    
    @FXML
    private VBox contenedorGraficas;
    
    @FXML
    private Label lblPromedioSaldo;
    
    @FXML
    private Label lblUsuarioMasTransacciones;

    @FXML
    private Label lblCategoriaMasUsada;

    @FXML
    private Label lblTransaccionMasGrande;

    @FXML
    private Label lblPromedioTransacciones;

    @FXML
    private Label lblDiaMasActivo;
    
    @FXML
    void initialize() {
        usuarioController = new UsuarioController();
        transaccionController = new TransaccionController();
        categoriaController = new CategoriaController();
        
        // Inicializar ComboBox de tipos de estadísticas
        cbTipoEstadistica.getItems().addAll(
            "Transacciones por Categoría",
            "Saldo por Usuario",
            "Gastos vs Ingresos",
            "Categorías Más Usadas",
            "Evolución por Días",      
            "Distribución de Montos"
        );
        
        cargarDatos();
    }
    
    private void cargarDatos() {
        listaUsuarios = usuarioController.obtenerUsuarios();
        listaTransacciones = transaccionController.obtenerTransacciones();
        listaCategorias = categoriaController.obtenerCategorias();
        
        // Calcular estadísticas generales
        calcularEstadisticasGenerales();
    }
    
    private void calcularEstadisticasGenerales() {
        // Promedio de saldo de usuarios
        double sumaTotal = 0.0;
        for(UsuarioDto usuario : listaUsuarios) {
            sumaTotal += Double.parseDouble(usuario.saldo());
        }
        double promedio = listaUsuarios.isEmpty() ? 0.0 : sumaTotal / listaUsuarios.size();
        lblPromedioSaldo.setText(String.format("%.2f", promedio));
        
        Map<String, Integer> contadorPorUsuario = new HashMap<>();
        CuentaController cuentaController = new CuentaController();
        
        for(TransaccionDto transaccion : listaTransacciones) {
            // Contar transacciones por cuenta origen
            if(transaccion.idCuentaOrigen() != null && !transaccion.idCuentaOrigen().isEmpty()) {
                // Buscar el dueño de esta cuenta
                for(UsuarioDto usuario : listaUsuarios) {
                    List<CuentaDto> cuentasUsuario = cuentaController.obtenerCuentasPorUsuario(usuario.idUsuario());
                    for(CuentaDto cuenta : cuentasUsuario) {
                        if(cuenta.idCuenta().equals(transaccion.idCuentaOrigen())) {
                            contadorPorUsuario.merge(usuario.nombreCompleto(), 1, Integer::sum);
                            break;
                        }
                    }
                }
            }
            
            // Contar transacciones por cuenta destino (solo si es diferente)
            if(transaccion.idCuentaDestino() != null && 
            !transaccion.idCuentaDestino().isEmpty() &&
            !transaccion.idCuentaDestino().equals(transaccion.idCuentaOrigen())) {
                
                for(UsuarioDto usuario : listaUsuarios) {
                    List<CuentaDto> cuentasUsuario = cuentaController.obtenerCuentasPorUsuario(usuario.idUsuario());
                    for(CuentaDto cuenta : cuentasUsuario) {
                        if(cuenta.idCuenta().equals(transaccion.idCuentaDestino())) {
                            contadorPorUsuario.merge(usuario.nombreCompleto(), 1, Integer::sum);
                            break;
                        }
                    }
                }
            }
        }
        
        // Encontrar el usuario con más transacciones
        String usuarioMasTransacciones = "Ninguno";
        int maxTransacciones = 0;
        for(Map.Entry<String, Integer> entry : contadorPorUsuario.entrySet()) {
            if(entry.getValue() > maxTransacciones) {
                maxTransacciones = entry.getValue();
                usuarioMasTransacciones = entry.getKey();
            }
        }
        
        lblUsuarioMasTransacciones.setText(usuarioMasTransacciones + " (" + maxTransacciones + ")");
        
        calcularCategoriaMasUsada();
        calcularTransaccionMasGrande();
        calcularPromedioTransaccionPorUsuario();
        calcularDiaMasActivo();
    }
    
    @FXML
    void onGenerarEstadistica(ActionEvent event) {
        String tipoEstadistica = cbTipoEstadistica.getValue();
        if(tipoEstadistica == null || tipoEstadistica.isEmpty()) {
            return;
        }
        
        // Limpiar contenedor de gráficas
        contenedorGraficas.getChildren().clear();
        
        switch(tipoEstadistica) {
            case "Transacciones por Categoría":
                generarGraficaTransaccionesPorCategoria();
                break;
            case "Saldo por Usuario":
                generarGraficaSaldoPorUsuario();
                break;
            case "Gastos vs Ingresos":
                generarGraficaGastosVsIngresos();
                break;
            case "Categorías Más Usadas":           
                generarGraficaCategoriasMasUsadas();
                break;
            case "Evolución por Días":              
                generarGraficaEvolucionPorDias();
                break;
            case "Distribución de Montos":          
                generarGraficaDistribucionMontos();
                break;
        }
    }
    
    private void generarGraficaTransaccionesPorCategoria() {
        // Contar transacciones por categoría
        Map<String, Integer> contadorCategoria = new HashMap<>();
        for(TransaccionDto transaccion : listaTransacciones) {
            String idCategoria = transaccion.idCategoria();
            if(idCategoria != null && !idCategoria.isEmpty()) {
                String nombreCategoria = obtenerNombreCategoria(idCategoria);
                contadorCategoria.put(
                    nombreCategoria,
                    contadorCategoria.getOrDefault(nombreCategoria, 0) + 1
                );
            } else {
                contadorCategoria.put(
                    "Sin Categoría",
                    contadorCategoria.getOrDefault("Sin Categoría", 0) + 1
                );
            }
        }
        
        // Crear datos para el gráfico de pastel
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(Map.Entry<String, Integer> entry : contadorCategoria.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
        }
        
        // Crear gráfico de pastel
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Transacciones por Categoría");
        pieChart.setLabelsVisible(true);
        
        // Agregar gráfico al contenedor
        contenedorGraficas.getChildren().add(pieChart);
    }
    
    private void generarGraficaSaldoPorUsuario() {
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
        
        for(UsuarioDto usuario : listaUsuarios) {
            series.getData().add(new XYChart.Data<>(
                usuario.nombreCompleto(),
                Double.parseDouble(usuario.saldo())
            ));
        }
        
        barChart.getData().add(series);
        
        
        contenedorGraficas.getChildren().add(barChart);
    }
    
    private void generarGraficaGastosVsIngresos() {

        double totalIngresos = 0.0;
        double totalGastos = 0.0;
        
        for(TransaccionDto transaccion : listaTransacciones) {
            double monto = Double.parseDouble(transaccion.monto());
            String tipo = transaccion.tipoTransaccion();
            

            if (tipo.equals(TransaccionConstantes.TIPO_DEPOSITO_CUENTA) ||
                tipo.equals(TransaccionConstantes.TIPO_DEPOSITO_PRESUPUESTO) ||
                tipo.equals(TransaccionConstantes.TIPO_AJUSTE_POSITIVO) ||
                tipo.equals(TransaccionConstantes.TIPO_DEPOSITO_INICIAL) ||
                tipo.equals(TransaccionConstantes.TIPO_BONIFICACION)) {
                
                totalIngresos += monto;
                
            } else if (tipo.equals(TransaccionConstantes.TIPO_RETIRO_CUENTA) ||
                    tipo.equals(TransaccionConstantes.TIPO_RETIRO_PRESUPUESTO) ||
                    tipo.equals(TransaccionConstantes.TIPO_AJUSTE_NEGATIVO) ||
                    tipo.equals(TransaccionConstantes.TIPO_PENALIZACION)) {
                
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
        
        contenedorGraficas.getChildren().add(barChart);
    }
    
    private String obtenerNombreCategoria(String idCategoria) {
        for(CategoriaDto categoria : listaCategorias) {
            if(categoria.idCategoria().equals(idCategoria)) {
                return categoria.nombre();
            }
        }
        return "Desconocida";
    }

    private void generarGraficaCategoriasMasUsadas() {
        Map<String, Integer> contadorCategorias = new HashMap<>();
        
        for(TransaccionDto transaccion : listaTransacciones) {
            String idCategoria = transaccion.idCategoria();
            String nombreCategoria = idCategoria != null && !idCategoria.isEmpty() ? 
                obtenerNombreCategoria(idCategoria) : "Sin Categoría";
            contadorCategorias.merge(nombreCategoria, 1, Integer::sum);
        }
        
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
        contenedorGraficas.getChildren().add(barChart);
    }

    private void generarGraficaEvolucionPorDias() {
        Map<String, Integer> transaccionesPorDia = new TreeMap<>();
        
        for(TransaccionDto transaccion : listaTransacciones) {
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
        contenedorGraficas.getChildren().add(lineChart);
    }

    private void generarGraficaDistribucionMontos() {
        // Categorizar montos en rangos
        Map<String, Integer> rangoMontos = new LinkedHashMap<>();
        rangoMontos.put("$0 - $50", 0);
        rangoMontos.put("$50 - $100", 0);
        rangoMontos.put("$100 - $500", 0);
        rangoMontos.put("$500 - $1000", 0);
        rangoMontos.put("$1000+", 0);
        
        for(TransaccionDto transaccion : listaTransacciones) {
            double monto = Double.parseDouble(transaccion.monto());
            
            if(monto <= 50) {
                rangoMontos.merge("$0 - $50", 1, Integer::sum);
            } else if(monto <= 100) {
                rangoMontos.merge("$50 - $100", 1, Integer::sum);
            } else if(monto <= 500) {
                rangoMontos.merge("$100 - $500", 1, Integer::sum);
            } else if(monto <= 1000) {
                rangoMontos.merge("$500 - $1000", 1, Integer::sum);
            } else {
                rangoMontos.merge("$1000+", 1, Integer::sum);
            }
        }

        // Crear gráfico de pastel
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(Map.Entry<String, Integer> entry : rangoMontos.entrySet()) {
            if(entry.getValue() > 0) { // Solo mostrar rangos con datos
                pieChartData.add(new PieChart.Data(
                    entry.getKey() + " (" + entry.getValue() + ")", 
                    entry.getValue()));
            }
        }
        
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Distribución de Transacciones por Monto");
        pieChart.setLabelsVisible(true);
        
        contenedorGraficas.getChildren().add(pieChart);
    }

    private void calcularCategoriaMasUsada() {
        Map<String, Integer> contadorCategorias = new HashMap<>();
    
        for(TransaccionDto transaccion : listaTransacciones) {
            String idCategoria = transaccion.idCategoria();
            String nombreCategoria;
            
            if(idCategoria != null && !idCategoria.isEmpty()) {
                nombreCategoria = obtenerNombreCategoria(idCategoria);
            } else {
                nombreCategoria = "Sin Categoría";
            }
            
            contadorCategorias.merge(nombreCategoria, 1, Integer::sum);
        }
        
        // Encontrar la categoría más usada
        String categoriaMasUsada = "Ninguna";
        int maxUsos = 0;
        for(Map.Entry<String, Integer> entry : contadorCategorias.entrySet()) {
            if(entry.getValue() > maxUsos) {
                maxUsos = entry.getValue();
                categoriaMasUsada = entry.getKey();
            }
        }
        
        // Actualizar label
        if (lblCategoriaMasUsada != null) {
            lblCategoriaMasUsada.setText(categoriaMasUsada + " (" + maxUsos + ")");
        }
    }

    private void calcularTransaccionMasGrande() {
        double montoMayor = 0.0;
        String tipoTransaccionMayor = "Ninguna";
        
        for(TransaccionDto transaccion : listaTransacciones) {
            double monto = Double.parseDouble(transaccion.monto());
            if(monto > montoMayor) {
                montoMayor = monto;
                tipoTransaccionMayor = simplificarTipoTransaccion(transaccion.tipoTransaccion());
            }
        }
        
        // Actualizar label
        if (lblTransaccionMasGrande != null) {
            lblTransaccionMasGrande.setText(String.format("$%.2f (%s)", montoMayor, tipoTransaccionMayor));
        }
    }

    private void calcularPromedioTransaccionPorUsuario() {
        if(listaUsuarios.isEmpty()) {
            if (lblPromedioTransacciones != null) {
                lblPromedioTransacciones.setText("0");
            }
            return;
        }
        
        double promedio = (double) listaTransacciones.size() / listaUsuarios.size();
        
        if (lblPromedioTransacciones != null) {
            lblPromedioTransacciones.setText(String.format("%.1f", promedio));
        }
    }

    private void calcularDiaMasActivo() {
        Map<String, Integer> transaccionesPorDia = new HashMap<>();
        
        for(TransaccionDto transaccion : listaTransacciones) {
            try {
                // Extraer fecha de la transacción (formato: "yyyy-MM-dd HH:mm:ss")
                String fecha = transaccion.fecha();
                String dia = fecha.substring(0, 10); // Solo la parte de la fecha
                transaccionesPorDia.merge(dia, 1, Integer::sum);
            } catch (Exception e) {
                // Si hay error con el formato, ignorar esta transacción
            }
        }
        
        String diaMasActivo = "Ninguno";
        int maxTransacciones = 0;
        for(Map.Entry<String, Integer> entry : transaccionesPorDia.entrySet()) {
            if(entry.getValue() > maxTransacciones) {
                maxTransacciones = entry.getValue();
                diaMasActivo = entry.getKey();
            }
        }
        
        if (lblDiaMasActivo != null) {
            lblDiaMasActivo.setText(diaMasActivo + " (" + maxTransacciones + ")");
        }
    }

    private String simplificarTipoTransaccion(String tipoCompleto) {
        if (tipoCompleto.contains("DEPÓSITO")) return "Depósito";
        if (tipoCompleto.contains("RETIRO")) return "Retiro";
        if (tipoCompleto.contains("TRANSFERENCIA")) return "Transferencia";
        if (tipoCompleto.contains("AJUSTE")) return "Ajuste";
        if (tipoCompleto.contains("BONIFICACIÓN")) return "Bonificación";
        if (tipoCompleto.contains("PENALIZACIÓN")) return "Penalización";
        return tipoCompleto;
    }


}