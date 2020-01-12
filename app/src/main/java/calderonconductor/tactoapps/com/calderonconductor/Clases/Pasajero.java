package calderonconductor.tactoapps.com.calderonconductor.Clases;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by tactomotion on 30/08/16.
 */
public class Pasajero {


    String idPasajero;
    String apellido;
    String celular ="";
    String conApp;
    String correo;
    String foto;
    String idCliente;
    String nombre;
    String rol;
    String tokenDevice;
    String tipo;
    public ArrayList<MisPasajeros> misPasajeros = new ArrayList<MisPasajeros>();


    public  Pasajero(){

    }


    public void setIdPasajero(String idPasajero){
        this.idPasajero = idPasajero;
    }

    public String getIdPasajero(){
        return this.idPasajero;
    }

    public void setApellido(String apellido){
        this.apellido = apellido;
    }

    public String getApellido(){
        return this.apellido;
    }


    public void setCelular(String celular){
        this.celular = celular;
    }

    public String getCelular(){
        return this.celular;
    }

    public void setConApp(String conApp){
        this.conApp = conApp;
    }

    public String getConApp(){
        return this.conApp;
    }


    public void setCorreo(String correo){
        this.correo = correo;
    }

    public String getCorreo(){
        return this.correo;
    }

    public void setFoto(String foto){
        this.foto = foto;
    }

    public String getFoto( ){
        return this.foto;
    }

    public void setIdCliente(String idCliente){
        this.idCliente = idCliente;
    }

    public String getIdCliente(){
        return this.idCliente;
    }

    public void setNombre(String nombre){
        Log.i("PASA:", "Nombre " + nombre);

        this.nombre = nombre;
    }

    public String getNombre(){
        return this.nombre;
    }


    public void setRol(String rol){
        this.rol = rol;
    }

    public String getRol( ){
        return this.rol;
    }


    public void setTokenDevice(String tokenDevice){
        this.tokenDevice = tokenDevice;
    }

    public String getTokenDevice( ){
        return this.tokenDevice;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public  String getNombreCompleto(){
        return nombre + " " + apellido;
    }
}
