package calderonconductor.tactoapps.com.calderonconductor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import calderonconductor.tactoapps.com.calderonconductor.Adapter.ListaComentariosHistoricoAdapter;
import calderonconductor.tactoapps.com.calderonconductor.Adapter.ListaPasajerosHistoricoAdapter;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Cliente;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoCliente;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoListadoPasajeros;


public class InformacionDelServicioDetallada extends Activity implements ComandoListadoPasajeros.OnComandoPasajerosChangeListener, ComandoCliente.OnClienteChangeListener, Modelo.OnModeloChangelistener {

    String idServicio;
    TextView numero_orden;
    TextView text_ciudad_origen;
    ImageView imagen_estado;
    TextView text_ciudad_llegada;
    TextView txt_abierto;
    TextView barrio_recogida;
    TextView direcion_recogida;
    TextView barrio_llegada;
    TextView direcion_llegada;
    TextView fecha_y_hora_recogida;
    TextView vehiculo_asignado;
    TextView cantidad_pasajeros;
    TextView idCliente;
    TextView texto_foter;
    TextView texto_cambio_estado;
    ImageView estado_conductor;
    LinearLayout paradas;
    OrdenConductor ordenConductor;
    ListView lvcomentario;
    RatingBar ratingBar;
    String fechaHistorco, razonSocal;
    Button bonton_estado;
    ListaComentariosHistoricoAdapter mAdapterComentario;
    ImageView cancelar_servicio;
    Modelo modelo = Modelo.getInstance();
    //instaciamos ComandoClinetes
    ComandoCliente cliente = new ComandoCliente(this);

    LinearLayout tamanolistapasajeros;
    LinearLayout.LayoutParams params;


    LinearLayout layout_pasa;
    ImageView flechap;
    TextView nombre;
    TextView celular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_informacion_del_servicio_detallada);

        if (savedInstanceState != null) {
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }


        Bundle bundle = getIntent().getExtras();
        idServicio = bundle.getString("id");
        fechaHistorco = bundle.getString("fecha");
        razonSocal = bundle.getString("razonSocial");


        //se llama el metodo  getOrden del snglton(Clase Modelo) para mostrar una sola  orden segun el id de la orden
        ordenConductor = modelo.getOrdenHistorial(idServicio);
        //Toast.makeText(getApplicationContext(),"/"+ordenConductor.getEstado(),Toast.LENGTH_SHORT).show();

        numero_orden = (TextView) findViewById(R.id.numero_orden);
        text_ciudad_origen = (TextView) findViewById(R.id.text_ciudad_origen);
        imagen_estado = (ImageView) findViewById(R.id.imagen_estado);
        text_ciudad_llegada = (TextView) findViewById(R.id.text_ciudad_llegada);
        txt_abierto = (TextView) findViewById(R.id.txt_abierto);
        barrio_recogida = (TextView) findViewById(R.id.barrio_recogida);
        //direcion_recogida = (TextView)findViewById(R.id.direcion_recogida);
        barrio_llegada = (TextView) findViewById(R.id.barrio_llegada);
        direcion_llegada = (TextView) findViewById(R.id.direcion_llegada);
        fecha_y_hora_recogida = (TextView) findViewById(R.id.fecha_y_hora_recogida);
        vehiculo_asignado = (TextView) findViewById(R.id.vehiculo_asignado);
        texto_foter = (TextView) findViewById(R.id.texto_foter);
        idCliente = (TextView) findViewById(R.id.idCliente);
        texto_cambio_estado = (TextView) findViewById(R.id.texto_cambio_estado);
        cantidad_pasajeros = (TextView) findViewById(R.id.cantidad_pasajeros);
        estado_conductor = (ImageView) findViewById(R.id.estado_conductor);
        lvcomentario = (ListView) findViewById(R.id.lvcomentario);
        bonton_estado = (Button) findViewById(R.id.bonton_estado);
        cancelar_servicio = (ImageView) findViewById(R.id.cancelar_servicio);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        tamanolistapasajeros = (LinearLayout) findViewById(R.id.tamanolistapasajeros);
        paradas = (LinearLayout) findViewById(R.id.paradas);

        layout_pasa = (LinearLayout) findViewById(R.id.layout_pasa);
        flechap = (ImageView) findViewById(R.id.flechap);
        nombre = (TextView) findViewById(R.id.nombre);
        celular = (TextView) findViewById(R.id.celular);

        modelo.setModeloListener(this);
        boolean result = Utility.checkPermissionCall(InformacionDelServicioDetallada.this);

        text_ciudad_origen.setText(ordenConductor.getOrigen());
        text_ciudad_llegada.setText(ordenConductor.getDestino());
        numero_orden.setText("SERVICIO " + ordenConductor.getCosecutivoOrden());
        barrio_recogida.setText(ordenConductor.getDireccionOrigen());
        barrio_llegada.setText(ordenConductor.getDireccionDestino());
        fecha_y_hora_recogida.setText("Recoger: " + modelo.dfsimple.format(ordenConductor.getFechaEnOrigen()) + " " + ordenConductor.getHora());
        vehiculo_asignado.setText("Vehículo asignado: " + ordenConductor.getMatricula());


        ListaComentariosHistoricoAdapter lAdapter = new ListaComentariosHistoricoAdapter(this, ordenConductor.getId());
        lvcomentario.setAdapter(lAdapter);

        if (ordenConductor.getDestino().equals("ABIERTO")) {
            //txt_abierto.setVisibility(View.VISIBLE);
            paradas.setVisibility(View.GONE);
            barrio_llegada.setText("");


            if (ordenConductor.getDiasServicio().equals("") && ordenConductor.getHorasServicio().equals("")) {
                barrio_llegada.setText("Duración del servicio: Indefinido");
            }

            if (!ordenConductor.getDiasServicio().equals("")) {
                String texto = "";

                if (ordenConductor.getDiasServicio().equals("1")) {
                    texto = "día";
                } else {
                    texto = "días";
                }
                barrio_llegada.setText("Duración del servicio: " + ordenConductor.getDiasServicio() + " " + texto);
            }

            if (!ordenConductor.getHorasServicio().equals("")) {
                String texto = "";

                if (ordenConductor.getHorasServicio().equals("1")) {
                    texto = "hora";
                } else {
                    texto = "horas";
                }
                barrio_llegada.setText("Duración del servicio:" + " " + texto);
            }
        }

        if (ordenConductor.getEstado().equals("Almacenado")) {
            imagen_estado.setImageResource(R.drawable.estado_finalizado_i5);
        } else {
            imagen_estado.setImageResource(R.drawable.estado_finalizado_i5);
        }


        //insataciamos las clase ComandoListadoPasajeros
        ComandoListadoPasajeros comando = new ComandoListadoPasajeros(this);

        cliente.getRazonSocialClienteHistorial(ordenConductor.getIdCliente(), idServicio);
