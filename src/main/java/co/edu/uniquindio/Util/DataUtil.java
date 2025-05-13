package co.edu.uniquindio.Util;

import co.edu.uniquindio.model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataUtil {
    
    public static List<Usuario> getUsuariosIniciales() {
        List<Usuario> usuarios = new ArrayList<>();
        
        usuarios.add(Usuario.builder().nombreCompleto("juan").idUsuario("123").correo("juan@gmail.com").telefono("321").direccion("cs 1").saldo(1000.0).build());
        usuarios.add(Usuario.builder().nombreCompleto("johan").idUsuario("124").correo("johan@gmail.com").telefono("421").direccion("cs 2").saldo(2000.0).build());
        usuarios.add(Usuario.builder().nombreCompleto("felipe").idUsuario("125").correo("felipe@gmail.com").telefono("521").direccion("cs 3").saldo(3000.0).build());
        usuarios.add(Usuario.builder().nombreCompleto("sofia").idUsuario("126").correo("sofia@gmail.com").telefono("621").direccion("cs 4").saldo(4000.0).build());
        
        return usuarios;
    }
    
    public static List<Categoria> getCategoriasIniciales() {
        List<Categoria> categorias = new ArrayList<>();
        
        categorias.add(new Categoria("CAT01", "Mercado", "Gastos de alimentación y hogar"));
        categorias.add(new Categoria("CAT02", "Transporte", "Gastos de movilidad"));
        categorias.add(new Categoria("CAT03", "Ocio", "Entretenimiento y actividades recreativas"));
        categorias.add(new Categoria("CAT04", "Servicios", "Pagos de servicios públicos"));
        
        return categorias;
    }
    
    public static List<Administrador> getAdministradoresIniciales() {
        List<Administrador> administradores = new ArrayList<>();
        
        administradores.add(new Administrador("ADM01", "Admin Principal", "admin@billeteravirtual.com", "admin123"));
        
        return administradores;
    }
    
    public static List<Cuenta> getCuentasIniciales(List<Usuario> usuarios) {
        List<Cuenta> cuentas = new ArrayList<>();
        
        cuentas.add(Cuenta.builder().idCuenta("CUE01").nombreBanco("Banco Popular").numeroCuenta("100001").tipoCuenta("Ahorro").usuario(usuarios.get(0)).build());
        cuentas.add(Cuenta.builder().idCuenta("CUE02").nombreBanco("Banco de Bogotá").numeroCuenta("200001").tipoCuenta("Corriente").usuario(usuarios.get(0)).build());
        cuentas.add(Cuenta.builder().idCuenta("CUE03").nombreBanco("Bancolombia").numeroCuenta("300001").tipoCuenta("Ahorro").usuario(usuarios.get(1)).build());
        cuentas.add(Cuenta.builder().idCuenta("CUE04").nombreBanco("Davivienda").numeroCuenta("400001").tipoCuenta("Ahorro").usuario(usuarios.get(2)).build());
        
        return cuentas;
    }
    
    public static List<Presupuesto> getPresupuestosIniciales(List<Usuario> usuarios, List<Categoria> categorias) {
        List<Presupuesto> presupuestos = new ArrayList<>();
        
        presupuestos.add(new Presupuesto("PRE01", "Gastos Mensuales", 1500.0, 200.0, categorias.get(0), usuarios.get(0), 1000.0));
        presupuestos.add(new Presupuesto("PRE02", "Ahorros", 2000.0, 0.0, null, usuarios.get(0), 2000.0));
        presupuestos.add(new Presupuesto("PRE03", "Gastos Diarios", 1000.0, 300.0, categorias.get(1), usuarios.get(1), 2000.0));
        presupuestos.add(new Presupuesto("PRE04", "Entretenimiento", 500.0, 100.0, categorias.get(2), usuarios.get(2), 3000.0));
        
        return presupuestos;
    }
    
    public static void asociarPresupuestosCuentas(List<Cuenta> cuentas, List<Presupuesto> presupuestos) {
        cuentas.get(0).setPresupuesto(presupuestos.get(0));
        cuentas.get(1).setPresupuesto(presupuestos.get(1));
        cuentas.get(2).setPresupuesto(presupuestos.get(2));
        cuentas.get(3).setPresupuesto(presupuestos.get(3));
    }
    
    public static List<Transaccion> getTransaccionesIniciales(List<Cuenta> cuentas, List<Categoria> categorias) {
        List<Transaccion> transacciones = new ArrayList<>();
        
        transacciones.add(new Transaccion(
            "TRA01", 
            LocalDateTime.now().minusDays(5), 
            TransaccionConstantes.TIPO_DEPOSITO, 
            500.0, 
            "Depósito inicial", 
            null, 
            cuentas.get(0), 
            categorias.get(0)
        ));
        
        transacciones.add(new Transaccion(
            "TRA02", 
            LocalDateTime.now().minusDays(3), 
            TransaccionConstantes.TIPO_RETIRO, 
            200.0, 
            "Retiro para compras", 
            cuentas.get(0), 
            null, 
            categorias.get(0)
        ));
        
        transacciones.add(new Transaccion(
            "TRA03", 
            LocalDateTime.now().minusDays(1), 
            TransaccionConstantes.TIPO_TRANSFERENCIA, 
            300.0, 
            "Transferencia entre cuentas", 
            cuentas.get(0), 
            cuentas.get(1), 
            null
        ));
        
        return transacciones;
    }
}