package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.controller.CategoriaController;
import co.edu.uniquindio.controller.CuentaController;
import co.edu.uniquindio.controller.PresupuestoController;
import co.edu.uniquindio.controller.TransaccionController;
import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.mapping.dto.CuentaDto;
import co.edu.uniquindio.mapping.dto.PresupuestoDto;
import co.edu.uniquindio.mapping.dto.TransaccionDto;
import co.edu.uniquindio.Util.TransaccionConstantes;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.format.DateTimeFormatter;

public class TransaccionViewController {
    
    private TransaccionController transaccionController;
    private PresupuestoController presupuestoController;
    private CategoriaController categoriaController;
    private CuentaController cuentaController;
    private ObservableList<TransaccionDto> listaTransacciones = FXCollections.observableArrayList();
    private ObservableList<CuentaDto> listaCuentas = FXCollections.observableArrayList();
    private ObservableList<CategoriaDto> listaCategorias = FXCollections.observableArrayList();
    private ObservableList<PresupuestoDto> listaPresupuestos = FXCollections.observableArrayList();
    private String idUsuarioActual;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @FXML
    private ComboBox<String> cbTipoTransaccion;
    
    @FXML
    private ComboBox<CuentaDto> cbCuentaOrigen;
    
    @FXML
    private ComboBox<CuentaDto> cbCuentaDestino;
    
    @FXML
    private ComboBox<CategoriaDto> cbCategoria;
    
    @FXML
    private TextField txtMonto;

    @FXML
    private Label lblCuentaDestino;

    @FXML
    private Label lblCuentaOrigen;
    
    @FXML
    private TextArea txtDescripcion;
    
    @FXML
    private Button btnTransaccion;

    @FXML private Label lblPresupuesto;

    @FXML private ComboBox<PresupuestoDto> cbPresupuesto;

    @FXML private TableColumn<TransaccionDto, String> tcPresupuesto;
    
    @FXML
    private TableView<TransaccionDto> tableTransacciones;
    
    @FXML
    private TableColumn<TransaccionDto, String> tcFecha;
    
    @FXML
    private TableColumn<TransaccionDto, String> tcTipo;
    
    @FXML
    private TableColumn<TransaccionDto, String> tcMonto;
    
    @FXML
    private TableColumn<TransaccionDto, String> tcDescripcion;
    
    @FXML
    private TableColumn<TransaccionDto, String> tcCuentaOrigen;
    
    @FXML
    private TableColumn<TransaccionDto, String> tcCuentaDestino;
    
    @FXML
    private TableColumn<TransaccionDto, String> tcCategoria;
    
