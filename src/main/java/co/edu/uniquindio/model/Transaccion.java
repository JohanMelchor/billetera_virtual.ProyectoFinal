package co.edu.uniquindio.model;

import co.edu.uniquindio.model.Builder.TransaccionBuilder;
import java.time.LocalDate;

public class Transaccion {
    private String idTransaccion;
    private LocalDate fecha;
    private String tipo;
    private Double monto;
    private String descripcion;

    public Transaccion() {
    }

    public Transaccion(String idTransaccion, LocalDate fecha, String tipo, Double monto, String descripcion) {
        this.idTransaccion = idTransaccion;
        this.fecha = fecha;
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    public static TransaccionBuilder builder() {
        return new TransaccionBuilder();
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }
    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public Double getMonto() {
        return monto;
    }
    public void setMonto(Double monto) {
        this.monto = monto;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
