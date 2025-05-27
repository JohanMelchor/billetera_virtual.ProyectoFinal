package co.edu.uniquindio.service;

import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.model.Transaccion;
import co.edu.uniquindio.model.Usuario;
import java.util.List;

public interface IReporteAdapter {
    boolean generarReporteUsuario(String rutaArchivo, Usuario usuario, List<Transaccion> transacciones, List<Cuenta> cuentas);
    boolean generarReporteAdmin(String rutaArchivo, List<Usuario> usuarios, List<Transaccion> transacciones, List<Cuenta> cuentas);
}

