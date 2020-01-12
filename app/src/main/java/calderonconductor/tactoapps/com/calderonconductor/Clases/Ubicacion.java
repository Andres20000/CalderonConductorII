package calderonconductor.tactoapps.com.calderonconductor.Clases;

/**
 * Created by tacto on 7/07/17.
 */

public class Ubicacion {

    double lat;
    double lon;
    String pasajero;
    Long timestamp;
    String pathUbicacion;


    double latOrigen;
    double lonOrigen;

    double latDestino;
    double lonDestino;


    public Ubicacion(){

    }


    public void setLat(double lat){
        this.lat = lat;
    }

    public double getLat(){
        return this.lat;
    }


    public void setLon(double lon){
        this.lon = lon;
    }

    public double getLon(){
        return this.lon;
    }

    public void setPasajero(String pasajero){
        this.pasajero = pasajero;
    }

    public String getPasajero(){
        return this.pasajero;
    }

    public void setTimestamp(Long timestamp){
        this.timestamp = timestamp;
    }

    public Long getTimestamp(){
        return this.timestamp;
    }


    public void setPathUbicacion(String pathUbicacion){
        this.pathUbicacion = pathUbicacion;
    }

    public String getPathUbicacion(){
        return this.pathUbicacion;
    }

    public double getLatDestino() {
        return latDestino;
    }

    public void setLatDestino(double latDestino) {
        this.latDestino = latDestino;
    }

    public double getLonDestino() {
        return lonDestino;
    }

    public void setLonDestino(double lonDestino) {
        this.lonDestino = lonDestino;
    }

    public double getLatOrigen() {
        return latOrigen;
    }

    public void setLatOrigen(double latOrigen) {
        this.latOrigen = latOrigen;
    }

    public double getLonOrigen() {
        return lonOrigen;
    }

    public void setLonOrigen(double lonOrigen) {
        this.lonOrigen = lonOrigen;
    }
}

