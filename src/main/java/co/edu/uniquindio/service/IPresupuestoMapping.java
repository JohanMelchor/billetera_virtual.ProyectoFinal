package co.edu.uniquindio.service;

import co.edu.uniquindio.mapping.dto.PresupuestoDto;
import co.edu.uniquindio.model.Presupuesto;

import java.util.List;

public interface IPresupuestoMapping {
    List<PresupuestoDto> getPresupuestoDto(List<Presupuesto> listaPresupuestos);
    PresupuestoDto presupuestoToPresupuestoDto(Presupuesto presupuesto);
    Presupuesto presupuestoDtoToPresupuesto(PresupuestoDto presupuestoDto);
}