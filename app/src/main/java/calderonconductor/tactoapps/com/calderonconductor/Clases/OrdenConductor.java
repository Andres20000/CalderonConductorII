package calderonconductor.tactoapps.com.calderonconductor.Clases;

import android.util.Log;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import android.location.Location;

import org.joda.time.DateTime;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by tactomotion on 2/08/16.
 */
public class OrdenConductor {

    private String origen;
    private String estado;
    private String destino;
    private Date fechaEnOrigen;
    private String fechaEnOrigenEs;
    private String hora;
    private String tiempo_restante;
    private String direcion_destino;
    private String id;
    private String asignadoPor;
    private String conductor ;
    private String cosecutivoOrden;
    private String direccionOrigen;
    private String fechaEnDestino;
    private String horaEnDestino;
    private String horaGeneracion;
    private String idCliente;
    private String matricula;
    public ArrayList<Pasajero> pasajeros = new ArrayList<Pasajero>();
    public ArrayList<Calificacion> calificaciones = new ArrayList<Calificacion>();
    public ArrayList<Municipio> municipios = new ArrayList<Municipio>();
    public ArrayList<Ubicacion> ubicacionGPss = new ArrayList<Ubicacion>();
    public Ubicacion ubiOrigen = null;
    public boolean conSonidoDireccion = false;


    public ArrayList<Retraso> retrasos = new ArrayList<>();
    private String ruta;
    private String solicitadoPor;
    private String tarifa;
    private String trayectos;
    private Cliente cliente = new Cliente();
    private Long timeStamp;
    boolean ofertadaATerceros;
    private String observaciones;
    private int cotizacion =0;
    private String diasServicio = "";
    private String horasServicio = "";

    private int numeroOfertas;

    public int precioHora;          //precio por hora que le pusieron a esa orden especifica
    public int precioKm;
    public int precioHoraOrden;     //precio por hora del cliente por defecto en esa epoca del tiempo
    public int precioKmOrden;

    public boolean envioPuntoRecogida = false;
    public boolean servicioInmediato = true;

    public long distanciaConduciendo  = -1;
    private String valorCarrera;


    public ArrayList<CotizacionesTerceros> cotizacionesT = new ArrayList<CotizacionesTerceros>();




    public  OrdenConductor(String key){
        Log.i("CREAR ORDEN", key);

        this.id = key;
    }

    public  OrdenConductor(){
        Log.i("CREAR ORDEN", "Creo orden");

    }

    public void setTimeStamp(Long timeStamp){
        this.timeStamp = timeStamp;
    }

