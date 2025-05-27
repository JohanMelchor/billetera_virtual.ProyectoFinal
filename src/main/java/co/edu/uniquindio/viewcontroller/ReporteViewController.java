package co.edu.uniquindio.viewcontroller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;

import co.edu.uniquindio.facade.BilleteraFacade;

public class ReporteViewController {
    private BilleteraFacade facade;
    
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
    void initialize() {
        facade = new BilleteraFacade();
        
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
                "• Estadísticas de todo el sistema\n" +
                "• Lista de todos los usuarios\n" +
                "• Todas las cuentas del sistema\n" +
                "• Todas las transacciones\n" +
                "• Análisis completo del sistema");
        } else {
            lblTitulo.setText("Generar Mi Reporte Personal");
            lblTitulo.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
            txtInfo.setText("REPORTE PERSONAL\n\n" +
                "Este reporte incluirá:\n" +
                "• Tu información personal\n" +
                "• Tus cuentas y saldos\n" +
                "• Tus transacciones\n" +
                "• Tu resumen financiero\n" +
                "• Análisis de tus gastos por categoría");
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
        
        btnGenerar.setText("Generando...");
        btnGenerar.setDisable(true);
        
        boolean resultado;
        if (esAdmin) {
            resultado = facade.generarReporteAdmin(txtRuta.getText());
        } else {
            resultado = facade.generarReporteUsuario(idUsuarioActual, txtRuta.getText());
        }
        
        btnGenerar.setText("Generar Reporte PDF");
        btnGenerar.setDisable(false);
        
        if (resultado) {
            mostrarAlerta("Éxito", "Reporte generado", 
                "El reporte se guardó exitosamente en:\n" + txtRuta.getText(), 
                Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "Error al generar reporte", 
                "No se pudo generar el reporte. Verifique la ruta e intente nuevamente.", 
                Alert.AlertType.ERROR);
        }
    }
    
    private String generarNombreArchivo() {
        String fecha = LocalDate.now().toString();
        if (esAdmin) {
            return "Reporte_Administrativo_" + fecha + ".pdf";
        } else {
            return "Reporte_Personal_" + idUsuarioActual + "_" + fecha + ".pdf";
        }
    }
    
    private void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}