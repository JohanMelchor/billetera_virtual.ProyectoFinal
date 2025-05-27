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
        
        // ✅ SIN LISTENER AUTOMÁTICO - Solo se cambia con botón Actualizar
        
        initView();
    }
    
    public void inicializarVista(String idUsuario, boolean esAdmin) {
        this.esAdmin = esAdmin;
        this.idUsuarioActual = idUsuario;

        if (esAdmin) {
            cargarTodasCuentas();
            habilitarVistasAdmin();
        } else {
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
        tcUsuarioAsignado.setCellValueFactory(cellData -> {
            String idUsuario = cellData.getValue().idUsuario();
            UsuarioDto usuario = facade.buscarUsuarioPorId(idUsuario);
            return new SimpleStringProperty(
                usuario != null ? usuario.nombreCompleto() : ""
            );
        });
    }
    
    // ✅ CORREGIDO: Listener más seguro
    private void listenerSeleccion() {
        tableCuentas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // ✅ NUEVO: Verificación adicional para evitar errores
            if (newSelection != null && !listaCuentas.isEmpty()) {
                cuentaSeleccionada = newSelection;
                mostrarInformacionCuenta(cuentaSeleccionada);
            } else {
                cuentaSeleccionada = null;
                limpiarCampos();
            }
        });
    }
    
    // ✅ CORREGIDO: Mostrar información incluyendo el estado actual
    private void mostrarInformacionCuenta(CuentaDto cuentaDto) {
        if(cuentaDto != null) {
            txtIdCuenta.setText(cuentaDto.idCuenta());
            txtNombreBanco.setText(cuentaDto.nombreBanco());
            txtNumeroCuenta.setText(cuentaDto.numeroCuenta());
            cbTipoCuenta.setValue(cuentaDto.tipoCuenta());
            txtSaldo.setText(String.valueOf(cuentaDto.saldoTotal()));
            
            // ✅ Verificación de nulos
            if (cbUsuarios != null) {
                cbUsuarios.setValue(facade.buscarUsuarioPorId(cuentaDto.idUsuario()));
            }
            
            // ✅ Mostrar el estado actual de la cuenta (solo mostrar, no cambiar automáticamente)
            if (cuentaDto.tipoEstado() != null) {
                cbEstadoCuenta.setValue(cuentaDto.tipoEstado());
            } else {
                cbEstadoCuenta.setValue(TipoEstadoCuenta.ACTIVA); // Estado por defecto
            }
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
    
    // ✅ MODIFICADO: Ahora incluye cambio de estado junto con actualização
    @FXML
    void onActualizarCuenta(ActionEvent event) {
        if(cuentaSeleccionada != null) {
            CuentaDto cuentaActualizada = crearCuentaDto();
            if(datosValidos(cuentaActualizada)) {
                
                // ✅ NUEVO: Verificar si el estado cambió y aplicar el cambio
                TipoEstadoCuenta estadoAnterior = cuentaSeleccionada.tipoEstado();
                TipoEstadoCuenta estadoNuevo = cbEstadoCuenta.getValue();
                
                boolean cambioExitoso = true;
                
                // Primero cambiar el estado si es diferente
                if (estadoNuevo != null && !estadoNuevo.equals(estadoAnterior)) {
                    try {
                        boolean estadoCambiado = facade.cambiarEstadoCuenta(cuentaSeleccionada.idCuenta(), estadoNuevo);
                        if (!estadoCambiado) {
                            mostrarAlerta("Error", "No se pudo cambiar estado", 
                                "Error al cambiar el estado de la cuenta", Alert.AlertType.ERROR);
                            cambioExitoso = false;
                        }
                    } catch (IllegalStateException e) {
                        mostrarAlerta("Error", "Transición no permitida", 
                            e.getMessage(), Alert.AlertType.ERROR);
                        cambioExitoso = false;
                    }
                }
                
                // Luego actualizar otros datos si el cambio de estado fue exitoso
                if (cambioExitoso) {
                    if(facade.actualizarCuenta(cuentaActualizada)) {
                        if (esAdmin) {
                            cargarTodasCuentas();
                        } else {
                            cargarCuentasUsuario();
                        }
                        limpiarCampos();
                        
                        String mensaje = CuentaConstantes.EXITO_ACTUALIZAR_CUENTA;
                        if (estadoNuevo != null && !estadoNuevo.equals(estadoAnterior)) {
                            mensaje += ". Estado cambiado a: " + estadoNuevo.getDescripcion();
                        }
                        
                        mostrarAlerta(
                            "Cuenta actualizada", 
                            "Éxito", 
                            mensaje, 
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
                        cargarTodasCuentas();
                    } else {
                        cargarCuentasUsuario();
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
    
    // ✅ CORREGIDO: Preservar saldo actual en lugar de usar 0.0
    private CuentaDto crearCuentaDto() {
        String idUsuarioAsignado;
        if (esAdmin) {
            UsuarioDto usuarioSeleccionado = cbUsuarios.getValue();
            idUsuarioAsignado = usuarioSeleccionado != null ? 
                usuarioSeleccionado.idUsuario() : null;
        } else {
            idUsuarioAsignado = idUsuarioActual;
        }
        
        // ✅ Preservar el saldo actual
        Double saldoActual = 0.0;
        if (cuentaSeleccionada != null) {
            // Para actualizaciones: mantener saldo existente
            saldoActual = cuentaSeleccionada.saldoTotal();
        } else {
            // Para cuentas nuevas: tomar del campo de texto
            try {
                String saldoTexto = txtSaldo.getText();
                if (saldoTexto != null && !saldoTexto.trim().isEmpty()) {
                    saldoActual = Double.parseDouble(saldoTexto);
                }
            } catch (NumberFormatException e) {
                saldoActual = 0.0; // Valor por defecto para cuentas nuevas
            }
        }
        
        return new CuentaDto(
            txtIdCuenta.getText(),
            txtNombreBanco.getText(),
            txtNumeroCuenta.getText(),
            cbTipoCuenta.getValue(),
            idUsuarioAsignado,
            saldoActual,  // ✅ Usar el saldo correcto
            cbEstadoCuenta.getValue() // Puede ser null, se usará ACTIVA por defecto
        );
    }
    
    private boolean datosValidos(CuentaDto cuentaDto) {
        return cuentaDto.idCuenta() != null && !cuentaDto.idCuenta().isEmpty() &&
               cuentaDto.nombreBanco() != null && !cuentaDto.nombreBanco().isEmpty() &&
               cuentaDto.numeroCuenta() != null && !cuentaDto.numeroCuenta().isEmpty() &&
               cuentaDto.tipoCuenta() != null && !cuentaDto.tipoCuenta().isEmpty() &&
               cuentaDto.idUsuario() != null && !cuentaDto.idUsuario().isEmpty();
               // ✅ NOTA: tipoEstado puede ser null (se usará ACTIVA por defecto)
    }
    
    private void limpiarCampos() {
        generarIdUnico();
        txtNombreBanco.setText("");
        txtNumeroCuenta.setText("");
        cbTipoCuenta.setValue(null);
        cuentaSeleccionada = null;
        txtSaldo.setText("");
        if (cbUsuarios != null) {
            cbUsuarios.setValue(null);
        }
        cbEstadoCuenta.setValue(null);
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
}