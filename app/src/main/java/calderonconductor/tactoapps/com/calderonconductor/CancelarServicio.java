package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoCancelarServicio;

public class CancelarServicio extends Activity implements ComandoCancelarServicio.OnCancelacionChangeListener{

    String idServicio;
    Button problemasVehiculo;
    Button accidente;
    Button enfermedad;
    Button otro;
    Button enviarcancelacion;
    EditText descripcion;
    String gone  = "oculto";
    TextView asignarMotivo;
    OrdenConductor ordenConductor;
    Modelo modelo  = Modelo.getInstance();
    ComandoCancelarServicio comando = new ComandoCancelarServicio(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cancelar_servicio);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        Bundle bundle = getIntent().getExtras();
        idServicio = bundle.getString("id");
        Log.v("idServicioCanclar","idServicioCanclar "+idServicio);

        problemasVehiculo = (Button)findViewById(R.id.problemasVehiculo);
        accidente = (Button)findViewById(R.id.accidente);
        enfermedad = (Button)findViewById(R.id.enfermedad);
        otro = (Button)findViewById(R.id.otro);
        descripcion = (EditText)findViewById(R.id.descripcion);
        enviarcancelacion = (Button) findViewById(R.id.enviarcancelacion);
        asignarMotivo = (TextView) findViewById(R.id.asignarMotivo);


    }

    //btn atras
    /*
    public void atras(View v){
        Intent i  = new Intent(getApplicationContext(), InformacionServicio.class);
        i.putExtra("id", "" + idServicio);
        startActivity(i);
        finish();

    }*/

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void problemasVehiculo(View v){
        descripcion.setVisibility(View.GONE);
        gone  = "oculto";
        descripcion.setText("");
        asignarMotivo.setText("Problemas con el vehiculo");

        problemasVehiculo.setBackgroundResource(R.color.colorCafe);
        accidente.setBackgroundResource(R.color.color_blanco);
        enfermedad.setBackgroundResource(R.color.color_blanco);

        problemasVehiculo.setTextColor(Color.WHITE);
        accidente.setTextColor(Color.BLACK);
        enfermedad.setTextColor(Color.BLACK);
    }

    public void accidente(View v){
        descripcion.setVisibility(View.GONE);
        gone  = "oculto";
        descripcion.setText("");
        asignarMotivo.setText("Tuve un accidente");

        problemasVehiculo.setBackgroundResource(R.color.color_blanco);
        accidente.setBackgroundResource(R.color.colorCafe);
        enfermedad.setBackgroundResource(R.color.color_blanco);

        problemasVehiculo.setTextColor(Color.BLACK);
        accidente.setTextColor(Color.WHITE);
        enfermedad.setTextColor(Color.BLACK);
    }

    public void enfermedad(View v){
        descripcion.setVisibility(View.GONE);
        gone  = "oculto";
        descripcion.setText("");
        asignarMotivo.setText("Enfermedad");

        problemasVehiculo.setBackgroundResource(R.color.color_blanco);
        accidente.setBackgroundResource(R.color.color_blanco);
        enfermedad.setBackgroundResource(R.color.colorCafe);

        problemasVehiculo.setTextColor(Color.BLACK);
        accidente.setTextColor(Color.BLACK);
        enfermedad.setTextColor(Color.WHITE);
    }

    public void otro(View v){
        problemasVehiculo.setBackgroundResource(R.color.color_blanco);
        accidente.setBackgroundResource(R.color.color_blanco);
        enfermedad.setBackgroundResource(R.color.color_blanco);
        if(gone.equals("oculto")){
            descripcion.setVisibility(View.VISIBLE);
            gone  = "visible";
            descripcion.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    asignarMotivo.setText(""+descripcion.getText().toString());
                    return false;
                }
            });

        }else{
            descripcion.setVisibility(View.GONE);
            gone  = "oculto";
            descripcion.setText("");

        }

    }

    public void enviar(View v) {
        ordenConductor = modelo.getOrden(idServicio);

        if (gone.equals("visible")) {
            if (descripcion.getText().toString().equals("")) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Describe el motivo");
                dialogo1.setMessage("Describe el motivo");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.show();
            }
            else {
                asignarMotivo.setText(descripcion.getText().toString());
                //Toast.makeText(getApplicationContext(), "1- "+ asignarMotivo.getText().toString()+"id-conductor "+ordenConductor.getConductor(), Toast.LENGTH_LONG).show();
                comando.getCancelacionServicio(idServicio, ordenConductor.getConductor(), asignarMotivo.getText().toString());
                comando.getCambioEstado(idServicio);
                comando.getCamioConductor(idServicio);
                enviarcancelacion.setText("Enviando...");
                enviarcancelacion.setClickable(false);
            }
        } else {
            if (asignarMotivo.getText().toString().equals("")) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Seleccione un motivo");
                dialogo1.setMessage("Deve seleccionar un motivo");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.show();
            } else {
              // Toast.makeText(getApplicationContext(), "2- " + asignarMotivo.getText().toString()+"id-conductor "+ordenConductor.getConductor(), Toast.LENGTH_LONG).show();
                comando.getCancelacionServicio(idServicio, ordenConductor.getConductor(), asignarMotivo.getText().toString());
                comando.getCambioEstado(idServicio);
                comando.getCamioConductor(idServicio);
                comando.getCambioMatricula(idServicio);
                enviarcancelacion.setText("Enviando...");
                enviarcancelacion.setClickable(false);

            }
        }
    }

    @Override
    public  void cargoCancelacion(){
        Toast.makeText(getApplicationContext(), "Se envio la cancelacion del servicio ", Toast.LENGTH_LONG).show();

        /*Intent i  = new Intent(getApplicationContext(), Pgina_Principa.class);
        i.putExtra("vistaPosicion","dos");
        startActivity(i);
        finish();*/
        onBackPressed();
    }

    //bhtn_atras_hadware
   /*public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("cerrar","cerrar");
        }
        return false;
    }*/

}
