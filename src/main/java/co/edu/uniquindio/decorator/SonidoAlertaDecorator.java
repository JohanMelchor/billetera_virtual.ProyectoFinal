package co.edu.uniquindio.decorator;

import co.edu.uniquindio.service.IAlertaManager;
import javafx.scene.control.Alert;
import java.awt.Toolkit;

public class SonidoAlertaDecorator extends AlertaDecorator {

    public SonidoAlertaDecorator(IAlertaManager alertaManager) {
        super(alertaManager);
    }
    
    @Override
    public void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        // Reproducir sonido según el tipo
        reproducirSonido(tipo);
        
        // Mostrar la alerta
        super.mostrarAlerta(titulo, header, contenido, tipo);
    }
    
    private void reproducirSonido(Alert.AlertType tipo) {
        try {
            switch (tipo) {
                case ERROR:
                    // Sonido de error del sistema
                    Toolkit.getDefaultToolkit().beep();
                    Thread.sleep(200);
                    Toolkit.getDefaultToolkit().beep();
                    break;
                case WARNING:
                    // Sonido de advertencia
                    Toolkit.getDefaultToolkit().beep();
                    break;
                case INFORMATION:
                    // Sonido suave para información
                    // (Se podría implementar un sonido personalizado aquí)
                    break;
                case CONFIRMATION:
                    // Sonido de confirmación
                    Toolkit.getDefaultToolkit().beep();
                    break;
                case NONE:
                    // No hacer nada para NONE
                    break;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
