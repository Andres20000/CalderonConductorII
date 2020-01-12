package calderonconductor.tactoapps.com.calderonconductor.Clases;

/**
 * Created by tactomotion on 1/09/16.
 */
public class Cliente {
    private String activo;
    private String celular;
    private String consecutivoOrden;
    private String correo;
    private String direccion;
    private String inicialesConsecutivo;
    private String logo;
    private String nit;
    private String nombreContacto;
    private String razonSocial;
    public int precioHora;
    public int precioKm;
    public int tarifaMinima;


    public  Cliente(){

    }



    public void setActivo(String activo){
        this.activo = activo;
    }

    public String getActivo(){
        return this.activo;
    }

    public void setCelular(String celular){
        this.celular = celular;
    }

    public String getCelular(){
        return this.celular;
    }

    public void setConsecutivoOrden(String consecutivoOrden){
        this.consecutivoOrden = consecutivoOrden;
    }

    public String getConsecutivoOrden(){
        return this.consecutivoOrden;
    }

    public void setCorreo(String correo){
        this.correo = correo;
    }

    public String getCorreo(){
        return this.correo;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public String getDireccion(){
        return this.direccion;
    }

    public void setInicialesConsecutivo(String inicialesConsecutivo){
        this.inicialesConsecutivo = inicialesConsecutivo;
    }

    public String getInicialesConsecutivo(){
        return this.inicialesConsecutivo;
    }

    public void setLogo(String logo){
        this.logo = logo;
    }

    public String getLogo(){
        return this.logo;
    }

    public void setNit(String nit){
        this.nit = nit;
    }

    public String getNit(){
        return this.nit;
    }

    public void setNombreContacto(String nombreContacto){
        this.nombreContacto = nombreContacto;
    }

    public String getNombreContacto(){
        return this.nombreContacto;
    }


    public void setRazonSocial(String razonSocial){
        this.razonSocial = razonSocial;
    }

    public String getRazonSocial(){
        return this.razonSocial;
    }



}
