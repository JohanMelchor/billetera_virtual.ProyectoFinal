package co.edu.uniquindio.service;

import co.edu.uniquindio.strategy.DatosEstadisticas;
import javafx.scene.Node;

public interface IEstadisticaStrategy {
    Node generarGrafico(DatosEstadisticas datos);
    String getNombre();
    String getDescripcion();
}
