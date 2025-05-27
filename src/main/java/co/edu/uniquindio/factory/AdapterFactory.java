package co.edu.uniquindio.factory;

import co.edu.uniquindio.adapter.CSVAdapter;
import co.edu.uniquindio.adapter.ExcelAdapter;
import co.edu.uniquindio.adapter.PDFAdapter;
import co.edu.uniquindio.model.BilleteraVirtual;
import co.edu.uniquindio.service.IReporteAdapter;

public class AdapterFactory {
    public static IReporteAdapter crearAdapter(String tipoReporte, BilleteraVirtual billeteraVirtual) {
        
        switch (tipoReporte.toLowerCase()) {
            case "pdf":
                return new PDFAdapter(billeteraVirtual);
                
            case "excel":
            case "xlsx":
                return new ExcelAdapter();
                
            case "csv":
                return new CSVAdapter();
                
            default:
                // Por defecto PDF
                return new PDFAdapter(billeteraVirtual);
        }
    }
    
    public static String determinarTipo(String rutaArchivo) {
        if (rutaArchivo.toLowerCase().endsWith(".xlsx")) {
            return "excel";
        } else if (rutaArchivo.toLowerCase().endsWith(".csv")) {
            return "csv";
        } else {
            return "pdf";
        }
    }
    

    public static String[] getTiposDisponibles() {
        return new String[]{"PDF - Presentación visual","Excel - Análisis de datos", "CSV - Importar a otras apps"};
    }
}
