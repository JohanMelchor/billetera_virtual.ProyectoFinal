package co.edu.uniquindio.factory;

import co.edu.uniquindio.BilleteraVirtualApp;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.mapping.mappers.UsuarioMappingImpl;
import co.edu.uniquindio.model.BilleteraVirtual;
import co.edu.uniquindio.model.Usuario;
import co.edu.uniquindio.model.Builder.UsuarioBuilder;
import co.edu.uniquindio.service.IModelFactoryServices;
import co.edu.uniquindio.service.IUsuarioMapping;


import java.util.List;

public class ModelFactory implements IModelFactoryServices {
    private static ModelFactory modelFactory;
    private BilleteraVirtual billeteraVirtual;
    private IUsuarioMapping usuarioMapping;

    private ModelFactory(){
        usuarioMapping= new UsuarioMappingImpl();
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

        Usuario usuario1 = Usuario.builder().NombreCompleto("juan").IdUsuario("123").Correo("juan@gmail.com").Telefono("321").Direccion("cs 1").Saldo(1000.0).build();
        Usuario usuario2 = Usuario.builder().NombreCompleto("johan").IdUsuario("124").Correo("johan@gmail.com").Telefono("421").Direccion("cs 2").Saldo(2000.0).build();
        Usuario usuario3 = Usuario.builder().NombreCompleto("felipe").IdUsuario("125").Correo("felipe@gmail.com").Telefono("521").Direccion("cs 3").Saldo(3000.0).build();
        Usuario usuario4 = Usuario.builder().NombreCompleto("sofia").IdUsuario("126").Correo("sofia@gmail.com").Telefono("621").Direccion("cs 4").Saldo(4000.0).build();
        Usuario usuario5 = Usuario.builder().NombreCompleto("maria").IdUsuario("127").Correo("maria@gmail.com").Telefono("721").Direccion("cs 5").Saldo(5000.0).build();

        billeteraVirtual.getListaUsuarios().add(usuario1);
        billeteraVirtual.getListaUsuarios().add(usuario2);
        billeteraVirtual.getListaUsuarios().add(usuario3);
        billeteraVirtual.getListaUsuarios().add(usuario4);
        billeteraVirtual.getListaUsuarios().add(usuario5);
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
    public boolean eliminarUsuario(UsuarioDto usuarioDto) {
        return billeteraVirtual.eliminarUsuario(usuarioMapping.usuarioDtoToUsuario(usuarioDto));
    }

    @Override
    public boolean actualizarUsuario(UsuarioDto usuarioDto) {
        return billeteraVirtual.actualizarUsuario(usuarioMapping.usuarioDtoToUsuario(usuarioDto));
    }
}
