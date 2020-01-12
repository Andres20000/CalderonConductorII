package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.Arrays;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;

/**
 * Created by tacto on 3/10/17.
 */

public class Rutas extends Activity {
    ListView lista_rutas;
    String idServicio;
    String estado;
    OrdenConductor ordenConductor;
    Modelo modelo = Modelo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rutas);

        if (savedInstanceState != null) {
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        Bundle bundle = getIntent().getExtras();
        idServicio = bundle.getString("id");
        estado = bundle.getString("estado");

        if (estado.equals("activo")) {
            ordenConductor = modelo.getOrden(idServicio);
        } else {

            ordenConductor = modelo.getOrdenHistorial(idServicio);
        }
        lista_rutas = (ListView) findViewById(R.id.lista_rutas);


        String commaSeparated = ordenConductor.getRuta();


        ArrayList<String> items = new ArrayList<String>(Arrays.asList(commaSeparated.split("-")));
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lista_rutas.setAdapter(adaptador);

    }

    public void atras(View v) {
        atras2();
    }

    public void atras2() {
        Intent i = new Intent(getApplicationContext(), InformacionServicio.class);
        i.putExtra("id", "" + idServicio);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
