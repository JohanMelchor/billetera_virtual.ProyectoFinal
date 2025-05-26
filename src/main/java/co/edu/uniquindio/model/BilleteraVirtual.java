package co.edu.uniquindio.model;

import co.edu.uniquindio.service.*;
import co.edu.uniquindio.Util.TransaccionConstantes;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BilleteraVirtual implements IUsuarioServices,IAdministradorServices, ICuentaServices, ITransaccionServices, IPresupuestoServices, ICategoriaServices {
    private ArrayList<Usuario>listaUsuarios= new ArrayList<>();
    private ArrayList<Cuenta>listaCuentas= new ArrayList<>();
    private ArrayList<Transaccion>listaTransacciones= new ArrayList<>();
    private ArrayList<Presupuesto>listaPresupuestos= new ArrayList<>();
    private ArrayList<Categoria>listaCategorias= new ArrayList<>();
    private ArrayList<Administrador>listaAdministradores= new ArrayList<>();

    public BilleteraVirtual(){
        this.listaUsuarios = new ArrayList<>();
        this.listaCuentas = new ArrayList<>();
        this.listaTransacciones = new ArrayList<>();
        this.listaPresupuestos = new ArrayList<>();
        this.listaCategorias = new ArrayList<>();
        this.listaAdministradores = new ArrayList<>();
    }

    public ArrayList<Cuenta> getListaCuentas() {
        return listaCuentas;
    }

    public ArrayList<Administrador> getListaAdministradores() {
        return listaAdministradores;
    }

    public ArrayList<Categoria> getListaCategorias() {
        return listaCategorias;
    }

    public ArrayList<Presupuesto> getListaPresupuestos() {
        return listaPresupuestos;
    }

    public void setListaTransacciones(ArrayList<Transaccion> listaTransacciones) {
        this.listaTransacciones = listaTransacciones;
    }

    public ArrayList<Transaccion> getListaTransacciones() {
        return listaTransacciones;
    }

    public void setListaCuentas(ArrayList<Cuenta> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    // Métodos para Usuario
    @Override
    public boolean crearUsuario(Usuario usuario) {
        if (buscarUsuarioPorId(usuario.getIdUsuario()) == null) {
            listaUsuarios.add(usuario);
            return true;
        }
        return false;
    }
    

    @Override
    public boolean eliminarUsuario(String idUsuario) {
        Usuario usuarioEliminado = buscarUsuarioPorId(idUsuario);
        if(usuarioEliminado != null){
            listaUsuarios.remove(usuarioEliminado);
            return true;
        }
        return false;
    }

    public Usuario buscarUsuarioPorId(String idUsuario) {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getIdUsuario().equals(idUsuario)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean validarCredenciales(String idUsuario, String password) {
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        if(usuario == null) return false;
            return usuario.getPassword().equals(password);
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        
        Usuario usuarioExistente = buscarUsuarioPorId(usuario.getIdUsuario());
        if(usuarioExistente != null){
            // Actualizar solo los campos permitidos
            usuarioExistente.setNombreCompleto(usuario.getNombreCompleto());
            usuarioExistente.setCorreo(usuario.getCorreo());
            usuarioExistente.setTelefono(usuario.getTelefono());
            usuarioExistente.setDireccion(usuario.getDireccion());
            usuarioExistente.setPassword(usuario.getPassword());

            return true;
        }
        return false;
    }

    // Métodos para Cuenta
    @Override
    public boolean crearCuenta(Cuenta cuenta) {
        Cuenta cuentaExistente = buscarCuenta(cuenta);
        if(cuentaExistente == null) {
            listaCuentas.add(cuenta);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarCuenta(String idCuenta) {
        Cuenta cuentaEliminar = buscarCuentaPorId(idCuenta);
        if(cuentaEliminar != null) {
            listaCuentas.remove(cuentaEliminar);
            return true;
        }
        return false;
    }

    @Override
    public boolean actualizarCuenta(Cuenta cuenta) {
        Cuenta cuentaActualizar = buscarCuentaPorId(cuenta.getIdCuenta());
        if(cuentaActualizar != null) {
            cuentaActualizar.setNombreBanco(cuenta.getNombreBanco());
            cuentaActualizar.setNumeroCuenta(cuenta.getNumeroCuenta());
            cuentaActualizar.setTipoCuenta(cuenta.getTipoCuenta());
            cuentaActualizar.setUsuario(cuenta.getUsuario());
            return true;
        }
        return false;
    }

    public Cuenta buscarCuenta(Cuenta cuenta) {
        for(Cuenta c : listaCuentas) {
            if(c.getIdCuenta().equals(cuenta.getIdCuenta())) {
                return c;
            }
        }
        return null;
    }

    public Cuenta buscarCuentaPorId(String idCuenta) {
        for(Cuenta cuenta : listaCuentas) {
            if(cuenta.getIdCuenta().equals(idCuenta)) {
                return cuenta;
            }
        }
        return null;
    }

    public List<Cuenta> obtenerCuentasPorUsuario(String idUsuario) {
        List<Cuenta> cuentasUsuario = new ArrayList<>();
        for(Cuenta cuenta : listaCuentas) {
            if(cuenta.getUsuario().getIdUsuario().equals(idUsuario)) {
                cuentasUsuario.add(cuenta);
            }
        }
        return cuentasUsuario;
    }

    // Métodos para Transacción
    @Override
    public boolean crearTransaccion(Transaccion transaccion) {
        listaTransacciones.add(transaccion);
        return true;
    }

    @Override
    public boolean retirarCuenta(String idCuenta, Double monto, String descripcion, String idCategoria) {
        Cuenta cuenta = buscarCuentaPorId(idCuenta);
        if (cuenta == null || !cuenta.reducirSaldoTotal(monto)) {
            return false;
        }

        Categoria categoria = buscarCategoriaPorId(idCategoria);
        
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            "RETIRO_CUENTA",
            monto,
            descripcion,
            cuenta,
            null,
            categoria
        );
        
        return crearTransaccion(transaccion);
    }

    @Override
    public boolean retirarPresupuesto(String idCuenta, String idPresupuesto, Double monto, 
                                    String descripcion, String idCategoria) {
        Cuenta cuenta = buscarCuentaPorId(idCuenta);
        if (cuenta == null) return false;

        Presupuesto presupuesto = buscarPresupuestoPorId(idPresupuesto);
        if (presupuesto == null || !presupuesto.reducirSaldo(monto)) {
            return false;
        }

        Categoria categoria = buscarCategoriaPorId(idCategoria);
        
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            "RETIRO_PRESUPUESTO",
            monto,
            descripcion + " (Presupuesto: " + presupuesto.getNombre() + ")",
            cuenta,
            null,
            categoria
        );
        
        return crearTransaccion(transaccion);
    }

    @Override
    public boolean realizarTransferencia(String idCuentaOrigen, String idCuentaDestino, 
                                    Double monto, String descripcion, String idCategoria) {
        // Validar que no sea la misma cuenta
        if (idCuentaOrigen.equals(idCuentaDestino)) {
            return false;
        }

        Cuenta cuentaOrigen = buscarCuentaPorId(idCuentaOrigen);
        Cuenta cuentaDestino = buscarCuentaPorId(idCuentaDestino);
        
        // Validar cuentas y saldo suficiente
        if (cuentaOrigen == null || cuentaDestino == null || 
            !cuentaOrigen.reducirSaldoTotal(monto)) {
            return false;
        }
        
        // Realizar transferencia
        cuentaDestino.aumentarSaldoTotal(monto);
        
        // Registrar transacción
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransaccionConstantes.TIPO_TRANSFERENCIA,
            monto,
            descripcion,
            cuentaOrigen,
            cuentaDestino,
            buscarCategoriaPorId(idCategoria)
        );
        
        return crearTransaccion(transaccion);
    }

    public List<Transaccion> obtenerTransaccionesPorUsuario(String idUsuario) {
        List<Transaccion> transaccionesUsuario = new ArrayList<>();
        List<Cuenta> cuentasUsuario = obtenerCuentasPorUsuario(idUsuario);
        
        for(Transaccion transaccion : listaTransacciones) {
            // Verificar si la cuenta de origen o destino pertenece al usuario
            boolean esOrigen = transaccion.getCuentaOrigen() != null && 
                              cuentasUsuario.contains(transaccion.getCuentaOrigen());
            boolean esDestino = transaccion.getCuentaDestino() != null && 
                               cuentasUsuario.contains(transaccion.getCuentaDestino());
            
            if(esOrigen || esDestino) {
                transaccionesUsuario.add(transaccion);
            }
        }
        return transaccionesUsuario;
    }

    public List<Transaccion> obtenerTransaccionesPorCuenta(String idCuenta) {
        List<Transaccion> transaccionesCuenta = new ArrayList<>();
        for(Transaccion transaccion : listaTransacciones) {
            boolean esOrigen = transaccion.getCuentaOrigen() != null && 
                              transaccion.getCuentaOrigen().getIdCuenta().equals(idCuenta);
            boolean esDestino = transaccion.getCuentaDestino() != null && 
                               transaccion.getCuentaDestino().getIdCuenta().equals(idCuenta);
            
            if(esOrigen || esDestino) {
                transaccionesCuenta.add(transaccion);
            }
        }
        return transaccionesCuenta;
    }

    // Métodos para Presupuesto
    @Override
    public boolean crearPresupuesto(Presupuesto presupuesto) {
        Presupuesto presupuestoExistente = buscarPresupuesto(presupuesto);
        if(presupuestoExistente == null) {
            listaPresupuestos.add(presupuesto);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarPresupuesto(String idPresupuesto) {
        Presupuesto presupuestoEliminar = buscarPresupuestoPorId(idPresupuesto);
        if(presupuestoEliminar != null) {
            listaPresupuestos.remove(presupuestoEliminar);
            return true;
        }
        return false;
    }

    @Override
    public boolean actualizarPresupuesto(Presupuesto presupuesto) {
        Presupuesto presupuestoActualizar = buscarPresupuestoPorId(presupuesto.getIdPresupuesto());
        if(presupuestoActualizar != null) {
            presupuestoActualizar.setNombre(presupuesto.getNombre());
            presupuestoActualizar.setMontoAsignado(presupuesto.getMontoAsignado());
            presupuestoActualizar.setMontoGastado(presupuesto.getMontoGastado());
            presupuestoActualizar.setCategoria(presupuesto.getCategoria());
            return true;
        }
        return false;
    }

    public Presupuesto buscarPresupuesto(Presupuesto presupuesto) {
        for(Presupuesto p : listaPresupuestos) {
            if(p.getIdPresupuesto().equals(presupuesto.getIdPresupuesto())) {
                return p;
            }
        }
        return null;
    }

    public Presupuesto buscarPresupuestoPorId(String idPresupuesto) {
        for(Presupuesto presupuesto : listaPresupuestos) {
            if(presupuesto.getIdPresupuesto().equals(idPresupuesto)) {
                return presupuesto;
            }
        }
        return null;
    }

    public List<Presupuesto> obtenerPresupuestosPorUsuario(String idUsuario) {
        List<Presupuesto> presupuestosUsuario = new ArrayList<>();
        for(Presupuesto presupuesto : listaPresupuestos) {
            if(presupuesto.getUsuario().getIdUsuario().equals(idUsuario)) {
                presupuestosUsuario.add(presupuesto);
            }
        }
        return presupuestosUsuario;
    }

    public List<Presupuesto> obtenerPresupuestosPorCuenta(String idCuenta) {
        List<Presupuesto> presupuestosCuenta = new ArrayList<>();
        for(Presupuesto presupuesto : listaPresupuestos) {
            if(presupuesto.getCuenta() != null && 
            presupuesto.getCuenta().getIdCuenta().equals(idCuenta)) {
                presupuestosCuenta.add(presupuesto);
            }
        }
        return presupuestosCuenta;
    }

    // Métodos para Categoría
    @Override
    public boolean crearCategoria(Categoria categoria) {
        Categoria categoriaExistente = buscarCategoria(categoria);
        if(categoriaExistente == null) {
            listaCategorias.add(categoria);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarCategoria(String idCategoria) {
        Categoria categoriaEliminar = buscarCategoriaPorId(idCategoria);
        if(categoriaEliminar != null) {
            listaCategorias.remove(categoriaEliminar);
            return true;
        }
        return false;
    }

    @Override
    public boolean actualizarCategoria(Categoria categoria) {
        Categoria categoriaActualizar = buscarCategoriaPorId(categoria.getIdCategoria());
        if(categoriaActualizar != null) {
            categoriaActualizar.setNombre(categoria.getNombre());
            categoriaActualizar.setDescripcion(categoria.getDescripcion());
            return true;
        }
        return false;
    }

    public Categoria buscarCategoria(Categoria categoria) {
        for(Categoria c : listaCategorias) {
            if(c.getIdCategoria().equals(categoria.getIdCategoria())) {
                return c;
            }
        }
        return null;
    }

    public Categoria buscarCategoriaPorId(String idCategoria) {
        for(Categoria categoria : listaCategorias) {
            if(categoria.getIdCategoria().equals(idCategoria)) {
                return categoria;
            }
        }
        return null;
    }

    @Override
    public boolean crearAdministrador(Administrador administrador) {
        if(buscarAdministradorPorId(administrador.getIdAdmin()) != null) {
            return false;
        }
        listaAdministradores.add(administrador);
        return true;
    }

    @Override
    public Administrador buscarAdministradorPorId(String idAdmin) {
        return listaAdministradores.stream()
            .filter(a -> a.getIdAdmin().equals(idAdmin))
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean depositoCuenta(String idCuenta, Double monto, String descripcion, String idCategoria) {
        Cuenta cuenta = buscarCuentaPorId(idCuenta);
        if (cuenta == null) return false;
        
        cuenta.aumentarSaldoTotal(monto);
        
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransaccionConstantes.TIPO_DEPOSITO_CUENTA,
            monto,
            descripcion,
            null,  // No hay cuenta origen en depósito
            cuenta,
            buscarCategoriaPorId(idCategoria)
        );
        
        return crearTransaccion(transaccion);
    }

    @Override
    public boolean depositoPresupuesto(String idCuenta, String idPresupuesto, Double monto, 
                                         String descripcion, String idCategoria) {
        Cuenta cuenta = buscarCuentaPorId(idCuenta);
        if (cuenta == null) return false;
        
        Presupuesto presupuesto = buscarPresupuestoPorId(idPresupuesto);
        if (presupuesto == null) return false;
        
        // Validar que la cuenta tenga saldo suficiente
        if (cuenta.getSaldoTotal() < monto) return false;
        
        // Realizar operaciones
        cuenta.reducirSaldoTotal(monto);  // Restar de la cuenta
        presupuesto.aumentarSaldo(monto); // Añadir al presupuesto
        
        Transaccion transaccion = new Transaccion(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            TransaccionConstantes.TIPO_DEPOSITO_PRESUPUESTO,
            monto,
            descripcion + " (Presupuesto: " + presupuesto.getNombre() + ")",
            cuenta,  // Origen: la cuenta
            null,    // Destino: el presupuesto (no es una cuenta)
            buscarCategoriaPorId(idCategoria)
        );

        return crearTransaccion(transaccion);
    }

    public boolean generarReporteUsuario(String idUsuario, String rutaArchivo) {
        try {
            Usuario usuario = buscarUsuarioPorId(idUsuario);
            if (usuario == null) return false;
            
            List<Transaccion> transacciones = obtenerTransaccionesPorUsuario(idUsuario);
            List<Cuenta> cuentas = obtenerCuentasPorUsuario(idUsuario);
            
            return crearPDFUsuario(rutaArchivo, usuario, transacciones, cuentas);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean generarReporteAdmin(String rutaArchivo) {
        try {
            return crearPDFAdmin(rutaArchivo, listaUsuarios, listaTransacciones, listaCuentas);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean crearPDFUsuario(String rutaArchivo, Usuario usuario, 
                                List<Transaccion> transacciones, 
                                List<Cuenta> cuentas) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            // Título
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("REPORTE FINANCIERO PERSONAL");
            contentStream.endText();
            
            // Info usuario
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.newLineAtOffset(100, 650);
            contentStream.showText("Usuario: " + usuario.getNombreCompleto());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Email: " + usuario.getCorreo());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Saldo Total: $" + usuario.getSaldo());
            contentStream.endText();
            
            // Resumen financiero
            double totalIngresos = transacciones.stream()
                .filter(t -> t.getTipoTransaccion().contains("DEPÓSITO"))
                .mapToDouble(t -> t.getMonto()).sum();
            double totalGastos = transacciones.stream()
                .filter(t -> t.getTipoTransaccion().contains("RETIRO"))
                .mapToDouble(t -> t.getMonto()).sum();
            
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
            contentStream.newLineAtOffset(100, 550);
            contentStream.showText("RESUMEN FINANCIERO");
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.newLineAtOffset(0, -25);
            contentStream.showText("Total Ingresos: $" + String.format("%.2f", totalIngresos));
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Total Gastos: $" + String.format("%.2f", totalGastos));
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Balance: $" + String.format("%.2f", totalIngresos - totalGastos));
            contentStream.endText();
            
            // Mis cuentas
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
            contentStream.newLineAtOffset(100, 450);
            contentStream.showText("MIS CUENTAS");
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            
            int yPosition = 420;
            for(Cuenta cuenta : cuentas) {
                contentStream.newLineAtOffset(0, -25);
                contentStream.showText("• " + cuenta.getNombreBanco() + " - " + 
                    cuenta.getNumeroCuenta() + ": $" + cuenta.getSaldoTotal());
                yPosition -= 25;
            }
            contentStream.endText();
            
            // Transacciones (últimas 10)
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
            contentStream.newLineAtOffset(100, yPosition - 30);
            contentStream.showText("MIS TRANSACCIONES (Últimas 10)");
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            
            int contador = 0;
            yPosition -= 55;
            for(Transaccion transaccion : transacciones) {
                if(contador >= 10) break;
                if(yPosition < 100) break; // No salirse de la página
                
                String nombreCategoria = "Sin categoría";
                if(transaccion.getCategoria() != null) {
                    nombreCategoria = transaccion.getCategoria().getNombre();
                }
                
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(String.format("• %s - %s: $%.2f (%s)", 
                    transaccion.getFecha().toString().substring(0, 10), 
                    transaccion.getTipoTransaccion(), 
                    transaccion.getMonto(), nombreCategoria));
                yPosition -= 15;
                contador++;
            }
            contentStream.endText();
            
            contentStream.close();
            document.save(rutaArchivo);
            document.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean crearPDFAdmin(String rutaArchivo, List<Usuario> usuarios, 
                                List<Transaccion> transacciones, 
                                List<Cuenta> cuentas) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            // Título
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("REPORTE ADMINISTRATIVO - SISTEMA COMPLETO");
            contentStream.endText();
            
            // Estadísticas del sistema
            double saldoTotal = usuarios.stream().mapToDouble(u -> u.getSaldo()).sum();
            double totalIngresos = transacciones.stream()
                .filter(t -> t.getTipoTransaccion().contains("DEPÓSITO"))
                .mapToDouble(t -> t.getMonto()).sum();
            double totalGastos = transacciones.stream()
                .filter(t -> t.getTipoTransaccion().contains("RETIRO"))
                .mapToDouble(t -> t.getMonto()).sum();
            
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
            contentStream.newLineAtOffset(100, 650);
            contentStream.showText("ESTADÍSTICAS DEL SISTEMA");
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.newLineAtOffset(0, -25);
            contentStream.showText("Usuarios registrados: " + usuarios.size());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Cuentas activas: " + cuentas.size());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Transacciones totales: " + transacciones.size());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Saldo total sistema: $" + String.format("%.2f", saldoTotal));
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Total ingresos: $" + String.format("%.2f", totalIngresos));
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Total gastos: $" + String.format("%.2f", totalGastos));
            contentStream.endText();
            
            // Lista de usuarios
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
            contentStream.newLineAtOffset(100, 500);
            contentStream.showText("USUARIOS DEL SISTEMA");
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            
            int yPosition = 470;
            for(Usuario usuario : usuarios) {
                if(yPosition < 100) break;
                long numCuentas = cuentas.stream()
                    .filter(c -> c.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                    .count();
                contentStream.newLineAtOffset(0, -25);
                contentStream.showText(String.format("• %s (%s): $%.2f - %d cuentas", 
                    usuario.getNombreCompleto(), usuario.getIdUsuario(), 
                    usuario.getSaldo(), numCuentas));
                yPosition -= 25;
            }
            contentStream.endText();
            
            contentStream.close();
            document.save(rutaArchivo);
            document.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}