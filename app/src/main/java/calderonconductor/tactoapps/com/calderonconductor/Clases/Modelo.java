package calderonconductor.tactoapps.com.calderonconductor.Clases;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import calderonconductor.tactoapps.com.calderonconductor.BuildConfig;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoHistorial;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductorTerceros;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoValidarUsuario;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoValidarUsuario.OnValidarUsuarioChangeListener;

/**
 * Created by tactomotion on 27/07/16.
 */
public class Modelo implements ComandoHistorial.OnOrdenesHistorialChangeListener, ComandoOrdenesConductorTerceros.OnOrdenesConductorTercerosChangeListener{



    private static Modelo ourInstance = new Modelo();
    public String ordenHoy = "";
    public int estadoTimer = 0;
    public Timer myTimer = null;
    public String distanciasRecorrida;
    ComandoOrdenesConductor comandoOrdenCondutr = new ComandoOrdenesConductor();
    ComandoOrdenesConductorTerceros comandoOrdenesConductorTerceros = new ComandoOrdenesConductorTerceros(this);
    ComandoHistorial comandoHistorial  = new ComandoHistorial(this);

    public Conductor conductor = new Conductor();
    public Cliente cliente = new Cliente();

    private OnModeloChangelistener mListener;

    public String uid ="";
    public int contadorNotificaciones =0;
    public double latitud;
    public double longitud;


    public double latitudAnterior;
    public double longitudAnterior;

    public double latitudD;
    public double longitudD;
    public double latitudO;
    public double longitudO;


    public Location cLoc;   //cuerrent Location
    public Location sLoc;    // start location
    public Location eLoc;    //end location
    public Location aLoc;    //anterior location


    public int acumPlanos=0;
    public int acumCurvos=0;

    public boolean ocupado =false;
    public String idGpsUbicacion  = "";
    public String tipoConductor  = "";
    public String placa  = "";
    public  String estadoConductorPendiente = "";
    public int boleandatosCondutor =0;

    //para utilizar en la seleccion de fechas
    public String[] fechas = {"Hoy", "Mañana", "Pasado Mañana"};
    public SimpleDateFormat df2 = new SimpleDateFormat("EEEE, MMMM dd");
    private SimpleDateFormat df = new SimpleDateFormat("EEEE, MMMM dd");
    public SimpleDateFormat dfsimple = new SimpleDateFormat("dd/MM/yyyy");



    // actualizacion app
    public Params params = new Params();
    public Sistema sistema = new Sistema();

    ComandoValidarUsuario comandoValidarUsuario;


    public interface OnModelRecargarListener {
        void terminoPrecarga();

    }


    public static Modelo getInstance() {
        return ourInstance;
    }

    private Modelo() {
    }

    //varable privada para que solo accedan por el metodo getOrdenes
    private ArrayList<OrdenConductor> ordenes = new ArrayList<OrdenConductor>();
    public ArrayList<OrdenConductor> historial = new ArrayList<OrdenConductor>();
    public ArrayList<String> pueblos = new ArrayList<String>();
    public ArrayList<OrdenConductor> filtrohistorialmes = new ArrayList<OrdenConductor>();
    public ArrayList<Ubicacion> ubicaionGps = new ArrayList<Ubicacion>();
    public ArrayList<String> tiposVehiculosTerceros = new ArrayList<String>();



    //conductores tercero
    public PropietarioVehiculo propietarioVehiculo = new PropietarioVehiculo();
    public ConductoresTerceros conductoresTerceros = new ConductoresTerceros();
    public Vehiculo vehiculo = new Vehiculo();
    public ConductorDocumentosTerceros conductorDocumentosTerceros = new ConductorDocumentosTerceros();
    public InformacionGeneral informacionGeneral = new InformacionGeneral();
    public Llantas llantas = new Llantas();



    public Hashtable razonesSociales = new Hashtable();

    public void setModeloListener(OnModeloChangelistener mListener){

        this.mListener  = mListener;
    }



