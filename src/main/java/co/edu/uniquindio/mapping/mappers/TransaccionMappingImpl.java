package co.edu.uniquindio.mapping.mappers;

import co.edu.uniquindio.mapping.dto.TransaccionDto;
import co.edu.uniquindio.model.Categoria;
import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.model.Transaccion;
import co.edu.uniquindio.service.ITransaccionMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransaccionMappingImpl implements ITransaccionMapping {
    // CHANGED: Removed direct field initialization that causes circular dependency
    // ModelFactory modelFactory = ModelFactory.getInstance();
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public List<TransaccionDto> getTransaccionDto(List<Transaccion> listaTransacciones) {
        if(listaTransacciones == null) return null;
        
        List<TransaccionDto> listaTransaccionesDto = new ArrayList<>(listaTransacciones.size());
        for (Transaccion transaccion : listaTransacciones) {
            listaTransaccionesDto.add(transaccionToTransaccionDto(transaccion));
        }
        return listaTransaccionesDto;
    }

    @Override
    public TransaccionDto transaccionToTransaccionDto(Transaccion transaccion) {
        String idCuentaOrigen = transaccion.getCuentaOrigen() != null ? 
                                transaccion.getCuentaOrigen().getIdCuenta() : "";
        String idCuentaDestino = transaccion.getCuentaDestino() != null ? 
                                  transaccion.getCuentaDestino().getIdCuenta() : "";
        String idCategoria = transaccion.getCategoria() != null ? 
                             transaccion.getCategoria().getIdCategoria() : "";
        
        return new TransaccionDto(
                transaccion.getIdTransaccion(),
                transaccion.getFecha().format(formatter),
                transaccion.getTipoTransaccion(),
                String.valueOf(transaccion.getMonto()),
                transaccion.getDescripcion(),
                idCuentaOrigen,
                idCuentaDestino,
                idCategoria
        );
    }

    @Override
    public Transaccion transaccionDtoToTransaccion(TransaccionDto dto) {
        // CHANGED: Get ModelFactory instance only when needed
        var modelFactory = co.edu.uniquindio.factory.ModelFactory.getInstance();
        
        Cuenta cuentaOrigen = !dto.idCuentaOrigen().isEmpty() ? 
                              modelFactory.buscarCuentaPorId(dto.idCuentaOrigen()) : null;
        Cuenta cuentaDestino = !dto.idCuentaDestino().isEmpty() ? 
                               modelFactory.buscarCuentaPorId(dto.idCuentaDestino()) : null;
        Categoria categoria = !dto.idCategoria().isEmpty() ? 
                             modelFactory.buscarCategoriaPorId(dto.idCategoria()) : null;
        
        return new Transaccion(
                dto.idTransaccion(),
                LocalDateTime.parse(dto.fecha(), formatter),
                dto.tipoTransaccion(),
                Double.parseDouble(dto.monto()),
                dto.descripcion(),
                cuentaOrigen,
                cuentaDestino,
                categoria
        );
    }
}