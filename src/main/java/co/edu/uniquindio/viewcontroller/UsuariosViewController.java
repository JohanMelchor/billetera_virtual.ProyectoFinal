package co.edu.uniquindio.viewcontroller;


import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.factory.AlertaManagerFactory;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.service.IAlertaManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UsuariosViewController {

    ObservableList<UsuarioDto> listaUsuarios = FXCollections.observableArrayList();
    UsuarioDto usuarioSeleccionado;
    private BilleteraFacade facade;
    private IAlertaManager alertaManager;

    @FXML
    private Button btnActualizarUsuario;

    @FXML
    private Button btnAgregarUsuario;

    @FXML
    private Button btnEliminarUsuario;

    @FXML
    private Button btnLimpiarCampos;

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
    private TextField txtSaldo;

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
        facade = new BilleteraFacade();
        alertaManager = AlertaManagerFactory.crearManagerCompleto();
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
        listaUsuarios.addAll(facade.obtenerUsuarios());
    }


    private void agregarUsuario(){
        UsuarioDto usuarioDto = crearUsuarioDto();
        if(datosValidos(usuarioDto)){
            if(facade.agregarUsuario(usuarioDto)){
                listaUsuarios.clear();  // Limpiar y volver a cargar
                listaUsuarios.addAll(facade.obtenerUsuarios());
                limpiarCampos();
                mostrarAlerta("Usuario agregado", "Éxito", "El usuario fue agregado correctamente", Alert.AlertType.INFORMATION);
        }
            else{
                mostrarAlerta("Usuario no agregado", "Notificacion", "El cliente no fue agregado",
                        Alert.AlertType.ERROR);
            }
        }else{
            //volver esto constantes
            mostrarAlerta("Campos incompletos", "Notificacion",
                    "Los datos del formulario estan incompletos",Alert.AlertType.WARNING);
        }
    }

    private UsuarioDto crearUsuarioDto() {
        return new UsuarioDto(txtIdUsuario.getText(),
                txtNombreUsuario.getText(),txtCorreoUsuario.getText(),
                txtTelefonoUsuario.getText(),txtDireccionUsuario.getText(),
                txtSaldo.getText(), "****");
    }


    private void limpiarCampos() {
        txtCorreoUsuario.setText("");
        txtDireccionUsuario.setText("");
        txtIdUsuario.setText("");
        txtNombreUsuario.setText("");
        txtTelefonoUsuario.setText("");
        txtSaldo.setText("");
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
            boolean eliminado = facade.eliminarUsuario(usuarioSeleccionado.idUsuario());
            if(eliminado){
                listaUsuarios.remove(usuarioSeleccionado);
                limpiarCampos();
                mostrarAlerta("Usuario eliminado", "Éxito", "El usuario fue eliminado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("No se pudo eliminar", "Error", "El usuario no fue eliminado", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Selección requerida", "Advertencia", "Debe seleccionar un usuario de la tabla", Alert.AlertType.WARNING);
        }
    }

    private void actualizarUsuario() {
        UsuarioDto usuarioDto=crearUsuarioDto();
        if(datosValidos(usuarioDto)){
            if(facade.actualizarUsuario(usuarioDto)){
                listaUsuarios.set(listaUsuarios.indexOf(usuarioSeleccionado),usuarioDto);
                limpiarCampos();
            }else{
                mostrarAlerta("No se pudo actualizar", "Error", "El usuario no fue actualizado", Alert.AlertType.ERROR);
            }
        }else{
            mostrarAlerta("Campos incompletos", "Notificacion",
                    "Los datos del formulario estan incompletos",Alert.AlertType.WARNING);
        }
    }

    private void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        alertaManager.mostrarAlerta(titulo, header, contenido, tipo);
    }

    private void listenerSeleccion() {
        tableUsuario.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) -> {usuarioSeleccionado=newSelection;mostrarInformacionUsuario(usuarioSeleccionado);});
    }

    private void mostrarInformacionUsuario(UsuarioDto usuarioSeleccionado) {
        if(usuarioSeleccionado != null){
            txtIdUsuario.setText(usuarioSeleccionado.idUsuario());
            txtNombreUsuario.setText(usuarioSeleccionado.nombreCompleto());
            txtCorreoUsuario.setText(usuarioSeleccionado.correo());
            txtDireccionUsuario.setText(usuarioSeleccionado.direccion());
            txtTelefonoUsuario.setText(usuarioSeleccionado.telefono());
            txtSaldo.setText(usuarioSeleccionado.saldo());
        }
    }

    private void initDataBanding() {
        tcIdUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().idUsuario()));
        tcNombreUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().nombreCompleto()));
        tcCorreoUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().correo()));
        tcTelefonoUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().telefono()));
        tcDireccionUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().direccion()));
        tcSaldoUsuario.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().saldo())));
    }

    @FXML
    void onLimpiarCampos(ActionEvent event) {
        limpiarCampos();
        usuarioSeleccionado = null;
        tableUsuario.getSelectionModel().clearSelection();
    }
}