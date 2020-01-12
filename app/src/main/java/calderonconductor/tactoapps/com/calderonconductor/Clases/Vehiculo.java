package calderonconductor.tactoapps.com.calderonconductor.Clases;

import java.util.ArrayList;

/**
 * Created by tacto on 1/10/17.
 */

public class Vehiculo {

    private String fecha_Expedicion_Tarjeta_Propiedad;
    private String marca;
    private String placa;
    private String tipo;
    public String referencia;
    public String ccolor;
    public String modelo;



    public  Vehiculo(){


    }

    public String getFecha_Expedicion_Tarjeta_Propiedad() {
        return fecha_Expedicion_Tarjeta_Propiedad;
    }

    public void setFecha_Expedicion_Tarjeta_Propiedad(String fecha_Expedicion_Tarjeta_Propiedad) {
        this.fecha_Expedicion_Tarjeta_Propiedad = fecha_Expedicion_Tarjeta_Propiedad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
