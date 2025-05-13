package co.edu.uniquindio.model;

import java.time.LocalDateTime;
import java.util.List;

public class Reporte {
    private String idReporte;
    private String tipoReporte;
    private LocalDateTime fechaGeneracion;
    private Usuario usuario;
    private List<Transaccion> transacciones;
    private String contenido;

    public Reporte(String idReporte, String tipoReporte, LocalDateTime fechaGeneracion, 
                  Usuario usuario, List<Transaccion> transacciones, String contenido) {
        this.idReporte = idReporte;
        this.tipoReporte = tipoReporte;
        this.fechaGeneracion = fechaGeneracion;
        this.usuario = usuario;
        this.transacciones = transacciones;
        this.contenido = contenido;
    }

    public String getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}