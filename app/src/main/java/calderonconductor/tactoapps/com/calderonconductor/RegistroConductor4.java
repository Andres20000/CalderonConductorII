package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import java.util.Date;
import java.text.DateFormat;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

public class RegistroConductor4 extends Activity implements  DatePickerDialog.OnDateSetListener{

final Context context = this;
        Modelo modelo = Modelo.getInstance();
        Date date;
        DateFormat hourFormat;
        int setHora = 0;
    EditText txt_tarjetaexpedicion, txt_soat_desde,txt_soat_hasta, txt_poliza_contractual_desde,txt_poliza_contractual_hasta,
    txt_poliza_riesgo_desde,txt_poliza_riesgo_hasta,txt_tarjeta_desde, txt_tarjeta_hasta,txt_revicion_desde,txt_revicion_hasta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registro_conductor4);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        txt_tarjetaexpedicion = (EditText)findViewById(R.id.txt_tarjetaexpedicion);
        txt_soat_desde  = (EditText)findViewById(R.id.txt_soat_desde);
        txt_soat_hasta  = (EditText)findViewById(R.id.txt_soat_hasta);
        txt_poliza_contractual_desde  = (EditText)findViewById(R.id.txt_poliza_contractual_desde);
        txt_poliza_contractual_hasta  = (EditText)findViewById(R.id.txt_poliza_contractual_hasta);
        txt_poliza_riesgo_desde  = (EditText)findViewById(R.id.txt_poliza_riesgo_desde);
        txt_poliza_riesgo_hasta  = (EditText)findViewById(R.id.txt_poliza_riesgo_hasta);

        txt_tarjeta_desde  = (EditText)findViewById(R.id.txt_tarjeta_desde);
        txt_tarjeta_hasta  = (EditText)findViewById(R.id.txt_tarjeta_hasta);
        txt_revicion_desde  = (EditText)findViewById(R.id.txt_revicion_desde);
        txt_revicion_hasta  = (EditText)findViewById(R.id.txt_revicion_hasta);
        loadDatos4();

    }

    private void loadDatos4() {

        txt_tarjetaexpedicion.setText(modelo.conductorDocumentosTerceros.getTxt_tarjetaexpedicion());
        txt_soat_desde.setText(modelo.conductorDocumentosTerceros.getTxt_soat_desde());
        txt_soat_hasta.setText(modelo.conductorDocumentosTerceros.getTxt_soat_hasta());
        txt_poliza_contractual_desde.setText( modelo.conductorDocumentosTerceros.getTxt_poliza_contractual_desde());
        txt_poliza_contractual_hasta.setText( modelo.conductorDocumentosTerceros.getTxt_poliza_contractual_hasta());
        txt_poliza_riesgo_desde.setText(modelo.conductorDocumentosTerceros.getTxt_poliza_riesgo_desde());
        txt_poliza_riesgo_hasta.setText( modelo.conductorDocumentosTerceros.getTxt_poliza_riesgo_hasta());
        txt_tarjeta_desde.setText(modelo.conductorDocumentosTerceros.getTxt_tarjeta_desde());
        txt_tarjeta_hasta.setText(modelo.conductorDocumentosTerceros.getTxt_tarjeta_hasta());
        txt_revicion_desde.setText( modelo.conductorDocumentosTerceros.getTxt_revicion_desde());
        txt_revicion_hasta.setText(modelo.conductorDocumentosTerceros.getTxt_revicion_hasta());
    }


    private void clarDatos4() {
        modelo.conductorDocumentosTerceros.setTxt_tarjetaexpedicion("");
        modelo.conductorDocumentosTerceros.setTxt_soat_desde("");
        modelo.conductorDocumentosTerceros.setTxt_soat_hasta("");
        modelo.conductorDocumentosTerceros.setTxt_poliza_contractual_desde("");
        modelo.conductorDocumentosTerceros.setTxt_poliza_contractual_hasta("");
        modelo.conductorDocumentosTerceros.setTxt_poliza_riesgo_desde("");
        modelo.conductorDocumentosTerceros.setTxt_poliza_riesgo_hasta("");
        modelo.conductorDocumentosTerceros.setTxt_tarjeta_desde("");
        modelo.conductorDocumentosTerceros.setTxt_tarjeta_hasta("");
        modelo.conductorDocumentosTerceros.setTxt_revicion_desde("");
        modelo.conductorDocumentosTerceros.setTxt_revicion_hasta("");

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
        clarDatos4();
        Intent i = new Intent(getApplicationContext(), RegistroConductor3.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
    }

    //dialog timer

    public void hora(View view){

        switch (view.getId()) {

            case R.id.cal_0:
                setHora =1;
                customTimePickerDialog();
                break;
            case R.id.cal_2:
                setHora =2;
                customTimePickerDialog();
                break;

            case R.id.cal_2_2:
                setHora =3;
                customTimePickerDialog();
                break;

            case R.id.cal_3:
                setHora =4;
                customTimePickerDialog();
                break;

            case R.id.cal_3_3:
                setHora =5;
                customTimePickerDialog();
                break;

            case R.id.cal_4:
                setHora =6;
                customTimePickerDialog();
                break;

            case R.id.cal_4_4:
                setHora =7;
                customTimePickerDialog();
                break;

            case R.id.cal_5:
                setHora =8;
                customTimePickerDialog();
                break;

            case R.id.cal_5_5:
                setHora =9;
                customTimePickerDialog();
                break;

            case R.id.cal_6:
                setHora =10;
                customTimePickerDialog();
                break;
            case R.id.cal_6_6:
                setHora =11;
                customTimePickerDialog();
                break;
        }
    }


    //__methode will be call when we click on "Custom Date Picker Dialog" and will be show the custom date selection dilog.
    public void customTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datepickerdialog = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) RegistroConductor4.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datepickerdialog.setThemeDark(true); //set dark them for dialog?
        datepickerdialog.vibrate(true); //vibrate on choosing date?
        datepickerdialog.dismissOnPause(true); //dismiss dialog when onPause() called?
        datepickerdialog.showYearPickerFirst(false); //choose year first?
        datepickerdialog.setAccentColor(Color.parseColor("#80ae49")); // custom accent color
        datepickerdialog.setTitle("Please select a date"); //dialog title
        datepickerdialog.show(getFragmentManager(), "Datepickerdialog"); //show dialog


    }


    public void continuar4(View v){


        if(txt_tarjetaexpedicion.getText().toString().length() < 5){
            txt_tarjetaexpedicion.setError("Fecha Erronea");
        }

        else if(txt_soat_desde.getText().toString().length() < 5){
            txt_soat_desde.setError("Fecha Erronea");
        }
        else if(txt_soat_hasta.getText().toString().length() < 5){
            txt_soat_hasta.setError("Fecha Erronea");
        }
        else if(txt_poliza_contractual_desde.getText().toString().length() < 5){
            txt_poliza_contractual_desde.setError("Fecha Erronea");
        }
        else if(txt_poliza_contractual_hasta.getText().toString().length() < 5){
            txt_poliza_contractual_hasta.setError("Fecha Erronea");
        }
        else if(txt_poliza_riesgo_desde.getText().toString().length() < 5){
            txt_poliza_riesgo_desde.setError("Fecha Erronea");
        }
        else if(txt_poliza_riesgo_hasta.getText().toString().length() < 5){
            txt_poliza_riesgo_hasta.setError("Fecha Erronea");
        }
        else if(txt_tarjeta_desde.getText().toString().length() < 5){
            txt_tarjeta_desde.setError("Fecha Erronea");
        }
        else if(txt_tarjeta_hasta.getText().toString().length() < 5){
            txt_tarjeta_hasta.setError("Fecha Erronea");
        }
        else if(txt_revicion_desde.getText().toString().length() < 5){
            txt_revicion_desde.setError("Fecha Erronea");
        }
        else if(txt_revicion_hasta.getText().toString().length() < 5){
            txt_revicion_hasta.setError("Fecha Erronea");
        }
        else{
            modelo.conductorDocumentosTerceros.setTxt_tarjetaexpedicion(txt_tarjetaexpedicion.getText().toString());
            modelo.conductorDocumentosTerceros.setTxt_soat_desde(txt_soat_desde.getText().toString());
            modelo.conductorDocumentosTerceros.setTxt_soat_hasta(txt_soat_hasta.getText().toString());
            modelo.conductorDocumentosTerceros.setTxt_poliza_contractual_desde(txt_poliza_contractual_desde.getText().toString());
            modelo.conductorDocumentosTerceros.setTxt_poliza_contractual_hasta(txt_poliza_contractual_hasta.getText().toString());
            modelo.conductorDocumentosTerceros.setTxt_poliza_riesgo_desde(txt_poliza_riesgo_desde.getText().toString());
            modelo.conductorDocumentosTerceros.setTxt_poliza_riesgo_hasta(txt_poliza_riesgo_hasta.getText().toString());
            modelo.conductorDocumentosTerceros.setTxt_tarjeta_desde(txt_tarjeta_desde.getText().toString());
            modelo.conductorDocumentosTerceros.setTxt_tarjeta_hasta(txt_tarjeta_hasta.getText().toString());
            modelo.conductorDocumentosTerceros.setTxt_revicion_desde(txt_revicion_desde.getText().toString());
            modelo.conductorDocumentosTerceros.setTxt_revicion_hasta(txt_revicion_hasta.getText().toString());

            Intent i  = new Intent(getApplicationContext(), RegistroConductor5.class);
            startActivity(i);
            finish();
        }
    }

    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;

        if(setHora ==1){
            txt_tarjetaexpedicion.setText(""+date);
        }
        if(setHora ==2){
            txt_soat_desde.setText(""+date);
        }
        if(setHora ==3){
            txt_soat_hasta.setText(""+date);
        }
        if(setHora ==4){
            txt_poliza_contractual_desde.setText(""+date);
        }

        if(setHora ==5){
            txt_poliza_contractual_hasta.setText(""+date);
        }

        if(setHora ==6){
            txt_poliza_riesgo_desde.setText(""+date);
        }
        if(setHora ==7){
            txt_poliza_riesgo_hasta.setText(""+date);
        }
        if(setHora ==8){
            txt_tarjeta_desde.setText(""+date);
        }

        if(setHora ==9){
            txt_tarjeta_hasta.setText(""+date);
        }

        if(setHora ==10){
            txt_revicion_desde.setText(""+date);
        }

        if(setHora ==11){
            txt_revicion_hasta.setText(""+date);
        }
    }
}
