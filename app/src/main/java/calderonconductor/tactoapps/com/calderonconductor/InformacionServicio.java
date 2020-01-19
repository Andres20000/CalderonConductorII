package calderonconductor.tactoapps.com.calderonconductor;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

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


import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Cliente;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes.OnCheckDistancia;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoActualizarOfertaTerceros;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoCliente;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoCompartirUbicacion;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoEnviarMensaje;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoListadoPasajeros;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoNotificaciones;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductor.OnFinalizarOrden;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductorTerceros;
import calderonconductor.tactoapps.com.calderonconductor.retrasos.ListaRetrasos;
import calderonconductor.tactoapps.com.calderonconductor.servicios.LocService;
import calderonconductor.tactoapps.com.calderonconductor.servicios.LocService.LocalBinder;
import calderonconductor.tactoapps.com.calderonconductor.servicios.LocService.OnLocCambio;

public class InformacionServicio extends Activity implements  ComandoListadoPasajeros.OnComandoPasajerosChangeListener, ComandoCliente.OnClienteChangeListener, ComandoNotificaciones.OnNotificacionesChangeListener, ComandoOrdenesConductorTerceros.OnOrdenesConductorTercerosChangeListener, ComandoActualizarOfertaTerceros.OnActualizarOfertaTercerosChangeListener, ComandoCompartirUbicacion.OnCompartirUbicacionChangeListener, ComandoEnviarMensaje.OnMensajeChangeListener {

    String idServicio = "";
    TextView numero_orden;
    TextView text_ciudad_origen;
    ImageView imagen_estado;
    TextView text_ciudad_llegada;
    TextView barrio_recogida;
    TextView direcion_recogida;
    TextView barrio_llegada;
    TextView direcion_llegada;
    TextView fecha_y_hora_recogida;
    TextView vehiculo_asignado;
    TextView cantidad_pasajeros;
    TextView idCliente;
    TextView texto_foter;
    //TextView sumDirecto;
    TextView texto_cambio_estado;
    TextView txt_abierto;
    TextView txt_consecutivo_orden;
    ImageView estado_conductor, cron;
    OrdenConductor ordenConductor;
    Button bonton_estado;
    Button btn_puntorecojida;
    TextView btn_paradas,txtRetrasos;
    ImageView cancelar_servicio;
    Modelo modelo = Modelo.getInstance();
    ImageView imageView7;
    ImageView hoistorialdestinos;
    EditText preciocotizar;
    ComandoCliente cliente = new ComandoCliente(this);
    ComandoOrdenesConductor comandoOrdenesConductor;
    ComandoOrdenesConductorTerceros comandoOrdenesConductorTerceros;
    ComandoCompartirUbicacion comandoCompartirUbicacion;
    final Context context = this;
    private final int REQUEST_PERMISSION = 1000;
    String[] datoArray;
    String[] UIArray;
    String[] tokens;
    ComandoNotificaciones comandoNotificaciones;
    LinearLayout acetar_rechazar, estadservicios, paradas;
    LinearLayout layout_cotizar;
    LinearLayout cotiobs,layRetrasos;
    TextView txt_cotizar;
    Button btn_cotizar;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ComandoActualizarOfertaTerceros comandoActualizarOfertaTerceros;

    private FusedLocationProviderClient fClient;
    private LocationCallback mLocationCallback;
    private boolean mLocationPermissionGranted = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    final static int REQUEST_LOCATION = 199;

    LocService music;
    boolean mBound = false;


    private ProgressDialog progressDialog;
    LinearLayout tamanolistapasajeros;
    LinearLayout.LayoutParams params;

    String precionSinconvercion = "";
    String nofillegada = "";

    ComandoEnviarMensaje comandoMensaje;

