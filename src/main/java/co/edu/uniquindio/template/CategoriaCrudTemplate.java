package co.edu.uniquindio.template;

import java.util.UUID;

import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.service.IAlertaManager;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CategoriaCrudTemplate extends CrudTemplate<CategoriaDto> {
    
    private final BilleteraFacade facade;
    private final TextField txtIdCategoria;
    private final TextField txtNombre;
    private final TextArea txtDescripcion;
    private final TableView<CategoriaDto> tableCategorias;
    private final Runnable recargarCallback;
    private CategoriaDto categoriaSeleccionada;
    
    public CategoriaCrudTemplate(
            BilleteraFacade facade,
            IAlertaManager alertaManager,
            TextField txtIdCategoria,
            TextField txtNombre,
            TextArea txtDescripcion,
            TableView<CategoriaDto> tableCategorias,
            Runnable recargarCallback) {
        
        super(alertaManager);
        this.facade = facade;
        this.txtIdCategoria = txtIdCategoria;
        this.txtNombre = txtNombre;
        this.txtDescripcion = txtDescripcion;
        this.tableCategorias = tableCategorias;
        this.recargarCallback = recargarCallback;
    }
    
    public void setCategoriaSeleccionada(CategoriaDto categoria) {
        this.categoriaSeleccionada = categoria;
    }
    
    @Override
    protected CategoriaDto crearObjeto() {
        String descripcion = txtDescripcion.getText() != null ? txtDescripcion.getText() : "";
        return new CategoriaDto(
            txtIdCategoria.getText(),
            txtNombre.getText(),
            descripcion
        );
    }
    
    @Override
    protected boolean validarDatos(CategoriaDto categoria) {
        return categoria.idCategoria() != null && !categoria.idCategoria().isEmpty() &&
               categoria.nombre() != null && !categoria.nombre().isEmpty();
    }
    
    @Override
    protected boolean ejecutarAgregar(CategoriaDto categoria) {
        return facade.agregarCategoria(categoria);
    }
    
    @Override
    protected boolean ejecutarActualizar(CategoriaDto categoria) {
        return facade.actualizarCategoria(categoria);
    }
    
    @Override
    protected boolean ejecutarEliminar() {
        return facade.eliminarCategoria(categoriaSeleccionada.idCategoria());
    }
    
    @Override
    protected boolean tieneSeleccion() {
        return categoriaSeleccionada != null;
    }
    
    @Override
    protected String getNombreEntidad() {
        return "Categoría";
    }
    
    @Override
    protected void recargarDatos() {
        recargarCallback.run();
    }
    
    @Override
    protected void limpiarCampos() {
        generarIdUnico();
        txtNombre.setText("");
        txtDescripcion.setText("");
        categoriaSeleccionada = null;
        tableCategorias.getSelectionModel().clearSelection();
    }
    
    @Override
    protected String construirMensajeConfirmacion() {
        return "¿Está seguro de que desea eliminar la categoría '" + 
               categoriaSeleccionada.nombre() + "'?";
    }
    
    @Override
    protected void postAgregar() {
        generarIdUnico();
    }
    
    @Override
    protected void postActualizar() {
        generarIdUnico();
    }
    
    @Override
    protected void postEliminar() {
        generarIdUnico();
    }
    
    private void generarIdUnico() {
        txtIdCategoria.setText("CAT" + UUID.randomUUID().toString().substring(0, 8));
    }
}
