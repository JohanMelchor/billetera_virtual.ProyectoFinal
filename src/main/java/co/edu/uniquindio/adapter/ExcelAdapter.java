package co.edu.uniquindio.adapter;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import java.util.List;

import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.model.Transaccion;
import co.edu.uniquindio.model.Usuario;
import co.edu.uniquindio.service.IReporteAdapter;

public class ExcelAdapter implements IReporteAdapter {

    @Override
    public boolean generarReporteUsuario(String rutaArchivo, Usuario usuario, 
                                        List<Transaccion> transacciones, List<Cuenta> cuentas) {
        try (Workbook workbook = new XSSFWorkbook()) {
            
            // Asegurar extensión .xlsx
            if (!rutaArchivo.endsWith(".xlsx")) {
                rutaArchivo += ".xlsx";
            }
            
            // Hoja 1: Info del usuario
            Sheet sheetUsuario = workbook.createSheet("Usuario");
            Row header = sheetUsuario.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Nombre");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Saldo");
            
            Row data = sheetUsuario.createRow(1);
            data.createCell(0).setCellValue(usuario.getIdUsuario());
            data.createCell(1).setCellValue(usuario.getNombreCompleto());
            data.createCell(2).setCellValue(usuario.getCorreo());
            data.createCell(3).setCellValue(usuario.getSaldo());
            
            // Hoja 2: Cuentas
            Sheet sheetCuentas = workbook.createSheet("Cuentas");
            Row headerCuentas = sheetCuentas.createRow(0);
            headerCuentas.createCell(0).setCellValue("ID Cuenta");
            headerCuentas.createCell(1).setCellValue("Banco");
            headerCuentas.createCell(2).setCellValue("Saldo");
            
            int row = 1;
            for (Cuenta cuenta : cuentas) {
                Row rowData = sheetCuentas.createRow(row++);
                rowData.createCell(0).setCellValue(cuenta.getIdCuenta());
                rowData.createCell(1).setCellValue(cuenta.getNombreBanco());
                rowData.createCell(2).setCellValue(cuenta.getSaldoTotal());
            }
            
            // Hoja 3: Transacciones
            Sheet sheetTrans = workbook.createSheet("Transacciones");
            Row headerTrans = sheetTrans.createRow(0);
            headerTrans.createCell(0).setCellValue("Fecha");
            headerTrans.createCell(1).setCellValue("Tipo");
            headerTrans.createCell(2).setCellValue("Monto");
            
            row = 1;
            for (Transaccion trans : transacciones) {
                Row rowData = sheetTrans.createRow(row++);
                rowData.createCell(0).setCellValue(trans.getFecha().toString());
                rowData.createCell(1).setCellValue(trans.getTipoTransaccion());
                rowData.createCell(2).setCellValue(trans.getMonto());
            }
            
            // Guardar archivo
            try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                workbook.write(fileOut);
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
        try (Workbook workbook = new XSSFWorkbook()) {
            
            // Asegurar extensión .xlsx
            if (!rutaArchivo.endsWith(".xlsx")) {
                rutaArchivo += ".xlsx";
            }
            
            // Hoja 1: Usuarios
            Sheet sheetUsuarios = workbook.createSheet("Usuarios");
            Row header = sheetUsuarios.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Nombre");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Saldo");
            
            int row = 1;
            for (Usuario usuario : usuarios) {
                Row rowData = sheetUsuarios.createRow(row++);
                rowData.createCell(0).setCellValue(usuario.getIdUsuario());
                rowData.createCell(1).setCellValue(usuario.getNombreCompleto());
                rowData.createCell(2).setCellValue(usuario.getCorreo());
                rowData.createCell(3).setCellValue(usuario.getSaldo());
            }
            
            // Hoja 2: Resumen
            Sheet sheetResumen = workbook.createSheet("Resumen");
            Row resumenHeader = sheetResumen.createRow(0);
            resumenHeader.createCell(0).setCellValue("Métrica");
            resumenHeader.createCell(1).setCellValue("Valor");
            
            Row row1 = sheetResumen.createRow(1);
            row1.createCell(0).setCellValue("Total Usuarios");
            row1.createCell(1).setCellValue(usuarios.size());
            
            Row row2 = sheetResumen.createRow(2);
            row2.createCell(0).setCellValue("Total Cuentas");
            row2.createCell(1).setCellValue(cuentas.size());
            
            Row row3 = sheetResumen.createRow(3);
            row3.createCell(0).setCellValue("Total Transacciones");
            row3.createCell(1).setCellValue(transacciones.size());
            
            // Guardar archivo
            try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                workbook.write(fileOut);
            }
            
            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
