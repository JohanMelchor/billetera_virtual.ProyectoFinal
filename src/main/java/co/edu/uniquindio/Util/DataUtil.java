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

        billeteraVirtual.getListaCategorias().add(new Categoria("CAT01", "Hogar", "Gastos de hogar"));
        billeteraVirtual.getListaCategorias().add(new Categoria("CAT02", "Transporte", "Gastos de transporte"));
        billeteraVirtual.getListaCategorias().add(new Categoria("CAT03", "Ocio", "actividades recreativas"));
        billeteraVirtual.getListaCategorias().add(new Categoria("CAT04", "Servicios", "Pagos de servicios públicos"));
        billeteraVirtual.getListaCategorias().add(new Categoria("CAT05", "Salud", "Gastos médicos y de salud"));
        billeteraVirtual.getListaCategorias().add(new Categoria("CAT06", "Educacion", "Gastos de educación"));
        billeteraVirtual.getListaCategorias().add(new Categoria("CAT07", "Entretenimiento", "Gastos de entretenimiento"));
        billeteraVirtual.getListaCategorias().add(new Categoria("CAT08", "Alimentacion", "Gastos de alimentación"));

        billeteraVirtual.getListaAdministradores().add(new Administrador("ADM01", "Admin Principal", "admin@billeteravirtual.com", "admin123"));


        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE01").nombreBanco("Banco Popular").numeroCuenta("100001").tipoCuenta("Ahorro").usuario(Usuario1).saldoTotal(1000.0).build());
        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE02").nombreBanco("Banco de Bogotá").numeroCuenta("200002").tipoCuenta("Corriente").usuario(Usuario1).saldoTotal(2000.0).build());
        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE03").nombreBanco("Davivienda").numeroCuenta("300003").tipoCuenta("Ahorro").usuario(Usuario2).saldoTotal(3000.0).build());
        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE07").nombreBanco("Bancolombia").numeroCuenta("700007").tipoCuenta("Corriente").usuario(Usuario2).saldoTotal(7000.0).build());
        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE04").nombreBanco("Bancolombia").numeroCuenta("400004").tipoCuenta("Corriente").usuario(Usuario3).saldoTotal(4000.0).build());
        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE04").nombreBanco("Bancolombia").numeroCuenta("400004").tipoCuenta("Corriente").usuario(Usuario3).saldoTotal(4000.0).build());
        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE05").nombreBanco("BBVA").numeroCuenta("500005").tipoCuenta("Ahorro").usuario(Usuario4).saldoTotal(5000.0).build());
        billeteraVirtual.getListaCuentas().add(Cuenta.builder().idCuenta("CUE06").nombreBanco("Nequi").numeroCuenta("600006").tipoCuenta("Ahorro").usuario(Usuario4).saldoTotal(6000.0).build());

        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE01", "Gastos Mensuales", 1500.0, 200.0, billeteraVirtual.getListaCategorias().get(0), Usuario1, 1300.0, billeteraVirtual.getListaCuentas().get(0)));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE02", "Ahorros", 2000.0, 0.0, billeteraVirtual.getListaCategorias().get(1), Usuario1, 2000.0, billeteraVirtual.getListaCuentas().get(1)));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE03", "Gastos Diarios", 1000.0, 300.0, billeteraVirtual.getListaCategorias().get(1), Usuario2, 700.0, billeteraVirtual.getListaCuentas().get(2)));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE04", "Entretenimiento", 500.0, 100.0, billeteraVirtual.getListaCategorias().get(2), Usuario3, 400.0, billeteraVirtual.getListaCuentas().get(3)));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE05", "Ahorros para viaje", 1000.0, 0.0, billeteraVirtual.getListaCategorias().get(3), Usuario4, 1000.0, billeteraVirtual.getListaCuentas().get(4)));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE06", "Gastos de salud", 800.0, 200.0, billeteraVirtual.getListaCategorias().get(4), Usuario4, 600.0, billeteraVirtual.getListaCuentas().get(5)));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE07", "Gastos de educación", 1200.0, 300.0, billeteraVirtual.getListaCategorias().get(5), Usuario2, 900.0, billeteraVirtual.getListaCuentas().get(6)));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE08", "Gastos de ocio", 600.0, 100.0, billeteraVirtual.getListaCategorias().get(3), Usuario3, 500.0, billeteraVirtual.getListaCuentas().get(7)));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE09", "Gastos de alimentación", 700.0, 150.0, billeteraVirtual.getListaCategorias().get(7), Usuario1, 550.0, billeteraVirtual.getListaCuentas().get(0)));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE10", "Gastos de transporte", 400.0, 50.0, billeteraVirtual.getListaCategorias().get(6), Usuario2, 350.0, billeteraVirtual.getListaCuentas().get(1)));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE11", "Gastos de entretenimiento", 500.0, 100.0, billeteraVirtual.getListaCategorias().get(2), Usuario3, 400.0, billeteraVirtual.getListaCuentas().get(2)));
        billeteraVirtual.getListaPresupuestos().add(new Presupuesto("PRE12", "Gastos de ocio", 600.0, 100.0, billeteraVirtual.getListaCategorias().get(3), Usuario4, 500.0, billeteraVirtual.getListaCuentas().get(3)));

        billeteraVirtual.getListaPresupuestos().get(0).setCuenta(billeteraVirtual.getListaCuentas().get(0));
        billeteraVirtual.getListaPresupuestos().get(1).setCuenta(billeteraVirtual.getListaCuentas().get(1));
        billeteraVirtual.getListaPresupuestos().get(2).setCuenta(billeteraVirtual.getListaCuentas().get(2));
        billeteraVirtual.getListaPresupuestos().get(3).setCuenta(billeteraVirtual.getListaCuentas().get(3));
        billeteraVirtual.getListaPresupuestos().get(4).setCuenta(billeteraVirtual.getListaCuentas().get(4));
        billeteraVirtual.getListaPresupuestos().get(5).setCuenta(billeteraVirtual.getListaCuentas().get(5));
        billeteraVirtual.getListaPresupuestos().get(6).setCuenta(billeteraVirtual.getListaCuentas().get(6));
        billeteraVirtual.getListaPresupuestos().get(7).setCuenta(billeteraVirtual.getListaCuentas().get(7));
        billeteraVirtual.getListaPresupuestos().get(8).setCuenta(billeteraVirtual.getListaCuentas().get(0));



        // 1. Depósitos a cuentas

        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA01", 
            LocalDateTime.now().minusDays(10), 
            TransaccionConstantes.TIPO_DEPOSITO_CUENTA, 
            1000.0, 
            "Depósito inicial cuenta principal", 
            null, 
            billeteraVirtual.getListaCuentas().get(0), // CUE01 - Banco Popular
            billeteraVirtual.getListaCategorias().get(0) // Hogar
        ));

        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA02", 
            LocalDateTime.now().minusDays(8), 
            TransaccionConstantes.TIPO_DEPOSITO_CUENTA, 
            500.0, 
            "Pago de nómina", 
            null, 
            billeteraVirtual.getListaCuentas().get(1), // CUE02 - Banco de Bogotá
            billeteraVirtual.getListaCategorias().get(5) // Educación
        ));

        // 2. Depósitos a presupuestos
        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA03", 
            LocalDateTime.now().minusDays(7), 
            TransaccionConstantes.TIPO_DEPOSITO_PRESUPUESTO, 
            300.0, 
            "Asignación a gastos mensuales", 
            billeteraVirtual.getListaCuentas().get(0), // CUE01
            null, 
            billeteraVirtual.getListaCategorias().get(0) // Hogar
        ));

        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA04", 
            LocalDateTime.now().minusDays(6), 
            TransaccionConstantes.TIPO_DEPOSITO_PRESUPUESTO, 
            200.0, 
            "Asignación a entretenimiento", 
            billeteraVirtual.getListaCuentas().get(3), // CUE04
            null, 
            billeteraVirtual.getListaCategorias().get(2) // Ocio
        ));

        // 3. Retiros de cuentas
        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA05", 
            LocalDateTime.now().minusDays(5), 
            TransaccionConstantes.TIPO_RETIRO_CUENTA, 
            150.0, 
            "Retiro para compras", 
            billeteraVirtual.getListaCuentas().get(0), // CUE01
            null, 
            billeteraVirtual.getListaCategorias().get(0) // Hogar
        ));

        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA06", 
            LocalDateTime.now().minusDays(4), 
            TransaccionConstantes.TIPO_RETIRO_CUENTA, 
            100.0, 
            "Retiro para transporte", 
            billeteraVirtual.getListaCuentas().get(2), // CUE03
            null, 
            billeteraVirtual.getListaCategorias().get(1) // Transporte
        ));

        // 4. Retiros de presupuestos
        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA07", 
            LocalDateTime.now().minusDays(3), 
            TransaccionConstantes.TIPO_RETIRO_PRESUPUESTO, 
            80.0, 
            "Supermercado", 
            billeteraVirtual.getListaCuentas().get(0), // CUE01
            null, 
            billeteraVirtual.getListaCategorias().get(7) // Alimentación
        ));

        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA08", 
            LocalDateTime.now().minusDays(2), 
            TransaccionConstantes.TIPO_RETIRO_PRESUPUESTO, 
            50.0, 
            "Cine", 
            billeteraVirtual.getListaCuentas().get(3), // CUE04
            null, 
            billeteraVirtual.getListaCategorias().get(2) // Ocio
        ));

        // 5. Transferencias entre cuentas
        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA09", 
            LocalDateTime.now().minusDays(1), 
            TransaccionConstantes.TIPO_TRANSFERENCIA, 
            200.0, 
            "Transferencia a ahorros", 
            billeteraVirtual.getListaCuentas().get(0), // CUE01
            billeteraVirtual.getListaCuentas().get(1), // CUE02
            null
        ));

        billeteraVirtual.getListaTransacciones().add(new Transaccion(
            "TRA10", 
            LocalDateTime.now().minusHours(6), 
            TransaccionConstantes.TIPO_TRANSFERENCIA, 
            100.0, 
            "Pago a amigo", 
            billeteraVirtual.getListaCuentas().get(2), // CUE03
            billeteraVirtual.getListaCuentas().get(4), // CUE05
            null
        ));

        // Actualizar saldos de cuentas según transacciones
        actualizarSaldosCuentas(billeteraVirtual);

        return billeteraVirtual;
    }

    private static void actualizarSaldosCuentas(BilleteraVirtual billeteraVirtual) {
        for (Transaccion transaccion : billeteraVirtual.getListaTransacciones()) {
            switch (transaccion.getTipoTransaccion()) {
                case TransaccionConstantes.TIPO_DEPOSITO_CUENTA:
                    transaccion.getCuentaDestino().aumentarSaldoTotal(transaccion.getMonto());
                    break;
                    
                case TransaccionConstantes.TIPO_DEPOSITO_PRESUPUESTO:
                    transaccion.getCuentaOrigen().reducirSaldoTotal(transaccion.getMonto());
                    break;
                    
                case TransaccionConstantes.TIPO_RETIRO_CUENTA:
                    transaccion.getCuentaOrigen().reducirSaldoTotal(transaccion.getMonto());
                    break;
                    
                case TransaccionConstantes.TIPO_RETIRO_PRESUPUESTO:
                    // El saldo del presupuesto se maneja internamente en la clase Presupuesto
                    break;
                    
                case TransaccionConstantes.TIPO_TRANSFERENCIA:
                    transaccion.getCuentaOrigen().reducirSaldoTotal(transaccion.getMonto());
                    transaccion.getCuentaDestino().aumentarSaldoTotal(transaccion.getMonto());
                    break;
            }
        }
    }
}