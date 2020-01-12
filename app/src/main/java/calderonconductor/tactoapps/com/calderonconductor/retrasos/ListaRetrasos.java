package calderonconductor.tactoapps.com.calderonconductor.retrasos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.Collections;
import java.util.Comparator;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Retraso;
import calderonconductor.tactoapps.com.calderonconductor.R;
import calderonconductor.tactoapps.com.calderonconductor.Splash;

public class ListaRetrasos extends Activity {


    private ListView lisView;
    private ListaRetrasosAdapter lAdapter;
    private String idOrden;
    private OrdenConductor orden;
    private Modelo model = Modelo.getInstance();
    FrameLayout btnmas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_retrasos);


        if (savedInstanceState != null) {
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;

        }

        lisView = findViewById(R.id.retrasos_list_view);
        lisView.setItemsCanFocus(true);

        lisView.setEmptyView(findViewById(R.id.vacio));
        btnmas = findViewById(R.id.btnmas);

        idOrden = getIntent().getStringExtra("IDORDEN");

        orden = model.getOrden(idOrden);

        Collections.sort(orden.retrasos, new Comparator<Retraso>() {
            @Override
            public int compare(Retraso r1, Retraso r2) {
                if (r1.startTime > r2.startTime) {
                    return -1;
                }else {
                    return 1;
                }
            }
        });


        lAdapter = new ListaRetrasosAdapter(this, orden, orden.retrasos);
        lisView.setAdapter(lAdapter);


        if (orden.getEstado().equals("En Camino")|| orden.getEstado().equals("Transportando")){
            if (orden.hayRetrazoAbierto()) {
                btnmas.setEnabled(false);
                btnmas.setAlpha((float) 0.2);
                return;
            }

            btnmas.setEnabled(true);
            btnmas.setAlpha(1);
        }else {
            btnmas.setEnabled(false);
            btnmas.setAlpha((float) 0.2);

        }

    }

    public void didTapNuevo(View view) {

        if (orden.hayRetrazoAbierto()) {
            return;
        }

        Retraso retraso = new Retraso();
        orden.retrasos.add(0, retraso);

        lAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {

        if (lAdapter != null){
            lAdapter.parar();
        }
        super.onDestroy();

    }
}
