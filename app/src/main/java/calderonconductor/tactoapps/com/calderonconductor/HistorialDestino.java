package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import calderonconductor.tactoapps.com.calderonconductor.Adapter.ListaMunicipioAdapter;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductor;


public class HistorialDestino extends Activity implements ListaMunicipioAdapter.AdapterCallback {
    String idServicio;
    ListaMunicipioAdapter mAdapterMunicipio;
    ListView lvcomentario;
    OrdenConductor ordenConductor;
    Modelo modelo  = Modelo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_historial_destino);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        Bundle bundle = getIntent().getExtras();
        idServicio = bundle.getString("id");
        lvcomentario = (ListView) findViewById(R.id.listaDestino);

        ComandoOrdenesConductor comandoOrdenCondutr = new ComandoOrdenesConductor();
        comandoOrdenCondutr.getOrdenesConductor();

        displayListaMunicipios();
    }


    /*@Override
    public void cargoUnaOrdenesConductor() {
        mAdapterMunicipio.notifyDataSetChanged();
    }*/




    private void displayListaMunicipios(){
        //pasamos los datos del adaptador
        mAdapterMunicipio = new ListaMunicipioAdapter(this,this,idServicio);
        lvcomentario.setAdapter(mAdapterMunicipio);

    }

    public void atras(View v){
        Intent i  = new Intent(getApplicationContext(), InformacionServicio.class);
        i.putExtra("id",idServicio);
        startActivity(i);
        finish();
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
}
