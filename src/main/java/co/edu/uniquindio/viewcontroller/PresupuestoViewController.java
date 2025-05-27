package co.edu.uniquindio.viewcontroller;


import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.mapping.dto.CuentaDto;
import co.edu.uniquindio.mapping.dto.PresupuestoDto;
import co.edu.uniquindio.service.IAlertaManager;
import co.edu.uniquindio.Util.PresupuestoConstantes;
import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.factory.AlertaManagerFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.util.UUID;

public class PresupuestoViewController {
    
    private ObservableList<PresupuestoDto> listaPresupuestos = FXCollections.observableArrayList();
    private ObservableList<CategoriaDto> listaCategorias = FXCollections.observableArrayList();
    private ObservableList<CuentaDto> listaCuentas = FXCollections.observableArrayList();
    private PresupuestoDto presupuestoSeleccionado;
    private String idUsuarioActual;
    private String idCuentaActual;
    private BilleteraFacade facade;
    private IAlertaManager alertaManager;
    
    @FXML
    private TextField txtIdPresupuesto;
    
    @FXML
    private TextField txtNombre;
    
    @FXML
    private TextField txtMontoAsignado;
    
    @FXML
    private TextField txtMontoGastado;
    
    @FXML
    private TextField txtSaldo;

    @FXML
    private TextField txtCuenta;

    @FXML
    private Pane pnCuenta;

    @FXML
    private ComboBox<CuentaDto> cbCuenta;
    
    @FXML
    private ComboBox<CategoriaDto> cbCategoria;
    
    @FXML
    private Button btnAgregarPresupuesto;
    
    @FXML
    private Button btnActualizarPresupuesto;
    
    @FXML
    private Button btnEliminarPresupuesto;

    @FXML
    private Button btnLimpiar;
    
    @FXML
    private TableView<PresupuestoDto> tablePresupuestos;
    
    @FXML
    private TableColumn<PresupuestoDto, String> tcIdPresupuesto;
    
    @FXML
    private TableColumn<PresupuestoDto, String> tcNombre;
    
    @FXML
    private TableColumn<PresupuestoDto, String> tcMontoAsignado;
    
    @FXML
    private TableColumn<PresupuestoDto, String> tcMontoGastado;
    
    @FXML
    private TableColumn<PresupuestoDto, String> tcSaldo;
    
    @FXML
    private TableColumn<PresupuestoDto, String> tcCategoria;

    @FXML
    private TableColumn<PresupuestoDto, String> tcCuenta;
    
    @FXML
    void initialize() {
        facade = new BilleteraFacade();
        alertaManager = AlertaManagerFactory.crearManagerCompleto();
        
        initView();
    }
    
    public void inicializarVista(String idUsuario, boolean esAdmin) {
        if (esAdmin) {
            cargarTodosPresupuestos();
            habilitarVistaAdministrador();
            cargarDatos(true);
        } else {
            this.idUsuarioActual = idUsuario;
            listaCuentas.clear();
            listaCuentas.addAll(facade.obtenerCuentasPorUsuario(idUsuario));
            habilitarVistaUsuario();
            cargarDatos(false);
        }
    }

    private void cargarTodosPresupuestos() {
        listaPresupuestos.clear();
        listaPresupuestos.addAll(facade.obtenerTodosPresupuestos());
    }

    public void inicializarConCuenta(String idCuenta) {
        this.idCuentaActual = idCuenta;
        cargarPresupuestosDeCuenta();
    }
    
    private void initView() {
        initDataBinding();
        tablePresupuestos.getItems().clear();
        tablePresupuestos.setItems(listaPresupuestos);
        listenerSeleccion();
        
        // Configurar ComboBox de categorías
        cbCategoria.setItems(listaCategorias);
        // Configurar converter para mostrar nombre de categoría en ComboBox
        cbCategoria.setConverter(new javafx.util.StringConverter<CategoriaDto>() {
            @Override
            public String toString(CategoriaDto categoria) {
                return categoria != null ? categoria.nombre() : "";
            }
            @Override
            public CategoriaDto fromString(String string) {
                return null; // No se necesita conversión inversa
            }
        });

        cbCuenta.setItems(listaCuentas);
        cbCuenta.setConverter(new javafx.util.StringConverter<CuentaDto>() {
            @Override
            public String toString(CuentaDto cuenta) {
                return cuenta != null ? cuenta.idCuenta() : "";
            }
            
            @Override
            public CuentaDto fromString(String string) {
                return null;
            }
        });
        // Generar automáticamente ID único al abrir la vista
        generarIdUnico();
    }

