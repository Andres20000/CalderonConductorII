package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import calderonconductor.tactoapps.com.calderonconductor.Adapter.HistorialAdapter;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoHistorial;


public class HistorialUltimosServicios extends Activity implements HistorialAdapter.AdapterCallback, ComandoHistorial.OnOrdenesHistorialChangeListener {


    private HistorialAdapter mAdapter;
    ListView lv;
    public ProgressBar progressBar;
    Modelo modelo = Modelo.getInstance();

    TextView sindatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_historial_ultimos_servicios);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        lv = (ListView)findViewById(R.id.lv);
        progressBar = (ProgressBar)findViewById(R.id.progressBar3);
        sindatos = (TextView) findViewById(R.id.sindatos);

        ComandoHistorial comandoOrdenCondutr = new ComandoHistorial(this);
        comandoOrdenCondutr.getOrdenesHistorial();


        displayHistorial();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),""+parent[position]+"-"+position, Toast.LENGTH_SHORT);
                //se captura la posicion
                OrdenConductor ordenc = modelo.getHistorial().get(position);

                Intent intent = new Intent(getApplicationContext(), InformacionDelServicioDetallada.class);
                intent.putExtra("id", "" +ordenc.getId());// se envia el id de la orden segun la posicion
                intent.putExtra("fecha", "");
                intent.putExtra("razonSocial", "");
                startActivity(intent);

            }
        });
        timerload();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }


    private void displayHistorial(){
        mAdapter = new HistorialAdapter(this,this);
        lv.setAdapter(mAdapter);
    }


    public void atras(View v){
       /* Intent i  = new Intent(getApplicationContext(), Pgina_Principa.class);
        i.putExtra("vistaPosicion", "tres");
        startActivity(i);*/
       onBackPressed();
    }



    @Override
    public void cargoHistorial() {
        modelo.filtrarUltimosVeinteHistorial();

        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }

      ocultartexto();

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

    public void timerload(){

        Thread  thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(2000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(modelo.getOrdenes().size() < 1){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    sindatos.setVisibility(View.VISIBLE);
                                }else{
                                    sindatos.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            };
        };
        thread.start();
    }

    public void ocultartexto(){

        Thread  thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(500);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(modelo.getOrdenes().size() < 1){
                                    sindatos.setVisibility(View.VISIBLE);
                                }else{
                                    sindatos.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            };
        };
        thread.start();
    }

}