    //metodo para evitar items repetidos y adicionar
    public void adicionarNuevaOrden(OrdenConductor nueva) {


        if (nueva.getConductor() != null && !nueva.getConductor().equals(uid)) {
            return;
        }

        if (!conductor.getEstado().equals("Aprobado") && esTercero()){
            return;
        }

        if (nueva.getEstado().equals("SinConfirmar")) {
            return;
        }

        if (params.hasRegistroInmediato) {   //En autoline carga todo


        } else {
            if (nueva.ofertadaATerceros && !esTercero()) {
                return;
            }

            if (nueva.getEstado().equals("NoAsignado")  && !esTercero()){
                return;
            }

            if (nueva.getEstado().equals("NoAsignado")  && !nueva.ofertadaATerceros ){
                return;
            }

        }

        if ( params.ocultarAsignacionTerceros && esTercero()){      //premium o integral!!! un de las dos
            if (!nueva.estaEnRangoDeMostrarATerceros(params.ocultarAsignacionTercerosMinutos)){
                return;
            }

        }


        Iterator<OrdenConductor> iterator = ordenes.iterator();
        while (iterator.hasNext()) {
            OrdenConductor orden = iterator.next();
            if (orden.getId().equals(nueva.getId())) {
                iterator.remove();
            }
        }


        ordenes.add(nueva);



    }


    public ArrayList<OrdenConductor> getOrdenes(){
        Collections.sort(ordenes, new Comparator<OrdenConductor>() {
            @Override
            public int compare(OrdenConductor r1, OrdenConductor r2) {
                if (r1.getFechaEnOrigen().compareTo(r2.getFechaEnOrigen()) < 1) {
                    return -1;
                }else {
                    return 1;
                }
            }
        });


        return ordenes;
    }

    public ArrayList<OrdenConductor> getOrdenesSinOrdenar(){

        return ordenes;
    }


    //metodo para mostrar una sola  orden segun el id de la orden
    public OrdenConductor getOrden(String idOrden){
        Iterator<OrdenConductor> iterator = ordenes.iterator();
        while (iterator.hasNext()) {
            OrdenConductor orden = iterator.next();
            if (orden.getId().equals(idOrden)) {
                return  orden;
            }
        }
        return null;
    }


    public void adicionarNuevaOrdenHistorial(OrdenConductor nuevaOrden){
        Iterator<OrdenConductor> iterator = historial.iterator();
        while (iterator.hasNext()){
            OrdenConductor  orden = iterator.next();
            if(orden.getId().equals(nuevaOrden.getId())){
                iterator.remove();
            }
        }
        historial.add(nuevaOrden);
    }


    public void eliminarOrden(String idOrden){
        Iterator<OrdenConductor> iterator = ordenes.iterator();
        while (iterator.hasNext()){
            OrdenConductor  orden = iterator.next();
            if(orden.getId().equals(idOrden)){
                iterator.remove();
            }
        }
    }

    public ArrayList<OrdenConductor> getHistorial(){
        return historial;
    }

    public OrdenConductor getOrdenHistorial(String idOrden){
        Iterator<OrdenConductor> iterator = historial.iterator();
        while (iterator.hasNext()) {
            OrdenConductor orden = iterator.next();
            if (orden.getId().equals(idOrden)) {
                return  orden;
            }
        }
        return null;
    }


    //metodo para cargar los  ultimos 20 del historial
    public void filtrarUltimosVeinteHistorial(){

        Collections.sort(historial, new Comparator<OrdenConductor>() {
            @Override
            public int compare(OrdenConductor lhs, OrdenConductor rhs) {
                return ((Long) lhs.getTimeStamp()).compareTo(rhs.getTimeStamp());
            }
        });

        Collections.reverse(historial);
        //recorremos el tamaño del historail hasta 20 Error
        while (historial.size() > 20) {
            historial.remove(historial.size()-1);
        }
    }


