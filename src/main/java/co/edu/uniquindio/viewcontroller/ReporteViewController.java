package co.edu.uniquindio.viewcontroller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;

import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.factory.AlertaManagerFactory;
import co.edu.uniquindio.service.IAlertaManager;

public class ReporteViewController {
    private BilleteraFacade facade;
    private IAlertaManager alertaManager;
    
    private String idUsuarioActual;
    private boolean esAdmin = false;
    
    @FXML
    private Label lblTitulo;
    
    @FXML
    private TextField txtRuta;
    
    @FXML
    private Button btnSeleccionar;
    
    @FXML
    private Button btnGenerar;
    
    @FXML
    private TextArea txtInfo;

    @FXML
    private ComboBox<String> cbFormato;

    @FXML
    private Label lblFormato;
    
    @FXML
    void initialize() {
        facade = new BilleteraFacade();
        alertaManager = AlertaManagerFactory.crearManagerCompleto();
        inicializarComboFormato();
        
        txtInfo.setEditable(false);
        txtInfo.setWrapText(true);
    }
    
    public void inicializarVista(String idUsuario, boolean esAdmin) {
        this.idUsuarioActual = idUsuario;
        this.esAdmin = esAdmin;
        
        if (esAdmin) {
            lblTitulo.setText("Generar Reporte Administrativo");
            lblTitulo.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            txtInfo.setText("REPORTE ADMINISTRATIVO\n\n" +
                "Este reporte incluirá:\n" +
                "• Estadísticas del sistema (usuarios, cuentas, transacciones)\n" +
                "• Lista de usuarios con sus saldos\n" +
                "• Resumen financiero del sistema\n" +
                "• Análisis básico de actividad\n\n" +
                "Formatos disponibles:\n" +
                " •PDF: Resumen visual para presentar\n" +
                " •Excel: Datos básicos para revisar\n" +
                " •CSV: Información para exportar");
        } else {
            lblTitulo.setText("Generar Mi Reporte Personal");
            lblTitulo.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
            txtInfo.setText("REPORTE PERSONAL\n\n" +
                        "Este reporte incluirá:\n" +
                        "• Tu información personal\n" +
                        "• Tus cuentas y saldos\n" +
                        "• Tus transacciones\n" +
                        "• Tu resumen financiero\n" +
                        "• Análisis de tus gastos por categoría\n\n" +
                        "Formatos disponibles:\n" +
                        "• PDF: Resumen visual para presentar\n" +
                        "• Excel: Datos básicos para revisar\n" +
                        "• CSV: Información para exportar");
        }
    }
    
    @FXML
    void onSeleccionar(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar carpeta para guardar el reporte");
        
        Stage stage = (Stage) btnSeleccionar.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);
        
        if (selectedDirectory != null) {
            String nombreArchivo = generarNombreArchivo();
            String rutaCompleta = selectedDirectory.getAbsolutePath() + File.separator + nombreArchivo;
            txtRuta.setText(rutaCompleta);
        }
    }
    
    @FXML
    void onGenerar(ActionEvent event) {
        if (txtRuta.getText().isEmpty()) {
            mostrarAlerta("Error", "Seleccione una carpeta", "Debe seleccionar dónde guardar el reporte", Alert.AlertType.WARNING);
            return;
        }
        
        // OBTENER FORMATO SELECCIONADO
        String formato = ""; // Por defecto
        if (cbFormato != null && cbFormato.getValue() != null) {
            formato = cbFormato.getValue().toLowerCase();
            String valorCompleto = cbFormato.getValue();
            formato = valorCompleto.split(" - ")[0].toLowerCase();
        }

        boolean resultado;
        
        if (esAdmin) {
            resultado = facade.generarReporteAdminFormato(txtRuta.getText(), formato);
        } else {
            resultado = facade.generarReporteUsuarioFormato(idUsuarioActual, txtRuta.getText(), formato);
        }
        
        if (resultado) {
            mostrarAlerta("Éxito", "Reporte generado", 
                String.format("El reporte %s se guardó exitosamente en:\n%s", 
                    formato.toUpperCase(), txtRuta.getText()), 
                Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "Error al generar reporte", 
                "No se pudo generar el reporte. Verifique la ruta e intente nuevamente.", 
                Alert.AlertType.ERROR);
        }

        txtRuta.clear();
        cbFormato.setValue(null);
    }
    
    private String generarNombreArchivo() {
        String fecha = LocalDate.now().toString();
    
        // Obtener formato seleccionado
        String formato = ""; // Por defecto
        if (cbFormato != null && cbFormato.getValue() != null) {
            String valorCompleto = cbFormato.getValue();
            formato = valorCompleto.split(" - ")[0].toLowerCase();
        }
        
        // Determinar extensión
        String extension;
        switch (formato) {
            case "excel":
                extension = ".xlsx";
                break;
            case "csv":
                extension = ".csv";
                break;
            default:
                extension = ".pdf";
                break;
        }
        
        // Generar nombre según tipo de usuario
        if (esAdmin) {
            return "Reporte_Administrativo_" + fecha + extension;
        } else {
            return "Reporte_Personal_" + idUsuarioActual + "_" + fecha + extension;
        }
    }
    
    private void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        alertaManager.mostrarAlerta(titulo, header, contenido, tipo);
    }

    private void inicializarComboFormato() {
        if (cbFormato != null) {
            // Cargar formatos disponibles
            String[] formatos = facade.getFormatosDisponibles();
            
            
            cbFormato.getItems().addAll(formatos);
            cbFormato.setValue(null);
            
            // Listener para actualizar la ruta cuando cambie el formato
            cbFormato.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && !txtRuta.getText().isEmpty()) {
                    actualizarExtensionArchivo(newVal);
                }
            });
        }
    }

    private void actualizarExtensionArchivo(String formatoCompleto) {
        String rutaActual = txtRuta.getText();
        if (!rutaActual.isEmpty()) {
            // Quitar extensión actual
            int ultimoPunto = rutaActual.lastIndexOf('.');
            if (ultimoPunto > 0) {
                rutaActual = rutaActual.substring(0, ultimoPunto);
            }
            // Obtener el formato completo (con descripción)
            String formato = formatoCompleto.split(" - ")[0].toLowerCase();
            // Agregar nueva extensión según formato
            switch (formato.toLowerCase()) {
                case "excel":
                    rutaActual += ".xlsx";
                    break;
                case "csv":
                    rutaActual += ".csv";
                    break;
                default:
                    rutaActual += ".pdf";
                    break;
            }
            
            txtRuta.setText(rutaActual);
        }
    }
}