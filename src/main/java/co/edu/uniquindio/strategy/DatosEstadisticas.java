package co.edu.uniquindio.strategy;

import co.edu.uniquindio.mapping.dto.CategoriaDto;
import co.edu.uniquindio.mapping.dto.TransaccionDto;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.facade.BilleteraFacade;
import java.util.List;

public class DatosEstadisticas {
    private final List<UsuarioDto> usuarios;
    private final List<TransaccionDto> transacciones;
    private final List<CategoriaDto> categorias;
    private final BilleteraFacade facade;
    
    public DatosEstadisticas(List<UsuarioDto> usuarios, List<TransaccionDto> transacciones, 
                           List<CategoriaDto> categorias, BilleteraFacade facade) {
        this.usuarios = usuarios;
        this.transacciones = transacciones;
        this.categorias = categorias;
        this.facade = facade;
    }
    
    // Getters
    public List<UsuarioDto> getUsuarios() { return usuarios; }
    public List<TransaccionDto> getTransacciones() { return transacciones; }
    public List<CategoriaDto> getCategorias() { return categorias; }
    public BilleteraFacade getFacade() { return facade; }
}

