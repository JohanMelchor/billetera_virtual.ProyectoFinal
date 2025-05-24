package co.edu.uniquindio.model;

public class Presupuesto {
    private String idPresupuesto;
    private String nombre;
    private Double montoAsignado;
    private Double montoGastado;
    private Categoria categoria;
    private Usuario usuario;
    private Double saldo;   
    private Cuenta Cuenta;

    public Presupuesto(String idPresupuesto, String nombre, Double montoAsignado, Double montoGastado, 
                      Categoria categoria, Usuario usuario, Double saldo, Cuenta cuenta) {
        this.idPresupuesto = idPresupuesto;
        this.nombre = nombre;
        this.montoAsignado = montoAsignado;
        this.montoGastado = montoGastado;
        this.categoria = categoria;
        this.usuario = usuario;
        this.saldo = saldo;
        this.Cuenta = cuenta;
    }

    public String getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(String idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getMontoAsignado() {
        return montoAsignado;
    }

    public void setMontoAsignado(Double montoAsignado) {
        this.montoAsignado = montoAsignado;
    }

    public Double getMontoGastado() {
        return montoGastado;
    }

    public void setMontoGastado(Double montoGastado) {
        this.montoGastado = montoGastado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Cuenta getCuenta() {
        return Cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        Cuenta = cuenta;
    }

    public void aumentarSaldo(Double monto) {
        this.saldo += monto;
    }

    public boolean reducirSaldo(Double monto) {
        if (this.saldo >= monto) {
            this.saldo -= monto;
            return true;
        }
        return false;
    }

}