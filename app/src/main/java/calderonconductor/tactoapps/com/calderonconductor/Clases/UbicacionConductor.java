package calderonconductor.tactoapps.com.calderonconductor.Clases;

public class UbicacionConductor {

    private double lat;
    private double lon;

    public UbicacionConductor(){

    }

    public UbicacionConductor(double Lat, double Lon){
        this.setLat(Lat);
        this.setLon(Lon);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

}
