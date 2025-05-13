package co.edu.uniquindio.mapping.mappers;

import co.edu.uniquindio.mapping.dto.PresupuestoDto;
import co.edu.uniquindio.model.Categoria;
import co.edu.uniquindio.model.Presupuesto;
import co.edu.uniquindio.model.Usuario;
import co.edu.uniquindio.service.IPresupuestoMapping;

import java.util.ArrayList;
import java.util.List;

public class PresupuestoMappingImpl implements IPresupuestoMapping {
    // CHANGED: Removed direct field initialization that causes circular dependency
    // ModelFactory modelFactory = ModelFactory.getInstance();
    
    @Override
    public List<PresupuestoDto> getPresupuestoDto(List<Presupuesto> listaPresupuestos) {
        if(listaPresupuestos == null) return null;
        
        List<PresupuestoDto> listaPresupuestosDto = new ArrayList<>(listaPresupuestos.size());
        for (Presupuesto presupuesto : listaPresupuestos) {
            listaPresupuestosDto.add(presupuestoToPresupuestoDto(presupuesto));
        }
        return listaPresupuestosDto;
    }

    @Override
    public PresupuestoDto presupuestoToPresupuestoDto(Presupuesto presupuesto) {
        String idCategoria = presupuesto.getCategoria() != null ? 
                             presupuesto.getCategoria().getIdCategoria() : "";
        
        return new PresupuestoDto(
                presupuesto.getIdPresupuesto(),
                presupuesto.getNombre(),
                String.valueOf(presupuesto.getMontoAsignado()),
                String.valueOf(presupuesto.getMontoGastado()),
                idCategoria,
                presupuesto.getUsuario().getIdUsuario(),
                String.valueOf(presupuesto.getSaldo())
        );
    }

    @Override
    public Presupuesto presupuestoDtoToPresupuesto(PresupuestoDto dto) {
        // CHANGED: Get ModelFactory instance only when needed
        var modelFactory = co.edu.uniquindio.factory.ModelFactory.getInstance();
        
        Usuario usuario = modelFactory.buscarUsuarioPorId(dto.idUsuario());
        Categoria categoria = !dto.idCategoria().isEmpty() ? 
                             modelFactory.buscarCategoriaPorId(dto.idCategoria()) : null;
        
        return new Presupuesto(
                dto.idPresupuesto(),
                dto.nombre(),
                Double.parseDouble(dto.montoAsignado()),
                Double.parseDouble(dto.montoGastado()),
                categoria,
                usuario,
                Double.parseDouble(dto.saldo())
        );
    }
}