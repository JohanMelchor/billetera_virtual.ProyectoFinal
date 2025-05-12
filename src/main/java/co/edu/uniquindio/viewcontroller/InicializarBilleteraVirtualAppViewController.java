package co.edu.uniquindio.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class InicializarBilleteraVirtualAppViewController {
    @FXML
    private TabPane tabPane;

    /**v@FXML
    void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab.getContent() != null) {
                // Obtener la ventana actual (Stage)
                Stage stage = (Stage) tabPane.getScene().getWindow();

                // Medir el tamaño preferido del nuevo contenido
                double prefWidth = newTab.getContent().prefWidth(-1);
                double prefHeight = newTab.getContent().prefHeight(-1);

                // Ajustar tamaño de la ventana (puedes afinar márgenes si lo necesitas)
                stage.setWidth(prefWidth + 40);
                stage.setHeight(prefHeight + 80);
            }
        });
    }*/
}
