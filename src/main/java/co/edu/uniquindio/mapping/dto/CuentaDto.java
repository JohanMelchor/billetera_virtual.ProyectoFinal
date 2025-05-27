package co.edu.uniquindio.mapping.dto;

import co.edu.uniquindio.state.TipoEstadoCuenta;

public record CuentaDto(String idCuenta,
                        String nombreBanco,
                        String numeroCuenta,
                        String tipoCuenta,
                        String idUsuario,
                        Double saldoTotal,
                        TipoEstadoCuenta estado) {
}
