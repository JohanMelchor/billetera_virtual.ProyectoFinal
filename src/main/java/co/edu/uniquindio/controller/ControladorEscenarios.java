package co.edu.uniquindio.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorEscenarios {
    private Stage stagePrincipal;
    private BorderPane borderPrincipal;

    public ControladorEscenarios(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }

    public void cargarEscena() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/co.edu.uniquindio/BilleteraVirtualApp.fxml"));
            borderPrincipal = (BorderPane) loader.load();
            Scene scene = new Scene(borderPrincipal);
            stagePrincipal.setScene(scene);
            stagePrincipal.show();
        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