    public void habilitarVistaAdministrador() {
        // Habilitar vista para administrador
        btnAgregarPresupuesto.setVisible(false);
        btnActualizarPresupuesto.setVisible(false);
        btnEliminarPresupuesto.setVisible(false);
        txtIdPresupuesto.setEditable(false);
        cbCategoria.setDisable(true);
        txtNombre.setEditable(false);
        txtMontoAsignado.setEditable(false);
        txtMontoGastado.setEditable(false);
        txtSaldo.setEditable(false);
        pnCuenta.setVisible(false);
        btnLimpiar.setVisible(false);
    }
    
    public void habilitarVistaUsuario() {
        // Habilitar vista para usuario
        btnAgregarPresupuesto.setDisable(false);
        btnActualizarPresupuesto.setDisable(false);
        btnEliminarPresupuesto.setDisable(false);
        cbCategoria.setDisable(false);
        txtIdPresupuesto.setEditable(false);
        txtNombre.setDisable(false);
        txtMontoAsignado.setDisable(false);
        txtMontoGastado.setDisable(false);
        txtSaldo.setDisable(false);
    }
    
    private void cargarDatos(boolean esAdmin) {
        listaPresupuestos.clear();
        if (esAdmin) {
            listaPresupuestos.addAll(facade.obtenerTodosPresupuestos());
        } else if (idUsuarioActual != null && !idUsuarioActual.isEmpty()) {
            listaPresupuestos.addAll(facade.obtenerPresupuestosPorUsuario(idUsuarioActual));
        }
        // Siempre cargar categorías
        listaCategorias.clear();
        listaCategorias.addAll(facade.obtenerCategorias());
    }

    private void cargarPresupuestosDeCuenta() {
        if (idCuentaActual != null) {
            listaPresupuestos.clear();
            listaPresupuestos.addAll(facade.obtenerPresupuestosPorCuenta(idCuentaActual));
            
        }
    }
    
