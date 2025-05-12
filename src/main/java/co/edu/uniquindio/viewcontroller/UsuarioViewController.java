package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.controller.UsuarioController;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class UsuarioViewController {

    UsuarioController usuarioController;
    ObservableList<UsuarioDto> listaUsuarios = FXCollections.observableArrayList();
    UsuarioDto usuarioSeleccionado;

    @FXML
    private Button btnActualizarUsuario;

    @FXML
    private Button btnAgregarUsuario;

    @FXML
    private Button btnEliminarUsuario;

    @FXML
    private TableView<UsuarioDto> tableUsuario;

    @FXML
    private TableColumn<UsuarioDto, String> tcCorreoUsuario;

    @FXML
    private TableColumn<UsuarioDto, String> tcDireccionUsuario;

    @FXML
    private TableColumn<UsuarioDto, String> tcIdUsuario;

    @FXML
    private TableColumn<UsuarioDto, String> tcNombreUsuario;

    @FXML
    private TableColumn<UsuarioDto, String > tcSaldoUsuario;

    @FXML
    private TableColumn<UsuarioDto, String> tcTelefonoUsuario;

    @FXML
    private TextField txtCorreoUsuario;

    @FXML
    private TextField txtDireccionUsuario;

    @FXML
    private TextField txtIdUsuario;

    @FXML
    private TextField txtNombreUsuario;

    @FXML
    private TextField txtTelefonoUsuario;

    @FXML
    private Label lblSaldo;

    @FXML
    void onActualizarUsuario(ActionEvent event) {
        actualizarUsuario();
    }

    @FXML
    void onAgregarUsuario(ActionEvent event) {
        agregarUsuario();
    }

    @FXML
    void onEliminarUsuario(ActionEvent event) {
        eliminarUsuario();
    }

    @FXML
    void initialize() {
        usuarioController = new UsuarioController();
        initView();
    }

    private void initView() {
        initDataBanding();
        obtenerUsuarios();
        tableUsuario.getItems().clear();
        tableUsuario.setItems(listaUsuarios);
        listenerSeleccion();
    }


    private void obtenerUsuarios() {
        listaUsuarios.addAll(usuarioController.obtenerUsuarios());
    }


    private void agregarUsuario(){
        UsuarioDto usuarioDto = crearUsuarioDto();
        if(datosValidos(usuarioDto)){
            if(usuarioController.agregarUsuario(usuarioDto)){
                listaUsuarios.clear();  // Limpiar y volver a cargar
                listaUsuarios.addAll(usuarioController.obtenerUsuarios());
                limpiarCampos();
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

    private UsuarioDto crearUsuarioDto() {
        return new UsuarioDto(txtNombreUsuario.getText(),
                txtIdUsuario.getText(),txtCorreoUsuario.getText(),
                txtTelefonoUsuario.getText(),txtDireccionUsuario.getText(),
                lblSaldo.getText(),"tipoUsuario","contraseña");
    }


    private void limpiarCampos() {
        txtCorreoUsuario.setText("");
        txtDireccionUsuario.setText("");
        txtIdUsuario.setText("");
        txtNombreUsuario.setText("");
        txtTelefonoUsuario.setText("");
        lblSaldo.setText("0.0");
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
            ||usuarioDto.saldo() == null){
            return false;
            
        }
        return true;
    }

    private void eliminarUsuario() {
        if(usuarioSeleccionado != null){
            boolean eliminado = usuarioController.eliminarUsuario(usuarioSeleccionado.idUsuario());
            if(eliminado){
                listaUsuarios.remove(usuarioSeleccionado);
                limpiarCampos();
                mostrarMensaje("Usuario eliminado", "Éxito", "El usuario fue eliminado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarMensaje("No se pudo eliminar", "Error", "El usuario no fue eliminado", Alert.AlertType.ERROR);
            }
        } else {
            mostrarMensaje("Selección requerida", "Advertencia", "Debe seleccionar un usuario de la tabla", Alert.AlertType.WARNING);
        }
    }

    private void actualizarUsuario() {
        UsuarioDto usuarioDto=crearUsuarioDto();
        if(datosValidos(usuarioDto)){
            if(usuarioController.actualizarUsuario(usuarioDto)){
                listaUsuarios.set(listaUsuarios.indexOf(usuarioSeleccionado),usuarioDto);
                limpiarCampos();
            }else{
                mostrarMensaje("No se pudo actualizar", "Error", "El usuario no fue actualizado", Alert.AlertType.ERROR);
            }
        }else{
            mostrarMensaje("Campos incompletos", "Notificacion",
                    "Los datos del formulario estan incompletos",Alert.AlertType.WARNING);
        }
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private boolean mostrarMensajeConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText(mensaje);

        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    private void listenerSeleccion() {
        tableUsuario.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) -> {usuarioSeleccionado=newSelection;mostrarInformacionUsuario(usuarioSeleccionado);});
    }

    private void mostrarInformacionUsuario(UsuarioDto usuarioSeleccionado) {
        if(usuarioSeleccionado != null){
            txtNombreUsuario.setText(usuarioSeleccionado.nombreCompleto());
            txtIdUsuario.setText(usuarioSeleccionado.idUsuario());
            txtCorreoUsuario.setText(usuarioSeleccionado.correo());
            txtDireccionUsuario.setText(usuarioSeleccionado.direccion());
            txtTelefonoUsuario.setText(usuarioSeleccionado.telefono());
            lblSaldo.setText(usuarioSeleccionado.saldo());
        }
    }

    private void initDataBanding() {
        tcNombreUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().nombreCompleto()));
        tcIdUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().idUsuario()));
        tcCorreoUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().correo()));
        tcTelefonoUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().telefono()));
        tcDireccionUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().direccion()));
        tcSaldoUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().saldo())));
    }
}