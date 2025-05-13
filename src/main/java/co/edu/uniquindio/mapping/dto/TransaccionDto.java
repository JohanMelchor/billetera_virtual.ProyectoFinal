package co.edu.uniquindio.mapping.dto;

public record TransaccionDto(String idTransaccion,
                            String fecha,
                            String tipoTransaccion,
                            String monto,
                            String descripcion,
                            String idCuentaOrigen,
                            String idCuentaDestino,
                            String idCategoria) {
}