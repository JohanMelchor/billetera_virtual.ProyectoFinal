package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.factory.AlertaManagerFactory;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.service.IAlertaManager;
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
    private String idUsuarioActual;
    private UsuarioDto usuarioActual;
    private boolean esAdmin;
    private BilleteraFacade facade;
    private IAlertaManager alertaManager;
    
    @FXML
    private BorderPane mainBorderPane;
    
    @FXML
    private Label lblUsuarioActual;

    @FXML
    private Label lblUsuarioActualA;
    
    @FXML
    private Button btnUsuarios;

    @FXML
    private Button btnReportes;
    
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
            facade = new BilleteraFacade();
            alertaManager = AlertaManagerFactory.crearManagerCompleto();
    
            
            // Configuración inicial de la interfaz
            configurarVistaPredeterminada();
        } catch (Exception e) {
            mostrarAlerta("Error de Inicialización", "Error al iniciar la aplicación", 
                         "No se pudo inicializar la aplicación correctamente: " + e.getMessage(), Alert.AlertType.ERROR);
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
        
        String nombreAdmin = facade.obtenerNombreAdmin(idAdmin);
        lblUsuarioActual.setText("Administrador : " + nombreAdmin);
        lblUsuarioActualA.setText("Bienvenido : " + nombreAdmin);
        configurarVistaAdmin();
    }

    private boolean cargarDatosUsuario() {
        List<UsuarioDto> usuarios = facade.obtenerUsuarios();
        
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
        btnReportes.setVisible(true);
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
        btnReportes.setVisible(true);
        // Cambiar estilo para indicar que es admin
        lblUsuarioActual.setStyle("");
        lblUsuarioActualA.setStyle("");
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
    void onReportes(ActionEvent event) {
        cargarVista("/co/edu/uniquindio/reporte.fxml", "Reportes PDF");
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
            mostrarAlerta("Error", "Error al cerrar sesión", 
                         "No se pudo cerrar la sesión correctamente: " + e.getMessage(), Alert.AlertType.ERROR);
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
                mostrarAlerta("Error", "Usuario no identificado", 
                             "Debe iniciar sesión para acceder a esta vista.", Alert.AlertType.WARNING);
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
            } else if(controller instanceof ReporteViewController) {
                ((ReporteViewController) controller).inicializarVista(idUsuarioActual, esAdmin);
            }
            
            mainBorderPane.setCenter(vista);
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al cargar la vista", 
                         "No se pudo cargar la vista: " + titulo, Alert.AlertType.ERROR);
        } catch (IllegalStateException e) {
            mostrarAlerta("Error", "Error de estado", 
                         "La vista no se pudo cargar correctamente: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error inesperado", 
                         "Ha ocurrido un error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void mostrarAlerta(String titulo, String header, String contenido, Alert.AlertType tipo) {
        alertaManager.mostrarAlerta(titulo, header, contenido, tipo);
    }
    
    private boolean mostrarConfirmacion(String titulo, String mensaje) {
        return alertaManager.mostrarConfirmacion(titulo, mensaje);
    }
}