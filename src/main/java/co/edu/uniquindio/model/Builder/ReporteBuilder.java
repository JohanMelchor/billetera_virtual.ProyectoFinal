package co.edu.uniquindio.model.Builder;
import co.edu.uniquindio.model.Reporte;

import java.time.LocalDate;

public class ReporteBuilder {
    private String idReporte;
    private String tipoReporte;
    private LocalDate rangoFechas;
    private String formato;

    
    public ReporteBuilder IdReporte(String idReporte) {
        this.idReporte = idReporte;
        return this;
    }

    public ReporteBuilder TipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
        return this;
    }
    public ReporteBuilder RangoFechas(LocalDate rangoFechas) {
        this.rangoFechas = rangoFechas;
        return this;
    }
    public ReporteBuilder Formato(String formato) {
        this.formato = formato;
        return this;
    } 

    public Reporte build() {
        return new Reporte(idReporte, tipoReporte, rangoFechas, formato);
    }
    

}