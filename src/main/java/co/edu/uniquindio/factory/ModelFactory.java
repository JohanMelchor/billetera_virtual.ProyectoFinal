package co.edu.uniquindio.factory;

import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.mapping.mappers.UsuarioMappingImpl;
import co.edu.uniquindio.model.BilleteraVirtual;
import co.edu.uniquindio.model.Usuario;
import co.edu.uniquindio.service.IModelFactoryServices;
import co.edu.uniquindio.service.IUsuarioMapping;


import java.util.List;

public class ModelFactory implements IModelFactoryServices {
    private static ModelFactory modelFactory;
    private BilleteraVirtual billeteraVirtual;
    private IUsuarioMapping usuarioMapping;

    private ModelFactory(){
        inicalizarDatos();
    }

    public static ModelFactory getInstance(){
        if(modelFactory==null){
            modelFactory=new ModelFactory();
        }
        return modelFactory;
    }

    private void inicalizarDatos() {
        billeteraVirtual = new BilleteraVirtual();
        usuarioMapping = new UsuarioMappingImpl();

        Usuario usuario1 = Usuario.builder().nombreCompleto("juan").idUsuario("123").correo("juan@gmail.com").telefono("321").direccion("cs 1").saldo(1000.0).tipoUsuario("Administrador").contrasenia("1234").build();
        Usuario usuario2 = Usuario.builder().nombreCompleto("johan").idUsuario("124").correo("johan@gmail.com").telefono("421").direccion("cs 2").saldo(2000.0).tipoUsuario("Usuario").contrasenia("1234").build();
        Usuario usuario3 = Usuario.builder().nombreCompleto("felipe").idUsuario("125").correo("felipe@gmail.com").telefono("521").direccion("cs 3").saldo(3000.0).tipoUsuario("Usuario").contrasenia("1234").build();
        Usuario usuario4 = Usuario.builder().nombreCompleto("sofia").idUsuario("126").correo("sofia@gmail.com").telefono("621").direccion("cs 4").saldo(4000.0).tipoUsuario("Administrador").contrasenia("1234").build();

        billeteraVirtual.crearUsuario(usuario1);
        billeteraVirtual.crearUsuario(usuario2);
        billeteraVirtual.crearUsuario(usuario3);
        billeteraVirtual.crearUsuario(usuario4);
    }

    @Override
    public List<UsuarioDto> obtenerUsuarios() {
        return usuarioMapping.getUsuarioDto(billeteraVirtual.getListaUsuarios());
    }

    @Override
    public boolean agregarUsuario(UsuarioDto usuarioDto) {
        return billeteraVirtual.crearUsuario(usuarioMapping.usuarioDtoToUsuario(usuarioDto));
    }

    @Override
    public boolean eliminarUsuario(String idUsuairo) {
        return billeteraVirtual.eliminarUsuario(idUsuairo);
    }

    @Override
    public boolean actualizarUsuario(UsuarioDto usuarioDto) {
        return billeteraVirtual.actualizarUsuario(usuarioMapping.usuarioDtoToUsuario(usuarioDto));
    }
}
