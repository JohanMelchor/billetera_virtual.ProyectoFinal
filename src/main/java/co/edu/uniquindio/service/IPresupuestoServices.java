package co.edu.uniquindio.service;

import co.edu.uniquindio.model.Presupuesto;

public interface IPresupuestoServices {
    boolean crearPresupuesto(Presupuesto presupuesto);
    boolean eliminarPresupuesto(String idPresupuesto);
    boolean actualizarPresupuesto(Presupuesto presupuesto);
}