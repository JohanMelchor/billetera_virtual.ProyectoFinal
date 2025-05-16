package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.controller.UsuarioController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

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
        String password = txtPassword.getText();
        
        if(idUsuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos", "Por favor ingrese su ID y contraseña", Alert.AlertType.ERROR);
            return;
        }
        
        if(usuarioController.validarCredenciales(idUsuario, password)) {
            cargarVistaPrincipal(idUsuario);
        } else {
            mostrarAlerta("Error de acceso", "Credenciales inválidas", 
                         "El usuario o contraseña ingresados son incorrectos.", Alert.AlertType.ERROR);
        }
    }
    
    private void cargarVistaPrincipal(String idUsuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/BilleteraVirtualApp.fxml"));
            Parent root = loader.load();

            BilleteraVirtualAppViewController controller = loader.getController();
            controller.iniciarSesion(idUsuario);
            
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Billetera Virtual - Panel de Usuario");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al cargar la vista", 
                         "No se pudo cargar la vista de usuario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    void onRegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/Registro.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnRegistro.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Billetera Virtual - Registro de Usuario");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al cargar la vista", 
                         "No se pudo cargar la vista de registro: " + e.getMessage(), Alert.AlertType.ERROR);
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