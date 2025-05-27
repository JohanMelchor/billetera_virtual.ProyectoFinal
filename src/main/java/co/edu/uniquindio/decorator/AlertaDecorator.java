package co.edu.uniquindio.decorator;

import co.edu.uniquindio.service.IAlertaManager;
import javafx.scene.control.Alert;

public abstract class AlertaDecorator implements IAlertaManager {
    
    protected IAlertaManager alertaManager;
    
    public AlertaDecorator(IAlertaManager alertaManager) {
        this.alertaManager = alertaManager;
    }
    
    @Override
    public void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        alertaManager.mostrarAlerta(titulo, header, contenido, tipo);
    }
    
    @Override
    public boolean mostrarConfirmacion(String titulo, String mensaje) {
        return alertaManager.mostrarConfirmacion(titulo, mensaje);
    }

}
