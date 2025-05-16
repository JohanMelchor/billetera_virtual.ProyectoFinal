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
        String password = txtPassword.getText();
        String confirmacion = txtConfirmPassword.getText();

        // Validar campos obligatorios
        if(txtNombre.getText().isEmpty() || txtIdUsuario.getText().isEmpty() || 
           txtCorreo.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            mostrarAlerta("Error", "Campos obligatorios", 
                         "Nombre, ID, correo y contraseña son campos obligatorios", Alert.AlertType.ERROR);
            return;
        }

        if (!password.equals(confirmacion)) {
            mostrarAlerta("Error", "Contraseñas no coinciden", 
                         "Las contraseñas ingresadas no coinciden", Alert.AlertType.ERROR);
            return;
        }

        UsuarioDto usuarioDto = new UsuarioDto(
            txtIdUsuario.getText(),
            txtNombre.getText(),
            txtCorreo.getText(),
            txtTelefono.getText(),
            txtDireccion.getText(),
            "0.0",
            password
        );

        if (usuarioController.registrarUsuario(usuarioDto)) {
            mostrarAlerta("Éxito", "Usuario registrado", 
                         "El usuario se ha registrado correctamente", Alert.AlertType.INFORMATION);
            onVolver(event);
        } else {
            mostrarAlerta("Error", "Usuario ya existe", 
                         "El ID de usuario ya está registrado", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
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
            mostrarAlerta("Error", "Error al cargar la vista", 
                         "No se pudo cargar la vista de login", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

}
