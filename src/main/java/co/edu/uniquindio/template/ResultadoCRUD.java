package co.edu.uniquindio.template;

public class ResultadoCRUD {
    private final ResultadoOperacion resultado;
    private final String mensaje;
    private final String titulo;
    private final TipoOperacion operacion;
    
    public ResultadoCRUD(ResultadoOperacion resultado, String titulo, String mensaje, TipoOperacion operacion) {
        this.resultado = resultado;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.operacion = operacion;
    }
    
    // Factory methods para crear resultados comunes
    public static ResultadoCRUD exito(TipoOperacion operacion, String entidad) {
        String titulo = "";
        String mensaje = "";
        
        switch (operacion) {
            case AGREGAR:
                titulo = entidad + " agregado";
                mensaje = "El " + entidad.toLowerCase() + " fue agregado correctamente";
                break;
            case ACTUALIZAR:
                titulo = entidad + " actualizado";
                mensaje = "El " + entidad.toLowerCase() + " fue actualizado correctamente";
                break;
            case ELIMINAR:
                titulo = entidad + " eliminado";
                mensaje = "El " + entidad.toLowerCase() + " fue eliminado correctamente";
                break;
        }
        
        return new ResultadoCRUD(ResultadoOperacion.EXITO, titulo, mensaje, operacion);
    }
    
    public static ResultadoCRUD error(TipoOperacion operacion, String entidad) {
        String titulo = "Error";
        String mensaje = "";
        
        switch (operacion) {
            case AGREGAR:
                mensaje = "No se pudo agregar el " + entidad.toLowerCase();
                break;
            case ACTUALIZAR:
                mensaje = "No se pudo actualizar el " + entidad.toLowerCase();
                break;
            case ELIMINAR:
                mensaje = "No se pudo eliminar el " + entidad.toLowerCase();
                break;
        }
        
        return new ResultadoCRUD(ResultadoOperacion.ERROR, titulo, mensaje, operacion);
    }
    
    public static ResultadoCRUD camposIncompletos() {
        return new ResultadoCRUD(
            ResultadoOperacion.CAMPOS_INCOMPLETOS,
            "Campos incompletos",
            "Los datos del formulario están incompletos",
            null
        );
    }
    
    public static ResultadoCRUD seleccionRequerida(String entidad) {
        return new ResultadoCRUD(
            ResultadoOperacion.SELECCION_REQUERIDA,
            "Selección requerida",
            "Debe seleccionar un " + entidad.toLowerCase() + " de la tabla",
            null
        );
    }
    
    public static ResultadoCRUD confirmacionCancelada() {
        return new ResultadoCRUD(
            ResultadoOperacion.CONFIRMACION_CANCELADA,
            "Operación cancelada",
            "La operación fue cancelada por el usuario",
            null
        );
    }
    
    // Getters
    public ResultadoOperacion getResultado() { return resultado; }
    public String getMensaje() { return mensaje; }
    public String getTitulo() { return titulo; }
    public TipoOperacion getOperacion() { return operacion; }
    
    public boolean esExitoso() { return resultado == ResultadoOperacion.EXITO; }
}
