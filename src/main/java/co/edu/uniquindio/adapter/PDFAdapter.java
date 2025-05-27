package co.edu.uniquindio.adapter;

import co.edu.uniquindio.model.BilleteraVirtual;
import co.edu.uniquindio.model.Cuenta;
import co.edu.uniquindio.model.Transaccion;
import co.edu.uniquindio.model.Usuario;
import co.edu.uniquindio.service.IReporteAdapter;
import java.util.List;

public class PDFAdapter implements IReporteAdapter {
    private BilleteraVirtual billeteraVirtual;
    
    public PDFAdapter(BilleteraVirtual billeteraVirtual) {
        this.billeteraVirtual = billeteraVirtual;
    }

    @Override
    public boolean generarReporteUsuario(String rutaArchivo, Usuario usuario, 
                                        List<Transaccion> transacciones, List<Cuenta> cuentas) {
        // Usar tu método existente
        String rutaPDF = asegurarExtensionPDF(rutaArchivo);
        return billeteraVirtual.generarReporteUsuario(usuario.getIdUsuario(), rutaPDF);
    }

    @Override
    public boolean generarReporteAdmin(String rutaArchivo, List<Usuario> usuarios, 
                                      List<Transaccion> transacciones, List<Cuenta> cuentas) {
        // Usar tu método existente
        String rutaPDF = asegurarExtensionPDF(rutaArchivo);
        return billeteraVirtual.generarReporteAdmin(rutaPDF);
    }
    
    private String asegurarExtensionPDF(String rutaArchivo) {
        return rutaArchivo.endsWith(".pdf") ? rutaArchivo : rutaArchivo + ".pdf";
    }
}
