package calderonconductor.tactoapps.com.calderonconductor.Clases;

import java.util.Date;

/**
 * Created by tactomotion on 21/09/16.
 */
public class Calificacion {

    String id;
    Date fecha;
    String obervacion;
    String pasajero;
    String  timestamp;
    String valor;


    public Calificacion(){

    }


    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void setFecha(Date fecha){
        this.fecha = fecha;
    }

    public Date getFecha(){
        return this.fecha;
    }

    public void setObervacion(String obervacion){
        this.obervacion = obervacion;
    }

    public String getObervacion(){
        return this.obervacion;
    }

    public void setPasajero(String pasajero){
        this.pasajero = pasajero;
    }

    public String getPasajero(){
        return this.pasajero;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getTimestamp(){
        return this.timestamp;
    }


    public void setValor(String valor){
        this.valor = valor;
    }

    public String getValor(){
        return this.valor;
    }
}