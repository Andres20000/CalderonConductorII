package calderonconductor.tactoapps.com.calderonconductor.principal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import calderonconductor.tactoapps.com.calderonconductor.Adapter.OrdenesConductorAdapter;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.UbicacionConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes.OnOrdenesDescargaListener;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes.OnOrdenesListener;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoConductor.OnTerceroEstadoListener;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductor.OnFinalizarOrden;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoUbicacionConductor;
import calderonconductor.tactoapps.com.calderonconductor.R;
import calderonconductor.tactoapps.com.calderonconductor.particular.ListaServiciosParticularAdapter;
import calderonconductor.tactoapps.com.calderonconductor.servicios.LocService;

public class ListaServicios extends Activity {


    private BaseAdapter mAdapter;
    ListView lv;
    public ProgressBar progressBar;
    Modelo modelo = Modelo.getInstance();
    TextView sindatos, tituloAsigandos;
    Button btn_disponible;
    final Context context = this;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ComandoConductor comandoConductor;

    private FusedLocationProviderClient fClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;

    LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    final static int REQUEST_LOCATION = 199;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    private MediaPlayer mediaPlayer = new MediaPlayer();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_servicios);

        lv = (ListView) findViewById(R.id.listView1);
        sindatos = (TextView) findViewById(R.id.sindatos);
        tituloAsigandos = (TextView) findViewById(R.id.tituloAsignados);

        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        btn_disponible = (Button) findViewById(R.id.btn_disponible);


        if(modelo.params.autoAsignarServicios)
            startLocationUpdates();

        if (modelo.params.hasRegistroInmediato) {  //modo Uber
            mAdapter = new ListaServiciosParticularAdapter(this,  modelo.getOrdenes());
        }else {
            mAdapter = new OrdenesConductorAdapter(this,  modelo.getOrdenes());   //modo calderon
        }


        fClient = LocationServices.getFusedLocationProviderClient(this);

        lv.setAdapter(mAdapter);

        sindatos.setVisibility(View.VISIBLE);


        if (modelo.tipoConductor.equals("conductor")) {

            comandoConductor.actualizarInicioSesion(modelo.uid,this,"conductores");
        }else{
            comandoConductor.actualizarInicioSesion(modelo.uid,this,"conductoresTerceros");
        }



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrdenConductor ordenc = modelo.getOrdenes().get(position);


                if (!modelo.ocupado) {
                    if (modelo.params.hasRegistroInmediato) {
                        ordenc = modelo.getOrdenesOrdenadasParaAutoline().get(position);
                    }
                } else {
                    ordenc = modelo.getOrdenesSoloMias().get(position);
                }

                if (ordenc.getEstado().equals("Cancelado")){
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                            ListaServicios.this);
                    alertDialogBuilder.setTitle("Lo sentimos");
                    final OrdenConductor finalOrdenc = ordenc;
                    alertDialogBuilder
                            .setMessage("El pasajero canceló el servicio.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    new ComandoOrdenesConductor().moverOrdenAlHistorico(finalOrdenc.getId(), new OnFinalizarOrden() {
                                        @Override
                                        public void finalizo(OrdenConductor orden) {

                                        }

                                        @Override
                                        public void fallo(OrdenConductor orden) {

                                        }
                                    });
                                    return;
                                }
                            });

                    // create alert dialog
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                    return;

                }


                Intent i = new Intent(getApplicationContext(), DetalleServicio.class);
                i.putExtra("id", "" + ordenc.getId());// se envia el id de la orden segun la posicion
                startActivity(i);
            }
        });



        if (modelo.params.hasRegistroInmediato || modelo.esTercero()){
            btn_disponible.setVisibility(View.VISIBLE);
            setBtnDisponible();
        }else {
            btn_disponible.setVisibility(View.GONE);
            modelo.ocupado = false;
        }





        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location loc : locationResult.getLocations()) {

                    if (loc != null && loc.getLatitude() != 0 && loc.getAccuracy() < 30 && modelo.cLoc != null) {

                        float temDistancia = loc.distanceTo(modelo.cLoc);
                        Log.i("LOCATION","Nueva Distancia ===== " + temDistancia);
                        if(modelo.params.autoAsignarServicios)
                            ComandoUbicacionConductor.ActualizaUbicacionConductor(new UbicacionConductor(loc.getLatitude(), loc.getLongitude()), modelo.vehiculo.getPlaca());
                        if (temDistancia > 100){
                            modelo.latitud = loc.getLatitude();
                            modelo.longitud = loc.getLongitude();
                            modelo.cLoc = loc;
                            modelo.updateLastLocation(loc);
                            mAdapter.notifyDataSetChanged();
                        }

                        return;
                    }

                }
            }

            ;
        };


        if (!modelo.esTercero()) {
            if (!modelo.params.registroConductorRequiereAprobacionAdmin){
                llamarOrdenes();
                return;
            }
            //else
            ComandoConductor.getEstadoNormal(modelo.uid, new OnTerceroEstadoListener() {
                @Override
                public void aprobado() {

                    llamarOrdenes();

                    sindatos.setText("");

                    btn_disponible.setVisibility(View.VISIBLE);
                    btn_disponible.setClickable(true);
                    setBtnDisponible();

                }

                @Override
                public void pendiente() {
                    btn_disponible.setVisibility(View.VISIBLE);
                    btn_disponible.setClickable(false);
                    btn_disponible.setBackgroundResource(R.color.color_gris_linea);
                    btn_disponible.setText("Pendiente de activación");
                    sindatos.setText("Los datos han sido enviados con éxito, será dado de alta cuando el administrador haya validado la información");
                    // Los datos han sido enviados con éxito, será dado de alta cuando el administrador haya validado la información
                    if(mAdapter != null){
                        modelo.getOrdenes().clear();
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void rechazado() {

                }
            });




        }

        if (modelo.esTercero()) {

            ComandoConductor.getEstadoTercero(modelo.uid, new OnTerceroEstadoListener() {
                @Override
                public void aprobado() {

                    llamarOrdenes();

                    sindatos.setText("");

                    btn_disponible.setVisibility(View.VISIBLE);
                    btn_disponible.setClickable(true);
                    setBtnDisponible();

                }

                @Override
                public void pendiente() {
                    btn_disponible.setVisibility(View.VISIBLE);
                    btn_disponible.setClickable(false);
                    btn_disponible.setBackgroundResource(R.color.color_gris_linea);
                    btn_disponible.setText("Pendiente de activación");
                    sindatos.setText("Los datos han sido enviados con éxito, será dado de alta cuando el administrador haya validado la información");
                    // Los datos han sido enviados con éxito, será dado de alta cuando el administrador haya validado la información
                    if(mAdapter != null){
                        modelo.getOrdenes().clear();
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void rechazado() {

                }
            });

        }

    }

    public void playDir(String idOrden){


        StorageReference ref = storage.getReference();
        StorageReference islandRef = ref.child("direcciones/" + idOrden  + ".mp3");


        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                playMp3(bytes);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("ERROR", exception.getMessage());
            }
        });

    }


    private void playMp3(byte[] mp3SoundByteArray) {
        try {
            File tempMp3 = File.createTempFile("kurchina", "mp3", getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            // resetting mediaplayer instance to evade problems
            mediaPlayer.reset();

            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }


    public void setBtnDisponible(){

            if (modelo.ocupado == true) {
                btn_disponible.setBackgroundResource(R.color.color_rojo);
                btn_disponible.setText("OCUPADO");

            } else {
                btn_disponible.setBackgroundResource(R.color.colorCafe);
                btn_disponible.setText("DISPONIBLE");
            }

    }



    @Override
    protected void onStart() {
        super.onStart();
        ordenarPintar();

    }


    private  void llamarOrdenes(){

        CmdOrdenes.checkTodasLasOrdenes(new OnOrdenesDescargaListener() {
            @Override
            public void termino() {
                setVarios();
                getUltimaUbicacion();
                startLocationUpdates();
                mAdapter.notifyDataSetChanged();
            }
        });


        CmdOrdenes.getTodasLasOrdenes(new OnOrdenesListener() {
            @Override
            public void nueva() {
                modelo.updateLastLocation(modelo.cLoc);
                ordenarPintar();
            }

            @Override
            public void modificada(String idServicio) {
                modelo.updateLastLocation(modelo.cLoc);
                ordenarPintar();
                trySound(idServicio);

            }

            @Override
            public void eliminada() {
                ordenarPintar();
            }
        });


    }


    private void trySound(String idOrden){


        if (modelo.ocupado){
            return;
        }

        if (!modelo.params.hasRegistroInmediato){
            return;
        }

        OrdenConductor orden = modelo.getOrden(idOrden);

        if (orden == null) {
            return;
        }

        if ( orden.getEstado().equals("NoAsignado") && orden.conSonidoDireccion && orden.esFactibleEnDistancia(modelo.params)){
             playDir(idOrden);
        }

    }



    private void ordenarPintar(){


        if (!modelo.ocupado) {

            if (modelo.params.hasRegistroInmediato) {
                if (modelo.getOrdenesOrdenadasParaAutoline().size() > 0){
                    tituloAsigandos.setVisibility(View.VISIBLE);
                }else {
                    tituloAsigandos.setVisibility(View.GONE);
                }
                ((ListaServiciosParticularAdapter) mAdapter).updateOrdenes(modelo.getOrdenesOrdenadasParaAutoline());
            }else{
                ((OrdenesConductorAdapter) mAdapter).updateOrdenes(modelo.getOrdenes());
            }
        }else {  //ocupado
            if (modelo.params.hasRegistroInmediato) {
                ((ListaServiciosParticularAdapter) mAdapter).updateOrdenes(modelo.getOrdenesSoloMias());
            }

        }
        mAdapter.notifyDataSetChanged();

    }





    private void setVarios(){
        if (modelo.getOrdenes().size() < 1) {
            progressBar.setVisibility(View.INVISIBLE);
            sindatos.setVisibility(View.VISIBLE);
        } else {
            sindatos.setVisibility(View.INVISIBLE);
            sindatos.setBackgroundColor(Color.TRANSPARENT);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }



    public void disponivilidad(View v) {

        if (modelo.ocupado == false) {
            if ( modelo.tipoConductor.equals("conductorTercero")){
                DatabaseReference ref = database.getReference("empresa/conductoresTerceros/" + modelo.uid + "/ocupado/");//ruta path
                ref.setValue(true);
            }else {
                DatabaseReference ref = database.getReference("empresa/conductores/" + modelo.uid + "/ocupado/");//ruta path
                ref.setValue(true);
            }
            modelo.ocupado = true;
            btn_disponible.setBackgroundResource(R.color.color_rojo);
            btn_disponible.setText("OCUPADO");

        } else {
            if ( modelo.tipoConductor.equals("conductorTercero")) {
                DatabaseReference ref = database.getReference("empresa/conductoresTerceros/" + modelo.uid + "/ocupado/");//ruta path
                ref.setValue(false);
            }else{
                DatabaseReference ref = database.getReference("empresa/conductores/" + modelo.uid + "/ocupado/");//ruta path
                ref.setValue(false);
            }
            modelo.ocupado = false;
            btn_disponible.setBackgroundResource(R.color.colorCafe);
            btn_disponible.setText("DISPONIBLE");
        }

        ordenarPintar();

    }





    @SuppressLint("MissingPermission")
    private void getUltimaUbicacion(){


        fClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location loc) {

                // Got last known location. In some rare situations this can be null.
                if (loc != null  &&  loc.getLatitude() != 0) {
                    modelo.latitud = loc.getLatitude();
                    modelo.longitud = loc.getLongitude();
                    modelo.cLoc = loc;

                }
            }
        });
    }


    protected void startLocationUpdates() {


        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10 * 1000);
        mLocationRequest.setFastestInterval(8 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);


        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task resul = settingsClient.checkLocationSettings(builder.build());

        resul.addOnCompleteListener(new OnCompleteListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onComplete(@NonNull Task task) {

                try {
                    //permisos.setVisibility(View.VISIBLE);
                    fClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null );

                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        });

        resul.addOnSuccessListener(this, new OnSuccessListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(Object o) {
                try {
                    fClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null );

                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        });




        resul.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    //permisos.setVisibility(View.VISIBLE);
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(ListaServicios.this, REQUEST_LOCATION);
                        //startIntentSenderForResult(resolvable.getResolution().getIntentSender(), REQUEST_LOCATION, null, 0, 0, 0, null);

                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }

            }
        });


    }




    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //checkGPS();
        if (requestCode == REQUEST_LOCATION ){
            if (resultCode == RESULT_OK){
                fClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null );
                //permisos.setVisibility(View.GONE);
                return;
            }
            else {
                Crashlytics.log("StarLocationUpdates llamando desde : 1883");
                startLocationUpdates();  //para que vuelve e intente
                //permisos.setVisibility(View.VISIBLE);
                return;
            }

        }





    }


    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                    //permisos.setVisibility(View.GONE);

                }
                else{
                    //permisos.setVisibility(View.VISIBLE);
                }
            }
        }
        //updateLocationUI();
    }







}