    private void initDataBinding() {
        tcIdPresupuesto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().idPresupuesto()));
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombre()));
        tcMontoAsignado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().montoAsignado()));
        tcMontoGastado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().montoGastado()));
        tcSaldo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().saldo()));
        tcCuenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().idCuenta()));

        tcCategoria.setCellValueFactory(cellData -> {
            String idCategoria = cellData.getValue().idCategoria();
            if(idCategoria != null && !idCategoria.isEmpty()) {
                for(CategoriaDto categoria : listaCategorias) {
                    if(categoria.idCategoria().equals(idCategoria)) {
                        return new SimpleStringProperty(categoria.nombre());
                    }
                }
            }
            return new SimpleStringProperty("");
        });
    }
    
    private void listenerSeleccion() {
        tablePresupuestos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            presupuestoSeleccionado = newSelection;
            mostrarInformacionPresupuesto(presupuestoSeleccionado);
        });
    }
    
    private void mostrarInformacionPresupuesto(PresupuestoDto presupuestoDto) {
        if(presupuestoDto != null) {
            txtIdPresupuesto.setText(presupuestoDto.idPresupuesto());
            txtNombre.setText(presupuestoDto.nombre());
            txtMontoAsignado.setText(presupuestoDto.montoAsignado());
            txtMontoGastado.setText(presupuestoDto.montoGastado());
            txtSaldo.setText(presupuestoDto.saldo());
            txtCuenta.setText(presupuestoDto.idCuenta());
            txtCuenta.setVisible(true);
            txtCuenta.setEditable(false);

            // No mostrar cuenta en el ComboBox al actualizar (solo lectura)
            cbCuenta.setDisable(true);
            
            // Seleccionar categoría en el ComboBox
            String idCategoria = presupuestoDto.idCategoria();
            if(idCategoria != null && !idCategoria.isEmpty()) {
                for(CategoriaDto categoria : listaCategorias) {
                    if(categoria.idCategoria().equals(idCategoria)) {
                        cbCategoria.setValue(categoria);
                        break;
                    }
                }
            } else {
                cbCategoria.setValue(null);
            }
        }
    }
    
    @FXML
    void onAgregarPresupuesto(ActionEvent event) {
        CuentaDto cuentaSeleccionada = cbCuenta.getValue();
        if (cuentaSeleccionada == null) {
            mostrarAlerta("Error", "Selección requerida", "Debe seleccionar una cuenta", Alert.AlertType.WARNING);
            return;
        }

        PresupuestoDto nuevoPresupuesto = crearPresupuestoDto(true);
        if (datosValidos(nuevoPresupuesto)) {
            if (facade.agregarPresupuestoACuenta(cuentaSeleccionada.idCuenta(), nuevoPresupuesto)) {
                // CAMBIO: Siempre cargar todos los presupuestos del usuario
                cargarDatos(false);
                mostrarAlerta("Éxito", "Presupuesto agregado", "Presupuesto creado correctamente", Alert.AlertType.INFORMATION);
                limpiarCampos();
            } else {
                double monto = Double.parseDouble(nuevoPresupuesto.montoAsignado());
                mostrarAlerta("Error", "No se pudo agregar", 
                    "Saldo insuficiente o error al crear. Monto solicitado: " + monto, 
                    Alert.AlertType.ERROR);
            }
        }
        txtCuenta.setVisible(false);
    }
    
    @FXML
    void onActualizarPresupuesto(ActionEvent event) {
        if(presupuestoSeleccionado != null) {
            PresupuestoDto presupuestoActualizado = crearPresupuestoDto(false);
            if(datosValidos(presupuestoActualizado)) {
                if(facade.actualizarPresupuesto(presupuestoActualizado)) {
                    // CAMBIO: Solo una llamada para recargar datos
                    cargarDatos(false);
                    limpiarCampos();
                    mostrarAlerta(
                        "Presupuesto actualizado", 
                        "Éxito", 
                        PresupuestoConstantes.EXITO_ACTUALIZAR_PRESUPUESTO, 
                        Alert.AlertType.INFORMATION
                    );
                    generarIdUnico();
                } else {
                    mostrarAlerta(
                        "Error", 
                        "No se pudo actualizar", 
                        PresupuestoConstantes.ERROR_ACTUALIZAR_PRESUPUESTO, 
                        Alert.AlertType.ERROR
                    );
                }
            } else {
                mostrarAlerta(
                    "Campos incompletos", 
                    "Datos incompletos", 
                    PresupuestoConstantes.ERROR_CAMPOS_VACIOS, 
                    Alert.AlertType.WARNING
                );
            }
        } else {
            mostrarAlerta(
                "Selección requerida", 
                "No hay selección", 
                "Debe seleccionar un presupuesto para actualizar", 
                Alert.AlertType.WARNING
            );
        }
    }
    
    @FXML
    void onEliminarPresupuesto(ActionEvent event) {
        if(presupuestoSeleccionado != null) {
            boolean confirmacion = mostrarConfirmacion(
                "Confirmación de eliminación", 
                "¿Está seguro de que desea eliminar el presupuesto '" + presupuestoSeleccionado.nombre() + "'?"
            );
            
            if(confirmacion) {
                if(facade.eliminarPresupuesto(presupuestoSeleccionado.idPresupuesto())) {
                    // CAMBIO: Solo cargar todos los presupuestos del usuario
                    cargarDatos(false);
                    limpiarCampos();
                    mostrarAlerta(
                        "Presupuesto eliminado", 
                        "Éxito", 
                        PresupuestoConstantes.EXITO_ELIMINAR_PRESUPUESTO, 
                        Alert.AlertType.INFORMATION
                    );
                    generarIdUnico();
                    txtCuenta.setVisible(false);
                } else {
                    mostrarAlerta(
                        "Error", 
                        "No se pudo eliminar", 
                        PresupuestoConstantes.ERROR_ELIMINAR_PRESUPUESTO, 
                        Alert.AlertType.ERROR
                    );
                }
            }
        } else {
            mostrarAlerta(
                "Selección requerida", 
                "No hay selección", 
                "Debe seleccionar un presupuesto para eliminar", 
                Alert.AlertType.WARNING
            );
        }
    }
    
    private PresupuestoDto crearPresupuestoDto(boolean esCreacion) {
        CategoriaDto categoriaSeleccionada = cbCategoria.getValue();
        String idCategoria = categoriaSeleccionada != null ? categoriaSeleccionada.idCategoria() : "";
        
        String idCuenta;
        if (esCreacion) {
            CuentaDto cuentaSeleccionada = cbCuenta.getValue();
            idCuenta = cuentaSeleccionada != null ? cuentaSeleccionada.idCuenta() : "";
        } else {
            idCuenta = presupuestoSeleccionado != null ? presupuestoSeleccionado.idCuenta() : "";
        }
        
        // Validar y formatear montos
        String montoAsignado = txtMontoAsignado.getText();
        String montoGastado = txtMontoGastado.getText();
        String saldo = txtSaldo.getText();
        
        try {
            // Forzar formato correcto
            if(montoAsignado == null || montoAsignado.isEmpty()) {
                montoAsignado = "0.00";
            } else {
                Double.parseDouble(montoAsignado); // Validar que sea número
            }
            
            if(montoGastado == null || montoGastado.isEmpty()) {
                montoGastado = "0.00";
            } else {
                Double.parseDouble(montoGastado);
            }
            
            if(saldo == null || saldo.isEmpty()) {
                saldo = "0.00";
            } else {
                Double.parseDouble(saldo);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Valor inválido", "Los montos deben ser números válidos", Alert.AlertType.ERROR);
            return null;
        }
        
        return new PresupuestoDto(
            txtIdPresupuesto.getText(),
            txtNombre.getText(),
            montoAsignado,
            montoGastado,
            idCategoria,
            idUsuarioActual,
            saldo,
            idCuenta
        );
    }
    
    private boolean datosValidos(PresupuestoDto presupuestoDto) {
        if(presupuestoDto.idPresupuesto() == null || presupuestoDto.idPresupuesto().isEmpty() ||
           presupuestoDto.nombre() == null || presupuestoDto.nombre().isEmpty() ||
           presupuestoDto.montoAsignado() == null || presupuestoDto.montoAsignado().isEmpty() ||
           presupuestoDto.idUsuario() == null || presupuestoDto.idUsuario().isEmpty()) {
            return false;
        }
        
        // Validar que los valores numéricos sean válidos
        try {
            Double.parseDouble(presupuestoDto.montoAsignado());
            Double.parseDouble(presupuestoDto.montoGastado());
            Double.parseDouble(presupuestoDto.saldo());
        } catch(NumberFormatException e) {
            return false;
        }
        
        return true;
    }
    
    private void limpiarCampos() {
        generarIdUnico();
        txtNombre.setText("");
        txtMontoAsignado.setText("");
        txtMontoGastado.setText("");
        txtSaldo.setText("");
        txtCuenta.setText("");
        cbCategoria.setValue(null);
        cbCuenta.setDisable(false);
        // cbCuenta.getSelectionModel().clearSelection(); // <-- comenta o elimina esta línea
        presupuestoSeleccionado = null;
    }
    
    private void generarIdUnico() {
        txtIdPresupuesto.setText("PRE" + UUID.randomUUID().toString().substring(0, 8));
    }
    
    private void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        alertaManager.mostrarAlerta(titulo, header, contenido, tipo);
    }
    
    private boolean mostrarConfirmacion(String titulo, String mensaje) {
        return alertaManager.mostrarConfirmacion(titulo, mensaje);
    }

    @FXML
    void onLimpiar(ActionEvent event) {
        limpiarCampos();
        tablePresupuestos.getSelectionModel().clearSelection();
        cbCuenta.getSelectionModel().clearSelection();
        cbCategoria.getSelectionModel().clearSelection();
        txtCuenta.setVisible(false);
    }
        
}