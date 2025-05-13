package co.edu.uniquindio.service;

import co.edu.uniquindio.mapping.dto.TransaccionDto;
import co.edu.uniquindio.model.Transaccion;

import java.util.List;

public interface ITransaccionMapping {
    List<TransaccionDto> getTransaccionDto(List<Transaccion> listaTransacciones);
    TransaccionDto transaccionToTransaccionDto(Transaccion transaccion);
    Transaccion transaccionDtoToTransaccion(TransaccionDto transaccionDto);
}