    @FXML
    void initialize() {
        transaccionController = new TransaccionController();
        presupuestoController = new PresupuestoController();
        categoriaController = new CategoriaController();
        cuentaController = new CuentaController();
        
        // Inicializar tipos de transacción
        cbTipoTransaccion.getItems().addAll(
            TransaccionConstantes.TIPO_DEPOSITO_CUENTA,
            TransaccionConstantes.TIPO_DEPOSITO_PRESUPUESTO,
            TransaccionConstantes.TIPO_RETIRO_CUENTA,
            TransaccionConstantes.TIPO_RETIRO_PRESUPUESTO,
            TransaccionConstantes.TIPO_TRANSFERENCIA
        );
        
        // Configurar listener para tipo de transacción
        cbTipoTransaccion.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            actualizarVisibilidadCampos(newValue);
        });

        cbPresupuesto.setItems(listaPresupuestos);
        cbPresupuesto.setConverter(new StringConverter<PresupuestoDto>() {
            @Override public String toString(PresupuestoDto p) {
                return p != null ? p.nombre() + " (" + p.saldo() + ")" : "";
            }
            @Override public PresupuestoDto fromString(String s) { return null; }
        });

        cbTipoTransaccion.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            actualizarVisibilidadCampos(newVal);
        });
        
        initView();
    }
    
    public void inicializarConUsuario(String idUsuario) {
        this.idUsuarioActual = idUsuario;
        cargarDatos();
    }
    
    private void initView() {
        initDataBinding();
        tableTransacciones.getItems().clear();
        tableTransacciones.setItems(listaTransacciones);
        
        // Configurar ComboBox de cuentas y categorías
        cbCuentaOrigen.setItems(listaCuentas);
        cbCuentaDestino.setItems(listaCuentas);
        cbCategoria.setItems(listaCategorias);
        
        // Configurar converters para mostrar información de las cuentas en los ComboBox
        cbCuentaOrigen.setConverter(new javafx.util.StringConverter<CuentaDto>() {
            @Override
            public String toString(CuentaDto cuenta) {
                return cuenta != null ? cuenta.nombreBanco() + " - " + cuenta.numeroCuenta() : "";
            }
            
            @Override
            public CuentaDto fromString(String string) {
                return null; // No se necesita conversión inversa
            }
        });
        
        cbCuentaDestino.setConverter(new javafx.util.StringConverter<CuentaDto>() {
            @Override
            public String toString(CuentaDto cuenta) {
                return cuenta != null ? cuenta.nombreBanco() + " - " + cuenta.numeroCuenta() : "";
            }
            
            @Override
            public CuentaDto fromString(String string) {
                return null; // No se necesita conversión inversa
            }
        });
        
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
        
        // Ocultar componentes inicialmente
        cbCuentaDestino.setVisible(false);
        Label lblCuentaDestino = new Label("Cuenta Destino:");
        lblCuentaDestino.setVisible(false);
    }
    
    private void cargarDatos() {
        if (idUsuarioActual != null) {
            // Cargar cuentas del usuario
            listaCuentas.setAll(cuentaController.obtenerCuentasPorUsuario(idUsuarioActual));
            // Cargar categorías (todas)
            listaCategorias.setAll(categoriaController.obtenerCategorias());
            // Cargar transacciones del usuario
            listaTransacciones.setAll(transaccionController.obtenerTransaccionesPorUsuario(idUsuarioActual));
            // Cargar presupuestos de las cuentas del usuario
            listaPresupuestos.clear();
            for (CuentaDto cuenta : listaCuentas) {
                listaPresupuestos.addAll(presupuestoController.obtenerPresupuestosPorCuenta(cuenta.idCuenta()));
            }  
        }
    }
    
    private void initDataBinding() {
        tcFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().fecha()));
        tcTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().tipoTransaccion()));
        tcMonto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().monto()));
        tcDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().descripcion()));
        
        // Estas columnas pueden tener valores nulos, así que se maneja adecuadamente
        tcCuentaOrigen.setCellValueFactory(cellData -> {
            String idCuentaOrigen = cellData.getValue().idCuentaOrigen();
            if(idCuentaOrigen != null && !idCuentaOrigen.isEmpty()) {
                for(CuentaDto cuenta : listaCuentas) {
                    if(cuenta.idCuenta().equals(idCuentaOrigen)) {
                        return new SimpleStringProperty(cuenta.nombreBanco() + " - " + cuenta.numeroCuenta());
                    }
                }
            }
            return new SimpleStringProperty("");
        });
        
        tcCuentaDestino.setCellValueFactory(cellData -> {
            String idCuentaDestino = cellData.getValue().idCuentaDestino();
            if(idCuentaDestino != null && !idCuentaDestino.isEmpty()) {
                for(CuentaDto cuenta : listaCuentas) {
                    if(cuenta.idCuenta().equals(idCuentaDestino)) {
                        return new SimpleStringProperty(cuenta.nombreBanco() + " - " + cuenta.numeroCuenta());
                    }
                }
            }
            return new SimpleStringProperty("");
        });
        
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
    
    private void actualizarVisibilidadCampos(String tipoTransaccion) {
        lblCuentaOrigen.setVisible(false);
        cbCuentaOrigen.setVisible(false);
        lblPresupuesto.setVisible(false);
        cbPresupuesto.setVisible(false);
        lblCuentaDestino.setVisible(false);
        cbCuentaDestino.setVisible(false);
        
        if (tipoTransaccion == null) return;
        
        switch (tipoTransaccion) {
            case TransaccionConstantes.TIPO_DEPOSITO_CUENTA:
                lblCuentaDestino.setVisible(true);
                cbCuentaDestino.setVisible(true);
                break;
                
            case TransaccionConstantes.TIPO_DEPOSITO_PRESUPUESTO:
                lblCuentaOrigen.setVisible(true);
                cbCuentaOrigen.setVisible(true);
                lblPresupuesto.setVisible(true);
                cbPresupuesto.setVisible(true);
                break;
                
            case TransaccionConstantes.TIPO_RETIRO_CUENTA:
                lblCuentaOrigen.setVisible(true);
                cbCuentaOrigen.setVisible(true);
                break;
                
            case TransaccionConstantes.TIPO_RETIRO_PRESUPUESTO:
                lblPresupuesto.setVisible(true);
                cbPresupuesto.setVisible(true);
                break;
                
            case TransaccionConstantes.TIPO_TRANSFERENCIA:
                lblCuentaOrigen.setVisible(true);
                cbCuentaOrigen.setVisible(true);
                lblCuentaDestino.setVisible(true);
                cbCuentaDestino.setVisible(true);
                break;
        }
    }
    
    @FXML
    void onTransaccion(ActionEvent event) {
        String tipo = cbTipoTransaccion.getValue();
        if (tipo == null) {
            mostrarMensaje("Error", "Seleccione un tipo de operación" , "Seleccione un tipo de operación", Alert.AlertType.ERROR);
            return;
        }
        
        try {
            double monto = Double.parseDouble(txtMonto.getText());
            String descripcion = txtDescripcion.getText();
            CategoriaDto categoria = cbCategoria.getValue();
            String idCategoria = categoria != null ? categoria.idCategoria() : null;
            
            boolean resultado = false;
            
            switch (tipo) {
                case TransaccionConstantes.TIPO_DEPOSITO_CUENTA:
                    CuentaDto cuentaDestino = cbCuentaDestino.getValue();
                    if (cuentaDestino == null) {
                        mostrarMensaje("Error", "Seleccione cuenta destino", "Seleccione cuenta destino para la operación", Alert.AlertType.ERROR);
                        return;
                    }
                    resultado = transaccionController.depositoCuenta(
                        cuentaDestino.idCuenta(), monto, descripcion, idCategoria);
                break;

                case TransaccionConstantes.TIPO_DEPOSITO_PRESUPUESTO:
                    CuentaDto cuentaOrigen = cbCuentaOrigen.getValue();
                    PresupuestoDto presupuestoDestino = cbPresupuesto.getValue();
                    if (cuentaOrigen == null || presupuestoDestino == null) {
                        mostrarMensaje("Error", "Seleccione cuenta y presupuesto", "Seleccione cuenta y presupuesto", Alert.AlertType.ERROR);
                        return;
                    }
                    resultado = transaccionController.depositoPresupuesto(
                        cuentaOrigen.idCuenta(), presupuestoDestino.idPresupuesto(), monto, descripcion, idCategoria);
                    break;
                    
                case TransaccionConstantes.TIPO_RETIRO_CUENTA:
                    cuentaOrigen = cbCuentaOrigen.getValue();
                    if (cuentaOrigen == null) {
                        mostrarMensaje("Error","Seleccione cuenta origen",  "Seleccione cuenta de origen para la operacion", Alert.AlertType.ERROR);
                        return;
                    }
                    resultado = transaccionController.retiroPorCuenta(
                        cuentaOrigen.idCuenta(), monto, descripcion, idCategoria);
                    break;
                    
                case TransaccionConstantes.TIPO_RETIRO_PRESUPUESTO:
                    CuentaDto cuenta = cbCuentaOrigen.getValue();
                    PresupuestoDto presupuesto = cbPresupuesto.getValue();
                    if (cuenta == null || presupuesto == null) {
                        mostrarMensaje("Error","Seleccione cuenta y presupuesto", "Seleccione cuenta y presupuesto", Alert.AlertType.ERROR);
                        return;
                    }
                    resultado = transaccionController.retiroPorPresupuesto(
                        cuenta.idCuenta(), presupuesto.idPresupuesto(), monto, descripcion, idCategoria);
                    break;
                    
                case TransaccionConstantes.TIPO_TRANSFERENCIA:
                    CuentaDto origen = cbCuentaOrigen.getValue();
                    CuentaDto destino = cbCuentaDestino.getValue();
                    if (origen == null || destino == null) {
                        mostrarMensaje("Error", "Seleccione cuentas de origen y destino", "Seleccione cuentas de origen y destino", Alert.AlertType.ERROR);
                        return;
                    }
                    if (origen.idCuenta().equals(destino.idCuenta())) {
                        mostrarMensaje("Error", "Las cuentas de origen y destino no pueden ser iguales", "Seleccione cuentas diferentes", Alert.AlertType.ERROR);
                        return;
                    }
                    resultado = transaccionController.realizarTransferencia(
                        origen.idCuenta(), 
                        destino.idCuenta(), 
                        monto, 
                        descripcion, 
                        idCategoria);
                    break;
            }
            
            if (resultado) {
                mostrarMensaje("Éxito","Transacción realizada", "Operación realizada correctamente", Alert.AlertType.INFORMATION);
                cargarDatos();
                limpiarCampos();
            } else {
                mostrarMensaje("Error", "Error al realizar la operación", "No se pudo completar la operación", Alert.AlertType.ERROR);
            }
            
        } catch (NumberFormatException e) {
            mostrarMensaje("Error","Error en el formato del monto",  "Ingrese un monto válido", Alert.AlertType.ERROR);
        }
    }
    
    private void limpiarCampos() {
        cbTipoTransaccion.setValue(null);
        cbCuentaOrigen.setValue(null);
        cbCuentaDestino.setValue(null);
        cbCategoria.setValue(null);
        txtMonto.setText("");
        txtDescripcion.setText("");
    }
    
    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}