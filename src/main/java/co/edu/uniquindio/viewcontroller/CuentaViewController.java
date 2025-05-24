package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.controller.CuentaController;
import co.edu.uniquindio.controller.UsuarioController;
import co.edu.uniquindio.mapping.dto.CuentaDto;
import co.edu.uniquindio.Util.CuentaConstantes;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Optional;
import java.util.UUID;

public class CuentaViewController {
    
    private CuentaController cuentaController;
    private UsuarioController usuarioController;
    private ObservableList<CuentaDto> listaCuentas = FXCollections.observableArrayList();
    private CuentaDto cuentaSeleccionada;
    private String idUsuarioActual;
    
    @FXML
    private ComboBox<String> cbTipoCuenta;
    
    @FXML
    private TextField txtIdCuenta;
    
    @FXML
    private TextField txtNombreBanco;
    
    @FXML
    private TextField txtNumeroCuenta;

    @FXML
    private TextField txtSaldo;
    
    @FXML
    private Button btnAgregarCuenta;
    
    @FXML
    private Button btnActualizarCuenta;
    
    @FXML
    private Button btnEliminarCuenta;

    @FXML
    private Button btnPresupuesto;
    
    @FXML
    private TableView<CuentaDto> tableCuentas;
    
    @FXML
    private TableColumn<CuentaDto, String> tcIdCuenta;
    
    @FXML
    private TableColumn<CuentaDto, String> tcNombreBanco;
    
    @FXML
    private TableColumn<CuentaDto, String> tcNumeroCuenta;
    
    @FXML
    private TableColumn<CuentaDto, String> tcTipoCuenta;

    @FXML
    private TableColumn<CuentaDto, String> tcSaldoTotal;
    
    @FXML
    private Label lblUsuarioActual;
    
    @FXML
    void initialize() {
        cuentaController = new CuentaController();
        usuarioController = new UsuarioController();
        
        // Inicializar tipos de cuenta
        cbTipoCuenta.getItems().addAll(CuentaConstantes.TIPO_CUENTA_AHORRO, CuentaConstantes.TIPO_CUENTA_CORRIENTE);
        
        initView();
    }
    
    public void inicializarConUsuario(String idUsuario) {
        this.idUsuarioActual = idUsuario;
        cargarCuentasUsuario();
    }
    
    private void initView() {
        initDataBinding();
        tableCuentas.getItems().clear();
        tableCuentas.setItems(listaCuentas);
        listenerSeleccion();
        
        // Generar ID único para nueva cuenta
        generarIdUnico();
    }
    
    private void cargarCuentasUsuario() {
        if(idUsuarioActual != null && !idUsuarioActual.isEmpty()) {
            listaCuentas.clear();
            listaCuentas.addAll(cuentaController.obtenerCuentasPorUsuario(idUsuarioActual));
        }
    }
    
