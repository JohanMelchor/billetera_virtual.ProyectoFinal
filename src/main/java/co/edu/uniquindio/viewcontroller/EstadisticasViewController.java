package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.factory.AlertaManagerFactory;
import co.edu.uniquindio.mapping.dto.*;
import co.edu.uniquindio.service.IAlertaManager;
import co.edu.uniquindio.strategy.DatosEstadisticas;
import co.edu.uniquindio.strategy.EstadisticaProcessor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import java.util.*;

public class EstadisticasViewController {
    
    private List<UsuarioDto> listaUsuarios;
    private List<TransaccionDto> listaTransacciones;
    private List<CategoriaDto> listaCategorias;
    private BilleteraFacade facade;
    private IAlertaManager alertaManager;
    private EstadisticaProcessor estadisticaProcessor;
    
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
        facade = new BilleteraFacade();
        alertaManager = AlertaManagerFactory.crearManagerCompleto();
        cargarDatos();
    
        // Crear processor con los datos
        DatosEstadisticas datos = new DatosEstadisticas(listaUsuarios, listaTransacciones, listaCategorias, facade);
        estadisticaProcessor = new EstadisticaProcessor(datos);
        
        // Cargar nombres automáticamente desde el factory
        cbTipoEstadistica.getItems().clear();
        cbTipoEstadistica.getItems().addAll(estadisticaProcessor.getEstadisticasDisponibles());
        
        calcularEstadisticasGenerales();
    }
    
    private void cargarDatos() {
        listaUsuarios = facade.obtenerUsuarios();
        listaTransacciones = facade.obtenerTransacciones();
        listaCategorias = facade.obtenerCategorias();
        
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
        
        for(TransaccionDto transaccion : listaTransacciones) {
            // Contar transacciones por cuenta origen
            if(transaccion.idCuentaOrigen() != null && !transaccion.idCuentaOrigen().isEmpty()) {
                // Buscar el dueño de esta cuenta
                for(UsuarioDto usuario : listaUsuarios) {
                    List<CuentaDto> cuentasUsuario = facade.obtenerCuentasPorUsuario(usuario.idUsuario());
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
                    List<CuentaDto> cuentasUsuario = facade.obtenerCuentasPorUsuario(usuario.idUsuario());
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
        if (tipoEstadistica == null || tipoEstadistica.isEmpty()) {
            return;
        }
        
        try {
            // Limpiar contenedor de gráficas
            contenedorGraficas.getChildren().clear();
            
            // Generar estadística usando Strategy
            Node grafico = estadisticaProcessor.generarEstadistica(tipoEstadistica);
            contenedorGraficas.getChildren().add(grafico);
            
        } catch (Exception e) {
            mostrarAlerta("Error al generar estadística", 
                "No se pudo generar la estadística", 
                "Ocurrió un error al intentar generar la estadística: " + e.getMessage(), 
                Alert.AlertType.ERROR);
        }
    }


    private String obtenerNombreCategoria(String idCategoria) {
        for(CategoriaDto categoria : listaCategorias) {
            if(categoria.idCategoria().equals(idCategoria)) {
                return categoria.nombre();
            }
        }
        return "Desconocida";
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

    private void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        alertaManager.mostrarAlerta(titulo, header, contenido, tipo);
    }

}