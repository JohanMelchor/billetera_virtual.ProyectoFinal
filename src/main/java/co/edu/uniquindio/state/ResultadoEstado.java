package co.edu.uniquindio.state;

public class ResultadoEstado {
    private final boolean permitido;
    private final String mensaje;
    private final String razon;
    
    public ResultadoEstado(boolean permitido, String mensaje, String razon) {
        this.permitido = permitido;
        this.mensaje = mensaje;
        this.razon = razon;
    }
    
    public static ResultadoEstado permitir(String mensaje) {
        return new ResultadoEstado(true, mensaje, null);
    }
    
    public static ResultadoEstado denegar(String mensaje, String razon) {
        return new ResultadoEstado(false, mensaje, razon);
    }
    
    // Getters
    public boolean isPermitido() { return permitido; }
    public String getMensaje() { return mensaje; }
    public String getRazon() { return razon; }
}
