package co.edu.uniquindio.mapping.dto;

public record PresupuestoDto(String idPresupuesto,
                            String nombre,
                            String montoAsignado,
                            String montoGastado,
                            String idCategoria,
                            String idUsuario,
                            String saldo,
                            String idCuenta) {
}