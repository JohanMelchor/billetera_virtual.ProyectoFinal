package co.edu.uniquindio.controller;

import co.edu.uniquindio.factory.ModelFactory;
import co.edu.uniquindio.mapping.dto.PresupuestoDto;

import java.util.List;

public class PresupuestoController {
    ModelFactory modelFactory;
    
    public PresupuestoController() {
        modelFactory = ModelFactory.getInstance();
    }
    
    public List<PresupuestoDto> obtenerPresupuestos() {
        return modelFactory.obtenerPresupuestos();
    }
    
    public List<PresupuestoDto> obtenerPresupuestosPorUsuario(String idUsuario) {
        return modelFactory.obtenerPresupuestosPorUsuario(idUsuario);
    }
    
    public boolean agregarPresupuesto(PresupuestoDto presupuestoDto) {
        return modelFactory.agregarPresupuesto(presupuestoDto);
    }
    
    public boolean actualizarPresupuesto(PresupuestoDto presupuestoDto) {
        return modelFactory.actualizarPresupuesto(presupuestoDto);
    }
    
    public boolean eliminarPresupuesto(String idPresupuesto) {
        return modelFactory.eliminarPresupuesto(idPresupuesto);
    }

    public boolean agregarPresupuestoACuenta(String idCuenta, PresupuestoDto presupuestoDto) {
        return modelFactory.agregarPresupuestoACuenta(idCuenta, presupuestoDto);
    }

    public List<PresupuestoDto> obtenerPresupuestosPorCuenta(String idCuenta) {
        return modelFactory.obtenerPresupuestosPorCuenta(idCuenta);
    }
}
