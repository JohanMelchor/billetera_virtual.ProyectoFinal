package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.Util.DataUtil;
import co.edu.uniquindio.controller.UsuarioController;
import co.edu.uniquindio.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginUsuarioViewController {
    UsuarioController controller=new UsuarioController();

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private TextField txtContraseña;

    @FXML
    private TextField txtCorreo;

    @FXML
    void onIniciarSesion(ActionEvent event) {
        String correo=txtCorreo.getText();
        String contrasenia=txtContraseña.getText();
        Usuario usserAccount=controller.usuarioExist(correo, contrasenia);
        iniciarSesion(usserAccount);
    }

    private void iniciarSesion(Usuario usserAccount) {
        if (usserAccount != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/BilleteraVirtualApp.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            DataUtil.mostrarMensaje("Sesion invalida", "Inicio sesion fallido", "No se pudo iniciar sesion correo o contraseña invalidos",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    void initialize(){
    }
}


