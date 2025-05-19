package co.edu.uniquindio.mapping.dto;

public record UsuarioDto (String idUsuario,
                          String nombreCompleto,
                          String correo,
                          String telefono,
                          String direccion,
                          String saldo,
                          String password) {
    public UsuarioDto clonar() {
        return new UsuarioDto(
            this.idUsuario,
            this.nombreCompleto,
            this.correo,
            this.telefono,
            this.direccion,
            this.saldo,
            this.password
        );
    }
}
