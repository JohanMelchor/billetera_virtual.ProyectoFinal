package co.edu.uniquindio.viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.controller.UsuarioController;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistroViewController {
    private UsuarioController usuarioController;

    public RegistroViewController() {
        usuarioController = new UsuarioController();
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtIdUsuario;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtConfirmPassword;

    @FXML
    private TextField txtTelefono;

    @FXML
    void onRegister(ActionEvent event) {
        UsuarioDto usuarioDto = new UsuarioDto(
            txtNombre.getText(),
            txtIdUsuario.getText(),
            txtCorreo.getText(),
            txtTelefono.getText(),
            txtDireccion.getText(),
            "0.0" // Saldo inicial
        );

        if (usuarioController.registrarUsuario(usuarioDto)) {
            mostrarAlerta("Éxito", "Usuario registrado", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "El usuario ya existe", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void onVolver(ActionEvent event) {
        try {
        
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/co/edu/uniquindio/Login.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la vista de login", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

}
