package co.edu.uniquindio.decorator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import co.edu.uniquindio.service.IAlertaManager;
import javafx.scene.control.Alert;

public class TimestampAlertaDecorator extends AlertaDecorator {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public TimestampAlertaDecorator(IAlertaManager alertaManager) {
        super(alertaManager);
    }
    
    @Override
    public void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        // Agregar timestamp al contenido
        String timestamp = LocalDateTime.now().format(formatter);
        String contenidoConTimestamp = contenido + "\n\n⏰ " + timestamp;
        
        // Mostrar la alerta con timestamp
        super.mostrarAlerta(titulo, header, contenidoConTimestamp, tipo);
    }

}
