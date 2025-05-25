package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.controller.CategoriaController;
import co.edu.uniquindio.controller.CuentaController;
import co.edu.uniquindio.controller.PresupuestoController;
import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.mapping.dto.CuentaDto;
import co.edu.uniquindio.mapping.dto.PresupuestoDto;
import co.edu.uniquindio.Util.PresupuestoConstantes;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.util.Optional;
import java.util.UUID;

public class PresupuestoViewController {
    
    private PresupuestoController presupuestoController;
    private CategoriaController categoriaController;
    private CuentaController cuentaController;
    private ObservableList<PresupuestoDto> listaPresupuestos = FXCollections.observableArrayList();
    private ObservableList<CategoriaDto> listaCategorias = FXCollections.observableArrayList();
    private ObservableList<CuentaDto> listaCuentas = FXCollections.observableArrayList();
    private PresupuestoDto presupuestoSeleccionado;
    private String idUsuarioActual;
    private String idCuentaActual;
    
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
        presupuestoController = new PresupuestoController();
        categoriaController = new CategoriaController();
        cuentaController = new CuentaController();
        
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
            listaCuentas.addAll(cuentaController.obtenerCuentasPorUsuario(idUsuario));
            habilitarVistaUsuario();
            cargarDatos(false);
        }
    }

    private void cargarTodosPresupuestos() {
        listaPresupuestos.clear();
        listaPresupuestos.addAll(presupuestoController.obtenerTodosPresupuestos());
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
            listaPresupuestos.addAll(presupuestoController.obtenerTodosPresupuestos());
        } else if (idUsuarioActual != null && !idUsuarioActual.isEmpty()) {
            listaPresupuestos.addAll(presupuestoController.obtenerPresupuestosPorUsuario(idUsuarioActual));
        }
        // Siempre cargar categorías
        listaCategorias.clear();
        listaCategorias.addAll(categoriaController.obtenerCategorias());
    }

    private void cargarPresupuestosDeCuenta() {
        if (idCuentaActual != null) {
            listaPresupuestos.clear();
            listaPresupuestos.addAll(presupuestoController.obtenerPresupuestosPorCuenta(idCuentaActual));
            
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
            mostrarMensaje("Error", "Selección requerida", "Debe seleccionar una cuenta", Alert.AlertType.WARNING);
            return;
        }

        PresupuestoDto nuevoPresupuesto = crearPresupuestoDto();
        if (datosValidos(nuevoPresupuesto)) {
            // Usar el ID de la cuenta seleccionada en lugar de idCuentaActual
            if (presupuestoController.agregarPresupuestoACuenta(cuentaSeleccionada.idCuenta(), nuevoPresupuesto)) {
                cargarPresupuestosDeCuenta();
                mostrarMensaje("Éxito", "Presupuesto agregado", "Presupuesto creado correctamente", Alert.AlertType.INFORMATION);
                limpiarCampos();
            } else {
                // Mensaje más detallado
                double monto = Double.parseDouble(nuevoPresupuesto.montoAsignado());
                mostrarMensaje("Error", "No se pudo agregar", 
                    "Saldo insuficiente o error al crear. Monto solicitado: " + monto, 
                    Alert.AlertType.ERROR);
            }
        }
        txtCuenta.setVisible(false);
    }
    
    @FXML
    void onActualizarPresupuesto(ActionEvent event) {
        if(presupuestoSeleccionado != null) {
            PresupuestoDto presupuestoActualizado = crearPresupuestoDto();
            if(datosValidos(presupuestoActualizado)) {
                if(presupuestoController.actualizarPresupuesto(presupuestoActualizado)) {
                    cargarDatos(false);
                    limpiarCampos();
                    mostrarMensaje(
                        "Presupuesto actualizado", 
                        "Éxito", 
                        PresupuestoConstantes.EXITO_ACTUALIZAR_PRESUPUESTO, 
                        Alert.AlertType.INFORMATION
                    );
                    
                    // Generar nuevo ID único para el siguiente presupuesto
                    generarIdUnico();
                } else {
                    mostrarMensaje(
                        "Error", 
                        "No se pudo actualizar", 
                        PresupuestoConstantes.ERROR_ACTUALIZAR_PRESUPUESTO, 
                        Alert.AlertType.ERROR
                    );
                }
            } else {
                mostrarMensaje(
                    "Campos incompletos", 
                    "Datos incompletos", 
                    PresupuestoConstantes.ERROR_CAMPOS_VACIOS, 
                    Alert.AlertType.WARNING
                );
            }
        } else {
            mostrarMensaje(
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
            boolean confirmacion = mostrarMensajeConfirmacion(
                "¿Está seguro de eliminar el presupuesto seleccionado?"
            );
            
            if(confirmacion) {
                if(presupuestoController.eliminarPresupuesto(presupuestoSeleccionado.idPresupuesto())) {
                    listaPresupuestos.remove(presupuestoSeleccionado);
                    limpiarCampos();
                    mostrarMensaje(
                        "Presupuesto eliminado", 
                        "Éxito", 
                        PresupuestoConstantes.EXITO_ELIMINAR_PRESUPUESTO, 
                        Alert.AlertType.INFORMATION
                    );
                    
                    // Generar nuevo ID único para el siguiente presupuesto
                    generarIdUnico();
                    txtCuenta.setVisible(false);
                } else {
                    mostrarMensaje(
                        "Error", 
                        "No se pudo eliminar", 
                        PresupuestoConstantes.ERROR_ELIMINAR_PRESUPUESTO, 
                        Alert.AlertType.ERROR
                    );
                }
            }
        } else {
            mostrarMensaje(
                "Selección requerida", 
                "No hay selección", 
                "Debe seleccionar un presupuesto para eliminar", 
                Alert.AlertType.WARNING
            );
        }
    }
    
    private PresupuestoDto crearPresupuestoDto() {
        CategoriaDto categoriaSeleccionada = cbCategoria.getValue();
        String idCategoria = categoriaSeleccionada != null ? categoriaSeleccionada.idCategoria() : "";
        
        CuentaDto cuentaSeleccionada = cbCuenta.getValue();
        String idCuenta = cuentaSeleccionada != null ? cuentaSeleccionada.idCuenta() : "";
        
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
            mostrarMensaje("Error", "Valor inválido", "Los montos deben ser números válidos", Alert.AlertType.ERROR);
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
        txtMontoAsignado.setText("0.0");
        txtMontoGastado.setText("0.0");
        txtSaldo.setText("0.0");
        txtCuenta.setText("");
        cbCategoria.setValue(null);
        cbCuenta.setDisable(false); // Habilitar ComboBox al crear nuevo
        cbCuenta.getSelectionModel().clearSelection();
        presupuestoSeleccionado = null;
    }
    
    private void generarIdUnico() {
        txtIdPresupuesto.setText("PRE" + UUID.randomUUID().toString().substring(0, 8));
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

    @FXML
    void onLimpiar(ActionEvent event) {
        limpiarCampos();
        tablePresupuestos.getSelectionModel().clearSelection();
        cbCuenta.getSelectionModel().clearSelection();
        cbCategoria.getSelectionModel().clearSelection();
        txtCuenta.setVisible(false);
    }
        
}