package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.mapping.dto.CuentaDto;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.service.IAlertaManager;
import co.edu.uniquindio.state.TipoEstadoCuenta;
import co.edu.uniquindio.Util.CuentaConstantes;
import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.factory.AlertaManagerFactory;
import co.edu.uniquindio.factory.EstadoCuentaFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.util.UUID;

public class CuentaViewController {
    
    private ObservableList<CuentaDto> listaCuentas = FXCollections.observableArrayList();
    private CuentaDto cuentaSeleccionada;
    private String idUsuarioActual;
    private boolean esAdmin;
    private BilleteraFacade facade;
    private IAlertaManager alertaManager;
    
    @FXML
    private ComboBox<String> cbTipoCuenta;
    
    @FXML
    private TextField txtIdCuenta;

    @FXML
    private ComboBox<TipoEstadoCuenta> cbEstadoCuenta;
    
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
    private Button btnLimpiarCampos;
    
    @FXML
    private TableView<CuentaDto> tableCuentas;
    
    @FXML
    private TableColumn<CuentaDto, String> tcIdCuenta;

    @FXML
    private ComboBox<UsuarioDto> cbUsuarios;

    @FXML
    private HBox hbUsuario;

    @FXML
    private HBox hbEstado;

    @FXML
    private TableColumn<CuentaDto, String> tcUsuarioAsignado;
    
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
        facade = new BilleteraFacade();
        alertaManager = AlertaManagerFactory.crearManagerCompleto();
        