    //hashtable que me contiene el listado de las razones sociales
  public ArrayList<String> getRazonessocialesDisponibles(){
      ArrayList<String> listaEmpresa = new ArrayList<String>();
      Enumeration idEmpresas = razonesSociales.keys();
      while (idEmpresas.hasMoreElements()) {
       listaEmpresa.add(""+razonesSociales.get(idEmpresas.nextElement()));

      }
      return listaEmpresa;
  }


    ////Utilitarios
    /**
     * Convierte la fecha de String a tipo Datime
     * Tiene en cuenta que el texto puede ser Hoy, Mañana, Pasado Mañana
     * @param valor
     */
    public DateTime convertToFecha(String valor) {

        if (valor.equals(fechas[0])) {
            return DateTime.now();
        } else if (valor.equals(fechas[1])) {
            return DateTime.now().plusDays(1);
        } else if (valor.equals(fechas[2])) {
            return DateTime.now().plusDays(2);
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, MMMM dd");
        DateTime dt = formatter.parseDateTime(valor);

        return  dt;
    }

    /**
     * MEtodo que dada una fecha la convierte al formato hoy , mañana y pasado mañana o EEE MMMM dd
     * @return
     */
    public String formatearFecha(Date f)
    {


        if (df.format(f).equals(df.format(new Date())))
        {
            return fechas[0];
        }

        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 1);

        if (df.format(f).equals(df.format(gc.getTime())))
        {
            return fechas[1];
        }

        gc.add(Calendar.DATE, 1);
        if (df.format(f).equals(df.format(gc.getTime())))
        {
            return fechas[2];
        }

        return df.format(f);

    }

