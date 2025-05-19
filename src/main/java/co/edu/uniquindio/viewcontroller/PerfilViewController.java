package co.edu.uniquindio.viewcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.controller.UsuarioController;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class PerfilViewController {
    private UsuarioController usuarioController;
    private String idUsuarioActual;
    private UsuarioDto usuarioOriginal;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCambio;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    @FXML
    private TextField txtPasswordVisible;

    @FXML
    private ToggleButton btnShow;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtIdUsuario;

    @FXML
    private TextField txtNombre;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtTelefono;

    @FXML
    void initialize() {
        usuarioController = new UsuarioController();
        if (txtPasswordVisible != null) {
            txtPasswordVisible.setVisible(false);
        }
    }

    @FXML 
    void onShow(ActionEvent event) {
        if (btnShow.isSelected()) {
            txtPasswordVisible.setText(txtPassword.getText());
            txtPasswordVisible.setVisible(true);
            txtPassword.setVisible(false);
        } else {
            txtPassword.setText(txtPasswordVisible.getText());
            txtPassword.setVisible(true);
            txtPasswordVisible.setVisible(false);
        }
    }

    @FXML
    void onCambioPassword(ActionEvent event) {

    }

    @FXML
    void onCancelar(ActionEvent event) {
        cargarDatosUsuario();
    }

    @FXML
    void onGuardar(ActionEvent event) {

        if(validarCampos()) {
            UsuarioDto usuarioActualizado = new UsuarioDto(
                idUsuarioActual,
                txtNombre.getText(),
                txtCorreo.getText(),
                txtTelefono.getText(),
                txtDireccion.getText(),
                usuarioOriginal.saldo(), 
                usuarioOriginal.password()
            );
            if(usuarioController.actualizarUsuario(usuarioActualizado)) {
                mostrarAlerta("Éxito", "Perfil actualizado", 
                            "Tus datos se han actualizado correctamente", 
                            Alert.AlertType.INFORMATION);
                this.usuarioOriginal = usuarioActualizado;
                cargarDatosUsuario(); // Refresca los datos en pantalla
            } else {
                mostrarAlerta("Error", "Error al actualizar", 
                            "No se pudo actualizar tu perfil", 
                            Alert.AlertType.ERROR);
            }
        }
    }

    public void inicializarConUsuario(String idUsuario) {
        this.idUsuarioActual = idUsuario;
        this.usuarioOriginal = usuarioController.buscarUsuarioPorId(idUsuario);
        configurarCampos();
        cargarDatosUsuario();
    }

    private void configurarCampos() {
        // Puedes hacer campos editables/no editables según necesites
        txtNombre.setEditable(true);
        txtCorreo.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        txtIdUsuario.setEditable(false);
        txtPassword.setEditable(false);
        txtPasswordVisible.setEditable(false);
    }

    private void cargarDatosUsuario() {
        UsuarioDto usuario = usuarioController.buscarUsuarioPorId(idUsuarioActual);
        if(usuario != null) {
            usuarioOriginal = usuario; // Guarda el usuario original
            txtIdUsuario.setText(usuario.idUsuario());
            txtNombre.setText(usuario.nombreCompleto());
            txtCorreo.setText(usuario.correo());
            txtTelefono.setText(usuario.telefono());
            txtDireccion.setText(usuario.direccion());
            txtPassword.setText(usuario.password());
            txtPasswordVisible.setText(usuario.password()); 
        }
    }

    private boolean validarCampos() {
        if(txtNombre.getText().isEmpty() || txtCorreo.getText().isEmpty() || 
           txtTelefono.getText().isEmpty() || txtDireccion.getText().isEmpty()) {
            mostrarAlerta("Error", "Campos obligatorios", 
                        "Todos los campos son necesarios", 
                        Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
    
}
