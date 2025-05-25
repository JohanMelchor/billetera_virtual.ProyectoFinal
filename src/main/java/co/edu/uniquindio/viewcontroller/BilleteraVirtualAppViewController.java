package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.controller.*;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class BilleteraVirtualAppViewController {
    
    private UsuarioController usuarioController;
    private CuentaController cuentaController;
    private TransaccionController transaccionController;
    private PresupuestoController presupuestoController;
    private CategoriaController categoriaController;
    
    private String idUsuarioActual;
    private UsuarioDto usuarioActual;
    private boolean esAdmin;
    
    @FXML
    private BorderPane mainBorderPane;
    
    @FXML
    private Label lblUsuarioActual;

    @FXML
    private Label lblUsuarioActualA;
    
    @FXML
    private Button btnUsuarios;
    
    @FXML
    private Button btnCuentas;
    
    @FXML
    private Button btnTransacciones;
    
    @FXML
    private Button btnPresupuestos;
    
    @FXML
    private Button btnCategorias;
    
    @FXML
    private Button btnEstadisticas;
    
    @FXML
    private Button btnPerfil;
    
    @FXML
    private Button btnCerrarSesion;
    
    @FXML
    void initialize() {
        try {
            usuarioController = new UsuarioController();
            cuentaController = new CuentaController();
            transaccionController = new TransaccionController();
            presupuestoController = new PresupuestoController();
            categoriaController = new CategoriaController();
            
            // Configuración inicial de la interfaz
            configurarVistaPredeterminada();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de inicialización", 
                         "Error al cargar la aplicación", 
                         "Ha ocurrido un error durante la inicialización: " + e.getMessage());
        }
    }
    
    private void configurarVistaPredeterminada() {
        // Ocultar botones de administrador por defecto
        btnUsuarios.setVisible(false);
        btnEstadisticas.setVisible(false);
        
        // Configurar mensaje por defecto
        lblUsuarioActual.setText("Usuario: No identificado");
        lblUsuarioActualA.setText("Usuario: No identificado");
    }
    
    public void iniciarSesionUsuario(String idUsuario) {
        this.idUsuarioActual = idUsuario;
        this.esAdmin = false;
        
        if(cargarDatosUsuario()) {
            lblUsuarioActual.setText("Usuario : " + usuarioActual.nombreCompleto());
            lblUsuarioActualA.setText("Bienvenido: " + usuarioActual.nombreCompleto());
            configurarVistaUsuario();
        }
    }

    public void iniciarSesionAdmin(String idAdmin) {
        this.idUsuarioActual = idAdmin;
        this.esAdmin = true;
        
        String nombreAdmin = usuarioController.obtenerNombreAdmin(idAdmin);
        lblUsuarioActual.setText("Administrador : " + nombreAdmin);
        lblUsuarioActualA.setText("Bienvenido : " + nombreAdmin);
        configurarVistaAdmin();
    }

    private boolean cargarDatosUsuario() {
        List<UsuarioDto> usuarios = usuarioController.obtenerUsuarios();
        
        for(UsuarioDto usuario : usuarios) {
            if(usuario.idUsuario().equals(idUsuarioActual)) {
                this.usuarioActual = usuario;
                return true;
            }
        }
        
        return false; // Usuario no encontrado
    }

    private void configurarVistaUsuario() {
        // Ocultar funciones de administrador
        btnUsuarios.setVisible(false);
        btnEstadisticas.setVisible(false);
        btnCategorias.setVisible(false);
        // Mostrar funciones de usuario normal
        btnPerfil.setVisible(true);
        btnCuentas.setVisible(true);
        btnTransacciones.setVisible(true);
        btnPresupuestos.setVisible(true);
    }

    private void configurarVistaAdmin() {
        // Mostrar funciones de administrador
        btnPerfil.setVisible(false);
        btnUsuarios.setVisible(true);
        btnEstadisticas.setVisible(true);
        btnCuentas.setVisible(true);
        btnTransacciones.setVisible(true);
        btnPresupuestos.setVisible(true);
        btnCategorias.setVisible(true);
        
        // Cambiar estilo para indicar que es admin
        lblUsuarioActual.setStyle("-fx-text-fill: red;");
        lblUsuarioActualA.setStyle("-fx-text-fill: red;");
    }
    
    
    
    @FXML
    void onUsuarios(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/usuarios.fxml", "Gestión de Usuarios");
    }
    
    @FXML
    void onCuentas(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/cuenta.fxml", "Gestión de Cuentas");
    }
    
    @FXML
    void onTransacciones(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/transaccion.fxml", "Gestión de Transacciones");
    }
    
    @FXML
    void onPresupuestos(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/presupuesto.fxml", "Gestión de Presupuestos");
    }
    
    @FXML
    void onCategorias(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/categoria.fxml", "Gestión de Categorías");
    }
    
    @FXML
    void onEstadisticas(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/estadistica.fxml", "Estadísticas");
    }

    @FXML
    void onPerfil(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/perfil.fxml", "Perfil");
    }
    
    @FXML
    void onCerrarSesion(ActionEvent event) {
        try {
            // Preguntar al usuario si realmente desea cerrar sesión
            boolean confirmarCierre = mostrarConfirmacion("Cerrar Sesión", 
                                                        "¿Está seguro que desea cerrar la sesión?");
            if (!confirmarCierre) {
                return;
            }
            
            // Limpiar datos de sesión
            idUsuarioActual = null;
            usuarioActual = null;
            
            // Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/login.fxml"));
            Parent root = loader.load();
            
            // Mostrar la vista
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Billetera Virtual - Inicio de Sesión");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar la vista", 
                         "No se pudo cargar la vista de login: " + e.getMessage());
        }
    }
    
    private void cargarVista(String FXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML));
            Parent vista = loader.load();
            
            // Si la vista tiene un método para recibir el ID del usuario, lo usamos
            Object controller = loader.getController();
            
            // Verificar que exista un usuario actual antes de inicializar controladores
            if (idUsuarioActual == null || idUsuarioActual.isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Usuario no identificado", 
                             "Debe iniciar sesión para acceder a esta funcionalidad");
                return;
            }
            
            if(controller instanceof UsuariosViewController) {
                // No hace falta pasar nada, la vista gestiona todos los usuarios
            } else if(controller instanceof CuentaViewController) {
                ((CuentaViewController) controller).inicializarVista(idUsuarioActual, esAdmin);
            } else if(controller instanceof TransaccionViewController) {
                ((TransaccionViewController) controller).inicializarVista(idUsuarioActual, esAdmin);
            } else if(controller instanceof PresupuestoViewController) {
                ((PresupuestoViewController) controller).inicializarVista(idUsuarioActual, esAdmin);
            }else if(controller instanceof PerfilViewController) {
                ((PerfilViewController) controller).inicializarConUsuario(idUsuarioActual);
            } else if(controller instanceof CategoriaViewController) {
                // No hace falta pasar nada, las categorías son globales
            } else if(controller instanceof EstadisticasViewController) {
                // Para estadísticas, no necesita un usuario específico pero se puede
            }
            
            mainBorderPane.setCenter(vista);
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar la vista", 
                         "No se pudo cargar la vista '" + FXML + "': " + e.getMessage());
        } catch (IllegalStateException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Recurso no encontrado", 
                         "No se encontró el archivo: " + FXML);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error inesperado", 
                         "Ha ocurrido un error al cargar la vista: " + e.getMessage());
        }
    }
    
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String header, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
    
    private boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}