package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.controller.UsuarioController;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;

public class RegistroUsuarioViewController {
    UsuarioController usuarioController;

    @FXML
    private Button btnCrearUsuario;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtContrasenia;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    @FXML
    private ComboBox<String> cbTipoUsuario;


    @FXML
    void OnCrearUsuario(ActionEvent event) {
        agregarUsuario();

    }

    @FXML
    void initialize() {
        agregarDatosSplitMenu();
    }

    private void agregarDatosSplitMenu() {
        cbTipoUsuario.getItems().clear();
        cbTipoUsuario.getItems().addAll("Usuario", "Administrador");
    }


    private UsuarioDto crearUsuarioDto() {
        String tipoUsuario = cbTipoUsuario.getValue();
        return new UsuarioDto(txtNombre.getText(),
                txtId.getText(),txtCorreo.getText(),
                txtTelefono.getText(),txtDireccion.getText(),
               "0", tipoUsuario,txtContrasenia.getText());
    }

    private void agregarUsuario(){
        UsuarioDto usuarioDto = crearUsuarioDto();
        if(datosValidos(usuarioDto)){
            if(usuarioController.agregarUsuario(usuarioDto)){
                mostrarMensaje("Usuario agregado", "Éxito", "El usuario fue agregado correctamente", Alert.AlertType.INFORMATION);
            }
            else{
                mostrarMensaje("Usuario no agregado", "Notificacion", "El cliente no fue agregado",
                        Alert.AlertType.ERROR);
            }
        }else{
            //volver esto constantes
            mostrarMensaje("Campos incompletos", "Notificacion",
                    "Los datos del formulario estan incompletos",Alert.AlertType.WARNING);
        }
    }

    private boolean datosValidos(UsuarioDto usuarioDto) {
        if(usuarioDto.nombreCompleto().isEmpty()
                ||usuarioDto.idUsuario().isEmpty()
                ||usuarioDto.correo().isEmpty()
                ||usuarioDto.telefono().isEmpty()
                ||usuarioDto.direccion().isEmpty()
                ||usuarioDto.saldo().isEmpty()
                ||usuarioDto.nombreCompleto() == null
                ||usuarioDto.idUsuario() == null
                ||usuarioDto.correo() == null
                ||usuarioDto.telefono() == null
                ||usuarioDto.direccion() == null
                ||usuarioDto.saldo() == null
                || usuarioDto.contrasenia() == null){
            return false;

        }
        return true;
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

}

