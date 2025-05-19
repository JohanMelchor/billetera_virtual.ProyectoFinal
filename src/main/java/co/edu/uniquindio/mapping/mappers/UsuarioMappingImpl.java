package co.edu.uniquindio.mapping.mappers;

import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.model.Usuario;
import co.edu.uniquindio.service.IUsuarioMapping;

import java.util.ArrayList;
import java.util.List;

public class UsuarioMappingImpl implements IUsuarioMapping {
    @Override
    public List<UsuarioDto> getUsuarioDto(List<Usuario> listaUsuarios) {
        if(listaUsuarios == null)return null;
        List<UsuarioDto> listaUsuariosDto = new ArrayList<UsuarioDto>(listaUsuarios.size());
        for (Usuario usuario : listaUsuarios){
            listaUsuariosDto.add(usuarioToUsuarioDto(usuario));
        }

        return listaUsuariosDto;
    }

    @Override
    public UsuarioDto usuarioToUsuarioDto(Usuario usuario) {
        return new UsuarioDto(usuario.getIdUsuario(),usuario.getNombreCompleto(), usuario.getCorreo(),
                usuario.getTelefono(),usuario.getDireccion(),
                String.valueOf(usuario.getSaldo()),usuario.getPassword());
    }

    @Override
    public Usuario usuarioDtoToUsuario(UsuarioDto usuarioDto) {
        return new Usuario(usuarioDto.idUsuario(),usuarioDto.nombreCompleto(),
                usuarioDto.correo(),usuarioDto.telefono(),usuarioDto.direccion(),
                Double.parseDouble(usuarioDto.saldo()),usuarioDto.password());
    }
}
