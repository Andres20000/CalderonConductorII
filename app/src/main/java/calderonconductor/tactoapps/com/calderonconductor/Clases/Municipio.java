package calderonconductor.tactoapps.com.calderonconductor.Clases;

import java.util.Date;

/**
 * Created by tactomotion on 21/09/16.
 */
public class Municipio {

    String id;
    String municipio;
    String direcion;
    String  timestamp;


    public Municipio(){

    }


    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }


    public void setMunicipio(String municipio){
        this.municipio = municipio;
    }

    public String getMunicipio(){
        return this.municipio;
    }


    public void setDirecion(String direcion){
        this.direcion = direcion;
    }

    public String getDirecion(){
        return this.direcion;
    }



    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getTimestamp(){
        return this.timestamp;
    }


}