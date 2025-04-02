package co.edu.uniquindio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Cambiar
 */
public class BilleteraVirtualApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BilleteraVirtualApp.class.getResource("BilleteraVirtualApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("App Billetera virtual");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}