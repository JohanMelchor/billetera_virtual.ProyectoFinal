package co.edu.uniquindio.template;

import co.edu.uniquindio.facade.BilleteraFacade;
import co.edu.uniquindio.mapping.dto.UsuarioDto;
import co.edu.uniquindio.service.IAlertaManager;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class UsuarioCrudTemplate extends CrudTemplate<UsuarioDto> {
    private final BilleteraFacade facade;
    private final TextField txtIdUsuario;
    private final TextField txtNombreUsuario;
    private final TextField txtCorreoUsuario;
    private final TextField txtTelefonoUsuario;
    private final TextField txtDireccionUsuario;
    private final TextField txtSaldo;
    private final TableView<UsuarioDto> tableUsuario;
    private final Runnable recargarCallback;
    private UsuarioDto usuarioSeleccionado;
    
    public UsuarioCrudTemplate(
            BilleteraFacade facade,
            IAlertaManager alertaManager,
            TextField txtIdUsuario,
            TextField txtNombreUsuario,
            TextField txtCorreoUsuario,
            TextField txtTelefonoUsuario,
            TextField txtDireccionUsuario,
            TextField txtSaldo,
            TableView<UsuarioDto> tableUsuario,
            Runnable recargarCallback) {
        
        super(alertaManager);
        this.facade = facade;
        this.txtIdUsuario = txtIdUsuario;
        this.txtNombreUsuario = txtNombreUsuario;
        this.txtCorreoUsuario = txtCorreoUsuario;
        this.txtTelefonoUsuario = txtTelefonoUsuario;
        this.txtDireccionUsuario = txtDireccionUsuario;
        this.txtSaldo = txtSaldo;
        this.tableUsuario = tableUsuario;
        this.recargarCallback = recargarCallback;
    }
    
    public void setUsuarioSeleccionado(UsuarioDto usuario) {
        this.usuarioSeleccionado = usuario;
    }
    
    @Override
    protected UsuarioDto crearObjeto() {
        return new UsuarioDto(
            txtIdUsuario.getText(),
            txtNombreUsuario.getText(),
            txtCorreoUsuario.getText(),
            txtTelefonoUsuario.getText(),
            txtDireccionUsuario.getText(),
            txtSaldo.getText(),
            "****"
        );
    }
    
    @Override
    protected boolean validarDatos(UsuarioDto usuario) {
        return usuario.nombreCompleto() != null && !usuario.nombreCompleto().isEmpty() &&
               usuario.idUsuario() != null && !usuario.idUsuario().isEmpty() &&
               usuario.correo() != null && !usuario.correo().isEmpty() &&
               usuario.telefono() != null && !usuario.telefono().isEmpty() &&
               usuario.direccion() != null && !usuario.direccion().isEmpty() &&
               usuario.saldo() != null && !usuario.saldo().isEmpty();
    }
    
    @Override
    protected boolean ejecutarAgregar(UsuarioDto usuario) {
        return facade.agregarUsuario(usuario);
    }
    
    @Override
    protected boolean ejecutarActualizar(UsuarioDto usuario) {
        return facade.actualizarUsuario(usuario);
    }
    
    @Override
    protected boolean ejecutarEliminar() {
        return facade.eliminarUsuario(usuarioSeleccionado.idUsuario());
    }
    
    @Override
    protected boolean tieneSeleccion() {
        return usuarioSeleccionado != null;
    }
    
    @Override
    protected String getNombreEntidad() {
        return "Usuario";
    }
    
    @Override
    protected void recargarDatos() {
        recargarCallback.run();
    }
    
    @Override
    protected void limpiarCampos() {
        txtCorreoUsuario.setText("");
        txtDireccionUsuario.setText("");
        txtIdUsuario.setText("");
        txtNombreUsuario.setText("");
        txtTelefonoUsuario.setText("");
        txtSaldo.setText("");
        usuarioSeleccionado = null;
        tableUsuario.getSelectionModel().clearSelection();
    }
}
