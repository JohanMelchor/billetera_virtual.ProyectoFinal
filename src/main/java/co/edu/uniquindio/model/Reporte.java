package co.edu.uniquindio.model;

import co.edu.uniquindio.model.Builder.ReporteBuilder;
import java.time.LocalDate;

public class Reporte {
    private String idReporte;
    private String tipoReporte;
    private LocalDate rangoFechas;
    private String formato;

    public Reporte() {
    }

    public Reporte(String idReporte, String tipoReporte, LocalDate rangoFechas, String formato) {
        this.idReporte = idReporte;
        this.tipoReporte = tipoReporte;
        this.rangoFechas = rangoFechas;
        this.formato = formato;
    }
    public static ReporteBuilder builder() {
        return new ReporteBuilder();
    }

    public String getIdReporte() {
        return idReporte;
    }
    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }
    public String getTipoReporte() {
        return tipoReporte;
    }
    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }
    public LocalDate getRangoFechas() {
        return rangoFechas;
    }
    public void setRangoFechas(LocalDate rangoFechas) {
        this.rangoFechas = rangoFechas;
    }
    public String getFormato() {
        return formato;
    }
    public void setFormato(String formato) {
        this.formato = formato;
    }
}
