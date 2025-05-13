package co.edu.uniquindio.viewcontroller;

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
    void initialize() {
        usuarioController = new UsuarioController();
        transaccionController = new TransaccionController();
        categoriaController = new CategoriaController();
        
        // Inicializar ComboBox de tipos de estadísticas
        cbTipoEstadistica.getItems().addAll(
            "Transacciones por Categoría",
            "Saldo por Usuario",
            "Gastos vs Ingresos"
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
        
        // Usuario con más transacciones
        Map<String, Integer> contadorTransacciones = new HashMap<>();
        for(TransaccionDto transaccion : listaTransacciones) {
            String idCuentaOrigen = transaccion.idCuentaOrigen();
            String idCuentaDestino = transaccion.idCuentaDestino();
            
            // Contar transacciones por usuario (origen y destino)
            if(idCuentaOrigen != null && !idCuentaOrigen.isEmpty()) {
                // Buscar el usuario dueño de la cuenta
                for(UsuarioDto usuario : listaUsuarios) {
                    List<CuentaDto> cuentasUsuario = obtenerCuentasPorUsuario(usuario.idUsuario());
                    for(CuentaDto cuenta : cuentasUsuario) {
                        if(cuenta.idCuenta().equals(idCuentaOrigen)) {
                            contadorTransacciones.put(
                                usuario.nombreCompleto(),
                                contadorTransacciones.getOrDefault(usuario.nombreCompleto(), 0) + 1
                            );
                            break;
                        }
                    }
                }
            }
            
            if(idCuentaDestino != null && !idCuentaDestino.isEmpty()) {
                // Buscar el usuario dueño de la cuenta
                for(UsuarioDto usuario : listaUsuarios) {
                    List<CuentaDto> cuentasUsuario = obtenerCuentasPorUsuario(usuario.idUsuario());
                    for(CuentaDto cuenta : cuentasUsuario) {
                        if(cuenta.idCuenta().equals(idCuentaDestino)) {
                            contadorTransacciones.put(
                                usuario.nombreCompleto(),
                                contadorTransacciones.getOrDefault(usuario.nombreCompleto(), 0) + 1
                            );
                            break;
                        }
                    }
                }
            }
        }
        
        // Encontrar el usuario con más transacciones
        String usuarioMasTransacciones = "Ninguno";
        int maxTransacciones = 0;
        for(Map.Entry<String, Integer> entry : contadorTransacciones.entrySet()) {
            if(entry.getValue() > maxTransacciones) {
                maxTransacciones = entry.getValue();
                usuarioMasTransacciones = entry.getKey();
            }
        }
        
        lblUsuarioMasTransacciones.setText(usuarioMasTransacciones + " (" + maxTransacciones + ")");
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
        
        // Agregar gráfico al contenedor
        contenedorGraficas.getChildren().add(barChart);
    }
    
    private void generarGraficaGastosVsIngresos() {
        // Calcular totales de gastos e ingresos
        double totalIngresos = 0.0;
        double totalGastos = 0.0;
        
        for(TransaccionDto transaccion : listaTransacciones) {
            double monto = Double.parseDouble(transaccion.monto());
            
            switch(transaccion.tipoTransaccion()) {
                case "DEPOSITO":
                    totalIngresos += monto;
                    break;
                case "RETIRO":
                    totalGastos += monto;
                    break;
                case "TRANSFERENCIA":
                    // Las transferencias no se cuentan como gasto ni ingreso
                    break;
            }
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
        
        // Agregar gráfico al contenedor
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
    
    private List<CuentaDto> obtenerCuentasPorUsuario(String idUsuario) {
        // Para simplificar, creamos una lista vacía
        // En un sistema real, usaríamos el controlador de cuentas
        return new ArrayList<>();
    }
}