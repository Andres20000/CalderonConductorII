package calderonconductor.tactoapps.com.calderonconductor.principal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import calderonconductor.tactoapps.com.calderonconductor.CancelarServicio;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Cliente;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes.OnCheckCancelanda;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes.OnCheckDistancia;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes.OnOrdenesListener;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes.OnSetEstado;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoActualizarOfertaTerceros;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoCliente;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoCompartirUbicacion;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoEnviarMensaje;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoListadoPasajeros;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoNotificaciones;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductor.OnFinalizarOrden;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductorTerceros;
import calderonconductor.tactoapps.com.calderonconductor.HistorialDestino;
import calderonconductor.tactoapps.com.calderonconductor.ListaPasajeros;
import calderonconductor.tactoapps.com.calderonconductor.MapsActivity;
import calderonconductor.tactoapps.com.calderonconductor.Mensaje;
import calderonconductor.tactoapps.com.calderonconductor.NuevoDestino;
import calderonconductor.tactoapps.com.calderonconductor.Observaciones;
import calderonconductor.tactoapps.com.calderonconductor.Pgina_Principa;
import calderonconductor.tactoapps.com.calderonconductor.R;
import calderonconductor.tactoapps.com.calderonconductor.Rutas;
import calderonconductor.tactoapps.com.calderonconductor.Splash;
import calderonconductor.tactoapps.com.calderonconductor.retrasos.ListaRetrasos;
import calderonconductor.tactoapps.com.calderonconductor.servicios.LocService;
import calderonconductor.tactoapps.com.calderonconductor.servicios.LocService.LocalBinder;
import calderonconductor.tactoapps.com.calderonconductor.servicios.LocService.OnLocCambio;

public class DetalleServicio extends Activity implements ComandoListadoPasajeros.OnComandoPasajerosChangeListener, ComandoCliente.OnClienteChangeListener, ComandoNotificaciones.OnNotificacionesChangeListener, ComandoOrdenesConductorTerceros.OnOrdenesConductorTercerosChangeListener, ComandoActualizarOfertaTerceros.OnActualizarOfertaTercerosChangeListener, ComandoCompartirUbicacion.OnCompartirUbicacionChangeListener, ComandoEnviarMensaje.OnMensajeChangeListener {

    String idServicio = "";
    TextView numero_orden,text_ciudad_origen, text_ciudad_llegada,barrio_recogida,direcion_recogida, barrio_llegada;
    TextView fecha_y_hora_recogida,vehiculo_asignado,cantidad_pasajeros;
    TextView idCliente, texto_foter, sumDirecto, texto_cambio_estado, txt_abierto,txt_consecutivo_orden;
    Button bonton_estado,btn_puntorecojida, btn_cotizar;
    TextView btn_paradas, txtRetrasos,txt_cotizar;
    ImageView cancelar_servicio,imagen_estado, imageView7,hoistorialdestinos,estado_conductor, cron;
    EditText preciocotizar;
    LinearLayout acetar_rechazar, estadservicios, paradas,layout_cotizar,cotiobs, layRetrasos;
    Space espacioRetra;


    ComandoCliente cliente = new ComandoCliente(this);
    ComandoOrdenesConductor comandoOrdenesConductor;
    ComandoOrdenesConductorTerceros comandoOrdenesConductorTerceros;
    ComandoCompartirUbicacion comandoCompartirUbicacion;
    ComandoNotificaciones comandoNotificaciones;
    ComandoActualizarOfertaTerceros comandoActualizarOfertaTerceros;

    final Context context = this;
    String[] datoArray;
    String[] UIArray;
    String[] tokens;


    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private FusedLocationProviderClient fClient;
    private LocationCallback mLocationCallback;
    private boolean mLocationPermissionGranted = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    final static int REQUEST_LOCATION = 199;

    LocService music;
    boolean mBound = false;


    Modelo modelo = Modelo.getInstance();


    private ProgressDialog progressDialog;
    LinearLayout tamanolistapasajeros;
    LinearLayout.LayoutParams params;

    String precionSinconvercion = "";

    ComandoEnviarMensaje comandoMensaje;

    LinearLayout layout_pasa;
    ImageView flechap;
    TextView nombre;
    TextView celular;

    OrdenConductor orden;