    //cargar ciudades
    public void cargarPueblos(Context context) {

        AssetManager am = context.getAssets();


        try {

            InputStream input = am.open("Pueblos.txt");
            int size =  input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            String pueblosF = new String(buffer);
            String[] losPueblos = pueblosF.split("\n");
            for (String ciu : losPueblos) {
                pueblos.add(ciu);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    //metodo para filtrar mes y año y razon social

    public  void filtrarPorFechaYCliente(String fecha, String  rasonSocial){


        String fechaResivida ="01/"+fecha;
        Date fechas = null;
        try {
            fechas = dfsimple.parse(fechaResivida);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        DateTime fechaFiltro = new DateTime(fechas);
        filtrohistorialmes.clear();
        Iterator<OrdenConductor> iterator = historial.iterator();
        while (iterator.hasNext()) {
            OrdenConductor orden = iterator.next();
            String razonSociales =  (String) razonesSociales.get(orden.getIdCliente());
            DateTime fechaOrden = new DateTime(orden.getFechaEnOrigen());
            if (razonSociales.equals(rasonSocial)) {
                //filtrohistorialmes.add(orden);
                if (fechaFiltro.getYear()==fechaOrden.getYear() && fechaFiltro.getMonthOfYear()== fechaOrden.getMonthOfYear()){
                    filtrohistorialmes.add(orden);
                }

            }
        }

    }


   /* @Override
    public void cargoUnaOrdenesConductor() {

        if(tipoConductor.equals("conductor")) {
            if(mListener != null){
                mListener.setActualizoListadoDeServicios();
            }

        }else{
            comandoOrdenesConductorTerceros.getTodasLasOrdenesTerceros();
        }

    }*/

    @Override
    public void cargoUnaOrdenesConductorTerceros() {
        comandoOrdenesConductorTerceros.getTodasLasOrdenesTercerosCotizar();


    }

    @Override
    public void cargoUnaOrdenesConductorTercerosCoti() {

        if(mListener != null) {
            mListener.setActualizoListadoDeServicios();
        }

    }

    //transaciones
    @Override
    public void resultadoAlAsignar(boolean exitoso) {

    }

    @Override
    public void resultadoAlAsignarCancel(boolean cancel) {

    }



    @Override
    public void cargoHistorial() {

        if(mListener != null){
            mListener.setActualizoListadoDeServicios();
        }

    }



    public interface OnModeloChangelistener {

        void setActualizoListadoDeServicios();


    }
    public void llamarServicios(){


        comandoOrdenCondutr.getTodasLasOrdenes();

    }

    public void llamarServiciosHistorial(){

        comandoHistorial.getTodasLasOrdenesHistorial();
    }


    public void limpiarregistro(){

        //propietarioVehiculo
        propietarioVehiculo.setNombre("");
        propietarioVehiculo.setDireccion("");
        propietarioVehiculo.setNit_cc("");
        propietarioVehiculo.setTelefono("");
        propietarioVehiculo.setTipo("");
        propietarioVehiculo.setNombrePropietario("");
        propietarioVehiculo.setApelidoPropietario("");

        //conductoresTerceros


        conductoresTerceros.setActivo(false);
        conductoresTerceros.setApellido("");
        conductoresTerceros.setCedula("");
        conductoresTerceros.setCelular("");
        conductoresTerceros.setCorreo("");
        conductoresTerceros.setDireccion("");
        conductoresTerceros.setEstado("");
        conductoresTerceros.setFoto("");
        conductoresTerceros.setNombre("");
        conductoresTerceros.setOcupado(false);
        conductoresTerceros.setTokenDevice("");
        conductoresTerceros.setPasswordConductorTercero("");
        conductoresTerceros.setCategoria_licencia("");
        conductoresTerceros.setLicencia_de_transito("");

        //vehiculo
        vehiculo.setFecha_Expedicion_Tarjeta_Propiedad("");
        vehiculo.setMarca("");
        vehiculo.setPlaca("");
        vehiculo.setTipo("");

        //conductorDocumentosTerceros
        conductorDocumentosTerceros.setTxt_tarjetaexpedicion("");
        conductorDocumentosTerceros.setTxt_soat_desde("");
        conductorDocumentosTerceros.setTxt_soat_hasta("");
        conductorDocumentosTerceros.setTxt_poliza_contractual_desde("");
        conductorDocumentosTerceros.setTxt_poliza_contractual_hasta("");
        conductorDocumentosTerceros.setTxt_poliza_riesgo_desde("");
        conductorDocumentosTerceros.setTxt_poliza_riesgo_hasta("");
        conductorDocumentosTerceros.setTxt_tarjeta_desde("");
        conductorDocumentosTerceros.setTxt_tarjeta_hasta("");
        conductorDocumentosTerceros.setTxt_revicion_desde("");
        conductorDocumentosTerceros.setTxt_revicion_hasta("");

        //informacionGeneral
        informacionGeneral.setAire_Acondicionado(false);
        informacionGeneral.setBotiquin(false);
        informacionGeneral.setEstado_Mecanico(false);
        informacionGeneral.setIluminacion_Externa(false);
        informacionGeneral.setIluminación_Interna(false);
        informacionGeneral.setKit_de_carretera(false);
        informacionGeneral.setLatoneria_y_Pintura(false);


        //llantas
        llantas.setDelantera_derecha(false);
        llantas.setDelantera_izquierda(false);
        llantas.setTrasera_derecha(false);
        llantas.setTrasera_izquierda(false);
        llantas.setRepuesto(false);

        tiposVehiculosTerceros.clear();
    }





    public String puntoDeMil(String txtprecio){

        String precio = txtprecio;
        String temp = "";

        int precio2 = 0;


        try {
            precio = txtprecio.replace(" ", "");
            Locale colombia = new Locale("es", "CO");
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(colombia);
            defaultFormat.setMaximumFractionDigits(0);
            precio = precio.replace(".", "");
            precio2 = Integer.parseInt(precio.replace(" ", "").replaceAll("\\s", "").trim());
            temp = defaultFormat.format(precio2);
            temp = temp.replace(",", ".");
            temp = temp.replace("$", "");



        } catch (RuntimeException r) {
            Log.i("MONEY", r.getLocalizedMessage());
            Log.i("MONEY", r.getMessage());

        }
        return temp;

    }


    public static String getFechaHora(){
        DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy h:mm a");

        String fecha = hourdateFormat.format(new Date());
        fecha = fecha.replace("a. m.","AM");
        fecha = fecha.replace("p. m.","PM");
        fecha = fecha.replace("a.m.","AM");
        fecha = fecha.replace("p.m.","PM");
        fecha = fecha.replace("am","AM");
        fecha = fecha.replace("pm","PM");

        return fecha;
    }


    public static String getHora(Date hora){
        DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy h:mm a");

        DateFormat hourFormat = new SimpleDateFormat("hh:mm a");

        String fecha = hourFormat.format(hora);
        fecha = fecha.replace("a. m.","AM");
        fecha = fecha.replace("p. m.","PM");
        fecha = fecha.replace("a.m.","AM");
        fecha = fecha.replace("p.m.","PM");
        fecha = fecha.replace("am","AM");
        fecha = fecha.replace("pm","PM");

        return fecha;
    }

    public boolean esTercero(){
        if (tipoConductor.equals("conductorTercero")){
            return true;
        }

        return false;
    }

    public void updateLastLocation(Location cLoc){

        if (!params.hasRegistroInmediato){
            return;
        }

        for (OrdenConductor orden:ordenes){
            orden.updateLastDistancia(cLoc);
        }
    }


    /// No es que sea solo para AUTOLINE era solo para ponerle un nombre representativo
    public ArrayList<OrdenConductor> getOrdenesOrdenadasParaAutoline(){

        ArrayList<OrdenConductor> ordenadas = new ArrayList<>();

          Collections.sort(ordenes, new Comparator<OrdenConductor>() {
            @Override
            public int compare(OrdenConductor r1, OrdenConductor r2) {
                if (r1.distanciaConduciendo < r2.distanciaConduciendo) {
                    return -1;
                }else {
                    return 1;
                }
            }
        });



        for (OrdenConductor orden:ordenes){

            if (orden.getEstado().equals("NoAsignado")  && orden.esFactibleEnDistancia(params)){
                ordenadas.add(orden);
                continue;
            }


            if (orden.getEstado().equals("Asignado") && orden.servicioInmediato == false){
                ordenadas.add(0, orden);
                continue;

            }

        }

        for (OrdenConductor orden:ordenes){

            if (orden.getEstado().equals("Asignado") && orden.servicioInmediato){
                ordenadas.add(0, orden);
                continue;
            }


            if (orden.getEstado().equals("En Camino")){
                ordenadas.add(0, orden);
                continue;
            }



            if (orden.getEstado().equals("Transportando")){
                ordenadas.add(0, orden);
                continue;
            }

            if (orden.getEstado().equals("Cancelado")){
                ordenadas.add(0, orden);
                continue;
            }

        }

        return ordenadas;


    }


    public ArrayList<OrdenConductor> getOrdenesSoloMias(){

        ArrayList<OrdenConductor> ordenadas = new ArrayList<>();

        Collections.sort(ordenes, new Comparator<OrdenConductor>() {
            @Override
            public int compare(OrdenConductor r1, OrdenConductor r2) {
                if (r1.getFechaEnOrigen().compareTo(r2.getFechaEnOrigen()) < 0) {
                    return -1;
                }else {
                    return 1;
                }
            }
        });



        for (OrdenConductor orden:ordenes){

            if (orden.getEstado().equals("Asignado")){
                ordenadas.add(orden);
                continue;
            }


            if (orden.getEstado().equals("En Camino")){
                ordenadas.add( orden);
                continue;
            }



            if (orden.getEstado().equals("Transportando")){
                ordenadas.add( orden);
                continue;
            }

            if (orden.getEstado().equals("Cancelado")){
                ordenadas.add( orden);
                continue;
            }

        }

        return ordenadas;


    }


}
