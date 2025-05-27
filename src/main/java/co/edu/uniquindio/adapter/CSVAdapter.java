package co.edu.uniquindio.adapter;

import java.io.FileWriter;
import java.io.IOException;

import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.model.Transaccion;
import co.edu.uniquindio.model.Usuario;
import co.edu.uniquindio.service.IReporteAdapter;
import java.util.List;

public class CSVAdapter implements IReporteAdapter {
    
    @Override
    public boolean generarReporteUsuario(String rutaArchivo, Usuario usuario, 
                                        List<Transaccion> transacciones, List<Cuenta> cuentas) {
        try (FileWriter writer = new FileWriter(asegurarExtensionCSV(rutaArchivo))) {
            
            // Información del usuario
            writer.append("REPORTE PERSONAL\n");
            writer.append("================\n\n");
            
            writer.append("DATOS DEL USUARIO\n");
            writer.append("ID,Nombre,Email,Saldo\n");
            writer.append(String.format("%s,%s,%s,%.2f\n", 
                usuario.getIdUsuario(), usuario.getNombreCompleto(), 
                usuario.getCorreo(), usuario.getSaldo()));
            
            // Cuentas
            writer.append("\nMIS CUENTAS\n");
            writer.append("ID Cuenta,Banco,Saldo\n");
            for (Cuenta cuenta : cuentas) {
                writer.append(String.format("%s,%s,%.2f\n", 
                    cuenta.getIdCuenta(), cuenta.getNombreBanco(), cuenta.getSaldoTotal()));
            }
            
            // Transacciones (últimas 20)
            writer.append("\nMIS TRANSACCIONES (Últimas 20)\n");
            writer.append("Fecha,Tipo,Monto\n");
            
            int contador = 0;
            for (Transaccion trans : transacciones) {
                if (contador >= 20) break;
                writer.append(String.format("%s,%s,%.2f\n", 
                    trans.getFecha().toString(), trans.getTipoTransaccion(), trans.getMonto()));
                contador++;
            }
            
            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean generarReporteAdmin(String rutaArchivo, List<Usuario> usuarios, 
                                      List<Transaccion> transacciones, List<Cuenta> cuentas) {
        try (FileWriter writer = new FileWriter(asegurarExtensionCSV(rutaArchivo))) {
            
            // Resumen general
            writer.append("REPORTE ADMINISTRATIVO\n");
            writer.append("======================\n\n");
            
            writer.append("RESUMEN GENERAL\n");
            writer.append("Métrica,Valor\n");
            writer.append(String.format("Total Usuarios,%d\n", usuarios.size()));
            writer.append(String.format("Total Cuentas,%d\n", cuentas.size()));
            writer.append(String.format("Total Transacciones,%d\n", transacciones.size()));
            
            // Usuarios
            writer.append("\nUSUARIOS DEL SISTEMA\n");
            writer.append("ID,Nombre,Email,Saldo\n");
            for (Usuario usuario : usuarios) {
                writer.append(String.format("%s,%s,%s,%.2f\n", 
                    usuario.getIdUsuario(), usuario.getNombreCompleto(), 
                    usuario.getCorreo(), usuario.getSaldo()));
            }
            
            // Cuentas
            writer.append("\nCUENTAS DEL SISTEMA\n");
            writer.append("ID Cuenta,Propietario,Banco,Saldo\n");
            for (Cuenta cuenta : cuentas) {
                writer.append(String.format("%s,%s,%s,%.2f\n", 
                    cuenta.getIdCuenta(), cuenta.getUsuario().getNombreCompleto(),
                    cuenta.getNombreBanco(), cuenta.getSaldoTotal()));
            }
            
            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private String asegurarExtensionCSV(String rutaArchivo) {
        return rutaArchivo.endsWith(".csv") ? rutaArchivo : rutaArchivo + ".csv";
    }

}
