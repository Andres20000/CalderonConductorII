package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import java.util.Date;
import java.text.DateFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import android.widget.TimePicker;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;


public class RegistroConductor3 extends Activity implements  TimePickerDialog.OnTimeSetListener, android.app.TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{

    EditText txt_placa,txt_marca,txt_tipo, fecha_expedicion_tarjeta_propiedad;
    final Context context = this;
    Modelo modelo = Modelo.getInstance();
    String listaDeVehiculos ="";

    Date date;
    DateFormat hourFormat;

    int setHora = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registro_conductor3);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        txt_placa = (EditText)findViewById(R.id.txt_placa);
        txt_marca = (EditText)findViewById(R.id.txt_marca);
        txt_tipo = (EditText)findViewById(R.id.txt_tipo);
        fecha_expedicion_tarjeta_propiedad = (EditText)findViewById(R.id.fecha_expedicion_tarjeta_propiedad);

        if(modelo.tiposVehiculosTerceros.size() > 0){
            for(int i = 0; i < modelo.tiposVehiculosTerceros.size(); i++){
                listaDeVehiculos +=  modelo.tiposVehiculosTerceros.get(i)+",";
            }
        }

        Log.v("ok", "ok"+listaDeVehiculos);
        loadDatos3();
    }

    private void loadDatos3() {

        txt_placa.setText(modelo.vehiculo.getPlaca());
        txt_marca.setText(modelo.vehiculo.getMarca());
        txt_tipo.setText(modelo.vehiculo.getTipo());
        fecha_expedicion_tarjeta_propiedad.setText(modelo.vehiculo.getFecha_Expedicion_Tarjeta_Propiedad());

    }

    public void borrarDatos3(){
       modelo.vehiculo.setPlaca("");
        modelo.vehiculo.setMarca("");
        modelo.vehiculo.setTipo("");
        modelo.vehiculo.setFecha_Expedicion_Tarjeta_Propiedad("");


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
        borrarDatos3();
        Intent i = new Intent(getApplicationContext(), RegistroConductor2.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
    }

    public void continuar3(View v){

        if(txt_placa.getText().toString().length() < 6){
            txt_placa.setError("Información muy corta");
        }
        else if(txt_marca.getText().toString().length() < 3){
            txt_marca.setError("Información muy corta");
        }
        else if(txt_tipo.getText().toString().length() < 3){
            txt_tipo.setError("Información muy corta");
        }
        else if(fecha_expedicion_tarjeta_propiedad.getText().toString().length() < 4){
            fecha_expedicion_tarjeta_propiedad.setError("Información muy corta");
        }
        else{
            modelo.vehiculo.setFecha_Expedicion_Tarjeta_Propiedad(fecha_expedicion_tarjeta_propiedad.getText().toString());
            modelo.vehiculo.setMarca(txt_marca.getText().toString());
            modelo.vehiculo.setPlaca(txt_placa.getText().toString());
            modelo.vehiculo.setTipo(txt_tipo.getText().toString());

            Intent i  = new Intent(getApplicationContext(), RegistroConductor4.class);
            startActivity(i);
            finish();
        }
    }


    //dialog timer

    public void hora(View view){

        switch (view.getId()) {
            case R.id.fecha_expedicion_tarjeta_propiedad:
                setHora =1;
                customTimePickerDialog();
                break;
        }
    }

    //__methode will be call when we click on "Custom Date Picker Dialog" and will be show the custom date selection dilog.
    public void customTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datepickerdialog = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) RegistroConductor3.this,
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

    //__methode will be call when we click on "Custom Date Picker Dialog" and will be show the custom date selection dilog.
    public void customTimePickerDialog1() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), false);
        dpd.setAccentColor(getResources().getColor(R.color.colorVerdeOscuro));
        dpd.show(getFragmentManager(), "Timepickerdialog");
    }

    //___this is the listener callback method will be call on time selection by default date picker.
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        //Toast.makeText(this, "Selected by default time picker : " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    //___this is the listener callback method will be call on time selection by custom date picker.
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        date=datetime.getTime();
        //Toast.makeText(this, "Selected by custom time picker : " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),""+hourFormat.format(date), Toast.LENGTH_LONG).show();

        //fecha_expedicion_tarjeta_propiedad.setText(""+hourFormat.format(date));
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

        fecha_expedicion_tarjeta_propiedad.setText(""+date);
    }


    public void listadoVeiculos2(View v){
        showListadoVehiculos();
    }

    public void showListadoVehiculos(){
        final CharSequence[] items = {"Camioneta", "Campero", "Sedan"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tipo de Carro");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                txt_tipo.setText(items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void listadoVeiculos(View v) {

        final   CharSequence [] items = listaDeVehiculos.split(",");

        // final CharSequence[] items = new CharSequence[]{"Snacks", "Alimentos"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tipo de Carro");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                txt_tipo.setText(""+items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
