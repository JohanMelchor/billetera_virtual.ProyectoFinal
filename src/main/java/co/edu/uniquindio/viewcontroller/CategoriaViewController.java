package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.factory.AlertaManagerFactory;
import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.service.IAlertaManager;
import co.edu.uniquindio.Util.CategoriaConstantes;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.UUID;

public class CategoriaViewController {
    
    private ObservableList<CategoriaDto> listaCategorias = FXCollections.observableArrayList();
    private CategoriaDto categoriaSeleccionada;
    private BilleteraFacade facade;
    private IAlertaManager alertaManager;
    
    @FXML
    private TextField txtIdCategoria;
    
    @FXML
    private TextField txtNombre;
    
    @FXML
    private TextArea txtDescripcion;
    
    @FXML
    private Button btnAgregarCategoria;
    
    @FXML
    private Button btnActualizarCategoria;
    
    @FXML
    private Button btnEliminarCategoria;

    @FXML
    private Button btnLimpiarCampos;
    
    @FXML
    private TableView<CategoriaDto> tableCategorias;
    
    @FXML
    private TableColumn<CategoriaDto, String> tcIdCategoria;
    
    @FXML
    private TableColumn<CategoriaDto, String> tcNombre;
    
    @FXML
    private TableColumn<CategoriaDto, String> tcDescripcion;
    
    @FXML
    void initialize() {
        facade = new BilleteraFacade();
        alertaManager = AlertaManagerFactory.crearManagerCompleto();
        
        initView();
        cargarCategorias();
    }
    
    private void initView() {
        initDataBinding();
        tableCategorias.getItems().clear();
        tableCategorias.setItems(listaCategorias);
        listenerSeleccion();
        
        // Generar automáticamente ID único al abrir la vista
        generarIdUnico();
    }
    
    private void cargarCategorias() {
        listaCategorias.clear();
        listaCategorias.addAll(facade.obtenerCategorias());
    }
    
    private void initDataBinding() {
        tcIdCategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().idCategoria()));
        tcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombre()));
        tcDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().descripcion()));
    }
    
    private void listenerSeleccion() {
        tableCategorias.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            categoriaSeleccionada = newSelection;
            mostrarInformacionCategoria(categoriaSeleccionada);
        });
    }
    
    private void mostrarInformacionCategoria(CategoriaDto categoriaDto) {
        if(categoriaDto != null) {
            txtIdCategoria.setText(categoriaDto.idCategoria());
            txtNombre.setText(categoriaDto.nombre());
            txtDescripcion.setText(categoriaDto.descripcion());
        }
    }
    
    @FXML
    void onAgregarCategoria(ActionEvent event) {
        CategoriaDto nuevaCategoria = crearCategoriaDto();
        if(datosValidos(nuevaCategoria)) {
            if(facade.agregarCategoria(nuevaCategoria)) {
                cargarCategorias();
                limpiarCampos();
                mostrarAlerta(
                    "Categoría agregada", 
                    "Éxito", 
                    CategoriaConstantes.EXITO_AGREGAR_CATEGORIA, 
                    Alert.AlertType.INFORMATION
                );
                
                // Generar nuevo ID único para la siguiente categoría
                generarIdUnico();
            } else {
                mostrarAlerta(
                    "Error", 
                    "No se pudo agregar", 
                    CategoriaConstantes.ERROR_AGREGAR_CATEGORIA, 
                    Alert.AlertType.ERROR
                );
            }
        } else {
            mostrarAlerta(
                "Campos incompletos", 
                "Datos incompletos", 
                CategoriaConstantes.ERROR_CAMPOS_VACIOS, 
                Alert.AlertType.WARNING
            );
        }
    }
    
    @FXML
    void onActualizarCategoria(ActionEvent event) {
        if(categoriaSeleccionada != null) {
            CategoriaDto categoriaActualizada = crearCategoriaDto();
            if(datosValidos(categoriaActualizada)) {
                if(facade.actualizarCategoria(categoriaActualizada)) {
                    cargarCategorias();
                    limpiarCampos();
                    mostrarAlerta(
                        "Categoría actualizada", 
                        "Éxito", 
                        CategoriaConstantes.EXITO_ACTUALIZAR_CATEGORIA, 
                        Alert.AlertType.INFORMATION
                    );
                    
                    // Generar nuevo ID único para la siguiente categoría
                    generarIdUnico();
                } else {
                    mostrarAlerta(
                        "Error", 
                        "No se pudo actualizar", 
                        CategoriaConstantes.ERROR_ACTUALIZAR_CATEGORIA, 
                        Alert.AlertType.ERROR
                    );
                }
            } else {
                mostrarAlerta(
                    "Campos incompletos", 
                    "Datos incompletos", 
                    CategoriaConstantes.ERROR_CAMPOS_VACIOS, 
                    Alert.AlertType.WARNING
                );
            }
        } else {
            mostrarAlerta(
                "Selección requerida", 
                "No hay selección", 
                "Debe seleccionar una categoría para actualizar", 
                Alert.AlertType.WARNING
            );
        }
    }
    
    @FXML
    void onEliminarCategoria(ActionEvent event) {
        if(categoriaSeleccionada != null) {
            boolean confirmacion = mostrarConfirmacion(
                "Confirmación de eliminación", 
                "¿Está seguro de que desea eliminar la categoría '" + categoriaSeleccionada.nombre() + "'?"
            );
            
            if(confirmacion) {
                if(facade.eliminarCategoria(categoriaSeleccionada.idCategoria())) {
                    listaCategorias.remove(categoriaSeleccionada);
                    limpiarCampos();
                    mostrarAlerta(
                        "Categoría eliminada", 
                        "Éxito", 
                        CategoriaConstantes.EXITO_ELIMINAR_CATEGORIA, 
                        Alert.AlertType.INFORMATION
                    );
                    
                    // Generar nuevo ID único para la siguiente categoría
                    generarIdUnico();
                } else {
                    mostrarAlerta(
                        "Error", 
                        "No se pudo eliminar", 
                        CategoriaConstantes.ERROR_ELIMINAR_CATEGORIA, 
                        Alert.AlertType.ERROR
                    );
                }
            }
        } else {
            mostrarAlerta("Selección requerida", 
                          "No hay selección", 
                          "Debe seleccionar una categoría para eliminar", 
                          Alert.AlertType.WARNING);
        }
    }
    
    private CategoriaDto crearCategoriaDto() {
        String descripcion = txtDescripcion.getText() != null ? txtDescripcion.getText() : "";
        
        return new CategoriaDto(
            txtIdCategoria.getText(),
            txtNombre.getText(),
            descripcion
        );
    }
    
    private boolean datosValidos(CategoriaDto categoriaDto) {
        return categoriaDto.idCategoria() != null && !categoriaDto.idCategoria().isEmpty() &&
               categoriaDto.nombre() != null && !categoriaDto.nombre().isEmpty();
    }
    
    private void limpiarCampos() {
        generarIdUnico();
        txtNombre.setText("");
        txtDescripcion.setText("");
        categoriaSeleccionada = null;
    }
    
    private void generarIdUnico() {
        txtIdCategoria.setText("CAT" + UUID.randomUUID().toString().substring(0, 8));
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
        tableCategorias.getSelectionModel().clearSelection();
    }
}