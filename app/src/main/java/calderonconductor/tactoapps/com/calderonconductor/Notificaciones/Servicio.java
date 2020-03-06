package calderonconductor.tactoapps.com.calderonconductor.Notificaciones;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import calderonconductor.tactoapps.com.calderonconductor.Adapter.OrdenesConductorAdapter;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Globales;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Municipio;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Pasajero;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Rechazo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Retraso;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Ubicacion;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdPasajero;
import calderonconductor.tactoapps.com.calderonconductor.particular.ListaServiciosParticularAdapter;
import calderonconductor.tactoapps.com.calderonconductor.principal.ListaServicios;

public class Servicio extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub


        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed android


        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, restartServicePI);

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        llamarOrdenes();
        //start a separate thread and start listening to your network object
    }

    boolean cambiodetectado = false;
    Modelo modelo = Modelo.getInstance();
    private  void llamarOrdenes(){
        try{
            final Modelo modelo;
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            modelo = Modelo.getInstance();

            DatabaseReference ref = database.getReference("ordenes/pendientes");
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snap, String s) {
                    final OrdenConductor nuevaOrden = readDatosOrdennesConductor(snap);
                    if(nuevaOrden != null){

                        boolean id_conductor_encontrado = false;
                        for(Rechazo tmp : nuevaOrden.getRechazos()){
                            if(tmp.getId().equals(modelo.uid)){
                                id_conductor_encontrado = true;
                                return;
                            }
                        }

                        if(orden_temporal != null){
                            if(orden_temporal.getEstado() == nuevaOrden.getEstado()){
                                id_conductor_encontrado = true;
                            }
                        }


                        if(id_conductor_encontrado == false){

                            if(nuevaOrden.getEstado().equals("PreAsignado")){

                                orden_temporal = new OrdenConductor();
                                orden_temporal = nuevaOrden;

                                final int idaleatorio = new Random().nextInt((10000 - 10) + 1) + 10;
                                Notificacion not_1 = new Notificacion();
                                not_1.Notificacion(
                                        Servicio.this,
                                        "Nuevo Servicio",
                                        nuevaOrden.getDestino() + " " + nuevaOrden.getDireccionDestino(),
                                        "",
                                        idaleatorio,
                                        1,
                                        false,
                                        false);

                                //not_1.Notificacion(this,);

                                @SuppressLint("InvalidWakeLockTag")
                                PowerManager.WakeLock screenLock =    ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                                screenLock.acquire();
                                screenLock.release();

                                Intent alarmIntent = new Intent("android.intent.action.MAIN");
                                alarmIntent.putExtra("id",nuevaOrden.getId());
                                alarmIntent.putExtra("conductor",modelo.uid);

                                alarmIntent.setClass(Servicio.this, vista_nuevo_servicio.class);
                                alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                final Window win= Globales.listaservicios.getWindow();
                                win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                                win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                                Servicio.this.startActivity(alarmIntent);
                            }
                        }
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot snap, String s) {

                    final OrdenConductor nuevaOrden = readDatosOrdennesConductor(snap);
                    if(nuevaOrden != null){

                        boolean id_conductor_encontrado = false;
                        for(Rechazo tmp : nuevaOrden.getRechazos()){
                            if(tmp.getId().equals(modelo.uid)){
                                id_conductor_encontrado = true;
                                return;
                            }
                        }

                        if(orden_temporal != null){
                            if(orden_temporal.getEstado() == nuevaOrden.getEstado()){
                                id_conductor_encontrado = true;
                            }
                        }


                        if(id_conductor_encontrado == false){

                            if(nuevaOrden.getEstado().equals("PreAsignado")){

                                orden_temporal = new OrdenConductor();
                                orden_temporal = nuevaOrden;

                                final int idaleatorio = new Random().nextInt((10000 - 10) + 1) + 10;
                                Notificacion not_1 = new Notificacion();
                                not_1.Notificacion(
                                        Servicio.this,
                                        "Nuevo Servicio",
                                        nuevaOrden.getDestino() + " " + nuevaOrden.getDireccionDestino(),
                                        "",
                                        idaleatorio,
                                        1,
                                        false,
                                        false);

                                //not_1.Notificacion(this,);

                                @SuppressLint("InvalidWakeLockTag")
                                PowerManager.WakeLock screenLock =    ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                                screenLock.acquire();
                                screenLock.release();

                                Intent alarmIntent = new Intent("android.intent.action.MAIN");
                                alarmIntent.putExtra("id",nuevaOrden.getId());
                                alarmIntent.putExtra("conductor",modelo.uid);

                                alarmIntent.setClass(Servicio.this, vista_nuevo_servicio.class);
                                alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



                                final Window win= Globales.listaservicios.getWindow();
                                win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                                win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                                Servicio.this.startActivity(alarmIntent);
                            }
                        }
                    }

                }

                @Override
                public void onChildRemoved(DataSnapshot snap) {
                    final OrdenConductor nuevaOrden = readDatosOrdennesConductor(snap);

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.v("dataSnapshot",dataSnapshot.getKey());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.v("dataSnapshot",databaseError.getMessage());
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),
                    Toast.LENGTH_LONG).show();

        }

    }

    private OrdenConductor orden_temporal;

    private void QuitarVentana(){
        if(orden_temporal != null){
            Globales.ventana_nuevo_servicio.finish();
        }

    }

    private void ordenarPintar(){

    try{

        if (!modelo.ocupado) {

            if (modelo.params.hasRegistroInmediato) {
                if (modelo.getOrdenesOrdenadasParaAutoline().size() > 0){



                }else {


                    if(cambiodetectado == true){
                        if(modelo.getOrdenes().size() > 0){

                        }


                    }



                }

            }else{

            }
        }else {  //ocupado
            if (modelo.params.hasRegistroInmediato) {

            }

        }

    } catch (Exception e){
        Toast.makeText(getApplicationContext(),e.getMessage().toString(),
                Toast.LENGTH_LONG).show();

    }





    }


    private static OrdenConductor readDatosOrdennesConductor(DataSnapshot snap) {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");


        OrdenConductor nuevaOrden = new OrdenConductor(snap.getKey());

        if (!snap.hasChild("origen")) {

            return null;
        }

        nuevaOrden.setOrigen(snap.child("origen").getValue().toString());
        nuevaOrden.setEstado(snap.child("estado").getValue().toString());

        if (snap.hasChild("destino")) {
            nuevaOrden.setDestino(snap.child("destino").getValue().toString());
        }else {
            nuevaOrden.setDestino(snap.child("origen").getValue().toString());
        }



        if (snap.hasChild("precioHora")) {
            nuevaOrden.precioHora = Integer.parseInt(snap.child("precioHora").getValue().toString());
        }else {
            nuevaOrden.precioHora = 40000;
        }
        if (snap.hasChild("precioKm")) {
            nuevaOrden.precioKm = Integer.parseInt(snap.child("precioKm").getValue().toString());
        }else {
            nuevaOrden.precioKm = 2600;
        }


        if (snap.hasChild("precioHoraOrden")) {
            nuevaOrden.precioHoraOrden = Integer.parseInt(snap.child("precioHoraOrden").getValue().toString());
        }else {
            nuevaOrden.precioHoraOrden = 40000;
        }
        if (snap.hasChild("precioKmOrden")) {
            nuevaOrden.precioKmOrden = Integer.parseInt(snap.child("precioKmOrden").getValue().toString());
        }else {
            nuevaOrden.precioKmOrden = 2600;
        }


        if (snap.hasChild("ofertadaATerceros")){
            nuevaOrden.setOfertadaATerceros((boolean)snap.child("ofertadaATerceros").getValue());
        }


        if (snap.hasChild("envioPuntoRecogida")){
            nuevaOrden.envioPuntoRecogida =  (boolean)snap.child("envioPuntoRecogida").getValue();
        }



        if (snap.child("destino").getValue().toString().equals("ABIERTO")) {
            nuevaOrden.setDiasServicio(snap.child("diasServicio").getValue().toString());
            nuevaOrden.setHorasServicio(snap.child("horasServicio").getValue().toString());
        }


        nuevaOrden.setHora(snap.child("horaEnOrigen").getValue().toString());
        if (snap.hasChild("observaciones")) {
            nuevaOrden.setObservaciones(snap.child("observaciones").getValue().toString());
        }
        nuevaOrden.setDireccionDestino(snap.child("direccionDestino").getValue().toString());
        nuevaOrden.setFechaEnOrigenEs(snap.child("fechaEnOrigen").getValue().toString());
        try {
            Date f = df.parse(snap.child("fechaEnOrigen").getValue().toString());
            nuevaOrden.setFechaEnOrigen(f);

        } catch (Exception e) {
            System.out.println("Error en el formato de la fecha");
        }

        nuevaOrden.setTiempoRestante(snap.child("fechaGeneracion").getValue().toString());

        if (snap.child("asignadoPor").getValue() != null) {
            nuevaOrden.setAsignadoPor(snap.child("asignadoPor").getValue().toString());
        }

        if (snap.child("conductor").getValue() != null) {
            nuevaOrden.setConductor(snap.child("conductor").getValue().toString());
        }

        if (snap.child("servicioInmediato").getValue() != null) {
            nuevaOrden.servicioInmediato = (boolean)snap.child("servicioInmediato").getValue();
        }else {
            nuevaOrden.servicioInmediato = false;
        }


        if (snap.hasChild("cosecutivoOrden")) {
            nuevaOrden.setCosecutivoOrden(snap.child("cosecutivoOrden").getValue().toString());
        } else {
            if (snap.hasChild("consecutivoOrden")) {
                nuevaOrden.setCosecutivoOrden(snap.child("consecutivoOrden").getValue().toString());
            } else {
                nuevaOrden.setCosecutivoOrden("");
            }
        }
        nuevaOrden.setDireccionOrigen(snap.child("direccionOrigen").getValue().toString());
        //nuevaOrden.setFechaEnDestino(snap.child("fechaEnDestino").getValue().toString());
        //nuevaOrden.setHoraEnDestino(snap.child("horaEnDestino").getValue().toString());
        nuevaOrden.setHoraGeneracion(snap.child("horaGeneracion").getValue().toString());
        nuevaOrden.setIdCliente(snap.child("idCliente").getValue().toString());

        if (snap.child("matricula").getValue() != null) {
            nuevaOrden.setMatricula(snap.child("matricula").getValue().toString());
        }



        if (snap.child("conSonidoDireccion").getValue() != null) {

            nuevaOrden.conSonidoDireccion = (boolean)snap.child("conSonidoDireccion").getValue();

        }


        nuevaOrden.setRuta(snap.child("ruta").getValue().toString());
        nuevaOrden.setSolicitadoPor(snap.child("solicitadoPor").getValue().toString());
        if (snap.child("tarifa").getValue() != null) {
            nuevaOrden.setTarifa(snap.child("tarifa").getValue().toString());
        }
        Long l = (Long) snap.child("timestamp").getValue();
        nuevaOrden.setTimeStamp(l);
        // Long longTimestamp = Long.valueOf(nuevaOrden.setTimeStamp(snap.child("timestamp").getValue().toString()));
        // nuevaOrden.setTrayectos(snap.child("trayectos").getValue().toString());
        nuevaOrden.setId(snap.getKey());

        //nuevo arbol
        DataSnapshot snapPasajeros;
        snapPasajeros = (DataSnapshot) snap.child("pasajeros");
        for (DataSnapshot pasajero : snapPasajeros.getChildren()) {
            Pasajero newPasajero = new Pasajero();
            newPasajero.setIdPasajero(pasajero.getKey());
            newPasajero.setTipo(pasajero.getValue().toString());
            nuevaOrden.pasajeros.add(newPasajero);//
        }



        //nuevo arbol
        DataSnapshot snapRechazos;
        snapRechazos = (DataSnapshot) snap.child("rechazos");
        for (DataSnapshot rechazo : snapRechazos.getChildren()) {
            Rechazo newPasajero = new Rechazo();
            newPasajero.setId(rechazo.getKey());
            newPasajero.setTipo((Boolean) rechazo.getValue());
            nuevaOrden.rechazos.add(newPasajero);//
        }




        DataSnapshot snapRetrasos = (DataSnapshot) snap.child("retrasos");

        for (DataSnapshot retraso : snapRetrasos.getChildren()) {
            Retraso newRetraso = new Retraso();
            newRetraso.id = retraso.getKey();
            newRetraso.fechaInicio = Utility.convertStringConHoraToDate(retraso.child("inicio").getValue().toString());
            if (retraso.hasChild("fin")) {
                newRetraso.fechaFin = Utility.convertStringConHoraToDate(retraso.child("fin").getValue().toString());
            }
            newRetraso.startTime = (long) retraso.child("startTime").getValue();
            if (retraso.hasChild("motivo")) {
                newRetraso.comentario = retraso.child("motivo").getValue().toString();
            }
            nuevaOrden.retrasos.add(newRetraso);
        }


//municipio

        DataSnapshot snapMunicipios;
        snapMunicipios = (DataSnapshot) snap.child("municipio");//municipios
        for (DataSnapshot municipio : snapMunicipios.getChildren()) {
            Municipio newMunicipio = new Municipio();
            newMunicipio.setId(municipio.getKey());
            newMunicipio.setMunicipio(municipio.child("ciudad").getValue().toString());
            newMunicipio.setDirecion(municipio.child("direccion").getValue().toString());
            nuevaOrden.municipios.add(newMunicipio);//  TODO: validacion de evitar duplicados
        }


        //nuevo arbol snapUbicacion
        DataSnapshot snapUbicacion;
        snapUbicacion = (DataSnapshot) snap.child("ubicacion");//ubicacion
        nuevaOrden.ubicacionGPss.clear();
        for (DataSnapshot gps : snapUbicacion.getChildren()) {
            Ubicacion newUbicacion = new Ubicacion();
            newUbicacion.setPathUbicacion(gps.getKey());
            double latd = (double) gps.child("lat").getValue();
            newUbicacion.setLat(latd);
            double lon = (double) gps.child("lon").getValue();
            newUbicacion.setLon(lon);
            Long lu = (Long) gps.child("timestamp").getValue();
            newUbicacion.setTimestamp(lu);
            newUbicacion.setPasajero(gps.child("pasajero").getValue().toString());

            nuevaOrden.ubicacionGPss.add(0, newUbicacion);
        }


        //nuevo arbol snapUbicacionOrigen
        DataSnapshot snapUbicacionOrigen;
        snapUbicacionOrigen = (DataSnapshot) snap.child("ubicacionOrigen");//ubicacion
        //nuevaOrden.ubicacionGPss.clear();


        if (snapUbicacionOrigen.exists()) {
            Ubicacion newUbicacionO = new Ubicacion();
            double latdO = (double) snapUbicacionOrigen.child("lat").getValue();
            newUbicacionO.setLatOrigen(latdO);
            double lonO = (double) snapUbicacionOrigen.child("lon").getValue();
            newUbicacionO.setLonOrigen(lonO);
            nuevaOrden.ubicacionGPss.add(0, newUbicacionO);

            Ubicacion ubiOrigen = new Ubicacion();
            ubiOrigen.setLat(latdO);
            ubiOrigen.setLon(lonO);

            nuevaOrden.ubiOrigen = ubiOrigen;

        }


        //nuevo arbol snapUbicacionDestino
        DataSnapshot snapUbicacionDestino;
        snapUbicacionDestino = (DataSnapshot) snap.child("ubicacionDestino");//ubicacion

        if (snapUbicacionDestino.exists()) {
            Ubicacion newUbicacionD = new Ubicacion();

            String valor = ""+snapUbicacionDestino.child("lat").getValue();

            double latdes = 0.0;
            double lonDes = 0.0;
            if (!snapUbicacionDestino.child("lat").getValue().toString().equals("0")){
                latdes = (double) snapUbicacionDestino.child("lat").getValue();
            }
            //double latdes = (double) snapUbicacionDestino.child("lat").getValue();

            if (!snapUbicacionDestino.child("lon").getValue().toString().equals("0")){
                lonDes = (double) snapUbicacionDestino.child("lon").getValue();
            }
            newUbicacionD.setLatDestino(latdes);
            //double lonDes = (double) snapUbicacionDestino.child("lon").getValue();
            newUbicacionD.setLonDestino(lonDes);
            nuevaOrden.ubicacionGPss.add(0, newUbicacionD);
        }


        return nuevaOrden;

    }

}