    private void initDataBinding() {
        tcIdCuenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().idCuenta()));
        tcNombreBanco.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombreBanco()));
        tcNumeroCuenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().numeroCuenta()));
        tcTipoCuenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().tipoCuenta()));
        tcSaldoTotal.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().saldoTotal())));
    }
    
    private void listenerSeleccion() {
        tableCuentas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            cuentaSeleccionada = newSelection;
            mostrarInformacionCuenta(cuentaSeleccionada);
        });
    }
    
    private void mostrarInformacionCuenta(CuentaDto cuentaDto) {
        if(cuentaDto != null) {
            txtIdCuenta.setText(cuentaDto.idCuenta());
            txtNombreBanco.setText(cuentaDto.nombreBanco());
            txtNumeroCuenta.setText(cuentaDto.numeroCuenta());
            cbTipoCuenta.setValue(cuentaDto.tipoCuenta());
            txtSaldo.setText(String.valueOf(cuentaDto.saldoTotal()));
        }
    }
    
    @FXML
    void onAgregarCuenta(ActionEvent event) {
        CuentaDto nuevaCuenta = crearCuentaDto();
        if(datosValidos(nuevaCuenta)) {
            if(cuentaController.agregarCuenta(nuevaCuenta)) {
                cargarCuentasUsuario();
                limpiarCampos();
                mostrarMensaje(
                    "Cuenta agregada", 
                    "Éxito", 
                    CuentaConstantes.EXITO_AGREGAR_CUENTA, 
                    Alert.AlertType.INFORMATION
                );
            } else {
                mostrarMensaje(
                    "Error", 
                    "No se pudo agregar", 
                    CuentaConstantes.ERROR_AGREGAR_CUENTA, 
                    Alert.AlertType.ERROR
                );
            }
        } else {
            mostrarMensaje(
                "Campos incompletos", 
                "Datos incompletos", 
                CuentaConstantes.ERROR_CAMPOS_VACIOS, 
                Alert.AlertType.WARNING
            );
        }
    }
    
    @FXML
    void onActualizarCuenta(ActionEvent event) {
        if(cuentaSeleccionada != null) {
            CuentaDto cuentaActualizada = crearCuentaDto();
            if(datosValidos(cuentaActualizada)) {
                if(cuentaController.actualizarCuenta(cuentaActualizada)) {
                    cargarCuentasUsuario();
                    limpiarCampos();
                    mostrarMensaje(
                        "Cuenta actualizada", 
                        "Éxito", 
                        CuentaConstantes.EXITO_ACTUALIZAR_CUENTA, 
                        Alert.AlertType.INFORMATION
                    );
                } else {
                    mostrarMensaje(
                        "Error", 
                        "No se pudo actualizar", 
                        CuentaConstantes.ERROR_ACTUALIZAR_CUENTA, 
                        Alert.AlertType.ERROR
                    );
                }
            } else {
                mostrarMensaje(
                    "Campos incompletos", 
                    "Datos incompletos", 
                    CuentaConstantes.ERROR_CAMPOS_VACIOS, 
                    Alert.AlertType.WARNING
                );
            }
        } else {
            mostrarMensaje(
                "Selección requerida", 
                "No hay selección", 
                "Debe seleccionar una cuenta para actualizar", 
                Alert.AlertType.WARNING
            );
        }
    }
    
    @FXML
    void onEliminarCuenta(ActionEvent event) {
        if(cuentaSeleccionada != null) {
            boolean confirmacion = mostrarMensajeConfirmacion(
                "¿Está seguro de eliminar la cuenta seleccionada?"
            );
            
            if(confirmacion) {
                if(cuentaController.eliminarCuenta(cuentaSeleccionada.idCuenta())) {
                    listaCuentas.remove(cuentaSeleccionada);
                    limpiarCampos();
                    mostrarMensaje(
                        "Cuenta eliminada", 
                        "Éxito", 
                        CuentaConstantes.EXITO_ELIMINAR_CUENTA, 
                        Alert.AlertType.INFORMATION
                    );
                } else {
                    mostrarMensaje(
                        "Error", 
                        "No se pudo eliminar", 
                        CuentaConstantes.ERROR_ELIMINAR_CUENTA, 
                        Alert.AlertType.ERROR
                    );
                }
            }
        } else {
            mostrarMensaje(
                "Selección requerida", 
                "No hay selección", 
                "Debe seleccionar una cuenta para eliminar", 
                Alert.AlertType.WARNING
            );
        }
    }
    
    private CuentaDto crearCuentaDto() {
        String tipoCuenta = cbTipoCuenta.getValue() != null ? cbTipoCuenta.getValue() : "";
        
        return new CuentaDto(
            txtIdCuenta.getText(),
            txtNombreBanco.getText(),
            txtNumeroCuenta.getText(),
            tipoCuenta,
            idUsuarioActual,
            0.0
        );
    }
    
    private boolean datosValidos(CuentaDto cuentaDto) {
        return cuentaDto.idCuenta() != null && !cuentaDto.idCuenta().isEmpty() &&
               cuentaDto.nombreBanco() != null && !cuentaDto.nombreBanco().isEmpty() &&
               cuentaDto.numeroCuenta() != null && !cuentaDto.numeroCuenta().isEmpty() &&
               cuentaDto.tipoCuenta() != null && !cuentaDto.tipoCuenta().isEmpty() &&
               cuentaDto.idUsuario() != null && !cuentaDto.idUsuario().isEmpty();
    }
    
    private void limpiarCampos() {
        generarIdUnico();
        txtNombreBanco.setText("");
        txtNumeroCuenta.setText("");
        cbTipoCuenta.setValue(null);
        cuentaSeleccionada = null;
    }
    
    private void generarIdUnico() {
        txtIdCuenta.setText("CUE" + UUID.randomUUID().toString().substring(0, 8));
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
        return action.isPresent() && action.get() == ButtonType.OK;
    }
}