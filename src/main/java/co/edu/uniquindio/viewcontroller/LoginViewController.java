package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.controller.UsuarioController;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LoginViewController {
    
    private UsuarioController usuarioController;
    
    @FXML
    private TextField txtUsuario;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Button btnLogin;
    
    @FXML
    private Button btnRegistro;
    
    @FXML
    void initialize() {
        usuarioController = new UsuarioController();
    }
    
    @FXML
    void onLogin(ActionEvent event) {
        String idUsuario = txtUsuario.getText();
        // En un sistema real validaríamos la contraseña, pero para este ejemplo
        // solo validamos que exista el usuario
        if(validarUsuario(idUsuario)) {
            try {
                // Cargar la vista de usuario
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/BilleteraVirtualApp.fxml"));
                Parent root = loader.load();
                
                // Obtener controlador y pasar el ID del usuario
                BilleteraVirtualAppViewController controller = loader.getController();
                controller.iniciarSesionUsuario(idUsuario);
                
                // Mostrar la vista
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.setTitle("Billetera Virtual - Panel de Usuario");
                stage.show();
            } catch (IOException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar la vista", 
                             "No se pudo cargar la vista de usuario: " + e.getMessage());
            }
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de acceso", "Usuario no encontrado", 
                         "El usuario ingresado no existe en el sistema.");
        }
    }
    
    @FXML
    void onRegistro(ActionEvent event) {
        try {
            // Cargar la vista de registro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/Registro.fxml"));
            Parent root = loader.load();
            
            // Mostrar la vista
            Stage stage = (Stage) btnRegistro.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Billetera Virtual - Registro de Usuario");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar la vista", 
                         "No se pudo cargar la vista de registro: " + e.getMessage());
        }
    }
    
    private boolean validarUsuario(String idUsuario) {
        if(idUsuario == null || idUsuario.isEmpty()) {
            return false;
        }
        
        List<UsuarioDto> usuarios = usuarioController.obtenerUsuarios();
        for(UsuarioDto usuario : usuarios) {
            if(usuario.idUsuario().equals(idUsuario)) {
                return true;
            }
        }
        
        return false;
    }
    
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String header, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}