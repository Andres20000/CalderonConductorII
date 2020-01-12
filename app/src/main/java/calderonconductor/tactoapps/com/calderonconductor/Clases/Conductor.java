package calderonconductor.tactoapps.com.calderonconductor.Clases;

/**
 * Created by tactomotion on 27/07/16.
 */
public class Conductor {

    private String nombre;
    private String apellido;
    private String celular;
    private String correo;
    private String direccion;
    private String estado;
    public boolean activo;
    public String cedula;
    public String password;


    public  Conductor(){

    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre( ){
        return this.nombre;
    }

    public void setApellido(String apellido){
        this.apellido = apellido;
    }

    public  String getApellido(){
        return this.apellido;
    }

    public void setCelular(String celular){
        this.celular = celular;
    }

    public  String getCelular(){
        return this.celular;
    }

    public void setCorreo(String correo){
        this.correo = correo;
    }

    public  String getCorreo(){
        return this.correo;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public  String getDireccion(){
        return this.direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
