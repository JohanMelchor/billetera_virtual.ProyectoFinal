package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.controller.CategoriaController;
import co.edu.uniquindio.controller.PresupuestoController;
import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.mapping.dto.PresupuestoDto;
import co.edu.uniquindio.Util.PresupuestoConstantes;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;
import java.util.UUID;

public class PresupuestoViewController {
    
    private PresupuestoController presupuestoController;
    private CategoriaController categoriaController;
    private ObservableList<PresupuestoDto> listaPresupuestos = FXCollections.observableArrayList();
    private ObservableList<CategoriaDto> listaCategorias = FXCollections.observableArrayList();
    private PresupuestoDto presupuestoSeleccionado;
    private String idUsuarioActual;
    
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
    private ComboBox<CategoriaDto> cbCategoria;
    
    @FXML
    private Button btnAgregarPresupuesto;
    
    @FXML
    private Button btnActualizarPresupuesto;
    
    @FXML
    private Button btnEliminarPresupuesto;
    
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
    void initialize() {
        presupuestoController = new PresupuestoController();
        categoriaController = new CategoriaController();
        
        initView();
    }
    
    public void inicializarConUsuario(String idUsuario) {
        this.idUsuarioActual = idUsuario;
        cargarDatos();
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
        
        // Generar automáticamente ID único al abrir la vista
        generarIdUnico();
    }
    
    private void cargarDatos() {
        if(idUsuarioActual != null && !idUsuarioActual.isEmpty()) {
            // Cargar presupuestos del usuario
            listaPresupuestos.clear();
            listaPresupuestos.addAll(presupuestoController.obtenerPresupuestosPorUsuario(idUsuarioActual));
            
            // Cargar categorías
            listaCategorias.clear();
            listaCategorias.addAll(categoriaController.obtenerCategorias());
        }
    }
    
    private void initDataBinding() {
        tcIdPresupuesto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().idPresupuesto()));
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombre()));
        tcMontoAsignado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().montoAsignado()));
        tcMontoGastado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().montoGastado()));
        tcSaldo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().saldo()));
        
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
        PresupuestoDto nuevoPresupuesto = crearPresupuestoDto();
        if(datosValidos(nuevoPresupuesto)) {
            if(presupuestoController.agregarPresupuesto(nuevoPresupuesto)) {
                cargarDatos();
                limpiarCampos();
                mostrarMensaje(
                    "Presupuesto agregado", 
                    "Éxito", 
                    PresupuestoConstantes.EXITO_AGREGAR_PRESUPUESTO, 
                    Alert.AlertType.INFORMATION
                );
                
                // Generar nuevo ID único para el siguiente presupuesto
                generarIdUnico();
            } else {
                mostrarMensaje(
                    "Error", 
                    "No se pudo agregar", 
                    PresupuestoConstantes.ERROR_AGREGAR_PRESUPUESTO, 
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
    }
    
    @FXML
    void onActualizarPresupuesto(ActionEvent event) {
        if(presupuestoSeleccionado != null) {
            PresupuestoDto presupuestoActualizado = crearPresupuestoDto();
            if(datosValidos(presupuestoActualizado)) {
                if(presupuestoController.actualizarPresupuesto(presupuestoActualizado)) {
                    cargarDatos();
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
        
        // Asegurar que los valores numéricos sean válidos
        String montoAsignado = txtMontoAsignado.getText();
        String montoGastado = txtMontoGastado.getText();
        String saldo = txtSaldo.getText();
        
        if(montoAsignado == null || montoAsignado.isEmpty()) {
            montoAsignado = "0.0";
        }
        
        if(montoGastado == null || montoGastado.isEmpty()) {
            montoGastado = "0.0";
        }
        
        if(saldo == null || saldo.isEmpty()) {
            saldo = "0.0";
        }
        
        return new PresupuestoDto(
            txtIdPresupuesto.getText(),
            txtNombre.getText(),
            montoAsignado,
            montoGastado,
            idCategoria,
            idUsuarioActual,
            saldo
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
        cbCategoria.setValue(null);
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
}