    public Long getTimeStamp(){
        return this.timeStamp;
    }

    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }

    public Cliente getCliente(){
        return this.cliente;
    }

    public void setOrigen(String origen){
        this.origen = origen;
    }

    public String getOrigen( ){
        return this.origen;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    public String getEstado( ){
        return this.estado;
    }

    public void setDestino(String destino){
        this.destino = destino;
    }

    public String getDestino( ){
        return this.destino;
    }

    public int getNumeroOfertas() {
        return numeroOfertas;
    }

    public void setNumeroOfertas(int numeroOfertas) {
        this.numeroOfertas = numeroOfertas;
    }

    public void setFechaEnOrigen(Date fechaEnOrigen){
        this.fechaEnOrigen = fechaEnOrigen;
    }

    public Date getFechaEnOrigen(){
        return this.fechaEnOrigen;
    }


    public void setFechaEnOrigenEs(String fechaEnOrigenEs){
        this.fechaEnOrigenEs = fechaEnOrigenEs;
    }

    public String getFechaEnOrigenEs(){
        return this.fechaEnOrigenEs;
    }



    public void setHora(String hora){
        this.hora = hora;
    }

    public String getHora( ){
        return this.hora;
    }


    public void setDireccionDestino(String direccionDestino){
        this.direcion_destino = direccionDestino;
    }

    public String getDireccionDestino(){
        return this.direcion_destino;
    }


    public void setTiempoRestante(String tiempo_restante){
        this.tiempo_restante = tiempo_restante;
    }

    public String getTiempoRestante(){
        return this.tiempo_restante;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }


    public void setAsignadoPor(String asignadoPor){
        this.asignadoPor = asignadoPor;
    }

    public String getAsignadoPor(){
        return this.asignadoPor;
    }

    public void setConductor(String conductor){
        this.conductor = conductor;
    }

    public String getConductor(){
        return this.conductor;
    }


    public void setCosecutivoOrden(String cosecutivoOrden){
        this.cosecutivoOrden = cosecutivoOrden;
    }

    public String getCosecutivoOrden(){
        return this.cosecutivoOrden;
    }

    public void setDireccionOrigen(String direccionOrigen){
        this.direccionOrigen = direccionOrigen;
    }

    public String getDireccionOrigen(){
        return this.direccionOrigen;
    }


    public void setFechaEnDestino(String fechaEnDestino){
        this.fechaEnDestino = fechaEnDestino;
    }

    public String getFechaEnDestino(){
        return this.fechaEnDestino;
    }


    public void setHoraEnDestino(String horaEnDestino){
        this.horaEnDestino = horaEnDestino;
    }

    public String getHoraEnDestino(){
        return this.horaEnDestino;
    }

    public void setHoraGeneracion(String horaGeneracion){
        this.horaGeneracion = horaGeneracion;
    }

    public String getHoraGeneracion(){
        return this. horaGeneracion;
    }

    public void setIdCliente(String idCliente){
        this.idCliente = idCliente;
    }

    public String getIdCliente(){
        return this.idCliente;
    }
    public void setMatricula(String matricula){
        this.matricula = matricula;
    }

    public String getMatricula(){
        return this.matricula;
    }


    public void setRuta(String ruta){
        this.ruta = ruta;
    }

    public String getRuta(){

        return this.ruta;
    }
    public void setSolicitadoPor(String solicitadoPor){
        this.solicitadoPor = solicitadoPor;
    }

    public String getSolicitadoPor(){
        return this.solicitadoPor;
    }

    public void setTarifa(String tarifa){
        this.tarifa = tarifa;
    }

    public String getTarifa(){
        return this.tarifa;
    }

    public void setTrayectos(String trayectos){
        this.trayectos = trayectos;
    }

    public String getTrayectos(){
        return this.trayectos;
    }


    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(int cotizacion) {
        this.cotizacion = cotizacion;
    }


    public String getDiasServicio() {
        return diasServicio;
    }

    public void setDiasServicio(String diasServicio) {
        this.diasServicio = diasServicio;
    }

    public String getHorasServicio() {
        return horasServicio;
    }

    public void setHorasServicio(String horasServicio) {
        this.horasServicio = horasServicio;
    }

    //metodo para mostrar una sola  orden segun el id de la orden
    public Pasajero getPasajero(String idPasajero){
        Iterator<Pasajero> iterator = pasajeros.iterator();
        while (iterator.hasNext()) {
            Pasajero pasajero = iterator.next();
            if (pasajero.getIdPasajero().equals(idPasajero)) {
                return  pasajero;
            }
        }
        return null;
    }


    //metodo para evitar item repetidos
    public void adicionarNuevoPasajero(Pasajero nuevoPasajero){
        try {
        Iterator<Pasajero> iterator = pasajeros.iterator();
        while (iterator.hasNext()){
            Pasajero  pasajero = iterator.next();
            if(pasajero.getIdPasajero().equals(nuevoPasajero.getIdPasajero())){
                iterator.remove();
            }
        }
        }
        catch (Exception e){

        }
        pasajeros.add(0, nuevoPasajero);
    }


    //sacamos el promedio de la calificacion segun la orden
    public Double getCalificacionPromedio(){
        Double promedio = 0.0;
        Double valorCalificacion;
        Iterator<Calificacion> iterator = calificaciones.iterator();
        Double cantidad = Double.parseDouble(""+calificaciones.size());
        while (iterator.hasNext()) {
            Calificacion calificacion = iterator.next();
            valorCalificacion = Double.parseDouble(calificacion.getValor());
            promedio = promedio+valorCalificacion;

        }
        promedio = promedio/cantidad;
        return promedio;
    }



    //metodo para mostrar una sola  orden segun el id del municipio
    public Municipio getMunicipio(String idMunicipio){
        Iterator<Municipio> iterator = municipios.iterator();
        while (iterator.hasNext()) {
            Municipio municipio = iterator.next();
            if (municipio.getId().equals(idMunicipio)) {
                return  municipio;
            }
        }
        return null;
    }

    //metodo para evitar item repetidos
    public void adicionarNuevoMunicipio(Municipio nuevoMunicipio){
        Iterator<Municipio> iterator = municipios.iterator();
        while (iterator.hasNext()){
            Municipio  municipio = iterator.next();
            if(municipio.getId().equals(nuevoMunicipio.getId())){
                iterator.remove();
            }
        }
        municipios.add(nuevoMunicipio);
    }

    //ubicacion
    public Ubicacion getUbicaionGps(String uid){
        Iterator<Ubicacion> iterator = ubicacionGPss.iterator();
        while (iterator.hasNext()) {
            Ubicacion ubicacion = iterator.next();
            if (ubicacion.getPasajero().equals(uid)) {
                return  ubicacion;
            }
        }
        return null;
    }


    //metodo para evitar item repetidos
    public void adicionarUbicaionGps(Ubicacion nuevaUbicaionGps){
        Iterator<Ubicacion> iterator = ubicacionGPss.iterator();
        while (iterator.hasNext()){
            Ubicacion  ubicacion = iterator.next();
            if(ubicacion.getPasajero().equals(nuevaUbicaionGps.getPasajero())){
                iterator.remove();
            }
        }
        ubicacionGPss.add(nuevaUbicaionGps);
    }


    public boolean getOfertadaATerceros() {
        return ofertadaATerceros;
    }

    public void setOfertadaATerceros(boolean ofertadaATerceros) {
        this.ofertadaATerceros = ofertadaATerceros;
    }



    public boolean cotizacionPorTiempo(){

        if(numeroOfertas == -1){

            return true;
        }
        return false;
    }


    public boolean hayRetrazoAbierto(){

        if (retrasos.size() == 0 ){
            return false;
        }

        for (Retraso retra : retrasos ){
            if (retra.startTime == 0 ) {
                return true;
            }

            if (retra.startTime > 0  && retra.fechaFin == null) {
                return true;
            }

        }

        return false;

    }

    public Pasajero getPrimerPasajero(){
        if (pasajeros == null || pasajeros.size() == 0){
            return  null;
        }

        return pasajeros.get(0);
    }


    public boolean estaEnRangoDeMostrarATerceros(int ocultarAsignacionTercerosMinutos) {

        if (Utility.calcularMinutosEntresFechas(Utility.convertStringConHoraToDate(Utility.getFechaHora()), Utility.convertStringConHoraToDate( Utility.convertDateToString(fechaEnOrigen)+ " " + hora)) < ocultarAsignacionTercerosMinutos){
            return true;
        }

        return false;

    }

    public void updateLastDistancia(Location cLoc){

        if (cLoc == null){
            return;
        }

        try {

            Location targetLocation = new Location(""); //provider name is unnecessary
            targetLocation.setLatitude(ubiOrigen.lat); //your coords of course
            targetLocation.setLongitude(ubiOrigen.lon);

            distanciaConduciendo = (long) cLoc.distanceTo(targetLocation);


        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void updateLastDistanciaPro(Location cLoc){

        DirectionsResult result = null;
        DateTime now = new DateTime();

        if (cLoc == null){
            return;
        }

            try {


                 LatLng ori = new LatLng(cLoc.getLatitude(), cLoc.getLongitude());
                 LatLng des = new LatLng(ubiOrigen.lat, ubiOrigen.lon);

                Log.i("DISTANCIA:", "INICIO");
                result = DirectionsApi.newRequest(getGeoContext()).mode(TravelMode.DRIVING)
                         .origin(ori)
                        .destination(des)
                         .departureTime(now).await();
                Log.i("DISTANCIA:", "FIN");

                distanciaConduciendo =  getDistanciaConduciendo(result);

            }catch (ApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }


    }

    private  long getDistanciaConduciendo(DirectionsResult results){


        if (results == null || results.routes == null){
            return -1;
        }

        if (results.routes.length == 0 ){
            return -1;
        }
        if (results.routes[0].legs.length == 0 ){
            return -1;
        }

        return results.routes[0].legs[0].distance.inMeters;
    }


    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(30)
                .setApiKey("AIzaSyAr-LcaNMYFR6_4BsSSjfkjh-Gkrgv9u3Q")
                .setConnectTimeout(30, TimeUnit.SECONDS)
                .setReadTimeout(30, TimeUnit.SECONDS)
                .setWriteTimeout(30, TimeUnit.SECONDS);
    }

    public boolean esAsignadaDeOtro(String uid){

        if (estado.equals("Asignado") || estado.equals("En Camino") || estado.equals("Transportando")){
            if (conductor == null || !conductor.equals(uid)){
                return true;
            }
        }
        return false;

    }


    public boolean esFactibleEnDistancia(Params param){
        Log.i("INF:", "Distancia=" + distanciaConduciendo + "    direccion destino  " + direccionOrigen );

        if (servicioInmediato && distanciaConduciendo < param.radioNormal){
            return true;
        }

        if (!servicioInmediato && distanciaConduciendo < param.radioProgramadas){
            return true;
        }

        return false;


    }


    public String getValorCarrera() {
        return valorCarrera;
    }

    public void setValorCarrera(String valorCarrera) {
        this.valorCarrera = valorCarrera;
    }
}
