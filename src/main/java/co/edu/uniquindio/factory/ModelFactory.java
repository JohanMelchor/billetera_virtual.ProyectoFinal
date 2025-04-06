package co.edu.uniquindio.factory;

import co.edu.uniquindio.BilleteraVirtualApp;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.mapping.mappers.UsuarioMappingImpl;
import co.edu.uniquindio.model.BilleteraVirtual;
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
}
