package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import calderonconductor.tactoapps.com.calderonconductor.Adapter.ListaPasajerosAdapter;
import calderonconductor.tactoapps.com.calderonconductor.Adapter.ListaPasajerosHistoricoAdapter;

public class ListaPasajeros extends Activity implements ListaPasajerosAdapter.AdapterCallback, ListaPasajerosHistoricoAdapter.AdapterCallback{

    ListaPasajerosAdapter mAdapter;
    ListaPasajerosHistoricoAdapter mAdapter2;
    ListView lv;
    String idServicio ="";
    String estado ="";
    String vista ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lista_pasajeros);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        Bundle bundle = getIntent().getExtras();
        idServicio = bundle.getString("id");
        estado = bundle.getString("estado");
        vista = bundle.getString("vista");


        lv = (ListView) findViewById(R.id.listview);

        if(vista.equals("0")){
            mAdapter = new ListaPasajerosAdapter(this, this, idServicio);
            lv.setAdapter(mAdapter);
        }else{
            mAdapter2 = new ListaPasajerosHistoricoAdapter(this, this, idServicio);
            lv.setAdapter(mAdapter2);
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
