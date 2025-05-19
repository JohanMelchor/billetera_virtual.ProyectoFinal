package co.edu.uniquindio.mapping.dto;

public record UsuarioDto (String idUsuario,
                          String nombreCompleto,
                          String correo,
                          String telefono,
                          String direccion,
                          String saldo,
                          String password) {
}
