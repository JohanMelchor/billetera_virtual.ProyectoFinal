package co.edu.uniquindio.service;

import javafx.scene.control.Alert;

public interface IAlertaManager {
    void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo);
    boolean mostrarConfirmacion(String titulo, String mensaje);
}