    //mapa
    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey("AIzaSyAVsOhj9mBfW9dPLlJG3LIKmyPSnNzGjcI")
                .setConnectTimeout(30, TimeUnit.SECONDS)
                .setReadTimeout(30, TimeUnit.SECONDS)
                .setWriteTimeout(30, TimeUnit.SECONDS);

    }


    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            music = binder.getService();
            music.getUltimaUbicacion();
            mBound = true;

            music.listener = new OnLocCambio() {
                @Override
                public void cambio(final int acumulado) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            //sumDirecto.setText("" + acumulado);

                        }
                    });
                }
            };


        }


        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_informacion_servicio);


        if (savedInstanceState != null || getIntent().getExtras() == null) {
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }


        Bundle bundle;


        try {
            bundle = getIntent().getExtras();
            idServicio = bundle.getString("id");
            Log.i("SOS", "id en Detalle = " + idServicio);

        } catch (Exception e) {
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
        }


        if (idServicio.equals("")) {
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }


        orden = modelo.getOrden(idServicio);
        Log.i("SOS", "id en Detalle al cargar = " + orden.getId());

        numero_orden = (TextView) findViewById(R.id.numero_orden);
        text_ciudad_origen = (TextView) findViewById(R.id.text_ciudad_origen);
        imagen_estado = (ImageView) findViewById(R.id.imagen_estado);
        txt_consecutivo_orden = (TextView) findViewById(R.id.txt_consecutivo_orden);
        text_ciudad_llegada = (TextView) findViewById(R.id.text_ciudad_llegada);
        barrio_recogida = (TextView) findViewById(R.id.barrio_recogida);
        barrio_llegada = (TextView) findViewById(R.id.barrio_llegada);
        fecha_y_hora_recogida = (TextView) findViewById(R.id.fecha_y_hora_recogida);
        vehiculo_asignado = (TextView) findViewById(R.id.vehiculo_asignado);
        texto_foter = (TextView) findViewById(R.id.texto_foter);


        //sumDirecto = (TextView) findViewById(R.id.sumDirecto);


        idCliente = (TextView) findViewById(R.id.idCliente);
        texto_cambio_estado = (TextView) findViewById(R.id.texto_cambio_estado);
        cantidad_pasajeros = (TextView) findViewById(R.id.cantidad_pasajeros);
        txt_abierto = (TextView) findViewById(R.id.txt_abierto);
        estado_conductor = (ImageView) findViewById(R.id.estado_conductor);
        bonton_estado = (Button) findViewById(R.id.bonton_estado);
        btn_puntorecojida = (Button) findViewById(R.id.btn_puntorecojida);
        cancelar_servicio = (ImageView) findViewById(R.id.cancelar_servicio);
        imageView7 = (ImageView) findViewById(R.id.imageView7);
        hoistorialdestinos = (ImageView) findViewById(R.id.hoistorialdestinos);

        acetar_rechazar = (LinearLayout) findViewById(R.id.acetar_rechazar);
        estadservicios = (LinearLayout) findViewById(R.id.estadservicios);
        paradas = (LinearLayout) findViewById(R.id.paradas);
        layout_cotizar = (LinearLayout) findViewById(R.id.layout_cotizar);
        cotiobs = (LinearLayout) findViewById(R.id.cotiobs);

        btn_paradas = (TextView) findViewById(R.id.btn_paradas);
        preciocotizar = (EditText) findViewById(R.id.preciocotizar);
        txt_cotizar = (TextView) findViewById(R.id.txt_cotizar);
        btn_cotizar = (Button) findViewById(R.id.btn_cotizar);
        tamanolistapasajeros = (LinearLayout) findViewById(R.id.tamanolistapasajeros);

        layout_pasa = (LinearLayout) findViewById(R.id.layout_pasa);
        flechap = (ImageView) findViewById(R.id.flechap);
        nombre = (TextView) findViewById(R.id.nombre);
        celular = (TextView) findViewById(R.id.celular);


        fClient = LocationServices.getFusedLocationProviderClient(this);

        comandoOrdenesConductorTerceros = new ComandoOrdenesConductorTerceros(this);
        comandoActualizarOfertaTerceros = new ComandoActualizarOfertaTerceros(this);
        comandoCompartirUbicacion = new ComandoCompartirUbicacion(this);
        progressDialog = new ProgressDialog(this);
        comandoMensaje = new ComandoEnviarMensaje(this);


        boolean result = Utility.checkPermissionCall(DetalleServicio.this);


        layRetrasos = findViewById(R.id.layRetrasos);
        txtRetrasos = findViewById(R.id.retrasos);
        cron = findViewById(R.id.cron);
        espacioRetra = findViewById(R.id.espacioRetra);


        preciocotizar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i != 0) {
                    //Toast.makeText(getApplicationContext(), " on " + txt_precio.getText().toString(), Toast.LENGTH_SHORT).show();
                    String precio = preciocotizar.getText().toString();
                    String temp = "";

                    int precio2 = 0;


                    try {
                        precio = preciocotizar.getText().toString().replace(" ", "");
                        Locale colombia = new Locale("es", "CO");
                        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(colombia);
                        defaultFormat.setMaximumFractionDigits(0);
                        precio = precio.replace(".", "");
                        precio2 = Integer.parseInt(precio.replace(" ", "").replaceAll("\\s", "").trim());
                        temp = defaultFormat.format(precio2);
                        temp = temp.replace(",", ".");
                        temp = temp.replace("$", "");

                        preciocotizar.setText(temp);
                        preciocotizar.setSelection(preciocotizar.getText().length());
                        precionSinconvercion = temp.replace(".", "").replaceAll("\\s", "").trim();


                    } catch (RuntimeException r) {
                        Log.i("MONEY", r.getLocalizedMessage());
                        Log.i("MONEY", r.getMessage());

                    }
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //fin


        if (!orden.getOfertadaATerceros()  && !orden.getEstado().equals("Cotizar") &&  !orden.getEstado().equals("NoAsignado")){
            estadservicios.setVisibility(View.VISIBLE);
        } else {
            cancelar_servicio.setVisibility(View.INVISIBLE);
            cancelar_servicio.setClickable(false);
            acetar_rechazar.setVisibility(View.VISIBLE);
        }


        comandoOrdenesConductor = new ComandoOrdenesConductor();

        comandoNotificaciones = new ComandoNotificaciones(this);

        actualizarDatos();



        cliente.getRazonSocialCliente(orden.getIdCliente(), idServicio);

        String commaSeparated = orden.getRuta();
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(commaSeparated.split("-")));

        paradas.setClickable(false);
        paradas.setOnClickListener(null);

        if (items.size() > 0) {

            if (items.get(0).toString().equals("")) {
                btn_paradas.setText("Sin Paradas");
            } else {
                btn_paradas.setText("Paradas: " + items.size());
            }
        } else {
            btn_paradas.setText("Sin Paradas");
        }



        getLocationPermission();  //1 Este es para iniciar... en realidad nada que ver con el locationCallBack. Los permisos SI


        CmdOrdenes.checkDistancia(orden.getId(), new OnCheckDistancia() {
            @Override
            public void avance(int acumulado) {
                //sumDirecto.setText("" + acumulado + " mts");

            }
        });



        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location loc : locationResult.getLocations()) {

                    if (loc != null && loc.getLatitude() != 0 && loc.getAccuracy() < 30) {


                        modelo.latitud = loc.getLatitude();
                        modelo.longitud = loc.getLongitude();
                        modelo.cLoc = loc;


                        String estado = orden.getEstado();

                        if (estado.equals("EnCamino") || estado.equals("En Camino")) {
                            comandoCompartirUbicacion.actualizarUbicacion1(modelo.latitud, modelo.longitud, orden.getId());
                            return;
                        }

                        if (estado.equals("Transportando")) {
                            enviarConTodoYDistacia(loc);
                            return;
                        }


                        return;
                    }

                }
            }

            ;
        };



    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarBotonRetrasos();

        orden = modelo.getOrden(idServicio);
        CmdOrdenes.escucharSiCancelanOrden(orden.getId(), new OnCheckCancelanda() {
            @Override
            public void cancelado(final String idServicio) {
                orden = modelo.getOrden(idServicio);
                imagen_estado.setImageResource(R.drawable.estado_cancelado_i5);
                txt_consecutivo_orden.setText("Cancelado");
                if (orden.getId().equals(idServicio)){
                    try {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                DetalleServicio.this);
                        alertDialogBuilder.setTitle("Lo sentimos");
                        alertDialogBuilder
                                .setMessage("El pasajero canceló el servicio a " + orden.getDireccionOrigen())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        new ComandoOrdenesConductor().moverOrdenAlHistorico(idServicio, new OnFinalizarOrden() {
                                            @Override
                                            public void finalizo(OrdenConductor orden) {
                                                 finish();
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

                    }catch (Exception e) {

                    }


                }
            }
        });


    }


    public void actualizarBotonRetrasos() {



        if (modelo.params.hasRegistroInmediato){
            layRetrasos.setVisibility(View.GONE);
            espacioRetra.setVisibility(View.GONE);
            return;
        }


        orden = modelo.getOrden(idServicio);

        if (orden == null) {
            return;
        }

        if (orden.hayRetrazoAbierto()) {
            cron.setVisibility(View.VISIBLE);
            layRetrasos.setBackgroundColor(getResources().getColor(R.color.colorAmarilloQuemao));
            txtRetrasos.setTextColor(getResources().getColor(R.color.colorNegro));

        } else {
            cron.setVisibility(View.GONE);
            layRetrasos.setBackgroundColor(getResources().getColor(R.color.colorCafe));
            txtRetrasos.setTextColor(getResources().getColor(R.color.color_blanco));
        }


        if (orden.envioPuntoRecogida && (orden.getEstado().equals("EnCamino")  || orden.getEstado().equals("En Camino") || orden.getEstado().equals("Transportando") )){
            layRetrasos.setVisibility(View.VISIBLE);
            espacioRetra.setVisibility(View.VISIBLE);
        }
        else {
            layRetrasos.setVisibility(View.GONE);
            espacioRetra.setVisibility(View.GONE);
        }

    }


    public void onSaveInstanceState(Bundle outState) {
        outState.putString("IDSERVICIO", orden.getId());
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }


    private void enviarConTodoYDistacia(Location loc) {
        DirectionsResult result, result2 = null;

        DateTime now = new DateTime();
        try {

            if (modelo.sLoc == null) {
                modelo.sLoc = loc;
            } else {
                modelo.eLoc = loc;
            }


            if (modelo.aLoc == null) {
                modelo.aLoc = loc;
                return;
            }


            result = DirectionsApi.newRequest(getGeoContext()).mode(TravelMode.DRIVING).origin(new com.google.maps.model.LatLng(modelo.latitudAnterior, modelo.longitudAnterior))
                    .destination(new com.google.maps.model.LatLng(modelo.latitud, modelo.longitud)).departureTime(now).await();


            long metrosConduciendo = getDistanciaConduciendo(result);
            long metrosPlanosCond = (int) modelo.aLoc.distanceTo(loc);


            if (metrosConduciendo > 20) {

                modelo.acumCurvos = modelo.acumCurvos + (int) metrosConduciendo;
                modelo.acumPlanos = modelo.acumPlanos + (int) metrosPlanosCond;


                result = DirectionsApi.newRequest(getGeoContext()).mode(TravelMode.DRIVING).origin(new com.google.maps.model.LatLng(modelo.sLoc.getLatitude(), modelo.sLoc.getLongitude()))
                        .destination(new com.google.maps.model.LatLng(loc.getLatitude(), loc.getLongitude())).departureTime(now).await();

                long metrosDesdeInicio = getDistanciaConduciendo(result);


                //sumDirecto.setText("" + metrosDesdeInicio);


                comandoCompartirUbicacion.actualizarUbicacion(modelo.latitud, modelo.longitud, idServicio, metrosConduciendo);

                modelo.latitudAnterior = modelo.latitud;
                modelo.longitudAnterior = modelo.longitud;
                modelo.aLoc = modelo.cLoc;

                Log.v("acumCurvos", "" + modelo.acumCurvos);
                Log.v("acumPLanos", "" + modelo.acumPlanos);
                Log.v("distancia", "" + getDistanciaConduciendo(result));
                Log.v("------", "----------------------");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (com.google.maps.errors.ApiException e) {
            e.printStackTrace();
        }
    }




    public void terminarFinalizacionOrden(OrdenConductor orden) {

        comandoOrdenesConductor.crearActualizarDateTime(idServicio);
        comandoCompartirUbicacion.datosRecorreido(idServicio, modelo.cliente, orden);

        finish();

    }


    @Override
    public void cargoPasajero(String nombrePasajero) {

        orden = modelo.getOrden(idServicio);

        Log.i("PASA:", "Si hay  pasajeros asignados: " + nombrePasajero);

        int cantidadPasjeros = orden.pasajeros.size();
        cantidad_pasajeros.setText("" + cantidadPasjeros);

        if (cantidadPasjeros == 1) {
            layout_pasa.setClickable(false);
            flechap.setVisibility(View.GONE);
        }

        UIArray = new String[orden.pasajeros.size()];
        datoArray = new String[orden.pasajeros.size()];
        tokens = new String[orden.pasajeros.size()];


        String telefono = "";
        for (int i = 0; i < orden.pasajeros.size(); i++) {

            if (orden.pasajeros.get(i).getIdPasajero().equals(orden.getSolicitadoPor())) {
                Log.i("PASA:", "Si hay  pasajeros asignados:" + orden.pasajeros.get(i).getNombre() + orden.getId());
                nombre.setText(orden.pasajeros.get(i).getNombre() + " " + orden.pasajeros.get(i).getApellido());
                celular.setText(orden.pasajeros.get(i).getCelular());
                telefono = orden.pasajeros.get(i).getCelular();

            }
            if (orden.pasajeros.get(i).getTipo().equals("empresarial")) {
                nombre.setText(orden.pasajeros.get(i).getNombre() + " " + orden.pasajeros.get(i).getApellido());
                celular.setText(orden.pasajeros.get(i).getCelular());
                telefono = orden.pasajeros.get(i).getCelular();

            }


            UIArray[i] = orden.pasajeros.get(i).getIdPasajero();
            datoArray[i] = orden.pasajeros.get(i).getNombre() + " " + orden.pasajeros.get(i).getApellido();
            tokens[i] = orden.pasajeros.get(i).getTokenDevice();
        }



        final String finalTelefono = telefono;
        celular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + finalTelefono));
                startActivity(callIntent);


            }
        });

    }

    @Override
    public void cargoPasajeroCero() {
        Toast.makeText(getApplicationContext(), "No hay  pasajeros asignados", Toast.LENGTH_LONG).show();
    }


    @Override
    public void cargoCliente() {
        orden = modelo.getOrden(idServicio);
        Cliente cliente = orden.getCliente();
        idCliente.setText(cliente.getRazonSocial());
    }


    public void atras(View v) {
        finish();
    }



    public void cancelarServicio(View v) {
        Intent i = new Intent(getApplicationContext(), CancelarServicio.class);
        i.putExtra("id", "" + idServicio);
        startActivity(i);
    }

    //cambiar estado del servicio
    public void cambiarEstadoServicio(View v) {

        showAlerCambioDeEstado();

    }



    public void showAlerCambioDeEstado() {


        orden = modelo.getOrden(idServicio);

        if ( orden.hayRetrazoAbierto() ) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set title
            alertDialogBuilder.setTitle("Retrasos abiertos");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Tienes un retraso activo, antes de poder cambiar de estado debes terminarlo")
                    .setCancelable(false)
                    .setPositiveButton("Si, terminarlo", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent intent = new Intent(getApplicationContext(), ListaRetrasos.class);
                            intent.putExtra("IDORDEN", idServicio);
                            startActivity(intent);
                            return;

                        }
                    });

            alertDialogBuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            try {
                alertDialog.show();
            } catch (Exception ex) {
            }


            return;
        }



        String estado = orden.getEstado();
        String cambio_a = "";
        if (estado.equals("Asignado")) {

            cambio_a = "En Camino";

        } else if (estado.equals("En Camino") || estado.equals("EnCamino")) {
            cambio_a = "Transportando";
        } else if (estado.equals("Transportando")) {
            cambio_a = "Finalizado";
        }

        String estado2;
        if (!estado.equals("Finalizado")) {
            estado2 = "' " + " a '" + cambio_a;
        } else {
            estado2 = " ";
        }


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Cambio de estado");

        // set dialog message
        alertDialogBuilder
                .setMessage("¿Deseas cambiar tu estado " + "'" + orden.getEstado() + estado2 + "'?")
                .setCancelable(false)
                .setPositiveButton("SI, CAMBIAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //alert
                        bonton_estado.setClickable(false);
                        bonton_estado.setEnabled(false);
                        Log.i("TIEMPOX", "Deseas cambiar estado de la orden");
                        cambiarEstado(orden);

                    }
                });

        alertDialogBuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        try {
            alertDialog.show();
        } catch (Exception ex) {
        }
    }


    public void cambiarEstado(final OrdenConductor orden){

        Log.i("TIEMPOX", "Cambiar estado");
        final ProgressDialog progressDialog = new ProgressDialog(DetalleServicio.this);
        progressDialog.setMessage("Procesando...");
        progressDialog.setTitle("Actualizando el estado");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        CmdOrdenes.actualizarEstado(orden.getEstado(), orden.getId(), new OnSetEstado() {
            @Override
            public void cambio(String nuevoEstado) {

                Log.i("TIEMPOX", "Cambiar estado");
                orden.setEstado(nuevoEstado);
                sendNotiYProcess();
                try {
                    progressDialog.dismiss();
                }
                catch (Exception e) {

                }


            }

            @Override
            public void fallo(String nuevoEstado) {
                try {
                    progressDialog.dismiss();
                }
                catch (Exception e) {

                }

            }
        });

    }




    //evento del boton cambio de estado. Y actualiza textos pero tambien notica al pasajero
    public void sendNotiYProcess() {

        actualizarImagenesYBotones();
        Log.i("TIEMPOX", "Actualizo imagenes y botones");


        if (orden.getEstado().equals("Asignado")) {
            if (tokens != null) {
                comandoNotificaciones.enviaoDeMensajeAlPAsajero(tokens, "¡Estado del Servicio!", "Su servicio cambió de Estado, se encuentra Asignado");
            }
            return;
        }

        if (orden.getEstado().equals("En Camino") || orden.getEstado().equals("EnCamino")) {

            if (tokens != null) {
                comandoNotificaciones.enviaoDeMensajeAlPAsajero(tokens, "¡Estado del Servicio!", "Su servicio cambió de Estado, se encuentra En Camino");
            }



            lanzarLocService();
            music.startLocationUpdates(orden, new OnLocCambio() {
                @Override
                public void cambio(final int acumulado) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            //sumDirecto.setText("" + acumulado);

                        }
                    });

                }
            });


            return;
        }

        if (orden.getEstado().equals("Transportando")) {
            if (tokens != null) {
                comandoNotificaciones.enviaoDeMensajeAlPAsajero(tokens, "¡Estado del Servicio!", "Su servicio cambió de Estado, se encuentra Transportando");
            }


            modelo.latitudAnterior = modelo.latitud;
            modelo.longitudAnterior = modelo.longitud;

            if (music != null && music.serv != null) {
                music.serv.setEstado("Transportando");
            }
            return;
        }

        if (orden.getEstado().equals("Finalizado")) {
            finalizarOrden();
            return;
        }
        if (orden.getEstado().equals("No Asignado") || orden.getEstado().equals("NoAsignado") || orden.getEstado().equals("SinConfirmar") || orden.getEstado().equals("Cotizar")) {
            return;
        }


    }



    private void finalizarOrden(){


        Log.i("GGG", "Paso por Finalizar la orden");


        final ProgressDialog progressDialog = new ProgressDialog(DetalleServicio.this);
        progressDialog.setMessage("Procesando...");
        progressDialog.setTitle("Finalizando el servicio");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);


        comandoCompartirUbicacion.actualizarTimeStampFinal(modelo.latitud, modelo.longitud, idServicio);
        comandoOrdenesConductor.moverOrdenAlHistorico(orden.getId(), new OnFinalizarOrden() {
            @Override
            public void finalizo(OrdenConductor orden) {
                terminarFinalizacionOrden(orden);
                try {
                    progressDialog.dismiss();
                }catch (Exception e){

                }
            }

            @Override
            public void fallo(final OrdenConductor ordenC) {

                progressDialog.dismiss();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Error Finalizando  el servicio");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Volver a intentar?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                comandoOrdenesConductor.moverOrdenAlHistorico(ordenC.getId(), new OnFinalizarOrden() {
                                    @Override
                                    public void finalizo(OrdenConductor orden) {
                                        terminarFinalizacionOrden(orden);
                                    }

                                    @Override
                                    public void fallo(OrdenConductor orden) {

                                    }
                                });


                            }
                        });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                try {
                    alertDialog.show();
                } catch (Exception ex) {
                }

            }
        });


        if (tokens != null) {
            comandoNotificaciones.enviaoDeMensajeAlPAsajero(tokens, "¡Estado del Servicio!", "Su servicio cambió de Estado, se encuentra Finalizado");
        }

        music.stopLocationUpdates();



    }

    //primer ingreso
    public void actualizarImagenesYBotones() {

        cancelar_servicio.setVisibility(View.VISIBLE);
        cancelar_servicio.setClickable(true);

        bonton_estado.setClickable(true);
        bonton_estado.setEnabled(true);
        bonton_estado.setVisibility(View.VISIBLE);



        if (orden.getEstado().equals("Asignado")) {
            imagen_estado.setImageResource(R.drawable.estado_confirmado_i5);
            txt_consecutivo_orden.setText(orden.getEstado());
            texto_cambio_estado.setText("" + getString(R.string.enCamino));
            bonton_estado.setText("" + getString(R.string.enCamino));
            estado_conductor.setImageResource(R.drawable.img_confirmado_i5);
            texto_foter.setText("El o los pasajeros serán notificados que el servicio se dirige al punto de recogida.");

            acetar_rechazar.setVisibility(View.GONE);
            estadservicios.setVisibility(View.VISIBLE);
            layout_cotizar.setVisibility(View.GONE);

        } else if (orden.getEstado().equals("En Camino") || orden.getEstado().equals("EnCamino")) {


            imagen_estado.setImageResource(R.drawable.estado_en_camino_i5);
            txt_consecutivo_orden.setText("En Camino");
            texto_cambio_estado.setText("Este servicio está en Camino");
            bonton_estado.setText("" + getString(R.string.transportado));
            estado_conductor.setImageResource(R.drawable.img_en_camino_i5);
            texto_foter.setText("Notifique en la orden que el servicio ha dado inicio. El o los pasajeros serán notificados.");

            if (!orden.envioPuntoRecogida) {
                bonton_estado.setVisibility(View.GONE);
                btn_puntorecojida.setVisibility(View.VISIBLE);
            }



        } else if (orden.getEstado().equals("Transportando")) {
            imagen_estado.setImageResource(R.drawable.estado_transportando_i5);
            txt_consecutivo_orden.setText(orden.getEstado());
            texto_cambio_estado.setText("Actualmente transportando al(los) pasajero(s)");
            bonton_estado.setText("" + getString(R.string.finalizado));
            estado_conductor.setImageResource(R.drawable.img_transportando_i5);
            texto_foter.setText("Para reportar que el servicio ha finalizado y que el pasajero o pasajeros están en el lugar de destino.\n" +
                    "Los pasajeros serán notificados de la finalización.");


        } else if (orden.getEstado().equals("Finalizado")) {
            imagen_estado.setImageResource(R.drawable.estado_finalizado_i5);
            txt_consecutivo_orden.setText(orden.getEstado());
            texto_cambio_estado.setText("No se ha podido finalizar correctamente. Intente nuevamente");
            bonton_estado.setText("" + getString(R.string.finalizado));
            bonton_estado.setVisibility(View.VISIBLE);
            cancelar_servicio.setBackgroundResource(R.color.verdeLima);
            cancelar_servicio.setClickable(false);
            estado_conductor.setImageResource(R.drawable.img_finalizado_i5);
            texto_foter.setText("");
            orden.setEstado("Finalizado");



        } else if (orden.getEstado().equals("No Asignado") || orden.getEstado().equals("NoAsignado") || orden.getEstado().equals("SinConfirmar")) {
            imagen_estado.setImageResource(R.drawable.estado_sin_confirmar_i5);
            txt_consecutivo_orden.setText(orden.getEstado());
            cancelar_servicio.setVisibility(View.INVISIBLE);
            cancelar_servicio.setClickable(false);
            texto_foter.setText("No fue asignada");
            bonton_estado.setText("No fue asignada");

            bonton_estado.setVisibility(View.GONE);


            texto_foter.setVisibility(View.GONE);



            bonton_estado.setClickable(false);
            return;
        } else if (orden.getEstado().equals("Cotizar")) {

            imagen_estado.setImageResource(R.drawable.estado_cotizar);
            txt_consecutivo_orden.setText(orden.getEstado());
            estadservicios.setVisibility(View.GONE);
            layout_cotizar.setVisibility(View.VISIBLE);
            acetar_rechazar.setVisibility(View.GONE);

            cancelar_servicio.setVisibility(View.INVISIBLE);
            cancelar_servicio.setClickable(false);

            if (orden.cotizacionesT.size() > 0) {
                if (orden.cotizacionesT.get(0).cotizaciones.size() > 0) {


                    for (int i = 0; i < orden.cotizacionesT.get(0).cotizaciones.size(); i++) {
                        if (orden.cotizacionesT.get(0).cotizaciones.get(i).getId().equals(modelo.uid)) {

                            txt_cotizar.setText("El valor del servicio es:");
                            preciocotizar.setText(modelo.puntoDeMil("" + orden.cotizacionesT.get(0).cotizaciones.get(i).getValor()));
                            preciocotizar.setFocusable(false);
                            btn_cotizar.setVisibility(View.GONE);
                        }
                    }

                }
            }

            return;

        }



    }


    public void nuevoDestino(View v) {
        Intent i = new Intent(getBaseContext(), NuevoDestino.class);
        i.putExtra("id", "" + idServicio);
        i.putExtra("ciudad", "" + "Ciudad");
        this.startActivity(i);
    }

    public void destinoHistorial(View v) {
        Intent i = new Intent(getBaseContext(), HistorialDestino.class);
        i.putExtra("id", "" + idServicio);
        this.startActivity(i);

        comandoOrdenesConductor.getOrdenesConductor();

    }

    public void enviarMensaje(View v) {
        Intent i = new Intent(getApplicationContext(), Mensaje.class);
        i.putExtra("id", "" + idServicio);
        i.putExtra("arrayTokens", tokens);
        startActivity(i);

    }

    public void setRetrasos(View v) {
        Intent intent = new Intent(getApplicationContext(), ListaRetrasos.class);
        intent.putExtra("IDORDEN", idServicio);
        startActivity(intent);

    }





    private void actualizarDatos() {


        ComandoListadoPasajeros comando = new ComandoListadoPasajeros(this);

        if (orden == null) {
            return;
        }

        if (orden.getOrigen() != null) {
            text_ciudad_origen.setText(orden.getOrigen());
        } else {
            text_ciudad_origen.setText("Cargando información...");
        }

        text_ciudad_llegada.setText(orden.getDestino());
        numero_orden.setText("SERVICIO " + orden.getCosecutivoOrden());
        barrio_recogida.setText(orden.getDireccionOrigen());
        barrio_llegada.setText(orden.getDireccionDestino());
        fecha_y_hora_recogida.setText("Recoger: " + modelo.dfsimple.format(orden.getFechaEnOrigen()) + " " + orden.getHora());

        if (orden.getMatricula() == null) {
            vehiculo_asignado.setText("Vehículo no asignado");

        } else {
            vehiculo_asignado.setText("Vehículo asignado: " + orden.getMatricula());

        }

        for (int i = 0; i < orden.pasajeros.size(); i++) {
            comando.getListadoPasajerosConductor(orden.pasajeros.get(i).getTipo(), orden.pasajeros.get(i).getIdPasajero(), idServicio, orden.getSolicitadoPor());
            Log.v("pasajero", "pasajero" + orden.pasajeros.get(i).getCelular());

        }



        if (orden.getDestino().equals("ABIERTO")) {
            paradas.setVisibility(View.GONE);
            barrio_llegada.setText("");


            if (orden.getDiasServicio().equals("") && orden.getHorasServicio().equals("")) {
                barrio_llegada.setText("Duración del servicio: Indefinido");
            }

            if (!orden.getDiasServicio().equals("")) {
                String texto = "";

                if (orden.getDiasServicio().equals("1")) {
                    texto = "día";
                } else {
                    texto = "días";
                }
                barrio_llegada.setText("Duración del servicio: " + orden.getDiasServicio() + " " + texto);
            }

            if (!orden.getHorasServicio().equals("")) {
                String texto = "";

                if (orden.getHorasServicio().equals("1")) {
                    texto = "hora";
                } else {
                    texto = "horas";
                }
                barrio_llegada.setText("Duración del servicio:" + " " + texto);
            }
        }

        actualizarImagenesYBotones();

    }

    //gps

    //gps
    public void gps(View v) {
        //if (modelo.provider.equals("desactivado")) {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // if (modelo.provider.equals("desactivado")) {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlerGpsDesactivado();
        } else {
            infoUsuarios();
        }

    }


    @Override
    public void cargoActualizarOfertaTerceros(String idServicio) {

        orden = modelo.getOrden(idServicio);

        Toast.makeText(getApplicationContext(), "Servicio Aceptado", Toast.LENGTH_LONG).show();
        //
        acetar_rechazar.setVisibility(View.GONE);
        estadservicios.setVisibility(View.VISIBLE);
        actualizarDatos();

    }



    @Override
    public void cargoUbicacion() {

    }

    @Override
    public void actualizoUbicacion() {
        Log.v("distaciaylat", modelo.latitud + " " + modelo.distanciasRecorrida);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //lat_distancia.setText(modelo.latitud + " " + modelo.distanciasRecorrida);
            }
        });


    }

    @Override
    public void cargoMensaje() {

    }

    @Override
    public void cargoMensajeNoti() {

    }






    private long getDistanciaConduciendo(DirectionsResult results) {


        if (results == null || results.routes == null) {
            return 0;
        }

        if (results.routes.length == 0) {
            return 0;
        }
        if (results.routes[0].legs.length == 0) {
            return 0;
        }

        return results.routes[0].legs[0].distance.inMeters;
    }




    public void infoUsuarios() {

        if (orden.ubicacionGPss.size() > 0) {
            selectUbicacion2();

        } else {
            Toast.makeText(getApplicationContext(), "Sin datos de la ubicación", Toast.LENGTH_SHORT).show();
        }
    }

    public void selectUbicacion2() {

        modelo.latitudO = 0.0;
        modelo.latitudD = 0.0;
        modelo.longitudO = 0.0;
        modelo.longitudD = 0.0;

        if (orden.ubicacionGPss.size() > 0) {
            for (int i = 0; i < orden.ubicacionGPss.size(); i++) {

                //esta

                if (i == 0) {
                    if (orden.ubicacionGPss.get(i).getLatOrigen() != 0.0) {
                        modelo.latitudO = orden.ubicacionGPss.get(i).getLatOrigen();
                        modelo.longitudO = orden.ubicacionGPss.get(i).getLonOrigen();
                    } else {
                        modelo.latitudD = orden.ubicacionGPss.get(i).getLatDestino();
                        modelo.longitudD = orden.ubicacionGPss.get(i).getLonDestino();

                    }

                }
                if (i == 1) {

                    if (orden.ubicacionGPss.get(i).getLatOrigen() != 0.0) {
                        modelo.latitudO = orden.ubicacionGPss.get(i).getLatOrigen();
                        modelo.longitudO = orden.ubicacionGPss.get(i).getLonOrigen();
                    } else {
                        modelo.latitudD = orden.ubicacionGPss.get(i).getLatDestino();
                        modelo.longitudD = orden.ubicacionGPss.get(i).getLonDestino();

                    }
                }
            }

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("id", idServicio);
            intent.putExtra("origen", orden.getOrigen());
            intent.putExtra("destino", orden.getDestino());

            startActivity(intent);
        } else {
            //nadie a compartido ubicacion
            Toast.makeText(getApplicationContext(), "No hay ubicaciones compartidas.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cargoNotificaciones() {

    }

    @Override
    public void cargoUnaOrdenesConductorTerceros() {

        sendNotiYProcess();

    }

    @Override
    public void resultadoAlAsignar(boolean exitoso) {


        //comando actualizar la orden  llenar datos y poner terceros en false
        Log.v("Exitooso", "Exitooso");
        //  actualizarOrden();
        comandoActualizarOfertaTerceros.actualizarOrden(idServicio);
        actualizarDatos();

    }

    @Override
    public void resultadoAlAsignarCancel(boolean cancel) {
        showAlerDisponivilidad();
    }


    @Override
    public void cargoUnaOrdenesConductorTercerosCoti() {

    }

    //acptar y rechazar un servicio
    public void acetar_servicio(View v) {


        orden = modelo.getOrden(idServicio);



        if (orden != null && orden.getEstado().equals("NoAsignado")){
            comandoOrdenesConductorTerceros.asignarTrayectoSeguro(idServicio, modelo.uid);
            return;
        }

        showAlerDisponivilidadTemporal();

    }

    public void rechazar_servicio(View v) {
        finish();
    }



    public void paradas(View v) {
        Intent intent = new Intent(getApplicationContext(), Rutas.class);
        intent.putExtra("id", "" + idServicio);
        intent.putExtra("estado", "activo");
        startActivity(intent);
    }


    public void showAlerDisponivilidad() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("¡Lo sentimos!");

        // set dialog message
        alertDialogBuilder
                .setMessage("El servicio ha sido tomado")
                .setCancelable(false)
                .setPositiveButton("OK, ENTENDIDO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it

        try {
            alertDialog.show();
        } catch (Exception ex) {
        }

    }

    public void showAlerDisponivilidadTemporal() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("¡Lo sentimos!");

        // set dialog message
        alertDialogBuilder
                .setMessage("El servicio no esta disponible intenta más tarde")
                .setCancelable(false)
                .setPositiveButton("OK, ENTENDIDO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it

        try {
            alertDialog.show();
        } catch (Exception ex) {
        }

    }


    public void btncotizar(View v) {

        if (preciocotizar.length() > 4) {

            //ocultar el botón
            btn_cotizar.setEnabled(false);
            btn_cotizar.setVisibility(View.GONE);
            //ocultar teclado
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(preciocotizar.getWindowToken(), 0);

            // transaciones ok
            //comandoActualizarOfertaTerceros.enviarCotizacion(idServicio, preciocotizar.getText().toString());

            progressDialog.setMessage("Validando la información...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            comandoActualizarOfertaTerceros.setCotizacion(idServicio, precionSinconvercion, new ComandoActualizarOfertaTerceros.OnIntentoDeCotizar() {
                @Override
                public void cotizacionExitosa() {
                    if (progressDialog != null) {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception e) {
                        }
                    }

                    showAlerCotizar();
                }

                @Override
                public void cotizacionFallida() {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    showAlerCotizacionFallida();
                }
            });


        } else {
            preciocotizar.setError("la oferta debe ser superior a $1.000");
        }
    }


    @Override
    public void cargoCotizar() {


    }

    public void showAlerCotizar() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("¡Gracias!");

        // set dialog message
        alertDialogBuilder
                .setMessage("Hemos recibido tu oferta. Si ésta es aprobada, el servicio será asignado.")
                .setCancelable(false)
                .setPositiveButton("OK, ENTENDIDO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        layout_cotizar.setVisibility(View.GONE);
                        //
                        sendNotiYProcess();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it

        try {
            alertDialog.show();
        } catch (Exception ex) {
        }
    }


    public void showAlerCotizacionFallida() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Lo sentimos");

        // set dialog message
        alertDialogBuilder
                .setMessage("Tu oferta llego un poco tarde y ya no está disponible")
                .setCancelable(false)
                .setPositiveButton("OK, ENTENDIDO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it

        try {
            alertDialog.show();
        } catch (Exception ex) {
        }
    }

    public void observaciones(View v) {
        Intent intent = new Intent(getApplicationContext(), Observaciones.class);
        intent.putExtra("id", "" + idServicio);
        intent.putExtra("estado", "activo");
        startActivity(intent);
    }




    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(DetalleServicio.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mLocationPermissionGranted = true;
            startLocationUpdates();
            //getUltimaUbicacion();


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }


    }


    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5 * 1000);
        mLocationRequest.setSmallestDisplacement(3);
        mLocationRequest.setFastestInterval(4 * 1000);


        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);

        builder.setAlwaysShow(true);


        SettingsClient settingsClient = LocationServices.getSettingsClient(DetalleServicio.this);
        Task resul = settingsClient.checkLocationSettings(builder.build());


        final LocationRequest finalMLocationRequest = mLocationRequest;
        resul.addOnSuccessListener(this, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

                //fClient.requestLocationUpdates(finalMLocationRequest, mLocationCallback, null );


            }
        });

        resul.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(DetalleServicio.this,
                                REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION) {
            if (resultCode == RESULT_OK) {
                mLocationPermissionGranted = true;

            } else {
                mLocationPermissionGranted = false;
            }

            startLocationUpdates();
        }

    }

    private void stopLocationUpdates() {
        fClient.removeLocationUpdates(mLocationCallback);
    }


    /////////////////////////////

    @Override
    public void onStart() {
        super.onStart();


        Intent intent = new Intent(this, LocService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


        if (music == null) {
            return;
        }


    }


    @Override
    public void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
        }

        mBound = false;
        //Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();

    }


    private void lanzarLocService() {
        Intent i = new Intent(this, LocService.class);
        startService(i);
    }


    //Notificación de llegada

    public void notificaion_recojida(View v) {
        notificacionDeLlegada();
    }

    public void notificacionDeLlegada() {

        final String puntoDeRecojida = "El conductor se encuentra en el punto de recogida.";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Notificación de llegada");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("¿Desea notificar a los pasajeros que estás en el punto de recogida? ");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("SÍ, ENVIAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                bonton_estado.setVisibility(View.VISIBLE);
                btn_puntorecojida.setVisibility(View.GONE);
                comandoCompartirUbicacion.actualizarTimeStampInicial(idServicio);
                comandoMensaje.enviarMensaje(idServicio, puntoDeRecojida);
                orden.envioPuntoRecogida = true;
                if (!modelo.params.hasRegistroInmediato) {
                    layRetrasos.setVisibility(View.VISIBLE);
                    espacioRetra.setVisibility(View.VISIBLE);
                }


                if (tokens != null) {
                    comandoMensaje.notificacionMensaje(tokens, "", "" + puntoDeRecojida);
                }

            }
        });
        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //btn_puntorecojida.setVisibility(View.GONE);
                //bonton_estado.setVisibility(View.VISIBLE);
                //comandoCompartirUbicacion.actualizarTimeStampInicial(idServicio);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception ex) {
        }

    }


    public void showAlerGpsDesactivado() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("GPS deshabilitado");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Active la función de localización para determinar su ubicación ");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Ajustes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingsIntent);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        /*alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
            }
        });*/

        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception ex) {
        }

    }

    /*
    *   //comando que me valida los estados de los servicios
            ComandoOrdenesConductor.escucharEstadosOrden(orden.getId(), new ComandoOrdenesConductor.OnEscucharEstados() {
                @Override
                public void cambio() {
                    showAlerCotizar();
                    acetar_rechazar.setVisibility(View.GONE);
                    estadservicios.setVisibility(View.VISIBLE);
                }

                @Override
                public void cambioError() {
                    showAlerCotizacionFallida();
                }

            });
            */




    //ir al listado de pasajeros
    public void listaPasajeros(View v) {

        Intent intent = new Intent(getApplicationContext(), ListaPasajeros.class);
        intent.putExtra("id", "" + idServicio);
        intent.putExtra("estado", "activo");
        intent.putExtra("vista", "0");
        startActivity(intent);

    }


}
