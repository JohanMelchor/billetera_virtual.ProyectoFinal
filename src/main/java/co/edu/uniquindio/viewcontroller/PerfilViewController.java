package co.edu.uniquindio.viewcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PerfilViewController {
    
    private String idUsuarioActual;
    private UsuarioDto usuarioOriginal;
    private UsuarioDto usuarioCopia;
    private BilleteraFacade facade;

    @FXML
    private ResourceBundle resources;

    @FXML
    private VBox VbCambioPassword;

    @FXML
    private Pane pnPerfil;

    @FXML
    private URL location;

    @FXML
    private Button btnCambio;

    @FXML
    private Button btnCerrar;

    @FXML
    private Button btnGuardarPassword;

    @FXML
    private Button btnDefecto;

    @FXML
    private Button btnGuardar;

    @FXML
    private TextField txtPasswordVisible;

    @FXML
    private TextField txtPasswordActual;

    @FXML
    private TextField txtPasswordConfirm;

    @FXML
    private TextField txtPasswordNew;

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
        facade = new BilleteraFacade();
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
        VbCambioPassword.setVisible(true);
        VbCambioPassword.setManaged(true);
        pnPerfil.setVisible(false);
    }

    @FXML
    void onCerrar(ActionEvent event) {
        VbCambioPassword.setVisible(false);
        VbCambioPassword.setManaged(false);
        pnPerfil.setVisible(true);
        limpiarCamposContrasena();
    }

    @FXML
    void OnGuardarPassword(ActionEvent event) {
        if (validarContrasenas()) {
        UsuarioDto usuarioActualizado = new UsuarioDto(
            idUsuarioActual,
            usuarioOriginal.nombreCompleto(),
            usuarioOriginal.correo(),
            usuarioOriginal.telefono(),
            usuarioOriginal.direccion(),
            usuarioOriginal.saldo(),
            txtPasswordNew.getText()  // Nueva contraseña
        );
        if (facade.actualizarUsuario(usuarioActualizado)) {
            mostrarAlerta("Éxito", "Contraseña actualizada", "La contraseña se cambió correctamente.", Alert.AlertType.INFORMATION);
            // ACTUALIZA LOS OBJETOS EN MEMORIA
            this.usuarioOriginal = usuarioActualizado;
            this.usuarioCopia = usuarioActualizado.clonar();
            limpiarCamposContrasena();
            onCerrar(null);  // Oculta los campos después del cambio
            // Actualiza los campos de la vista
            txtPassword.setText(usuarioActualizado.password());
            txtPasswordVisible.setText(usuarioActualizado.password());
        }
    }
    }

    @FXML
    void onDefecto(ActionEvent event) {
        if(usuarioCopia != null) {
        // Restaurar los datos originales usando el prototype
        txtIdUsuario.setText(usuarioCopia.idUsuario());
        txtNombre.setText(usuarioCopia.nombreCompleto());
        txtCorreo.setText(usuarioCopia.correo());
        txtTelefono.setText(usuarioCopia.telefono());
        txtDireccion.setText(usuarioCopia.direccion());
        txtPassword.setText(usuarioCopia.password());
        txtPasswordVisible.setText(usuarioCopia.password());
    }
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
            if(facade.actualizarUsuario(usuarioActualizado)) {
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
        this.usuarioOriginal = facade.buscarUsuarioPorId(idUsuario);
        this.usuarioCopia = usuarioOriginal != null ? usuarioOriginal.clonar() : null;
        configurarCampos();
        cargarDatosUsuario();
    }

    private void configurarCampos() {
        txtNombre.setEditable(true);
        txtCorreo.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        txtIdUsuario.setEditable(false);
        txtPassword.setEditable(false);
        txtPasswordVisible.setEditable(false);
    }

    private void cargarDatosUsuario() {
        UsuarioDto usuario = facade.buscarUsuarioPorId(idUsuarioActual);
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

    private boolean validarContrasenas() {
        if (!facade.validarCredenciales(idUsuarioActual, txtPasswordActual.getText())) {
            mostrarAlerta("Error", "Contraseña incorrecta", "La contraseña actual no es válida.", Alert.AlertType.ERROR);
            return false;
        }
        if (txtPasswordNew.getText().length() < 3) {
            mostrarAlerta("Error", "Contraseña insegura", "La nueva contraseña debe tener al menos 6 caracteres.", Alert.AlertType.ERROR);
            return false;
        }
        if (!txtPasswordNew.getText().equals(txtPasswordConfirm.getText())) {
            mostrarAlerta("Error", "Contraseñas no coinciden", "Las contraseñas nuevas no coinciden.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void limpiarCamposContrasena() {
        txtPasswordActual.clear();
        txtPasswordNew.clear();
        txtPasswordConfirm.clear();
    }
    
}
