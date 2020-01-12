package calderonconductor.tactoapps.com.calderonconductor.Clases;

/**
 * Created by tacto on 1/10/17.
 */

public class ConductoresTerceros {


    private boolean activo = false;
    private String apellido = "";
    private String cedula = "";
    private String celular = "";
    private String correo = "";
    private String direccion = "";
    private String estado = "";
    private String foto = "";
    private String nombre = "";
    private boolean ocupado = false;
    private String tokenDevice = "";
    private String passwordConductorTercero = "";
    private String categoria_licencia = "";
    private String  licencia_de_transito = "";


    public  ConductoresTerceros(){

    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public String getTokenDevice() {
        return tokenDevice;
    }

    public void setTokenDevice(String tokenDevice) {
        this.tokenDevice = tokenDevice;
    }

    public String getPasswordConductorTercero() {
        return passwordConductorTercero;
    }

    public void setPasswordConductorTercero(String passwordConductorTercero) {
        this.passwordConductorTercero = passwordConductorTercero;
    }

    public String getCategoria_licencia() {
        return categoria_licencia;
    }

    public void setCategoria_licencia(String categoria_licencia) {
        this.categoria_licencia = categoria_licencia;
    }

    public String getLicencia_de_transito() {
        return licencia_de_transito;
    }

    public void setLicencia_de_transito(String licencia_de_transito) {
        this.licencia_de_transito = licencia_de_transito;
    }
}