        // Inicializar tipos de cuenta
        cbTipoCuenta.getItems().addAll(CuentaConstantes.TIPO_CUENTA_AHORRO, CuentaConstantes.TIPO_CUENTA_CORRIENTE);
        cbEstadoCuenta.getItems().addAll(EstadoCuentaFactory.getEstadosDisponibles());
        initView();
    }
    
    public void inicializarVista(String idUsuario, boolean esAdmin) {
        this.esAdmin = esAdmin;
        this.idUsuarioActual = idUsuario;

        if (esAdmin) {
            cargarTodasCuentas();
            habilitarVistasAdmin();
        } else {
            this.idUsuarioActual = idUsuario;
            cargarCuentasUsuario();
            habilitarVistasUsuario();
        }
        
        initDataBinding();
    }

    private void habilitarVistasAdmin() {
        hbUsuario.setVisible(true);
        hbEstado.setVisible(true);
        cbUsuarios.setItems(FXCollections.observableArrayList(facade.obtenerUsuarios()));
        cbUsuarios.setConverter(new StringConverter<UsuarioDto>() {
            @Override
            public String toString(UsuarioDto usuario) {
                return usuario != null ? usuario.nombreCompleto() : "";
            }

            @Override
            public UsuarioDto fromString(String string) {
                return null;
            }
        });
    
    }
    
    private void habilitarVistasUsuario() {
        hbUsuario.setVisible(false);
        hbEstado.setVisible(false);
        tcUsuarioAsignado.setVisible(false);
    }

    private void cargarTodasCuentas() {
        listaCuentas.clear();
        listaCuentas.addAll(facade.obtenerTodasCuentas());
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
            listaCuentas.addAll(facade.obtenerCuentasPorUsuario(idUsuarioActual));
        }
    }
    
    private void initDataBinding() {
        tcIdCuenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().idCuenta()));
        tcNombreBanco.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombreBanco()));
        tcNumeroCuenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().numeroCuenta()));
        tcTipoCuenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().tipoCuenta()));
        tcSaldoTotal.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().saldoTotal())));
        tcUsuarioAsignado.setCellValueFactory(cellData -> {String idUsuario = cellData.getValue().idUsuario();
            UsuarioDto usuario = facade.buscarUsuarioPorId(idUsuario);
            return new SimpleStringProperty(
                usuario != null ? usuario.nombreCompleto() : ""
            );
        });
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
            cbUsuarios.setValue(facade.buscarUsuarioPorId(cuentaDto.idUsuario()));
        }
    }
    
    @FXML
    void onAgregarCuenta(ActionEvent event) {
        CuentaDto nuevaCuenta = crearCuentaDto();
        if(datosValidos(nuevaCuenta)) {
            if(facade.agregarCuenta(nuevaCuenta)) {
                if (esAdmin) {
                    cargarTodasCuentas();
                } else {
                    cargarCuentasUsuario();
                }
                limpiarCampos();
                mostrarAlerta(
                    "Cuenta agregada", 
                    "Éxito", 
                    CuentaConstantes.EXITO_AGREGAR_CUENTA, 
                    Alert.AlertType.INFORMATION
                );
            } else {
                mostrarAlerta(
                    "Error", 
                    "No se pudo agregar", 
                    CuentaConstantes.ERROR_AGREGAR_CUENTA, 
                    Alert.AlertType.ERROR
                );
            }
        } else {
            mostrarAlerta(
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
                if(facade.actualizarCuenta(cuentaActualizada)) {
                    if (esAdmin) {
                        cargarTodasCuentas();    // ← Para admin: todas las cuentas
                    } else {
                        cargarCuentasUsuario();  // ← Para usuario: solo sus cuentas
                    }
                    limpiarCampos();
                    mostrarAlerta(
                        "Cuenta actualizada", 
                        "Éxito", 
                        CuentaConstantes.EXITO_ACTUALIZAR_CUENTA, 
                        Alert.AlertType.INFORMATION
                    );
                } else {
                    mostrarAlerta(
                        "Error", 
                        "No se pudo actualizar", 
                        CuentaConstantes.ERROR_ACTUALIZAR_CUENTA, 
                        Alert.AlertType.ERROR
                    );
                }
            } else {
                mostrarAlerta(
                    "Campos incompletos", 
                    "Datos incompletos", 
                    CuentaConstantes.ERROR_CAMPOS_VACIOS, 
                    Alert.AlertType.WARNING
                );
            }
        } else {
            mostrarAlerta(
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
            boolean confirmacion = mostrarConfirmacion(
                "Confirmación de eliminación", 
                "¿Está seguro de que desea eliminar la cuenta seleccionada?"
            );
            
            if(confirmacion) {
                if(facade.eliminarCuenta(cuentaSeleccionada.idCuenta())) {
                    if (esAdmin) {
                        cargarTodasCuentas();    // ← Para admin: todas las cuentas
                    } else {
                        cargarCuentasUsuario();  // ← Para usuario: solo sus cuentas
                    }
                    limpiarCampos();
                    mostrarAlerta(
                        "Cuenta eliminada", 
                        "Éxito", 
                        CuentaConstantes.EXITO_ELIMINAR_CUENTA, 
                        Alert.AlertType.INFORMATION
                    );
                } else {
                    mostrarAlerta(
                        "Error", 
                        "No se pudo eliminar", 
                        CuentaConstantes.ERROR_ELIMINAR_CUENTA, 
                        Alert.AlertType.ERROR
                    );
                }
            }
        } else {
            mostrarAlerta(
                "Selección requerida", 
                "No hay selección", 
                "Debe seleccionar una cuenta para eliminar", 
                Alert.AlertType.WARNING
            );
        }
    }
    
    private CuentaDto crearCuentaDto() {
        String idUsuarioAsignado;
        if (esAdmin) {
            UsuarioDto usuarioSeleccionado = cbUsuarios.getValue();
                idUsuarioAsignado = usuarioSeleccionado != null ? 
                usuarioSeleccionado.idUsuario() : null;
        } else {
            idUsuarioAsignado = idUsuarioActual;
        }
        return new CuentaDto(
            txtIdCuenta.getText(),
            txtNombreBanco.getText(),
            txtNumeroCuenta.getText(),
            cbTipoCuenta.getValue(),
            idUsuarioAsignado,
            0.0,
            cbEstadoCuenta.getValue()
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
        txtSaldo.setText("");
        cbUsuarios.setValue(null);
    }
    
    private void generarIdUnico() {
        txtIdCuenta.setText("CUE" + UUID.randomUUID().toString().substring(0, 8));
    }
    
    private void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        alertaManager.mostrarAlerta(titulo, header, contenido, tipo);
    }
    
    private boolean mostrarConfirmacion(String titulo, String mensaje) {
        return alertaManager.mostrarConfirmacion(titulo, mensaje);
    }

    @FXML
    void onLimpiarCampos(ActionEvent event) {
        limpiarCampos();
        tableCuentas.getSelectionModel().clearSelection();
    }

    @FXML
    void onCambiarEstado(ActionEvent event) {
        if (cuentaSeleccionada != null) {
            TipoEstadoCuenta nuevoEstado = cbEstadoCuenta.getValue();
            if (nuevoEstado != null) {
                try {
                    boolean cambiado = facade.cambiarEstadoCuenta(cuentaSeleccionada.idCuenta(), nuevoEstado);
                    if (cambiado) {
                        mostrarAlerta("Estado cambiado", "Éxito", 
                            "Estado cambiado a " + nuevoEstado.getDescripcion(), 
                            Alert.AlertType.INFORMATION);
                    }
                } catch (IllegalStateException e) {
                    mostrarAlerta("Error", "Transición no permitida", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
    }
}