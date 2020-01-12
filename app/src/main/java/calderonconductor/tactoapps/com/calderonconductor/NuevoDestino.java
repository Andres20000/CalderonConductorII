package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoEnviarMensaje;


public class NuevoDestino extends Activity implements ComandoEnviarMensaje.OnMensajeChangeListener {

    EditText nuevaDiredcion;
    String idServicio;
    TextView textView18;
    String ciudad;
    ComandoEnviarMensaje comandoCiudad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nuevo_destino);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        Bundle bundle = getIntent().getExtras();
        idServicio = bundle.getString("id");
        ciudad = bundle.getString("ciudad");


        nuevaDiredcion = (EditText)findViewById(R.id.nuevaDiredcion);
        textView18 = (TextView) findViewById(R.id.textView18);
        textView18.setText(""+ciudad);
        comandoCiudad = new ComandoEnviarMensaje(this);
    }

    public void atras(View v){
        Intent i  = new Intent(getApplicationContext(), InformacionServicio.class);
        i.putExtra("id", "" + idServicio);
        startActivity(i);
        finish();
    }



    public void puebos(View v){
        Intent i  = new Intent(getApplicationContext(), CiudadesActivity.class);
        i.putExtra("id", "" + idServicio);
        i.putExtra("ciudad", "" + ciudad);
        startActivity(i);
        finish();
    }

    public void enviarCiudad(View v){

        if (textView18.getText().toString().equals("Ciudad")){
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Selecione Ciudad");
            dialogo1.setMessage("No ha seleccionado una ciudad");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        }else if(nuevaDiredcion.getText().toString().equals("")){
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Ingrese  una dirección");
            dialogo1.setMessage("No ha Ingresado una dirección");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        }else {

            String ciudadSeleccionada = textView18.getText().toString();

            comandoCiudad.enviarCiudad(idServicio, ciudadSeleccionada,nuevaDiredcion.getText().toString());
            Toast.makeText(getApplicationContext(),"Nuevo destino guardado", Toast.LENGTH_SHORT).show();
            Intent i  = new Intent(getApplicationContext(), InformacionServicio.class);
            i.putExtra("id", "" + idServicio);
            startActivity(i);
            finish();
        }



    }


    @Override
    public  void cargoMensaje(){
        Toast.makeText(getApplicationContext(), "Se envio el nuevo destino ", Toast.LENGTH_LONG).show();

    }

    @Override
    public void cargoMensajeNoti() {

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
}
