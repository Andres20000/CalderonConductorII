package calderonconductor.tactoapps.com.calderonconductor.servicios;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.joda.time.DateTime;


import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes.OnCheckEstado;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoCompartirUbicacion;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoCompartirUbicacion.OnCompartirUbicacionChangeListener;
import calderonconductor.tactoapps.com.calderonconductor.InformacionServicio;
import calderonconductor.tactoapps.com.calderonconductor.R;




public class LocService extends Service {


    private final IBinder mBinder = new LocalBinder();
    private NotificationManager mNM;

    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient fClient;

    public Location cLoc;   //cuerrent Location
    public Location sLoc;    // start location
    public Location eLoc;    //end location
    public Location aLoc;    //anterior location

    public OrdenConductor serv = null;

    ComandoCompartirUbicacion comandoCompartirUbicacion;

    public OnLocCambio listener;

    int acumPlanos = 0;




    public interface OnLocCambio{

        void cambio(int acumulado);

    }

    public LocService() {


    }


    @Override
    public void onCreate() {
        super.onCreate();

        fClient = LocationServices.getFusedLocationProviderClient(this);
        comandoCompartirUbicacion = new ComandoCompartirUbicacion(new OnCompartirUbicacionChangeListener() {
            @Override
            public void cargoUbicacion() {

            }

            @Override
            public void actualizoUbicacion() {

            }
        });

        getUltimaUbicacion();

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {

        public LocService getService() {
            mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            return LocService.this;
        }
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "Siguiendo el trayecto";

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, InformacionServicio.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.estrella_full)   // the status icon
                .setTicker(text)                          // the status text
                .setWhen(System.currentTimeMillis())      // the time stamp
                .setContentTitle("Conductor Serv")          // the label of the entry
                .setContentText(text)                     // the contents of the entry
                .setContentIntent(contentIntent)          // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        //mNM.notify(55, notification);
        //startForeground(56,notification);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, notification);

    }


    private void startMyOwnForeground(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "com.tacto.conductor";
            String channelName = "Conductor Service Channel";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.estrella_full)
                    .setContentTitle("Conductor Serv")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);
        }
    }




    @SuppressLint("MissingPermission")
    public void startLocationUpdates(OrdenConductor ser, OnLocCambio listener) {

        this.listener = listener;

        // Create the location request to start receiving updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(4 * 1000);
        mLocationRequest.setSmallestDisplacement(3);
        mLocationRequest.setFastestInterval(3 * 1000);


        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);


        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task resul = settingsClient.checkLocationSettings(builder.build());


        final LocationRequest finalMLocationRequest = mLocationRequest;

        resul.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                fClient.requestLocationUpdates(finalMLocationRequest, mLocationCallback, null );
            }
        });

        escucharCambios(ser, listener);

        CmdOrdenes.checkEstadoOrden(ser.getId(), new OnCheckEstado() {
            @Override
            public void finalizado() {
                autoCerrar();
            }

            @Override
            public void trasnportando() {
                serv.setEstado("Transportando");
            }
        });

        showNotification();

    }

    private void autoCerrar(){
        stopSelf();
    }

    @SuppressLint("MissingPermission")
    public void getUltimaUbicacion(){

        fClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location loc) {

                // Got last known location. In some rare situations this can be null.
                if (loc != null  &&  loc.getLatitude() != 0) {
                    cLoc = loc;
                }
            }
        });
    }


    public void stopLocationUpdates() {

        if(mLocationCallback != null){
            fClient.removeLocationUpdates(mLocationCallback);
        }

    }


    public void escucharCambios(OrdenConductor ser, final OnLocCambio listener){

        serv = ser;

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location loc : locationResult.getLocations()) {

                    if (loc!=null && loc.getLatitude()!=0  && loc.getAccuracy() < 30) {

                        cLoc = loc;

                        String estado = serv.getEstado();

                        if (estado.equals("EnCamino") || estado.equals("En Camino")){
                            comandoCompartirUbicacion.actualizarUbicacion1(loc.getLatitude(),loc.getLongitude(), serv.getId());
                            return;
                        }

                        if (estado.equals("Transportando")){

                            enviarConTodoYDistacia(loc, listener);
                            return;
                        }

                        return;
                    }

                }

            };
        };
    }



    private void enviarConTodoYDistacia(Location loc, OnLocCambio listener) {

        DateTime now = new DateTime();
        if (sLoc == null) {
            sLoc = loc;
        } else {
            eLoc = loc;
        }


        if (aLoc == null) {
            aLoc = loc;
            return;
        }

        int metrosPlanosCond = (int) aLoc.distanceTo(loc);


        if (metrosPlanosCond > 20) {
            acumPlanos = acumPlanos + metrosPlanosCond;
            comandoCompartirUbicacion.actualizarUbicacionFinal(loc.getLatitude(), loc.getLongitude(), serv.getId(), acumPlanos);
            aLoc = loc;
            listener.cambio(acumPlanos);
            Log.i("LOCCPROM:            ",  "" + acumPlanos);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }
}
