package co.edu.uniquindio.template;

import co.edu.uniquindio.service.IAlertaManager;
import javafx.scene.control.Alert;

public abstract class CrudTemplate<T> {
    protected IAlertaManager alertaManager;
    
    public CrudTemplate(IAlertaManager alertaManager) {
        this.alertaManager = alertaManager;
    }
    
    // ==================== TEMPLATE METHODS ====================
    public final void agregar() {
        T nuevoObjeto = crearObjeto();
        
        if (!validarDatos(nuevoObjeto)) {
            mostrarResultado(ResultadoCRUD.camposIncompletos());
            return;
        }
        
        boolean exito = ejecutarAgregar(nuevoObjeto);
        ResultadoCRUD resultado = exito ? 
            ResultadoCRUD.exito(TipoOperacion.AGREGAR, getNombreEntidad()) :
            ResultadoCRUD.error(TipoOperacion.AGREGAR, getNombreEntidad());
        
        if (exito) {
            postProcesarExito(TipoOperacion.AGREGAR);
        }
        
        mostrarResultado(resultado);
    }
    
    /**
     * Template method para operación ACTUALIZAR
     */
    public final void actualizar() {
        if (!tieneSeleccion()) {
            mostrarResultado(ResultadoCRUD.seleccionRequerida(getNombreEntidad()));
            return;
        }
        
        T objetoActualizado = crearObjeto();
        
        if (!validarDatos(objetoActualizado)) {
            mostrarResultado(ResultadoCRUD.camposIncompletos());
            return;
        }
        
        boolean exito = ejecutarActualizar(objetoActualizado);
        ResultadoCRUD resultado = exito ? 
            ResultadoCRUD.exito(TipoOperacion.ACTUALIZAR, getNombreEntidad()) :
            ResultadoCRUD.error(TipoOperacion.ACTUALIZAR, getNombreEntidad());
        
        if (exito) {
            postProcesarExito(TipoOperacion.ACTUALIZAR);
        }
        
        mostrarResultado(resultado);
    }
    
    /**
     * Template method para operación ELIMINAR
     */
    public final void eliminar() {
        if (!tieneSeleccion()) {
            mostrarResultado(ResultadoCRUD.seleccionRequerida(getNombreEntidad()));
            return;
        }
        
        String mensajeConfirmacion = construirMensajeConfirmacion();
        if (!mostrarConfirmacion(mensajeConfirmacion)) {
            mostrarResultado(ResultadoCRUD.confirmacionCancelada());
            return;
        }
        
        boolean exito = ejecutarEliminar();
        ResultadoCRUD resultado = exito ? 
            ResultadoCRUD.exito(TipoOperacion.ELIMINAR, getNombreEntidad()) :
            ResultadoCRUD.error(TipoOperacion.ELIMINAR, getNombreEntidad());
        
        if (exito) {
            postProcesarExito(TipoOperacion.ELIMINAR);
        }
        
        mostrarResultado(resultado);
    }
    
    // ==================== MÉTODOS ABSTRACTOS (que deben implementar las subclases) ====================
    
    /**
     * Crear el objeto DTO desde los campos de la interfaz
     */
    protected abstract T crearObjeto();
    
    /**
     * Validar que los datos del objeto sean correctos
     */
    protected abstract boolean validarDatos(T objeto);
    
    /**
     * Ejecutar la operación de agregar en el facade
     */
    protected abstract boolean ejecutarAgregar(T objeto);
    
    /**
     * Ejecutar la operación de actualizar en el facade
     */
    protected abstract boolean ejecutarActualizar(T objeto);
    
    /**
     * Ejecutar la operación de eliminar en el facade
     */
    protected abstract boolean ejecutarEliminar();
    
    /**
     * Verificar si hay una selección en la tabla
     */
    protected abstract boolean tieneSeleccion();
    
    /**
     * Obtener el nombre de la entidad para mensajes
     */
    protected abstract String getNombreEntidad();
    
    /**
     * Recargar datos después de una operación exitosa
     */
    protected abstract void recargarDatos();
    
    /**
     * Limpiar campos del formulario
     */
    protected abstract void limpiarCampos();
    
    // ==================== MÉTODOS CON IMPLEMENTACIÓN POR DEFECTO (pueden sobrescribirse) ====================
    
    /**
     * Construir mensaje de confirmación para eliminar
     */
    protected String construirMensajeConfirmacion() {
        return "¿Está seguro de que desea eliminar el " + getNombreEntidad().toLowerCase() + " seleccionado?";
    }
    
    /**
     * Mostrar confirmación al usuario
     */
    protected boolean mostrarConfirmacion(String mensaje) {
        return alertaManager.mostrarConfirmacion("Confirmación de eliminación", mensaje);
    }
    
    /**
     * Procesar acciones adicionales después de una operación exitosa
     */
    protected void postProcesarExito(TipoOperacion operacion) {
        recargarDatos();
        limpiarCampos();
        
        // Acciones adicionales según el tipo de operación
        switch (operacion) {
            case AGREGAR:
                postAgregar();
                break;
            case ACTUALIZAR:
                postActualizar();
                break;
            case ELIMINAR:
                postEliminar();
                break;
        }
    }
    
    /**
     * Acciones después de agregar (por defecto no hace nada)
     */
    protected void postAgregar() {
        // Implementación por defecto vacía
    }
    
    /**
     * Acciones después de actualizar (por defecto no hace nada)
     */
    protected void postActualizar() {
        // Implementación por defecto vacía
    }
    
    /**
     * Acciones después de eliminar (por defecto no hace nada)
     */
    protected void postEliminar() {
        // Implementación por defecto vacía
    }
    
    /**
     * Mostrar resultado de la operación al usuario
     */
    protected void mostrarResultado(ResultadoCRUD resultado) {
        Alert.AlertType tipoAlerta;
        
        switch (resultado.getResultado()) {
            case EXITO:
                tipoAlerta = Alert.AlertType.INFORMATION;
                break;
            case ERROR:
                tipoAlerta = Alert.AlertType.ERROR;
                break;
            case CAMPOS_INCOMPLETOS:
            case SELECCION_REQUERIDA:
                tipoAlerta = Alert.AlertType.WARNING;
                break;
            default:
                tipoAlerta = Alert.AlertType.INFORMATION;
                break;
        }
        
        alertaManager.mostrarAlerta(
            resultado.getTitulo(),
            resultado.getTitulo(),
            resultado.getMensaje(),
            tipoAlerta
        );
    }
}
