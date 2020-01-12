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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoEnviarMensaje;


public class Mensaje extends Activity implements ComandoEnviarMensaje.OnMensajeChangeListener {

    String idServicio;
    Button mensaje1;
    Button mensaje2;
    Button mensaje3;
    TextView asignarMensaje;
    OrdenConductor ordenConductor;
    Modelo modelo  = Modelo.getInstance();
    ComandoEnviarMensaje comandoMensaje ;
    String[]  arraytoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mensaje);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        Bundle bundle = getIntent().getExtras();
        idServicio = bundle.getString("id");
        arraytoken = bundle.getStringArray("arrayTokens");

        mensaje1 = (Button)findViewById(R.id.mensaje1);
        mensaje2 = (Button)findViewById(R.id.mensaje2);
        mensaje3 = (Button)findViewById(R.id.mensaje3);
        asignarMensaje = (TextView) findViewById(R.id.asignarMensaje);
        comandoMensaje = new ComandoEnviarMensaje(this);

        ordenConductor = modelo.getOrden(idServicio);


    }

    public void opcion1(View v){
        asignarMensaje.setText("Voy en camino");

        mensaje1.setBackgroundResource(R.color.colorCafe);
        mensaje2.setBackgroundResource(R.color.color_blanco);
        mensaje3.setBackgroundResource(R.color.color_blanco);

        mensaje1.setTextColor(Color.WHITE);
        mensaje2.setTextColor(Color.BLACK);
        mensaje3.setTextColor(Color.BLACK);

    }

    public void opcion2(View v){

        asignarMensaje.setText("Estoy llegando");

        mensaje1.setBackgroundResource(R.color.color_blanco);
        mensaje2.setBackgroundResource(R.color.colorCafe);
        mensaje3.setBackgroundResource(R.color.color_blanco);

        mensaje1.setTextColor(Color.BLACK);
        mensaje2.setTextColor(Color.WHITE);
        mensaje3.setTextColor(Color.BLACK);

    }

    public void opcion3(View v){

        asignarMensaje.setText("Estoy en el punto de recogida");

        mensaje1.setBackgroundResource(R.color.color_blanco);
        mensaje2.setBackgroundResource(R.color.color_blanco);
        mensaje3.setBackgroundResource(R.color.colorCafe);

        mensaje1.setTextColor(Color.BLACK);
        mensaje2.setTextColor(Color.BLACK);
        mensaje3.setTextColor(Color.WHITE);

    }

    public void enviarMensaje(View v){

        if (asignarMensaje.getText().toString().equals("")) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Seleccione un mensaje");
            dialogo1.setMessage("Deve seleccionar un mensaje");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        } else {
            // Toast.makeText(getApplicationContext(), "2- " + asignarMensaje.getText().toString()+"id-conductor "+ordenConductor.getConductor(), Toast.LENGTH_LONG).show();

            comandoMensaje.enviarMensaje(idServicio,asignarMensaje.getText().toString());

            if (arraytoken != null) {
                comandoMensaje.notificacionMensaje(arraytoken, "Mensaje",""+asignarMensaje.getText().toString());


            }
        }

    }

    public void atras(View v){
        Intent i  = new Intent(getApplicationContext(), InformacionServicio.class);
        i.putExtra("id", "" + idServicio);
        startActivity(i);
        finish();
    }

    @Override
    public  void cargoMensaje(){
        Toast.makeText(getApplicationContext(), "Se envio el mensaje ", Toast.LENGTH_LONG).show();
       /* Intent i  = new Intent(getApplicationContext(), InformacionServicio.class);
        i.putExtra("id", "" + idServicio);
        startActivity(i);
        finish();*/
        onBackPressed();
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
