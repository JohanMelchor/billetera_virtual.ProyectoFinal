package co.edu.uniquindio.model.Builder;
import co.edu.uniquindio.model.Transaccion;

import java.time.LocalDate;

public class TransaccionBuilder {
    private String idTransaccion;
    private LocalDate fecha;
    private String tipo;
    private Double monto;
    private String descripcion;

    public TransaccionBuilder IdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
        return this;
    }
    public TransaccionBuilder Fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }
    public TransaccionBuilder Tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }
    public TransaccionBuilder Monto(Double monto) {
        this.monto = monto;
        return this;
    }
    public TransaccionBuilder Descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }
    public Transaccion build() {
        return new Transaccion(idTransaccion, fecha, tipo, monto, descripcion);
    }

}