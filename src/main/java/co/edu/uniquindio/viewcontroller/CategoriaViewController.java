package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.factory.AlertaManagerFactory;
import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.service.IAlertaManager;
import co.edu.uniquindio.template.CategoriaCrudTemplate;
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
    private CategoriaCrudTemplate crudTemplate;
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

        crudTemplate = new CategoriaCrudTemplate(
            facade, alertaManager,
            txtIdCategoria, txtNombre, txtDescripcion,
            tableCategorias, this::cargarCategorias
        );
        
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
            crudTemplate.setCategoriaSeleccionada(newSelection);
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
    
    @FXML void onAgregarCategoria(ActionEvent event) { crudTemplate.agregar(); }
    @FXML void onActualizarCategoria(ActionEvent event) { crudTemplate.actualizar(); }
    @FXML void onEliminarCategoria(ActionEvent event) { crudTemplate.eliminar(); }
    
    private void limpiarCampos() {
        generarIdUnico();
        txtNombre.setText("");
        txtDescripcion.setText("");
        categoriaSeleccionada = null;
    }
    
    private void generarIdUnico() {
        txtIdCategoria.setText("CAT" + UUID.randomUUID().toString().substring(0, 8));
    }

    @FXML
    void onLimpiarCampos(ActionEvent event) {
        limpiarCampos();
        tableCategorias.getSelectionModel().clearSelection();
    }
}