//recorremos los datos del pasejero segun el idServicio y el id  del pasajero
        for (int i = 0; i < modelo.getOrdenHistorial(idServicio).pasajeros.size(); i++) {
            if (modelo.getOrdenHistorial(idServicio).pasajeros.get(i) != null) {
                comando.getListadoPasajerosConductorHistorial(modelo.getOrdenHistorial(idServicio).pasajeros.get(i).getTipo(), modelo.getOrdenHistorial(idServicio).pasajeros.get(i).getIdPasajero(), idServicio, modelo.getOrdenHistorial(idServicio).getSolicitadoPor());
                Log.v("pasajero", "pasajero" + modelo.getOrdenHistorial(idServicio).pasajeros.get(i).getCelular());

            }
        }


        displayLista();
        reefresh();

    }

    private void displayLista() {
        //pasamos los datos del adaptador
        int cantH = modelo.getOrdenHistorial(idServicio).pasajeros.size();

        int cantitadadP = modelo.getOrdenHistorial(idServicio).pasajeros.size();

        /*if (cantitadadP <= 3) {
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

    private void displayListaComentarios() {


    }


    @Override
    public void cargoPasajero(String nombrePasajero) {

        int cantidadPasjeros = modelo.getOrdenHistorial(idServicio).pasajeros.size();

        cantidad_pasajeros.setText("" + cantidadPasjeros);

        if (cantidadPasjeros == 1) {
            layout_pasa.setClickable(false);
            flechap.setVisibility(View.GONE);
        }

        if (modelo.params.mostrarInfoSensible) {
            String telefono = "";
            for (int i = 0; i < modelo.getOrdenHistorial(idServicio).pasajeros.size(); i++) {

                if (modelo.getOrdenHistorial(idServicio).pasajeros.get(i).getIdPasajero().equals(modelo.getOrdenHistorial(idServicio).getSolicitadoPor())) {
                    nombre.setText(modelo.getOrdenHistorial(idServicio).pasajeros.get(i).getNombre() + " " + modelo.getOrdenHistorial(idServicio).pasajeros.get(0).getApellido());
                    celular.setText(modelo.getOrdenHistorial(idServicio).pasajeros.get(i).getCelular());
                    telefono = modelo.getOrdenHistorial(idServicio).pasajeros.get(i).getCelular();

                }

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
    }

    @Override
    public void cargoPasajeroCero() {

    }

    @Override
    public void cargoCliente() {
        Cliente cliente = modelo.getOrdenHistorial(idServicio).getCliente();
        idCliente.setText(cliente.getRazonSocial());
    }


    public void atras(View v) {

        if (fechaHistorco.equals("")) {
            Intent i = new Intent(getApplicationContext(), HistorialUltimosServicios.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(getApplicationContext(), HistoricoServiciosDetallada.class);
            i.putExtra("fecha", "" + fechaHistorco);
            i.putExtra("razonSocial", "" + razonSocal);
            startActivity(i);
            finish();
        }
    }


    //bhtn_atras_hadware
   /* public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("cerrar","cerrar");
        }
        return false;
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void setActualizoListadoDeServicios() {
        reefresh();
    }

    public void reefresh() {
        ordenConductor = modelo.getOrdenHistorial(idServicio);
        if (ordenConductor != null) {
            ratingBar.setRating(Float.parseFloat("" + ordenConductor.getCalificacionPromedio()));
            displayListaComentarios();
        }

    }


    public void observaciones(View v) {
        Intent intent = new Intent(getApplicationContext(), Observaciones.class);
        intent.putExtra("id", "" + idServicio);
        intent.putExtra("estado", "finalizado");
        startActivity(intent);
    }

    public void paradas(View v) {
        Intent intent = new Intent(getApplicationContext(), Rutas.class);
        intent.putExtra("id", "" + idServicio);
        intent.putExtra("estado", "finalizado");
        startActivity(intent);
    }


    //ir al listado de pasajeros
    public void listaPasajeros(View v) {

        Intent intent = new Intent(getApplicationContext(), ListaPasajeros.class);
        intent.putExtra("id", "" + idServicio);
        intent.putExtra("estado", "finalizado");
        intent.putExtra("vista", "1");
        startActivity(intent);

    }
}
