package co.edu.uniquindio.state;

public enum TipoEstadoCuenta {
    ACTIVA("Activa"),
    BLOQUEADA("Bloqueada"),
    CERRADA("Cerrada"),
    SUSPENDIDA("Suspendida");
    
    private final String descripcion;
    
    TipoEstadoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
