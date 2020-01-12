package calderonconductor.tactoapps.com.calderonconductor.Clases;

/**
 * Created by tacto on 1/10/17.
 */

public class PropietarioVehiculo {

    private String direccion = "";
    private String nit_cc = "";
    private String nombre = "";
    private String telefono = "";
    private String tipo = "";

    private String nombrePropietario = "";
    private String apelidoPropietario = "";


    public  PropietarioVehiculo(){

    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNit_cc() {
        return nit_cc;
    }

    public void setNit_cc(String nit_cc) {
        this.nit_cc = nit_cc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public String getApelidoPropietario() {
        return apelidoPropietario;
    }

    public void setApelidoPropietario(String apelidoPropietario) {
        this.apelidoPropietario = apelidoPropietario;
    }
}
