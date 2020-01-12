package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import calderonconductor.tactoapps.com.calderonconductor.Adapter.OrdenesConductorAdapter;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoConductor.OnConductorListener;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductor;

public class Tap_2 extends Activity implements  Modelo.OnModeloChangelistener {

    private OrdenesConductorAdapter mAdapter;
    ListView lv;
    public ProgressBar progressBar;
    Modelo modelo = Modelo.getInstance();
    TextView sindatos;
    Button btn_disponible;
    final Context context = this;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final String TAG = "android";
    ComandoConductor comandoConductor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tap_2);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }



        lv = (ListView) findViewById(R.id.listView1);
        sindatos = (TextView) findViewById(R.id.sindatos);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        btn_disponible = (Button) findViewById(R.id.btn_disponible);
        comandoConductor = new ComandoConductor();


        if (modelo.boleandatosCondutor == 0) {
            comandoConductor.getConductor(new OnConductorListener() {
                @Override
                public void cargoConductor() {

                }
            });
        } else {
            reload();
        }
        // Toast.makeText(getApplicationContext(),"tab2",Toast.LENGTH_SHORT).show();
        modelo.setModeloListener(this);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //se captura la posicion
                OrdenConductor ordenc = modelo.getOrdenes().get(position);
                if (ordenc.getEstado().equals("Finalizado")) {
                    //modelo.llamarServicios();
                    mAdapter.notifyDataSetChanged();

                    return;
                } else {
                    Intent i = new Intent(getApplicationContext(), InformacionServicio.class);
                    i.putExtra("id", "" + ordenc.getId());// se envia el id de la orden segun pa posicion
                    startActivity(i);

                }
            }
        });



        //comando que me valida los estados de los servicios
        ComandoOrdenesConductor.escucharEstados( new ComandoOrdenesConductor.OnEscucharEstados() {
            @Override
            public void cambio() {
               if(mAdapter != null){
                   mAdapter.notifyDataSetChanged();
               }
            }

            @Override
            public void cambioError() {
                if(mAdapter != null){
                    mAdapter.notifyDataSetChanged();
                }
            }

        });

        //timerload();

    }


    private void reload() {



        if (!modelo.tipoConductor.equals("conductor")) {   // Cuando es tercero
            if (!modelo.estadoConductorPendiente.equals("Aprobado")) {
                btn_disponible.setVisibility(View.VISIBLE);
                btn_disponible.setClickable(false);
                btn_disponible.setBackgroundResource(R.color.color_gris_linea);
                btn_disponible.setText("Pendiente de activación");
                sindatos.setText("Los datos han sido enviados con éxito, será dado de alta cuando el administrador haya validado la información");
                // Los datos han sido enviados con éxito, será dado de alta cuando el administrador haya validado la información
                if(mAdapter != null){
                    modelo.getOrdenes().clear();
                    modelo.llamarServicios();
                }
            } else {
                btn_disponible.setVisibility(View.VISIBLE);
                if (modelo.ocupado == true) {
                    btn_disponible.setBackgroundResource(R.color.color_rojo);
                    btn_disponible.setText("OCUPADO");
                } else {
                    btn_disponible.setBackgroundResource(R.color.colorCafe);
                    btn_disponible.setText("DISPONIBLE");
                }

                if(mAdapter != null){

                    modelo.llamarServicios();
                }
            }
            displayLista();

        }

        if (modelo.tipoConductor.equals("conductor")) {

            comandoConductor.actualizarInicioSesion(modelo.uid,this,"conductores");
        }else{
            comandoConductor.actualizarInicioSesion(modelo.uid,this,"conductoresTerceros");
        }
        Log.v("tab2", "Tab2");
        displayLista();
    }


    private void displayLista() {


        mAdapter = new OrdenesConductorAdapter(this,  modelo.getOrdenes());
        lv.setAdapter(mAdapter);
        if(mAdapter != null){
            sindatos.setVisibility(View.INVISIBLE);
            sindatos.setBackgroundColor(Color.TRANSPARENT);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void cargoUnaOrdenesConductor() {
        Log.v("cargo orden", "cargo orden");
        ocultartexto();
        // displayLista();
        if(modelo.getOrdenes().size()>0){
            sindatos.setVisibility(View.INVISIBLE);
            sindatos.setBackgroundColor(Color.TRANSPARENT);
            mAdapter.notifyDataSetChanged();
        }


    }




    public void timerload() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(2000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (modelo.getOrdenes().size() < 1) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    sindatos.setVisibility(View.VISIBLE);
                                } else {
                                    sindatos.setVisibility(View.INVISIBLE);
                                    sindatos.setBackgroundColor(Color.TRANSPARENT);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            ;
        };
        thread.start();
    }

    public void ocultartexto() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(500);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (modelo.getOrdenes().size() < 1) {
                                    sindatos.setVisibility(View.VISIBLE);
                                } else {
                                    sindatos.setVisibility(View.INVISIBLE);
                                    sindatos.setBackgroundColor(Color.TRANSPARENT);
                                }
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            ;
        };
        thread.start();
    }

    @Override
    public void setActualizoListadoDeServicios() {
        displayLista();
        ocultartexto();

    }

    //estado de disponivilidad de condutor terceros
    public void disponivilidad(View v) {

        if (modelo.ocupado == false) {
            btn_disponible.setBackgroundResource(R.color.colorCafe);
            btn_disponible.setText("DISPONIBLE");
        } else {
            btn_disponible.setBackgroundResource(R.color.color_rojo);
            btn_disponible.setText("OCUPADO");
        }

        showAlerDisponivilidad();
    }

    public void showAlerDisponivilidad() {
        String descripcionEstado = "";

        if (modelo.ocupado == false) {
            descripcionEstado = "¿Desea cambiar su estado de Disponible a Ocupado?";
        } else {
            descripcionEstado = "¿Desea cambiar su estado de Ocupado a Disponible?";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Cambio de estado");

        // set dialog message
        alertDialogBuilder
                .setMessage(descripcionEstado)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (modelo.ocupado == false) {
                            DatabaseReference ref = database.getReference("empresa/conductoresTerceros/" + modelo.uid + "/ocupado/");//ruta path
                            ref.setValue(true);
                            modelo.ocupado = true;

                            btn_disponible.setBackgroundResource(R.color.color_rojo);
                            btn_disponible.setText("OCUPADO");
                        } else {
                            DatabaseReference ref = database.getReference("empresa/conductoresTerceros/" + modelo.uid + "/ocupado/");//ruta path
                            ref.setValue(false);
                            modelo.ocupado = false;
                            btn_disponible.setBackgroundResource(R.color.colorCafe);
                            btn_disponible.setText("DISPONIBLE");
                        }

                        //comando update


                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAdapter != null){
            sindatos.setVisibility(View.INVISIBLE);
            sindatos.setBackgroundColor(Color.TRANSPARENT);
            mAdapter.notifyDataSetChanged();
        }

        timerload();
    }
}
