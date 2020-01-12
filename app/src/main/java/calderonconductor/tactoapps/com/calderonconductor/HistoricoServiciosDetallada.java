package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import calderonconductor.tactoapps.com.calderonconductor.Adapter.HistorialAdapterDetallada;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoHistorial;

public class HistoricoServiciosDetallada extends Activity implements HistorialAdapterDetallada.AdapterCallback, ComandoHistorial.OnOrdenesHistorialChangeListener  {
    private HistorialAdapterDetallada mAdapter;
    ListView lv;
    public ProgressBar progressBar;
    Modelo modelo = Modelo.getInstance();
    TextView fecha_historico;
    TextView empresa_historico;
    String fechaHistorco,razonSocial;
    Date date;
    TextView sindatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_historico_servicios_detallada);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        lv = (ListView)findViewById(R.id.lv);
        fecha_historico = (TextView)findViewById(R.id.fecha_historico);
        empresa_historico = (TextView)findViewById(R.id.empresa_historico);
        progressBar = (ProgressBar)findViewById(R.id.progressBar3);
        sindatos = (TextView) findViewById(R.id.sindatos);

        Bundle bundle = getIntent().getExtras();
        fechaHistorco = bundle.getString("fecha");
        razonSocial = bundle.getString("razonSocial");

        ComandoHistorial comandoOrdenCondutr = new ComandoHistorial(this);
        comandoOrdenCondutr.getOrdenesHistorial();


        mostrarMes(fechaHistorco);
        empresa_historico.setText(""+razonSocial);
        //fecha_historico.setText(""+fechaHistorco);


        displayHistorial();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //Toast.makeText(getApplicationContext(),""+parent[position]+"-"+position, Toast.LENGTH_SHORT);
                //se captura la posicion
                OrdenConductor ordenc = modelo.filtrohistorialmes.get(position);

                Intent intent = new Intent(getApplicationContext(), InformacionDelServicioDetallada.class);
                intent.putExtra("id", "" +ordenc.getId());// se envia el id de la orden segun la posicion
                intent.putExtra("fecha", "" +fechaHistorco);
                intent.putExtra("razonSocial", "" +razonSocial);
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
        mAdapter = new HistorialAdapterDetallada(this,this,fechaHistorco,razonSocial);
        lv.setAdapter(mAdapter);
    }


    public void atras(View v){
        /*Intent i  = new Intent(getApplicationContext(), Pgina_Principa.class);
        i.putExtra("vistaPosicion", "tres");
        startActivity(i);
        finish();*/
        onBackPressed();
    }



    @Override
    public void cargoHistorial() {
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
        ocultartexto();
    }

    public void mostrarMes(String fechaHistorco){
        String[] separated = fechaHistorco.split("/");

        if (separated[0].equals("01")){
            fecha_historico.setText("Enero de "+separated[1]);
        }
        else if (separated[0].equals("02")){
            fecha_historico.setText("Febrero de "+separated[1]);
        }
        else if (separated[0].equals("03")){
            fecha_historico.setText("Marzo de "+separated[1]);
        }
        else if (separated[0].equals("04")){
            fecha_historico.setText("Abril de "+separated[1]);
        }
        else if (separated[0].equals("05")){
            fecha_historico.setText("Mayo de "+separated[1]);
        }
        else if (separated[0].equals("06")){
            fecha_historico.setText("Junio de "+separated[1]);
        }
        else if (separated[0].equals("07")){
            fecha_historico.setText("Julio de "+separated[1]);
        }
        else if (separated[0].equals("08")){
            fecha_historico.setText("Agosto de "+separated[1]);
        }
        else if (separated[0].equals("09")){
            fecha_historico.setText("Septiembre de "+separated[1]);
        }
        else if (separated[0].equals("10")){
            fecha_historico.setText("Octubre de "+separated[1]);
        }
        else if (separated[0].equals("11")){
            fecha_historico.setText("Noviembre de "+separated[1]);
        }
        else if (separated[0].equals("12")){
            fecha_historico.setText("Diciembre de "+separated[1]);
        }
    }


    //bhtn_atras_hadware
    /*public boolean onKeyDown(int keyCode, KeyEvent event) {
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
