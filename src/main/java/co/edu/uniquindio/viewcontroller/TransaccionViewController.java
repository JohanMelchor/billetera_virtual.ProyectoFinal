package co.edu.uniquindio.viewcontroller;


import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.mapping.dto.CuentaDto;
import co.edu.uniquindio.mapping.dto.PresupuestoDto;
import co.edu.uniquindio.mapping.dto.TransaccionDto;
import co.edu.uniquindio.service.IAlertaManager;
import co.edu.uniquindio.Util.TransaccionConstantes;
import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.factory.AlertaManagerFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class TransaccionViewController {
    
    private ObservableList<TransaccionDto> listaTransacciones = FXCollections.observableArrayList();
    private ObservableList<CuentaDto> listaCuentas = FXCollections.observableArrayList();
    private ObservableList<CategoriaDto> listaCategorias = FXCollections.observableArrayList();
    private ObservableList<PresupuestoDto> listaPresupuestos = FXCollections.observableArrayList();
    private String idUsuarioActual;
    private boolean esAdmin = false;
    private BilleteraFacade facade;
    private IAlertaManager alertaManager;
    
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
    private Label lblMontoUsuario;

    @FXML
    private Label lbltipoUsuario;

    @FXML
    private Label lblCategoriaUsuario;

    @FXML
    private Label lblDescripcionUsuario;
    
    @FXML
    private TextArea txtDescripcion;
    
    @FXML
    private Button btnTransaccion;

    @FXML 
    private Label lblPresupuesto;

    @FXML 
    private ComboBox<PresupuestoDto> cbPresupuesto;

    @FXML 
    private TableColumn<TransaccionDto, String> tcPresupuesto;
    
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
    private Label lblTransaccionesAdmin;

    @FXML
    private ComboBox<String> cbTipoAdmin;

    @FXML
    private Label lblTipoAdmin;

    @FXML
    private Label lblCuentaAfectada;

    @FXML
    private Label lblMontoAdmin;

    @FXML
    private Label lblJustificacion;

    @FXML
    private ComboBox<CuentaDto> cbCuentaAfectada;

    @FXML
    private TextField txtMontoAdmin;

    @FXML
    private TextArea txtJustificacion;

    @FXML
    private Button btnCrearTransaccionAdmin;
    
    @FXML
    void initialize() {
        facade = new BilleteraFacade();
        alertaManager = AlertaManagerFactory.crearManagerCompleto();
        
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

        cbCuentaOrigen.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarPresupuestosDeCuenta(newVal.idCuenta());
            } else {
                listaPresupuestos.clear();
            }
        });

        cbTipoTransaccion.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            actualizarVisibilidadCampos(newVal);
        });
        
        initView();
    }
    
    public void inicializarVista(String idUsuario, boolean esAdmin) {
        this.esAdmin = esAdmin;
        if (esAdmin) {
            cargarTodasTransacciones();
            habilitarVistaAdministrador();
        } else {
            this.idUsuarioActual = idUsuario;
            cargarDatos();
            habilitarVistaUsuario();
        }
    }

    private void habilitarVistaAdministrador() {
        // Cargar todas las cuentas para el admin
        listaCuentas.setAll(facade.obtenerTodasCuentas());
        listaCategorias.setAll(facade.obtenerCategorias());
        
        // Configurar elementos administrativos
        if (lblTransaccionesAdmin != null) {
            lblTransaccionesAdmin.setVisible(true);
        }
        
        if (cbTipoAdmin != null) {
            cbTipoAdmin.setVisible(true);
            cbTipoAdmin.getItems().addAll(
                TransaccionConstantes.TIPO_AJUSTE_POSITIVO,
                TransaccionConstantes.TIPO_AJUSTE_NEGATIVO,
                TransaccionConstantes.TIPO_DEPOSITO_INICIAL,
                TransaccionConstantes.TIPO_BONIFICACION,
                TransaccionConstantes.TIPO_PENALIZACION
            );
        }
        
        if (cbCuentaAfectada != null) {
            cbCuentaAfectada.setVisible(true);
            cbCuentaAfectada.setItems(FXCollections.observableArrayList(listaCuentas));
            cbCuentaAfectada.setConverter(new javafx.util.StringConverter<CuentaDto>() {
                @Override
                public String toString(CuentaDto cuenta) {
                    if (cuenta != null) {
                        // Buscar el usuario de la cuenta para mostrarlo
                        String nombreUsuario = "Desconocido";
                        try {
                            co.edu.uniquindio.controller.UsuarioController usuarioController = 
                                new co.edu.uniquindio.controller.UsuarioController();
                            co.edu.uniquindio.mapping.dto.UsuarioDto usuario = 
                                usuarioController.buscarUsuarioPorId(cuenta.idUsuario());
                            if (usuario != null) {
                                nombreUsuario = usuario.nombreCompleto();
                            }
                        } catch (Exception e) {
                            nombreUsuario = "Error al cargar";
                        }
                        return cuenta.nombreBanco() + " - " + cuenta.numeroCuenta() + 
                            " (" + nombreUsuario + ")";
                    }
                    return "";
                }
                
                @Override
                public CuentaDto fromString(String string) {
                    return null;
                }
            });
        }
        txtMontoAdmin.setVisible(true);
        txtJustificacion.setVisible(true);
        btnCrearTransaccionAdmin.setVisible(true);
        lblTipoAdmin.setVisible(true);
        lblCuentaAfectada.setVisible(true);
        lblMontoAdmin.setVisible(true);
        lblJustificacion.setVisible(true);
        //desactivar campos de usuario
        lblCuentaOrigen.setVisible(false);
        cbCuentaOrigen.setVisible(false);
        lblCuentaDestino.setVisible(false);
        cbCuentaDestino.setVisible(false);
        lblPresupuesto.setVisible(false);
        cbPresupuesto.setVisible(false);
        lblMontoUsuario.setVisible(false);
        txtMonto.setVisible(false);
        lbltipoUsuario.setVisible(false);
        cbTipoTransaccion.setVisible(false);
        lblCategoriaUsuario.setVisible(false);
        cbCategoria.setVisible(false);
        lblDescripcionUsuario.setVisible(false);
        txtDescripcion.setVisible(false);
        btnTransaccion.setVisible(false);
    }

    private void habilitarVistaUsuario() {
        lblTransaccionesAdmin.setVisible(false);
        cbTipoAdmin.setVisible(false);
        cbCuentaAfectada.setVisible(false);
        btnTransaccion.setVisible(true);
        txtMontoAdmin.setVisible(false);
        txtJustificacion.setVisible(false);
        btnCrearTransaccionAdmin.setVisible(false);
    }

    private void cargarTodasTransacciones() {
        listaTransacciones.clear();
        listaTransacciones.addAll(facade.obtenerTodasTransacciones());
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
        if (esAdmin) {
            // Admin: cargar todas las cuentas y transacciones
            listaCuentas.setAll(facade.obtenerTodasCuentas());
            listaCategorias.setAll(facade.obtenerCategorias());
            listaTransacciones.setAll(facade.obtenerTodasTransacciones());
        } else if (idUsuarioActual != null) {
            // Usuario: cargar solo sus datos
            listaCuentas.setAll(facade.obtenerCuentasPorUsuario(idUsuarioActual));
            listaCategorias.setAll(facade.obtenerCategorias());
            listaTransacciones.setAll(facade.obtenerTransaccionesPorUsuario(idUsuarioActual));
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
        if (tcPresupuesto != null) {
            tcPresupuesto.setCellValueFactory(cellData -> {
                String descripcion = cellData.getValue().descripcion();
                if (descripcion != null && descripcion.contains("(Presupuesto: ")) {
                    int start = descripcion.indexOf("(Presupuesto: ") + 14;
                    int end = descripcion.indexOf(")", start);
                    if (end > start) {
                        return new SimpleStringProperty(descripcion.substring(start, end));
                    }
                }
                return new SimpleStringProperty("");
            });
        }
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
                lblCuentaOrigen.setVisible(true);
                cbCuentaOrigen.setVisible(true);
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
            mostrarAlerta("Error", "Seleccione un tipo de operación" , "Seleccione un tipo de operación", Alert.AlertType.ERROR);
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
                        mostrarAlerta("Error", "Seleccione cuenta destino", "Seleccione cuenta destino para la operación", Alert.AlertType.ERROR);
                        return;
                    }
                    resultado = facade.depositoCuenta(
                        cuentaDestino.idCuenta(), monto, descripcion, idCategoria);
                break;

                case TransaccionConstantes.TIPO_DEPOSITO_PRESUPUESTO:
                    CuentaDto cuentaOrigen = cbCuentaOrigen.getValue();
                    PresupuestoDto presupuestoDestino = cbPresupuesto.getValue();
                    if (cuentaOrigen == null || presupuestoDestino == null) {
                        mostrarAlerta("Error", "Seleccione cuenta y presupuesto", "Seleccione cuenta y presupuesto", Alert.AlertType.ERROR);
                        return;
                    }
                    resultado = facade.depositoPresupuesto(
                        cuentaOrigen.idCuenta(), presupuestoDestino.idPresupuesto(), monto, descripcion, idCategoria);
                    break;
                    
                case TransaccionConstantes.TIPO_RETIRO_CUENTA:
                    cuentaOrigen = cbCuentaOrigen.getValue();
                    if (cuentaOrigen == null) {
                        mostrarAlerta("Error","Seleccione cuenta origen",  "Seleccione cuenta de origen para la operacion", Alert.AlertType.ERROR);
                        return;
                    }
                    resultado = facade.retiroPorCuenta(
                        cuentaOrigen.idCuenta(), monto, descripcion, idCategoria);
                    break;
                    
                case TransaccionConstantes.TIPO_RETIRO_PRESUPUESTO:
                    cuentaOrigen = cbCuentaOrigen.getValue();
                    PresupuestoDto presupuesto = cbPresupuesto.getValue();
                    if (cuentaOrigen == null || presupuesto == null) {
                        mostrarAlerta("Error","Seleccione cuenta y presupuesto", "Seleccione cuenta y presupuesto", Alert.AlertType.ERROR);
                        return;
                    }
                    resultado = facade.retiroPorPresupuesto(
                        cuentaOrigen.idCuenta(), presupuesto.idPresupuesto(), monto, descripcion, idCategoria);
                    break;
                    
                case TransaccionConstantes.TIPO_TRANSFERENCIA:
                    CuentaDto origen = cbCuentaOrigen.getValue();
                    CuentaDto destino = cbCuentaDestino.getValue();
                    if (origen == null || destino == null) {
                        mostrarAlerta("Error", "Seleccione cuentas de origen y destino", "Seleccione cuentas de origen y destino", Alert.AlertType.ERROR);
                        return;
                    }
                    if (origen.idCuenta().equals(destino.idCuenta())) {
                        mostrarAlerta("Error", "Las cuentas de origen y destino no pueden ser iguales", "Seleccione cuentas diferentes", Alert.AlertType.ERROR);
                        return;
                    }
                    resultado = facade.realizarTransferencia(
                        origen.idCuenta(), 
                        destino.idCuenta(), 
                        monto, 
                        descripcion, 
                        idCategoria);
                    break;
            }
            
            if (resultado) {
                mostrarAlerta("Éxito","Transacción realizada", "Operación realizada correctamente", Alert.AlertType.INFORMATION);
                cargarDatos();
                limpiarCampos();
            } else {
                mostrarAlerta("Error", "Error al realizar la operación", "No se pudo completar la operación", Alert.AlertType.ERROR);
            }
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error","Error en el formato del monto",  "Ingrese un monto válido", Alert.AlertType.ERROR);
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
    
    private void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        alertaManager.mostrarAlerta(titulo, header, contenido, tipo);
    }

    private void cargarPresupuestosDeCuenta(String idCuenta) {
        listaPresupuestos.clear();
        listaPresupuestos.addAll(facade.obtenerPresupuestosPorCuenta(idCuenta));
    }

    @FXML
    void onCrearTransaccionAdmin(ActionEvent event) {
        String tipoTransaccion = cbTipoAdmin.getValue();
        CuentaDto cuentaAfectada = cbCuentaAfectada.getValue();
        String montoStr = txtMontoAdmin.getText();
        String justificacion = txtJustificacion.getText();
        
        // Validaciones
        if (tipoTransaccion == null || cuentaAfectada == null || 
            montoStr == null || montoStr.isEmpty() || 
            justificacion == null || justificacion.trim().isEmpty()) {
            mostrarAlerta("Error", "Campos incompletos", 
                TransaccionConstantes.ERROR_CAMPOS_VACIOS, Alert.AlertType.WARNING);
            return;
        }
        
        try {
            double monto = Double.parseDouble(montoStr);
            
            if (monto <= 0) {
                mostrarAlerta("Error", "Monto inválido", 
                    "El monto debe ser mayor a cero", Alert.AlertType.ERROR);
                return;
            }
            
            // Crear descripción detallada
            String descripcionCompleta = String.format(
                "%s - Justificación: %s", 
                tipoTransaccion, justificacion.trim()
            );
            
            boolean resultado = false;
            
            switch (tipoTransaccion) {
                case TransaccionConstantes.TIPO_AJUSTE_POSITIVO:
                case TransaccionConstantes.TIPO_DEPOSITO_INICIAL:
                case TransaccionConstantes.TIPO_BONIFICACION:
                    // Agregar dinero a la cuenta
                    resultado = facade.depositoCuenta(
                        cuentaAfectada.idCuenta(), monto, descripcionCompleta, null
                    );
                    break;
                    
                case TransaccionConstantes.TIPO_AJUSTE_NEGATIVO:
                case TransaccionConstantes.TIPO_PENALIZACION:
                    // Quitar dinero de la cuenta
                    resultado = facade.retiroPorCuenta(
                        cuentaAfectada.idCuenta(), monto, descripcionCompleta, null
                    );
                    break;
            }
            
            if (resultado) {
                mostrarAlerta("Éxito", "Transacción administrativa creada", 
                    TransaccionConstantes.EXITO_TRANSACCION_ADMIN, Alert.AlertType.INFORMATION);
                cargarTodasTransacciones(); // Recargar tabla
                limpiarCamposAdmin();
            } else {
                mostrarAlerta("Error", "No se pudo crear la transacción", 
                    TransaccionConstantes.ERROR_TRANSACCION_ADMIN, Alert.AlertType.ERROR);
            }
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Monto inválido", 
                "Ingrese un monto válido (solo números)", Alert.AlertType.ERROR);
        }
    }

    private void limpiarCamposAdmin() {
        cbTipoAdmin.setValue(null);
        cbCuentaAfectada.setValue(null);
        txtMontoAdmin.setText("");
        txtJustificacion.setText("");
    }
    
}