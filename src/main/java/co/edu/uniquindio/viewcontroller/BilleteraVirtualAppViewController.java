package co.edu.uniquindio.viewcontroller;

import co.edu.uniquindio.command.GestorComandos;
import co.edu.uniquindio.controller.*;
import co.edu.uniquindio.decorator.*;
import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.observer.*;
import co.edu.uniquindio.proxy.IOperacionesSeguras;
import co.edu.uniquindio.proxy.ProxySeguridad;
import javafx.application.Platform;
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

public class BilleteraVirtualAppViewController implements SaldoObserver {
    
    private UsuarioController usuarioController;
    private CuentaController cuentaController;
    private TransaccionController transaccionController;
    private PresupuestoController presupuestoController;
    private CategoriaController categoriaController;
    
    // Patrones integrados
    private BilleteraFacade billeteraFacade;
    private IOperacionesSeguras operacionesSeguras;
    private GestorComandos gestorComandos;
    private NotificacionUIObserver notificacionObserver;
    
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
    private Button btnDeshacer;
    
    @FXML
    private Button btnRehacer;
    
    @FXML
    private ListView<String> listNotificacionesRapidas;
    
    @FXML
    private Label lblResumenFinanciero;
    
    @FXML
    void initialize() {
        try {
            // Inicializar controladores tradicionales
            usuarioController = new UsuarioController();
            cuentaController = new CuentaController();
            transaccionController = new TransaccionController();
            presupuestoController = new PresupuestoController();
            categoriaController = new CategoriaController();
            
            // Inicializar patrones
            inicializarPatrones();
            
            // Configuración inicial de la interfaz
            configurarVistaPredeterminada();
            
            // Configurar observadores
            configurarObservadores();
            
            // Configurar controles avanzados
            configurarControlesAvanzados();
            
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de inicialización", 
                         "Error al cargar la aplicación", 
                         "Ha ocurrido un error durante la inicialización: " + e.getMessage());
        }
    }
    
    private void inicializarPatrones() {
        // Inicializar Facade
        billeteraFacade = BilleteraFacade.getInstance();
        
        // Inicializar Proxy de seguridad
        operacionesSeguras = new ProxySeguridad();
        
        // Inicializar Command Manager
        gestorComandos = GestorComandos.getInstance();
        
        // Configurar observadores del sistema
        ConfiguradorObservers.configurarObserversDefault();
        notificacionObserver = ConfiguradorObservers.obtenerNotificacionUI();
    }
    
    private void configurarObservadores() {
        // Registrar este controlador como observador
        GestorSaldos.getInstance().agregarObserver(this);
        GestorSaldos.getInstance().agregarObserver(notificacionObserver);
    }
    
    private void configurarControlesAvanzados() {
        // Configurar botones de comando
        if (btnDeshacer != null) {
            btnDeshacer.setDisable(true);
            btnDeshacer.setOnAction(e -> onDeshacer());
        }
        
        if (btnRehacer != null) {
            btnRehacer.setDisable(true);
            btnRehacer.setOnAction(e -> onRehacer());
        }
        
        // Configurar lista de notificaciones rápidas
        if (listNotificacionesRapidas != null) {
            listNotificacionesRapidas.setPrefHeight(80);
        }
        
        // Actualizar estado de botones de comando cada segundo
        Platform.runLater(() -> {
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        Platform.runLater(this::actualizarEstadoBotonesComando);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }).setDaemon(true).start();
        });
    }
    
    private void actualizarEstadoBotonesComando() {
        if (btnDeshacer != null) {
            btnDeshacer.setDisable(!gestorComandos.puedeDeshacer());
        }
        if (btnRehacer != null) {
            btnRehacer.setDisable(!gestorComandos.puedeRehacer());
        }
    }
    
    @Override
    public void actualizar(EventoSaldo evento) {
        // Solo procesar eventos del usuario actual
        if (idUsuarioActual != null && evento.getIdUsuario().equals(idUsuarioActual)) {
            Platform.runLater(() -> {
                actualizarNotificacionesRapidas();
                actualizarResumenFinanciero();
            });
        }
    }
    
    private void actualizarNotificacionesRapidas() {
        if (listNotificacionesRapidas != null && notificacionObserver != null) {
            List<String> notificaciones = notificacionObserver.getNotificaciones();
            listNotificacionesRapidas.getItems().clear();
            
            // Mostrar solo las últimas 3 notificaciones
            int start = Math.max(0, notificaciones.size() - 3);
            for (int i = start; i < notificaciones.size(); i++) {
                listNotificacionesRapidas.getItems().add(notificaciones.get(i));
            }
        }
    }
    
    private void actualizarResumenFinanciero() {
        if (lblResumenFinanciero != null && idUsuarioActual != null) {
            try {
                BilleteraFacade.ResumenFinanciero resumen = billeteraFacade.obtenerResumenFinanciero(idUsuarioActual);
                String textoResumen = String.format("Saldo Total: $%.2f | Gastos: $%.2f | Ingresos: $%.2f", 
                                                   resumen.getSaldoTotal(), resumen.getGastosMes(), resumen.getIngresosMes());
                lblResumenFinanciero.setText(textoResumen);
            } catch (Exception e) {
                lblResumenFinanciero.setText("Error al cargar resumen financiero");
            }
        }
    }
    
    private void onDeshacer() {
        boolean exito = gestorComandos.deshacerUltimoComando();
        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Operación deshecha", 
                         "Éxito", "La última operación ha sido deshecha correctamente");
            actualizarVistas();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", 
                         "No se pudo deshacer", "No hay operaciones para deshacer o falló la operación");
        }
    }
    
    private void onRehacer() {
        boolean exito = gestorComandos.rehacerComando();
        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Operación rehecha", 
                         "Éxito", "La operación ha sido rehecha correctamente");
            actualizarVistas();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", 
                         "No se pudo rehacer", "No hay operaciones para rehacer o falló la operación");
        }
    }
    
    private void actualizarVistas() {
        // Actualizar todas las vistas para reflejar los cambios
        Platform.runLater(() -> {
            actualizarResumenFinanciero();
            actualizarNotificacionesRapidas();
        });
    }
    
    private void configurarVistaPredeterminada() {
        // Ocultar botones de administrador por defecto
        btnUsuarios.setVisible(false);
        btnEstadisticas.setVisible(false);
        
        // Configurar mensajes por defecto
        lblUsuarioActual.setText("Usuario: No identificado");
        lblUsuarioActualA.setText("Usuario: No identificado");
        
        if (lblResumenFinanciero != null) {
            lblResumenFinanciero.setText("Resumen financiero no disponible");
        }
    }
    
    public void iniciarSesionUsuario(String idUsuario) {
        this.idUsuarioActual = idUsuario;
        this.esAdmin = false;
        
        if(cargarDatosUsuario()) {
            lblUsuarioActual.setText("Usuario: " + usuarioActual.nombreCompleto());
            lblUsuarioActualA.setText("Usuario: " + usuarioActual.nombreCompleto());
            configurarVistaUsuario();
            actualizarResumenFinanciero();
            
            // Limpiar notificaciones anteriores para el nuevo usuario
            if (notificacionObserver != null) {
                notificacionObserver.limpiarNotificaciones();
            }
        }
    }

    public void iniciarSesionAdmin(String idAdmin) {
        this.idUsuarioActual = idAdmin;
        this.esAdmin = true;
        
        String nombreAdmin = usuarioController.obtenerNombreAdmin(idAdmin);
        lblUsuarioActual.setText("Administrador: " + nombreAdmin);
        lblUsuarioActualA.setText("Administrador: " + nombreAdmin);
        configurarVistaAdmin();
        
        if (lblResumenFinanciero != null) {
            lblResumenFinanciero.setText("Vista de administrador - Acceso total al sistema");
        }
    }

    private boolean cargarDatosUsuario() {
        List<UsuarioDto> usuarios = usuarioController.obtenerUsuarios();
        
        for(UsuarioDto usuario : usuarios) {
            if(usuario.idUsuario().equals(idUsuarioActual)) {
                this.usuarioActual = usuario;
                return true;
            }
        }
        
        return false;
    }

    private void configurarVistaUsuario() {
        // Ocultar funciones de administrador
        btnUsuarios.setVisible(false);
        btnEstadisticas.setVisible(false);
        
        // Mostrar funciones de usuario normal
        btnPerfil.setVisible(true);
        btnCuentas.setVisible(true);
        btnTransacciones.setVisible(true);
        btnPresupuestos.setVisible(true);
        btnCategorias.setVisible(true);
        
        // Mostrar controles avanzados para usuarios
        if (btnDeshacer != null) btnDeshacer.setVisible(true);
        if (btnRehacer != null) btnRehacer.setVisible(true);
        if (listNotificacionesRapidas != null) listNotificacionesRapidas.setVisible(true);
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
        lblUsuarioActual.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        lblUsuarioActualA.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        
        // Mostrar controles avanzados para administradores
        if (btnDeshacer != null) btnDeshacer.setVisible(true);
        if (btnRehacer != null) btnRehacer.setVisible(true);
        if (listNotificacionesRapidas != null) listNotificacionesRapidas.setVisible(false); // Admin no necesita notificaciones personales
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
            boolean confirmarCierre = mostrarConfirmacion("Cerrar Sesión", 
                                                        "¿Está seguro que desea cerrar la sesión?");
            if (!confirmarCierre) {
                return;
            }
            
            // Cleanup de observadores
            cleanup();
            
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
    
    private void cleanup() {
        // Remover observadores
        if (GestorSaldos.getInstance() != null) {
            GestorSaldos.getInstance().removerObserver(this);
            if (notificacionObserver != null) {
                GestorSaldos.getInstance().removerObserver(notificacionObserver);
            }
        }
    }
    
    private void cargarVista(String FXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML));
            Parent vista = loader.load();
            
            Object controller = loader.getController();
            
            // Verificar que exista un usuario actual antes de inicializar controladores
            if (idUsuarioActual == null || idUsuarioActual.isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Usuario no identificado", 
                             "Debe iniciar sesión para acceder a esta funcionalidad");
                return;
            }
            
            // Inicializar controladores con el usuario actual
            inicializarControladorConUsuario(controller);
            
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
    
    private void inicializarControladorConUsuario(Object controller) {
        if(controller instanceof UsuariosViewController) {
            // Vista de administrador, no necesita usuario específico
        } else if(controller instanceof CuentaViewController) {
            ((CuentaViewController) controller).inicializarConUsuario(idUsuarioActual);
        } else if(controller instanceof TransaccionViewController) {
            ((TransaccionViewController) controller).inicializarConUsuario(idUsuarioActual);
        } else if(controller instanceof PresupuestoViewController) {
            ((PresupuestoViewController) controller).inicializarConUsuario(idUsuarioActual);
        } else if(controller instanceof PerfilViewController) {
            ((PerfilViewController) controller).inicializarConUsuario(idUsuarioActual);
        } else if(controller instanceof CategoriaViewController) {
            // Las categorías son globales, no necesita usuario específico
        } else if(controller instanceof EstadisticasViewController) {
            // Para estadísticas, puede funcionar sin usuario específico
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
    
    // Métodos públicos para acceso a los patrones desde otros controladores
    public BilleteraFacade getBilleteraFacade() {
        return billeteraFacade;
    }
    
    public IOperacionesSeguras getOperacionesSeguras() {
        return operacionesSeguras;
    }
    
    public GestorComandos getGestorComandos() {
        return gestorComandos;
    }
    
    public String getIdUsuarioActual() {
        return idUsuarioActual;
    }
    
    public boolean esAdministrador() {
        return esAdmin;
    }
}