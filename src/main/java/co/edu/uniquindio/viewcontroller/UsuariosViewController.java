package co.edu.uniquindio.viewcontroller;


import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.factory.AlertaManagerFactory;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.service.IAlertaManager;
import co.edu.uniquindio.template.UsuarioCrudTemplate;
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
    private UsuarioCrudTemplate crudTemplate;

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

    @FXML void onAgregarUsuario(ActionEvent event) { crudTemplate.agregar(); }
    @FXML void onActualizarUsuario(ActionEvent event) { crudTemplate.actualizar(); }
    @FXML void onEliminarUsuario(ActionEvent event) { crudTemplate.eliminar(); }

    @FXML
    void initialize() {
        facade = new BilleteraFacade();
        alertaManager = AlertaManagerFactory.crearManagerCompleto();

        crudTemplate = new UsuarioCrudTemplate(
            facade, alertaManager,
            txtIdUsuario, txtNombreUsuario, txtCorreoUsuario,
            txtTelefonoUsuario, txtDireccionUsuario, txtSaldo,
            tableUsuario, this::obtenerUsuarios
        );

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

    private void limpiarCampos() {
        txtCorreoUsuario.setText("");
        txtDireccionUsuario.setText("");
        txtIdUsuario.setText("");
        txtNombreUsuario.setText("");
        txtTelefonoUsuario.setText("");
        txtSaldo.setText("");
    }

    private void listenerSeleccion() {
        tableUsuario.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) -> {
            usuarioSeleccionado = newSelection;
            crudTemplate.setUsuarioSeleccionado(newSelection);
            mostrarInformacionUsuario(usuarioSeleccionado);});
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