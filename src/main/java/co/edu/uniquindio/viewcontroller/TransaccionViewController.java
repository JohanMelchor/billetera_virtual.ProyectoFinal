package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.controller.CategoriaController;
import co.edu.uniquindio.controller.CuentaController;
import co.edu.uniquindio.controller.TransaccionController;
import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.mapping.dto.CuentaDto;
import co.edu.uniquindio.mapping.dto.TransaccionDto;
import co.edu.uniquindio.Util.TransaccionConstantes;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class TransaccionViewController {
    
    private TransaccionController transaccionController;
    private CuentaController cuentaController;
    private CategoriaController categoriaController;
    private ObservableList<TransaccionDto> listaTransacciones = FXCollections.observableArrayList();
    private ObservableList<CuentaDto> listaCuentas = FXCollections.observableArrayList();
    private ObservableList<CategoriaDto> listaCategorias = FXCollections.observableArrayList();
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
    private TextArea txtDescripcion;
    
    @FXML
    private Button btnRealizarTransaccion;
    
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
        cuentaController = new CuentaController();
        categoriaController = new CategoriaController();
        
        // Inicializar tipos de transacción
        cbTipoTransaccion.getItems().addAll(
            TransaccionConstantes.TIPO_DEPOSITO,
            TransaccionConstantes.TIPO_RETIRO,
            TransaccionConstantes.TIPO_TRANSFERENCIA
        );
        
        // Configurar listener para tipo de transacción
        cbTipoTransaccion.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            actualizarVisibilidadCampos(newValue);
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
        if(idUsuarioActual != null && !idUsuarioActual.isEmpty()) {
            // Cargar transacciones del usuario
            listaTransacciones.clear();
            listaTransacciones.addAll(transaccionController.obtenerTransaccionesPorUsuario(idUsuarioActual));
            
            // Cargar cuentas del usuario
            listaCuentas.clear();
            listaCuentas.addAll(cuentaController.obtenerCuentasPorUsuario(idUsuarioActual));
            
            // Cargar categorías
            listaCategorias.clear();
            listaCategorias.addAll(categoriaController.obtenerCategorias());
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
        if(tipoTransaccion == null) {
            cbCuentaOrigen.setVisible(true);
            cbCuentaDestino.setVisible(false);
            return;
        }
        
        switch(tipoTransaccion) {
            case TransaccionConstantes.TIPO_DEPOSITO:
                cbCuentaOrigen.setVisible(false);
                cbCuentaDestino.setVisible(true);
                break;
            case TransaccionConstantes.TIPO_RETIRO:
                cbCuentaOrigen.setVisible(true);
                cbCuentaDestino.setVisible(false);
                break;
            case TransaccionConstantes.TIPO_TRANSFERENCIA:
                cbCuentaOrigen.setVisible(true);
                cbCuentaDestino.setVisible(true);
                break;
        }
    }
    
    @FXML
    void onRealizarTransaccion(ActionEvent event) {
        String tipoTransaccion = cbTipoTransaccion.getValue();
        if(tipoTransaccion == null || tipoTransaccion.isEmpty()) {
            mostrarMensaje(
                "Tipo requerido", 
                "Seleccione tipo", 
                "Debe seleccionar un tipo de transacción", 
                Alert.AlertType.WARNING
            );
            return;
        }
        
        Double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText());
            if(monto <= 0) {
                mostrarMensaje(
                    "Monto inválido", 
                    "Monto debe ser positivo", 
                    "El monto debe ser mayor que cero", 
                    Alert.AlertType.WARNING
                );
                return;
            }
        } catch(NumberFormatException e) {
            mostrarMensaje(
                "Monto inválido", 
                "Formato incorrecto", 
                "El monto debe ser un número válido", 
                Alert.AlertType.WARNING
            );
            return;
        }
        
        String descripcion = txtDescripcion.getText() != null ? txtDescripcion.getText() : "";
        CategoriaDto categoriaSeleccionada = cbCategoria.getValue();
        String idCategoria = categoriaSeleccionada != null ? categoriaSeleccionada.idCategoria() : "";
        
        boolean resultado = false;
        
        switch(tipoTransaccion) {
            case TransaccionConstantes.TIPO_DEPOSITO:
                CuentaDto cuentaDestino = cbCuentaDestino.getValue();
                if(cuentaDestino == null) {
                    mostrarMensaje(
                        "Cuenta requerida", 
                        "Seleccione cuenta", 
                        "Debe seleccionar una cuenta destino", 
                        Alert.AlertType.WARNING
                    );
                    return;
                }
                resultado = transaccionController.realizarDeposito(
                    cuentaDestino.idCuenta(), 
                    monto, 
                    descripcion, 
                    idCategoria
                );
                break;
                
            case TransaccionConstantes.TIPO_RETIRO:
                CuentaDto cuentaOrigen = cbCuentaOrigen.getValue();
                if(cuentaOrigen == null) {
                    mostrarMensaje(
                        "Cuenta requerida", 
                        "Seleccione cuenta", 
                        "Debe seleccionar una cuenta origen", 
                        Alert.AlertType.WARNING
                    );
                    return;
                }
                resultado = transaccionController.realizarRetiro(
                    cuentaOrigen.idCuenta(), 
                    monto, 
                    descripcion, 
                    idCategoria
                );
                break;
                
            case TransaccionConstantes.TIPO_TRANSFERENCIA:
                CuentaDto cuentaOrigenTransf = cbCuentaOrigen.getValue();
                CuentaDto cuentaDestinoTransf = cbCuentaDestino.getValue();
                if(cuentaOrigenTransf == null || cuentaDestinoTransf == null) {
                    mostrarMensaje(
                        "Cuentas requeridas", 
                        "Seleccione cuentas", 
                        "Debe seleccionar cuentas origen y destino", 
                        Alert.AlertType.WARNING
                    );
                    return;
                }
                if(cuentaOrigenTransf.idCuenta().equals(cuentaDestinoTransf.idCuenta())) {
                    mostrarMensaje(
                        "Cuentas iguales", 
                        "Error de selección", 
                        "Las cuentas origen y destino no pueden ser iguales", 
                        Alert.AlertType.WARNING
                    );
                    return;
                }
                resultado = transaccionController.realizarTransferencia(
                    cuentaOrigenTransf.idCuenta(), 
                    cuentaDestinoTransf.idCuenta(), 
                    monto, 
                    descripcion, 
                    idCategoria
                );
                break;
        }
        
        if(resultado) {
            cargarDatos();
            limpiarCampos();
            mostrarMensaje(
                "Transacción realizada", 
                "Éxito", 
                "La transacción se realizó correctamente", 
                Alert.AlertType.INFORMATION
            );
        } else {
            String mensajeError = "";
            switch(tipoTransaccion) {
                case TransaccionConstantes.TIPO_DEPOSITO:
                    mensajeError = TransaccionConstantes.ERROR_DEPOSITO;
                    break;
                case TransaccionConstantes.TIPO_RETIRO:
                    mensajeError = TransaccionConstantes.ERROR_RETIRO;
                    break;
                case TransaccionConstantes.TIPO_TRANSFERENCIA:
                    mensajeError = TransaccionConstantes.ERROR_TRANSFERENCIA;
                    break;
            }
            
            mostrarMensaje(
                "Error en transacción", 
                "No se pudo realizar", 
                mensajeError, 
                Alert.AlertType.ERROR
            );
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