    LinearLayout layout_pasa;
    ImageView flechap;
    TextView nombre;
    TextView celular;


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
        }catch (Exception e){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }


        if(idServicio.equals("") ){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }


        numero_orden = (TextView) findViewById(R.id.numero_orden);
        text_ciudad_origen = (TextView) findViewById(R.id.text_ciudad_origen);
        imagen_estado = (ImageView) findViewById(R.id.imagen_estado);
        txt_consecutivo_orden = (TextView) findViewById(R.id.txt_consecutivo_orden);
        text_ciudad_llegada = (TextView) findViewById(R.id.text_ciudad_llegada);
        barrio_recogida = (TextView) findViewById(R.id.barrio_recogida);
        //direcion_recogida = (TextView)findViewById(R.id.direcion_recogida);
        barrio_llegada = (TextView) findViewById(R.id.barrio_llegada);
        // direcion_llegada = (TextView) findViewById(R.id.direcion_llegada);
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


        boolean result = Utility.checkPermissionCall(InformacionServicio.this);

        ////
        layRetrasos = findViewById(R.id.layRetrasos);
        txtRetrasos = findViewById(R.id.retrasos);
        cron = findViewById(R.id.cron);
        ///






        //colocar el foco
        //preciocotizar.requestFocus();
        //metodo para colocar el punto a un precio en un edittext

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


        if (modelo.getOrden(idServicio).getOfertadaATerceros() == false && !modelo.getOrden(idServicio).getEstado().equals("Cotizar")) {
            estadservicios.setVisibility(View.VISIBLE);
        } else {
            cancelar_servicio.setVisibility(View.INVISIBLE);
            cancelar_servicio.setClickable(false);
            acetar_rechazar.setVisibility(View.VISIBLE);

        }


        comandoOrdenesConductor = new ComandoOrdenesConductor();

        comandoNotificaciones = new ComandoNotificaciones(this);

        actualizarDatosBasicos();


        cliente.getRazonSocialCliente(ordenConductor.getIdCliente(), idServicio);

        String commaSeparated = ordenConductor.getRuta();
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(commaSeparated.split("-")));

        paradas.setClickable(false);
        paradas.setOnClickListener(null);

        if (items.size() > 0)

        {

            if (items.get(0).toString().equals("")) {
                btn_paradas.setText("Sin Paradas");
            } else {
                btn_paradas.setText("Paradas: " + items.size());
            }
        } else

        {
            btn_paradas.setText("Sin Paradas");
        }




        displayLista();

        getLocationPermission();  //1 Este es para iniciar... en realidad nada que ver con el locationCallBack. Los permisos SI


        CmdOrdenes.checkDistancia(ordenConductor.getId(), new OnCheckDistancia() {
            @Override
            public void avance(int acumulado) {
                //sumDirecto.setText("" + acumulado + " mts");

            }
        });

        //comando que me valida los estados de los servicios
        ComandoOrdenesConductor.escucharEstadosOrden(ordenConductor.getId(), new ComandoOrdenesConductor.OnEscucharEstados() {
            @Override
            public void cambio() {

                if (ordenConductor.getEstado().equals("Asignado")) {
                    //showAlerCotizar();

                    imagen_estado.setImageResource(R.drawable.estado_confirmado_i5);
                    txt_consecutivo_orden.setText(ordenConductor.getEstado());
                    texto_cambio_estado.setText("" + getString(R.string.enCamino));
                    bonton_estado.setText("" + getString(R.string.enCamino));
                    estado_conductor.setImageResource(R.drawable.img_confirmado_i5);
                    texto_foter.setText("El o los pasajeros serán notificados que el servicio se dirige al punto de recogida.");

                    acetar_rechazar.setVisibility(View.GONE);
                    estadservicios.setVisibility(View.VISIBLE);
                    layout_cotizar.setVisibility(View.GONE);


                    return;
                }

            }

            @Override
            public void cambioError() {
                showAlerDisponivilidad();
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


                        String estado = ordenConductor.getEstado();

                        if (estado.equals("EnCamino") || estado.equals("En Camino")) {
                            comandoCompartirUbicacion.actualizarUbicacion1(modelo.latitud, modelo.longitud, ordenConductor.getId());
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


    }


    public void actualizarBotonRetrasos(){

        if (modelo.getOrden(idServicio) == null){
            return;
        }

        if (modelo.getOrden(idServicio).hayRetrazoAbierto()){
            cron.setVisibility(View.VISIBLE);
            layRetrasos.setBackgroundColor(getResources().getColor(R.color.colorAmarilloQuemao));
            txtRetrasos.setTextColor(getResources().getColor(R.color.colorNegro));

        }else{
            cron.setVisibility(View.GONE);
            layRetrasos.setBackgroundColor(getResources().getColor(R.color.colorCafe));
            txtRetrasos.setTextColor(getResources().getColor(R.color.color_blanco));
        }

    }


    public void onSaveInstanceState(Bundle outState) {
        outState.putString("IDSERVICIO", ordenConductor.getId());
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


    private void displayLista() {
        //pasamos los datos del adaptador


        int cantitadadP = modelo.getOrden(idServicio).pasajeros.size();

       /* if (cantitadadP <= 3) {
            int alto2 = 60 * cantitadadP;
            // lp = new FrameLayout.LayoutParams(alto2, ViewGroup.LayoutParams.MATCH_PARENT);
            //tamanolistapasajeros.getLayoutParams().height =alto2;
            params = (LinearLayout.LayoutParams) tamanolistapasajeros.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = alto2;
            tamanolistapasajeros.setLayoutParams(params);

        } else {
            //lp = new FrameLayout.LayoutParams(75, ViewGroup.LayoutParams.MATCH_PARENT);
            params = (LinearLayout.LayoutParams) tamanolistapasajeros.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = 180;
            tamanolistapasajeros.setLayoutParams(params);
        }*/


    }

  /*  @Override
    public void cargoUnaOrdenesConductor() {
        Log.v("prueba", "" + ordenConductor.getId());

    }*/


    public void terminarFinalizacionOrden(OrdenConductor orden) {

        /*for (int i = 0; i < modelo.getOrdenes().size(); i++) {

            if (modelo.getOrdenes().get(i).getId().equals(ordenConductor.getId())) {
                modelo.getOrdenes().remove(i);
            }
        }*/

        Log.v("finalizo", "finalizo");
        comandoOrdenesConductor.crearActualizarDateTime(idServicio);
        comandoCompartirUbicacion.datosRecorreido(idServicio, modelo.cliente, orden);


        Intent i = new Intent(getApplicationContext(), Pgina_Principa.class);
        i.putExtra("vistaPosicion", "dos");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();

    }


    @Override
    public void cargoPasajero(String nombrePasajero) {

        int cantidadPasjeros = modelo.getOrden(idServicio).pasajeros.size();
        cantidad_pasajeros.setText("" + cantidadPasjeros);

        if(cantidadPasjeros == 1){
            layout_pasa.setClickable(false);
            flechap.setVisibility(View.GONE);
        }

        UIArray = new String[modelo.getOrden(idServicio).pasajeros.size()];
        datoArray = new String[modelo.getOrden(idServicio).pasajeros.size()];
        tokens = new String[modelo.getOrden(idServicio).pasajeros.size()];


        String telefono ="";
        for (int i = 0; i < modelo.getOrden(idServicio).pasajeros.size(); i++) {

            if(modelo.getOrden(idServicio).pasajeros.get(i).getIdPasajero().equals(modelo.getOrden(idServicio).getSolicitadoPor())){
                nombre.setText(modelo.getOrden(idServicio).pasajeros.get(i).getNombre()+ " " + modelo.getOrden(idServicio).pasajeros.get(i).getApellido());
                celular.setText(modelo.getOrden(idServicio).pasajeros.get(i).getCelular());
                telefono =modelo.getOrden(idServicio).pasajeros.get(i).getCelular();

            }
            if(modelo.getOrden(idServicio).pasajeros.get(i).getTipo().equals("empresarial")){
                nombre.setText(modelo.getOrden(idServicio).pasajeros.get(i).getNombre()+ " " + modelo.getOrden(idServicio).pasajeros.get(i).getApellido());
                celular.setText(modelo.getOrden(idServicio).pasajeros.get(i).getCelular());
                telefono =modelo.getOrden(idServicio).pasajeros.get(i).getCelular();

            }



            UIArray[i] = modelo.getOrden(idServicio).pasajeros.get(i).getIdPasajero();
            datoArray[i] = modelo.getOrden(idServicio).pasajeros.get(i).getNombre() + " " + modelo.getOrden(idServicio).pasajeros.get(i).getApellido();
            tokens[i] = modelo.getOrden(idServicio).pasajeros.get(i).getTokenDevice();
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
        //Toast.makeText(getApplicationContext(), "No hay  pasajeros asignados", Toast.LENGTH_LONG).show();
    }


    @Override
    public void cargoCliente() {
        Cliente cliente = modelo.getOrden(idServicio).getCliente();
        idCliente.setText(cliente.getRazonSocial());
    }


    public void atras(View v) {
        atrasback();
    }

    public void atrasback() {
        finish();
    }

    public void cancelarServicio(View v) {
        Intent i = new Intent(getApplicationContext(), CancelarServicio.class);
        i.putExtra("id", "" + idServicio);
        startActivity(i);
    }

    //cambiar estado del servicio
    public void cambiarEstadoServicio(View v) {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // if (modelo.provider.equals("desactivado")) {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlerGpsDesactivado();
        } else {
            showAlerCambioDeEstado();
        }

    }

    public void showAlerCambioDeEstado() {


        String estado = ordenConductor.getEstado();
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
                    .setMessage("¿Deseas cambiar tu estado " + "'" + ordenConductor.getEstado() + estado2 + "'?")
                    .setCancelable(false)
                    .setPositiveButton("SI, CAMBIAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //alert
                            comandoOrdenesConductor.actualizarEstado(ordenConductor.getEstado(), ordenConductor.getId());
                            notificarYFinalizar();

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


    public void finalizarTimer() {
        if (modelo.myTimer != null) {
            modelo.myTimer.cancel();
            modelo.myTimer = null;

            modelo.ordenHoy = "";
            modelo.estadoTimer = 0;
        }
    }


    //evento del boton cambio de estado.
    public void notificarYFinalizar() {

        actualizarDatosBasicosYMoverAlHistorico();


        if (ordenConductor.getEstado().equals("Asignado")) {
            if (tokens != null) {
                comandoNotificaciones.enviaoDeMensajeAlPAsajero(tokens, "¡Estado del Servicio!", "Su servicio cambió de Estado, se encuentra Asignado");
            }
            return;
        }

        if (ordenConductor.getEstado().equals("En Camino") || ordenConductor.getEstado().equals("EnCamino")) {

            if (tokens != null) {
                comandoNotificaciones.enviaoDeMensajeAlPAsajero(tokens, "¡Estado del Servicio!", "Su servicio cambió de Estado, se encuentra En Camino");
            }

            nofillegada = "1";
            guardarSharedPreferenceOferente();

            lanzarLocService();


            music.startLocationUpdates(ordenConductor, new OnLocCambio() {
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
        if (ordenConductor.getEstado().equals("Transportando")) {

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
        if (ordenConductor.getEstado().equals("Finalizado")) {

            comandoCompartirUbicacion.actualizarTimeStampFinal(modelo.latitud, modelo.longitud, idServicio);
            comandoOrdenesConductor.moverOrdenAlHistorico(ordenConductor.getId(), new OnFinalizarOrden() {
                @Override
                public void finalizo(OrdenConductor orden) {
                    terminarFinalizacionOrden(orden);
                }

                @Override
                public void fallo(OrdenConductor orden) {

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
                                    comandoOrdenesConductor.moverOrdenAlHistorico(ordenConductor.getId(), new OnFinalizarOrden() {
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

            return;
        }

        if (ordenConductor.getEstado().equals("No Asignado") || ordenConductor.getEstado().equals("NoAsignado") || ordenConductor.getEstado().equals("SinConfirmar")) {
            return;
        }

        if (ordenConductor.getEstado().equals("Cotizar")) {
            return;
        }




    }

    public void actualizarDatosBasicosYMoverAlHistorico() {

        cancelar_servicio.setVisibility(View.VISIBLE);
        cancelar_servicio.setClickable(true);

        bonton_estado.setClickable(true);
        bonton_estado.setEnabled(true);


        if (ordenConductor.getEstado().equals("Asignado")) {
            imagen_estado.setImageResource(R.drawable.estado_confirmado_i5);
            txt_consecutivo_orden.setText(ordenConductor.getEstado());
            texto_cambio_estado.setText("" + getString(R.string.enCamino));
            bonton_estado.setText("" + getString(R.string.enCamino));
            estado_conductor.setImageResource(R.drawable.img_confirmado_i5);
            texto_foter.setText("El o los pasajeros serán notificados que el servicio se dirige al punto de recogida.");

            acetar_rechazar.setVisibility(View.GONE);
            estadservicios.setVisibility(View.VISIBLE);
            layout_cotizar.setVisibility(View.GONE);

        } else if (ordenConductor.getEstado().equals("En Camino") || ordenConductor.getEstado().equals("EnCamino")) {


            imagen_estado.setImageResource(R.drawable.estado_en_camino_i5);
            txt_consecutivo_orden.setText(ordenConductor.getEstado());
            texto_cambio_estado.setText("Este Servicio está en Camino");
            bonton_estado.setText("" + getString(R.string.transportado));
            estado_conductor.setImageResource(R.drawable.img_en_camino_i5);
            texto_foter.setText("Notifique en la orden que el servicio ha dado inicio. El o los pasajeros serán notificados.");

            if (nofillegada.equals("1")) {
                bonton_estado.setVisibility(View.GONE);
                btn_puntorecojida.setVisibility(View.VISIBLE);
            }

            validacion2();

        } else if (ordenConductor.getEstado().equals("Transportando")) {
            imagen_estado.setImageResource(R.drawable.estado_transportando_i5);
            txt_consecutivo_orden.setText(ordenConductor.getEstado());
            texto_cambio_estado.setText("Actualmente transportando al(los) pasajero(s)");
            bonton_estado.setText("" + getString(R.string.finalizado));
            estado_conductor.setImageResource(R.drawable.img_transportando_i5);
            texto_foter.setText("Para reportar que el servicio ha finalizado y que el pasajero o pasajeros están en el lugar de destino.\n" +
                    "Los pasajeros serán notificados de la finalización.");


        } else if (ordenConductor.getEstado().equals("Finalizado")) {
            imagen_estado.setImageResource(R.drawable.estado_finalizado_i5);
            txt_consecutivo_orden.setText(ordenConductor.getEstado());
            texto_cambio_estado.setText("Actualmente transportando al(los) pasajero(s)");
            bonton_estado.setVisibility(View.GONE);
            cancelar_servicio.setBackgroundResource(R.color.verdeLima);
            cancelar_servicio.setClickable(false);
            estado_conductor.setImageResource(R.drawable.img_finalizado_i5);
            texto_foter.setText("");
            ordenConductor.setEstado("Finalizado");

          /*  comandoCompartirUbicacion.actualizarTimeStampFinal(modelo.latitud, modelo.longitud, idServicio);
            comandoOrdenesConductor.moverOrdenAlHistorico(ordenConductor.getId(), new OnFinalizarOrden() {
                @Override
                public void finalizo(OrdenConductor orden) {
                    terminarFinalizacionOrden(orden);
                }

                @Override
                public void fallo(OrdenConductor orden) {

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
                                    comandoOrdenesConductor.moverOrdenAlHistorico(ordenConductor.getId(), new OnFinalizarOrden() {
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
            });*/



        } else if (ordenConductor.getEstado().equals("No Asignado") || ordenConductor.getEstado().equals("NoAsignado") || ordenConductor.getEstado().equals("SinConfirmar")) {
            imagen_estado.setImageResource(R.drawable.estado_sin_confirmar_i5);
            txt_consecutivo_orden.setText(ordenConductor.getEstado());
            cancelar_servicio.setVisibility(View.INVISIBLE);
            cancelar_servicio.setClickable(false);
            texto_foter.setText("No fue asignada");
            bonton_estado.setText("No fue asignada");
            bonton_estado.setClickable(false);
            return;
        } else if (ordenConductor.getEstado().equals("Cotizar")) {

            imagen_estado.setImageResource(R.drawable.estado_cotizar);
            txt_consecutivo_orden.setText(ordenConductor.getEstado());
            estadservicios.setVisibility(View.GONE);
            layout_cotizar.setVisibility(View.VISIBLE);
            acetar_rechazar.setVisibility(View.GONE);

            cancelar_servicio.setVisibility(View.INVISIBLE);
            cancelar_servicio.setClickable(false);

            if (ordenConductor.cotizacionesT.size() > 0) {
                if (ordenConductor.cotizacionesT.get(0).cotizaciones.size() > 0) {


                    for (int i = 0; i < ordenConductor.cotizacionesT.get(0).cotizaciones.size(); i++) {
                        if (ordenConductor.cotizacionesT.get(0).cotizaciones.get(i).getId().equals(modelo.uid)) {

                            txt_cotizar.setText("El valor del servicio es:");
                            preciocotizar.setText(modelo.puntoDeMil("" + ordenConductor.cotizacionesT.get(0).cotizaciones.get(i).getValor()));
                            preciocotizar.setFocusable(false);
                            btn_cotizar.setVisibility(View.GONE);
                        }
                    }

                }
            }


            return;

        }

        if (!ordenConductor.getConductor().equals(modelo.uid)) {

            atrasback();
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




    private void actualizarDatosBasicos() {


        ComandoListadoPasajeros comando = new ComandoListadoPasajeros(this);
        ordenConductor =  modelo.getOrden(idServicio);

        if (ordenConductor == null) {
            return;
        }

        if (ordenConductor.getOrigen() != null) {
            text_ciudad_origen.setText(ordenConductor.getOrigen());
        } else {
            text_ciudad_origen.setText("Cargando información...");
        }

        text_ciudad_llegada.setText(ordenConductor.getDestino());
        numero_orden.setText("SERVICIO " + ordenConductor.getCosecutivoOrden());
        barrio_recogida.setText(ordenConductor.getDireccionOrigen());
        barrio_llegada.setText(ordenConductor.getDireccionDestino());
        fecha_y_hora_recogida.setText("Recoger: " + modelo.dfsimple.format(ordenConductor.getFechaEnOrigen()) + " " + ordenConductor.getHora());

        if (ordenConductor.getMatricula() == null) {
            vehiculo_asignado.setText("Vehículo no asignado");

        } else {
            vehiculo_asignado.setText("Vehículo asignado: " + ordenConductor.getMatricula());

        }

        for (int i = 0; i < modelo.getOrden(idServicio).pasajeros.size(); i++) {
            comando.getListadoPasajerosConductor(modelo.getOrden(idServicio).pasajeros.get(i).getTipo(), modelo.getOrden(idServicio).pasajeros.get(i).getIdPasajero(), idServicio, modelo.getOrden(idServicio).getSolicitadoPor());
        }



        if (modelo.getOrden(idServicio).getDestino().equals("ABIERTO")) {
            paradas.setVisibility(View.GONE);
            barrio_llegada.setText("");


            if (modelo.getOrden(idServicio).getDiasServicio().equals("") && modelo.getOrden(idServicio).getHorasServicio().equals("")) {
                barrio_llegada.setText("Duración del servicio: Indefinido");
            }

            if (!modelo.getOrden(idServicio).getDiasServicio().equals("")) {
                String texto = "";

                if (modelo.getOrden(idServicio).getDiasServicio().equals("1")) {
                    texto = "día";
                } else {
                    texto = "días";
                }
                barrio_llegada.setText("Duración del servicio: " + modelo.getOrden(idServicio).getDiasServicio() + " " + texto);
            }

            if (!modelo.getOrden(idServicio).getHorasServicio().equals("")) {
                String texto = "";

                if (modelo.getOrden(idServicio).getHorasServicio().equals("1")) {
                    texto = "hora";
                } else {
                    texto = "horas";
                }
                barrio_llegada.setText("Duración del servicio:" + " " + texto);
            }
        }

        actualizarDatosBasicosYMoverAlHistorico();

    }


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
        Toast.makeText(getApplicationContext(), "Servicio Aceptado", Toast.LENGTH_LONG).show();
        //
        acetar_rechazar.setVisibility(View.GONE);
        estadservicios.setVisibility(View.VISIBLE);
        notificarYFinalizar();
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





    //validacion de fecha
    public void ordenHoy() {
        Calendar calendar = Calendar.getInstance();
        final int year1 = calendar.get(Calendar.YEAR);
        final int month1 = calendar.get(Calendar.MONTH) + 1;
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        final String fecha_actual = day1 + "/" + month1 + "/" + year1;

        try {
            //convertimos a tipo fecha
            Date date_1 = modelo.dfsimple.parse(fecha_actual);
            Date date_2 = ordenConductor.getFechaEnOrigen();

            //convertimos de date a DAtetime
            DateTime fechanow = new DateTime(date_1);
            DateTime fechaOrden = new DateTime(date_2);

            //capturamos la diferencia de dias
            int dias = Days.daysBetween(fechanow, fechaOrden).getDays();
            //System.out.println(date_1);
            System.out.println("" + dias);
            //if(dias == 0){
            if (modelo.estadoTimer == 0 && !modelo.ordenHoy.equals("")) {
                modelo.estadoTimer = 1;
                //comandoCompartirUbicacion.actualizarUbicacion(modelo.latitud, modelo.longitud, idServicio);
                //timeStamp
                //hilo1();
            }
            //}

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    public void hilo1() {

        modelo.myTimer = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                //perform your action here


                if (txt_consecutivo_orden.getText().toString().equals("Transportando")) {
                    if (modelo.latitud != 0 || modelo.longitudAnterior != 0) {
                        otenerDistancia();
                    }
                } else {
                    if (modelo.latitud != 0 || modelo.longitudAnterior != 0) {
                        comandoCompartirUbicacion.actualizarUbicacion1(modelo.latitud, modelo.longitud, idServicio);
                    }

                }


            }
        };
        modelo.myTimer.schedule(timerTaskObj, 0, 10000);

    }

    public void otenerDistancia() {

        DirectionsResult result = null;
        DateTime now = new DateTime();
        try {


            //origen lat y long vieja
            //destino lat y long nueva


            result = DirectionsApi.newRequest(getGeoContext()).mode(TravelMode.DRIVING).origin(new com.google.maps.model.LatLng(modelo.latitudAnterior, modelo.longitudAnterior))
                    .destination(new com.google.maps.model.LatLng(modelo.latitud, modelo.longitud)).departureTime(now).await();

            //  addPolyline(result, mMap);

            getDistanciaConduciendo(result);

            comandoCompartirUbicacion.actualizarUbicacion(modelo.latitud, modelo.longitud, idServicio, getDistanciaConduciendo(result));


            if (modelo.latitud != modelo.latitudAnterior) {
                modelo.latitudAnterior = modelo.latitud;
                modelo.longitudAnterior = modelo.longitud;

                Log.v("latitude11", "" + modelo.latitudAnterior);
                Log.v("longitude22", "" + modelo.longitudAnterior);
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


    //validacion conexion internet
    protected Boolean estaConectado() {
        if (conectadoWifi()) {
            Log.v("wifi", "Tu Dispositivo tiene Conexion a Wifi.");
            return true;
        } else {
            if (conectadoRedMovil()) {
                Log.v("Datos", "Tu Dispositivo tiene Conexion Movil.");
                return true;
            } else {
                showAlertSinInternet();
                // Toast.makeText(getApplicationContext(),"Sin Conexión a Internet", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    protected Boolean conectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }


    public void showAlertSinInternet() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Sin Internet");

        // set dialog message
        alertDialogBuilder
                .setMessage("Sin Conexión a Internet")
                .setCancelable(false)
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

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


    public void infoUsuarios() {

        if (ordenConductor.ubicacionGPss.size() > 0) {
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

        if (ordenConductor.ubicacionGPss.size() > 0) {
            for (int i = 0; i < ordenConductor.ubicacionGPss.size(); i++) {

                //esta

                if (i == 0) {
                    if (ordenConductor.ubicacionGPss.get(i).getLatOrigen() != 0.0) {
                        modelo.latitudO = ordenConductor.ubicacionGPss.get(i).getLatOrigen();
                        modelo.longitudO = ordenConductor.ubicacionGPss.get(i).getLonOrigen();
                    } else {
                        modelo.latitudD = ordenConductor.ubicacionGPss.get(i).getLatDestino();
                        modelo.longitudD = ordenConductor.ubicacionGPss.get(i).getLonDestino();

                    }


                }
                if (i == 1) {

                    if (ordenConductor.ubicacionGPss.get(i).getLatOrigen() != 0.0) {
                        modelo.latitudO = ordenConductor.ubicacionGPss.get(i).getLatOrigen();
                        modelo.longitudO = ordenConductor.ubicacionGPss.get(i).getLonOrigen();
                    } else {
                        modelo.latitudD = ordenConductor.ubicacionGPss.get(i).getLatDestino();
                        modelo.longitudD = ordenConductor.ubicacionGPss.get(i).getLonDestino();

                    }
                }
            }

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("id", idServicio);
            intent.putExtra("origen", ordenConductor.getOrigen());
            intent.putExtra("destino", ordenConductor.getDestino());

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

        notificarYFinalizar();

    }

    @Override
    public void resultadoAlAsignar(boolean exitoso) {


        //comando actualizar la orden  llenar datos y poner terceros en false
        Log.v("Exitooso", "Exitooso");
        //  actualizarOrden();
        comandoActualizarOfertaTerceros.actualizarOrden(idServicio);

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

        comandoOrdenesConductorTerceros.asignarTrayectoSeguro(idServicio, modelo.uid);
        //comandoOrdenesConductorTerceros.asignarTrayectoSeguro2(idServicio, modelo.uid);


    }

    public void rechazar_servicio(View v) {
        atrasback();
    }

    public void paradas(View v) {
        Intent intent = new Intent(getApplicationContext(), Rutas.class);
        intent.putExtra("id", "" + idServicio);
        intent.putExtra("estado", "activo");
        startActivity(intent);
    }

    public void actualizarOrden() {
        DatabaseReference ref = database.getReference("ordenes/pendientes/" + idServicio + "/asignadoPor/");
        ref.setValue("autoAsignado");

        DatabaseReference ref2 = database.getReference("ordenes/pendientes/" + idServicio + "/matricula/");
        ref2.setValue(modelo.placa);

        DatabaseReference ref3 = database.getReference("ordenes/pendientes/" + idServicio + "/ofertadaATerceros/");
        ref3.setValue(false);

        DatabaseReference ref4 = database.getReference("ordenes/pendientes/" + idServicio + "/estado/");
        ref4.setValue("Asignado");

        Toast.makeText(getApplicationContext(), "Servicio Aceptado", Toast.LENGTH_LONG).show();
        //
        acetar_rechazar.setVisibility(View.GONE);
        estadservicios.setVisibility(View.VISIBLE);
        //notificarYFinalizar();
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
                        atrasback();

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

            Log.v("errrorrrrr", "proceso alert");
            progressDialog.setMessage("Validando la información...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            comandoActualizarOfertaTerceros.setCotizacion(idServicio, precionSinconvercion, new ComandoActualizarOfertaTerceros.OnIntentoDeCotizar() {
                @Override
                public void cotizacionExitosa() {
                    if (progressDialog != null) {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception e){}
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
                        notificarYFinalizar();

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

    //bhtn_atras_hadware
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("cerrar", "cerrar");
            atrasback();
        }
        return false;
    }


    public void validacion2() {
        if (modelo.ordenHoy.equals("")) {
            modelo.ordenHoy = idServicio;
            ordenHoy();
        } else {
            if (!modelo.ordenHoy.equals(idServicio)) {
                modelo.ordenHoy = idServicio;
                modelo.estadoTimer = 0;
                if (modelo.myTimer != null) {
                    modelo.myTimer.cancel();
                }
                modelo.myTimer = null;

                ordenHoy();

            }

        }
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
        if (ContextCompat.checkSelfPermission(InformacionServicio.this,
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


        SettingsClient settingsClient = LocationServices.getSettingsClient(InformacionServicio.this);
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
                        resolvable.startResolutionForResult(InformacionServicio.this,
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
                nofillegada = " ";
                guardarSharedPreferenceOferente();
                bonton_estado.setVisibility(View.VISIBLE);
                btn_puntorecojida.setVisibility(View.GONE);
                comandoCompartirUbicacion.actualizarTimeStampInicial(idServicio);

                comandoMensaje.enviarMensaje(idServicio, puntoDeRecojida);

                if (tokens != null) {
                    comandoMensaje.notificacionMensaje(tokens, "", "" + puntoDeRecojida);
                }

            }
        });
        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nofillegada = " ";
                guardarSharedPreferenceOferente();
                btn_puntorecojida.setVisibility(View.GONE);
                bonton_estado.setVisibility(View.VISIBLE);
                comandoCompartirUbicacion.actualizarTimeStampInicial(idServicio);
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
            ComandoOrdenesConductor.escucharEstadosOrden(ordenConductor.getId(), new ComandoOrdenesConductor.OnEscucharEstados() {
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


    //sharedpreferences
    public void cargarSharedPreferenceOferente() {
        SharedPreferences sharedPreferences = getSharedPreferences("preferencias nofillegada", Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor =sharedPreferences.edit();

        nofillegada = sharedPreferences.getString("notifilelgada_", "");

    }

    public void guardarSharedPreferenceOferente() {
        SharedPreferences sharedPreferences = getSharedPreferences("preferencias nofillegada", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putString("notifilelgada_", nofillegada);

        editor.commit();
    }



    //ir al listado de pasajeros
    public void listaPasajeros(View v){

        Intent intent = new Intent(getApplicationContext(), ListaPasajeros.class);
        intent.putExtra("id", "" + idServicio);
        intent.putExtra("estado", "activo");
        intent.putExtra("vista", "0");
        startActivity(intent);

    }


}
