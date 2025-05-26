package co.edu.uniquindio.controller;

import co.edu.uniquindio.factory.ModelFactory;

public class ReporteController {
    private ModelFactory modelFactory;

    public ReporteController() {
        modelFactory = ModelFactory.getInstance();
    }

    public boolean generarReporteUsuario(String idUsuario, String rutaArchivo) {
        return modelFactory.generarReporteUsuario(idUsuario, rutaArchivo);
    }

    public boolean generarReporteAdmin(String rutaArchivo) {
        return modelFactory.generarReporteAdmin(rutaArchivo);
    }
}