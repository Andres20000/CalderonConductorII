package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

public class Help extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }
    }

    public void cancelar(View v) {
        Intent i = new Intent(Help.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        finish();
    }


    public void llamar(View v) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:3112082296"));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);


    }


    public void correo(View v){
        checkConnectivity();
    }


    public void correoenviar(){
        /* es necesario un intent que levante la actividad deseada */
        Intent itSend = new Intent(Intent.ACTION_SEND);

                            /* vamos a enviar texto plano a menos que el checkbox esta marcado */
        itSend.setType("plain/text");
//admin@transportescalderon.com.co
                            /* colocamos los datos para el envio */
        itSend.putExtra(Intent.EXTRA_EMAIL, new String[]{"admin@transportescalderon.com.co"});
        itSend.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de credenciales");
        itSend.putExtra(Intent.EXTRA_TEXT, "Nombre: "+"\r\n"+"Teléfono:"+"\r\n"+"asunto:");

                            /* revisamos si el checkbox esta marcado enviamos el cono de la aplicacion como adjunto */

                            /* iniciamos la actividad */
        startActivity(itSend);
    }



    /**validacion de internet**/
    private boolean checkConnectivity()
    {
        boolean enabled = true;

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ((info == null || !info.isConnected() || !info.isAvailable()))
        {
            enabled = false;
            //Toast.makeText(getBaseContext(), "sin internet", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("Sin conexión a Internet");
            alertbox.setMessage("Por favor, compruebe su conexión a Internet.");

            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.cancel();
                }
            });
            alertbox.show();

        } else{
            correoenviar();
        }
        return enabled;
    }



    //bhtn_atras_hadware
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("cerrar","cerrar");
            Intent i = new Intent(Help.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
            finish();
        }
        return true;
    }


}
