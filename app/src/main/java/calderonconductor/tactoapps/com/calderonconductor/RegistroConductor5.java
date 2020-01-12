package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;


public class RegistroConductor5 extends Activity {

    int uno =0;
    int dos =0;
    int tres =0;
    int cuatro =0;
    int cinco =0;
    int seis =0;
    int siete =0;
    boolean kit  = false;
    boolean botiquin  = false;
    boolean iluminacioExt = false;
    boolean iluminacioInt  = false;
    boolean estadoMeca  = false;
    boolean aireAcond  = false;
    boolean latoneriaP  = false;


    Button check_kit_de_carretera,check_kit_de_carretera_x;
    Button check_botiquin,check_botiquin_x;
    Button check_iluminacion_externa,check_iluminacion_externa_x;
    Button check_iluminacion_interna,check_iluminacion_interna_x;
    Button check_estado_mecanico,check_estado_mecanico_x;
    Button check_aire_acondicionado,check_aire_acondicionado_x;
    Button check_latoneria_y_pintura,check_latoneria_y_pintura_x;

    Modelo modelo = Modelo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registro_conductor5);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        check_kit_de_carretera = (Button) findViewById(R.id.check_kit_de_carretera);
        check_kit_de_carretera_x = (Button) findViewById(R.id.check_kit_de_carretera_x);
        check_botiquin = (Button) findViewById(R.id.check_botiquin);
        check_botiquin_x = (Button) findViewById(R.id.check_botiquin_x);
        check_iluminacion_externa = (Button) findViewById(R.id.check_iluminacion_externa);
        check_iluminacion_externa_x = (Button) findViewById(R.id.check_iluminacion_externa_x);
        check_iluminacion_interna = (Button) findViewById(R.id.check_iluminacion_interna);
        check_iluminacion_interna_x = (Button) findViewById(R.id.check_iluminacion_interna_x);
        check_estado_mecanico = (Button) findViewById(R.id.check_estado_mecanico);
        check_estado_mecanico_x = (Button) findViewById(R.id.check_estado_mecanico_x);
        check_aire_acondicionado = (Button) findViewById(R.id.check_aire_acondicionado);
        check_aire_acondicionado_x = (Button) findViewById(R.id.check_aire_acondicionado_x);
        check_latoneria_y_pintura = (Button) findViewById(R.id.check_latoneria_y_pintura);
        check_latoneria_y_pintura_x = (Button) findViewById(R.id.check_latoneria_y_pintura_x);
    }

    //bhtn_atras_hadware
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("atras","atras");
            atras2();
        }
        return false;
    }

    public void atras2(){
        Intent i = new Intent(getApplicationContext(), RegistroConductor4.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
    }

    public void kit(View view) {
        uno =1;
        switch (view.getId()) {
            case R.id.check_kit_de_carretera:
                check_kit_de_carretera.setBackgroundResource(R.drawable.radio_chulo);
                check_kit_de_carretera_x.setBackgroundResource(R.drawable.radio);
                 kit  = true;
                break;

            case R.id.check_kit_de_carretera_x:
                check_kit_de_carretera_x.setBackgroundResource(R.drawable.radio_x);
                check_kit_de_carretera.setBackgroundResource(R.drawable.radio);
                kit  = false;
                break;
        }
    }

    public void botiquin(View view) {
        dos =2;
        switch (view.getId()) {
            case R.id.check_botiquin:
                check_botiquin.setBackgroundResource(R.drawable.radio_chulo);
                check_botiquin_x.setBackgroundResource(R.drawable.radio);
                botiquin  = true;
                break;

            case R.id.check_botiquin_x:
                check_botiquin_x.setBackgroundResource(R.drawable.radio_x);
                check_botiquin.setBackgroundResource(R.drawable.radio);
                botiquin  = false;
                break;
        }
    }

    public void iluminacionExt(View view) {
        tres =3;
        switch (view.getId()) {
            case R.id.check_iluminacion_externa:
                check_iluminacion_externa.setBackgroundResource(R.drawable.radio_chulo);
                check_iluminacion_externa_x.setBackgroundResource(R.drawable.radio);
                iluminacioExt  = true;
                break;

            case R.id.check_iluminacion_externa_x:
                check_iluminacion_externa_x.setBackgroundResource(R.drawable.radio_x);
                check_iluminacion_externa.setBackgroundResource(R.drawable.radio);
                iluminacioExt  = false;
                break;
        }
    }

    public void iluminacionInt(View view) {
        cuatro =4;
        switch (view.getId()) {
            case R.id.check_iluminacion_interna:
                check_iluminacion_interna.setBackgroundResource(R.drawable.radio_chulo);
                check_iluminacion_interna_x.setBackgroundResource(R.drawable.radio);
                iluminacioInt  = true;
                break;

            case R.id.check_iluminacion_interna_x:
                check_iluminacion_interna_x.setBackgroundResource(R.drawable.radio_x);
                check_iluminacion_interna.setBackgroundResource(R.drawable.radio);
                iluminacioInt  = false;
                break;
        }
    }


    public void estadoMec(View view) {
        cinco =5;
        switch (view.getId()) {
            case R.id.check_estado_mecanico:
                check_estado_mecanico.setBackgroundResource(R.drawable.radio_chulo);
                check_estado_mecanico_x.setBackgroundResource(R.drawable.radio);
                estadoMeca  = true;
                break;

            case R.id.check_estado_mecanico_x:
                check_estado_mecanico_x.setBackgroundResource(R.drawable.radio_x);
                check_estado_mecanico.setBackgroundResource(R.drawable.radio);
                estadoMeca  = false;
                break;
        }
    }

    public void aireAcondi(View view) {
        seis =6;
        switch (view.getId()) {
            case R.id.check_aire_acondicionado:
                check_aire_acondicionado.setBackgroundResource(R.drawable.radio_chulo);
                check_aire_acondicionado_x.setBackgroundResource(R.drawable.radio);
                aireAcond  = true;
                break;

            case R.id.check_aire_acondicionado_x:
                check_aire_acondicionado_x.setBackgroundResource(R.drawable.radio_x);
                check_aire_acondicionado.setBackgroundResource(R.drawable.radio);
                aireAcond  = false;
                break;
        }
    }

    public void latoneriaP(View view) {
        siete =7;
        switch (view.getId()) {
            case R.id.check_latoneria_y_pintura:
                check_latoneria_y_pintura.setBackgroundResource(R.drawable.radio_chulo);
                check_latoneria_y_pintura_x.setBackgroundResource(R.drawable.radio);
                latoneriaP  = true;
                break;

            case R.id.check_latoneria_y_pintura_x:
                check_latoneria_y_pintura_x.setBackgroundResource(R.drawable.radio_x);
                check_latoneria_y_pintura.setBackgroundResource(R.drawable.radio);
                latoneriaP  = false;
                break;
        }
    }


    public void continuar5(View v) {

        if(uno == 0 || dos == 0 || tres == 0 || cuatro == 0 || cinco == 0 || seis == 0 || siete == 0){
            Toast.makeText(getApplicationContext(),"Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
        }else{
            modelo.informacionGeneral.setAire_Acondicionado(aireAcond);
            modelo.informacionGeneral.setBotiquin(botiquin);
            modelo.informacionGeneral.setEstado_Mecanico(estadoMeca);
            modelo.informacionGeneral.setIluminacion_Externa(iluminacioExt);
            modelo.informacionGeneral.setIluminación_Interna(iluminacioInt);
            modelo.informacionGeneral.setKit_de_carretera(kit);
            modelo.informacionGeneral.setLatoneria_y_Pintura(latoneriaP);

            Intent i  = new Intent(getApplicationContext(), RegistroConductor5_2.class);
            startActivity(i);
            finish();
        }
    }

}
