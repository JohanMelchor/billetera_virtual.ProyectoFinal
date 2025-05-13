package co.edu.uniquindio.service;

import co.edu.uniquindio.mapping.dto.CuentaDto;
import co.edu.uniquindio.model.Cuenta;

import java.util.List;

public interface ICuentaMapping {
    List<CuentaDto> getCuentaDto(List<Cuenta> listaCuentas);
    CuentaDto cuentaToCuentaDto(Cuenta cuenta);
    Cuenta cuentaDtoToCuenta(CuentaDto cuentaDto);
}