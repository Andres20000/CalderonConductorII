package calderonconductor.tactoapps.com.calderonconductor.Clases;

public class UbicacionConductor {

    private String matricula;
    private String color;
    private String marca;
    private String asignadoPor;
    private String estado;
    private String ofertadaATerceros;
    private String referencia;
    private String fechaHora;
    private double lat;
    private double lon;

    public UbicacionConductor(){

    }

    public UbicacionConductor(double lat, double lon, String Matricula, String Color, String Marca, String AsignadaPor, String Estado, String OfertadaATerceros, String Referencia, String FechaHora){
        this.setLat(lat);
        this.setLon(lon);
        this.setMatricula(Matricula);
        this.setColor(Color);
        this.setMarca(Marca);
        this.setAsignadoPor(AsignadaPor);
        this.setEstado(Estado);
        this.setOfertadaATerceros(OfertadaATerceros);
        this.setReferencia(Referencia);
        this.setFechaHora(FechaHora);
    }



    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getAsignadoPor() {
        return asignadoPor;
    }

    public void setAsignadoPor(String asignadoPor) {
        this.asignadoPor = asignadoPor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getOfertadaATerceros() {
        return ofertadaATerceros;
    }

    public void setOfertadaATerceros(String ofertadaATerceros) {
        this.ofertadaATerceros = ofertadaATerceros;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
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

