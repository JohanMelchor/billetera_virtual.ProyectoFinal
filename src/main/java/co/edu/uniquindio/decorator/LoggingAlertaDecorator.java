package co.edu.uniquindio.decorator;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import co.edu.uniquindio.service.IAlertaManager;
import javafx.scene.control.Alert;

public class LoggingAlertaDecorator extends AlertaDecorator {

    private static final String LOG_FILE = "alertas.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public LoggingAlertaDecorator(IAlertaManager alertaManager) {
        super(alertaManager);
    }
    
    @Override
    public void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        // Guardar en log antes de mostrar
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[%s] %s - %s: %s - %s%n", 
            timestamp, tipo.toString(), titulo, header, contenido);
        
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Error al escribir log de alerta: " + e.getMessage());
        }
        
        // Mostrar la alerta
        super.mostrarAlerta(titulo, header, contenido, tipo);
    }
    
    @Override
    public boolean mostrarConfirmacion(String titulo, String mensaje) {
        // Log de confirmación
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[%s] CONFIRMACION - %s: %s%n", 
            timestamp, titulo, mensaje);
        
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Error al escribir log de confirmación: " + e.getMessage());
        }
        
        return super.mostrarConfirmacion(titulo, mensaje);
    }

}
