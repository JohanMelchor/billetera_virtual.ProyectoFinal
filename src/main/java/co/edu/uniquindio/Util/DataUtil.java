package co.edu.uniquindio.Util;

import co.edu.uniquindio.model.*;
import java.time.LocalDateTime;

public class DataUtil {
    public static BilleteraVirtual inicializarDatos() {
        BilleteraVirtual billeteraVirtual = new BilleteraVirtual();

        Usuario Usuario1 = Usuario.builder().nombreCompleto("Juan").idUsuario("001").correo("juan@gmail.com").telefono("321").direccion("cs 1").saldo(1000.0).password("123").build();
        Usuario Usuario2 = Usuario.builder().nombreCompleto("Johan").idUsuario("002").correo("johan@gmail.com").telefono("421").direccion("cs 2").saldo(2000.0).password("124").build();
        Usuario Usuario3 = Usuario.builder().nombreCompleto("Felipe").idUsuario("003").correo("felipe@gmail.com").telefono("521").direccion("cs 3").saldo(3000.0).password("125").build();
        Usuario Usuario4 = Usuario.builder().nombreCompleto("Sofia").idUsuario("004").correo("sofia@gmail.com").telefono("621").direccion("cs 4").saldo(4000.0).password("126").build();

        billeteraVirtual.getListaUsuarios().add(Usuario1);
        billeteraVirtual.getListaUsuarios().add(Usuario2);
        billeteraVirtual.getListaUsuarios().add(Usuario3);
        billeteraVirtual.getListaUsuarios().add(Usuario4);

        billeteraVirtual.getListaCategorias().add(new Categoria("CAT01", "Mercado", "Gastos de alimentación y hogar"));
        billeteraVirtual.getListaCategorias().add(new Categoria("CAT02", "Transporte", "Gastos de movilidad"));
        billeteraVirtual.getListaCategorias().add(new Categoria("CAT03", "Ocio", "Entretenimiento y actividades recreativas"));
        billeteraVirtual.getListaCategorias().add(new Categoria("CAT04", "Servicios", "Pagos de servicios públicos"));

        billeteraVirtual.getListaAdministradores().add(new Administrador("ADM01", "Admin Principal", "admin@billeteravirtual.com", "admin123"));

        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE01").nombreBanco("Banco Popular").numeroCuenta("100001").tipoCuenta("Ahorro").usuario(Usuario1).build());
        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE02").nombreBanco("Banco de Bogotá").numeroCuenta("200001").tipoCuenta("Corriente").usuario(Usuario1).build());
        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE03").nombreBanco("Bancolombia").numeroCuenta("300001").tipoCuenta("Ahorro").usuario(Usuario2).build());
        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE04").nombreBanco("Davivienda").numeroCuenta("400001").tipoCuenta("Ahorro").usuario(Usuario3).build());
        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE05").nombreBanco("LuloBank").numeroCuenta("500001").tipoCuenta("Corriente").usuario(Usuario4).build());

        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE01", "Gastos Mensuales", 1500.0, 200.0, billeteraVirtual.getListaCategorias().get(0), Usuario1, 1000.0));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE02", "Ahorros", 2000.0, 0.0, null, Usuario1, 2000.0));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE03", "Gastos Diarios", 1000.0, 300.0, billeteraVirtual.getListaCategorias().get(1), Usuario2, 2000.0));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE04", "Entretenimiento", 500.0, 100.0, billeteraVirtual.getListaCategorias().get(2), Usuario3, 3000.0));


        billeteraVirtual.getListaCuentas().get(0).setPresupuesto(billeteraVirtual.getListaPresupuestos().get(0));
        billeteraVirtual.getListaCuentas().get(1).setPresupuesto(billeteraVirtual.getListaPresupuestos().get(1));
        billeteraVirtual.getListaCuentas().get(2).setPresupuesto(billeteraVirtual.getListaPresupuestos().get(2));
        billeteraVirtual.getListaCuentas().get(3).setPresupuesto(billeteraVirtual.getListaPresupuestos().get(3));

        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA01", 
            LocalDateTime.now().minusDays(5), 
            TransaccionConstantes.TIPO_DEPOSITO, 
            500.0, 
            "Depósito inicial", 
            null, 
            billeteraVirtual.getListaCuentas().get(0), 
            billeteraVirtual.getListaCategorias().get(0)
        ));

        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA02", 
            LocalDateTime.now().minusDays(3), 
            TransaccionConstantes.TIPO_RETIRO, 
            200.0, 
            "Retiro para compras", 
            billeteraVirtual.getListaCuentas().get(0), 
            null, 
            billeteraVirtual.getListaCategorias().get(0)
        ));

        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA03", 
            LocalDateTime.now().minusDays(1), 
            TransaccionConstantes.TIPO_TRANSFERENCIA, 
            300.0, 
            "Transferencia entre cuentas", 
            billeteraVirtual.getListaCuentas().get(0), 
            billeteraVirtual.getListaCuentas().get(1), 
            null
        ));

        return billeteraVirtual;
    }
}