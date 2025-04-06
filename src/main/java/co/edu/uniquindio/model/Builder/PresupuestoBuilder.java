package co.edu.uniquindio.model.Builder;
import co.edu.uniquindio.model.Presupuesto;


public class PresupuestoBuilder {
    private String idPresupuesto;
    private String nombre;
    private Double montoTotal;
    private Double montoGastado;

public PresupuestoBuilder IdPresupuesto(String idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
        return this;
    }
    public PresupuestoBuilder Nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }
    public PresupuestoBuilder MontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
        return this;
    }
    public PresupuestoBuilder MontoGastado(Double montoGastado) {
        this.montoGastado = montoGastado;
        return this;
    }
    public Presupuesto build() {
        return new Presupuesto(idPresupuesto, nombre, montoTotal, montoGastado);
    }
   
}
