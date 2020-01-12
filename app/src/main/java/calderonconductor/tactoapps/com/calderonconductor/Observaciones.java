package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;


public class Observaciones extends Activity {

    String idServicio;
    String estado;
    OrdenConductor ordenConductor;
    Modelo modelo  = Modelo.getInstance();
    EditText txt_observaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_observaciones);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        Bundle bundle = getIntent().getExtras();
        idServicio = bundle.getString("id");
        estado = bundle.getString("estado");

        if(estado.equals("activo")) {
            ordenConductor = modelo.getOrden(idServicio);
        }else{
            ordenConductor = modelo.getOrdenHistorial(idServicio);
        }


        txt_observaciones = (EditText)findViewById(R.id.txt_observaciones);

        String observacion = ordenConductor.getObservaciones();


        if(observacion == null || observacion.equals("")){
            txt_observaciones.setHint("No tiene observaciones.");
        }else{
            txt_observaciones.setText(""+observacion);
        }


    }


    public void atras(View v){
        atras2();
    }

    public void atras2(){
        Intent i  = new Intent(getApplicationContext(), InformacionServicio.class);
        i.putExtra("id", "" + idServicio);
        startActivity(i);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
