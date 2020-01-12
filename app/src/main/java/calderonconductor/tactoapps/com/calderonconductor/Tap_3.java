package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ScrollingTabContainerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoHistorial;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductor;

public class Tap_3 extends Activity implements ComandoHistorial.OnOrdenesHistorialChangeListener {


    private Modelo sing = Modelo.getInstance();
    public Spinner selecioneCliente;
    public TextView fechaHistorial;

    static final int DATE_DIALOG_ID = 1;
    private int mYear;
    private int mMonth;
    private int mDay;
    String datos;
    OrdenConductor orden;
    Date date_1, date_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tap_3);


        if (savedInstanceState != null) {
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        fechaHistorial = (TextView) findViewById(R.id.fechaHistorial);
        selecioneCliente = (Spinner) findViewById(R.id.selecioneCliente);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);

        orden = new OrdenConductor();

        ComandoHistorial comandoOrHistorial = new ComandoHistorial(this);
        comandoOrHistorial.getOrdenesHistorial();
        sing.llamarServiciosHistorial();


        // comandoOrHistorial.getRazonSocial(orden.getIdCliente());
        selecioneCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(), "nombre : " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void calendar(View v) {
        showDialog(DATE_DIALOG_ID);
    }

    public void historial(View v) {

        if (sing.getRazonessocialesDisponibles().size() <= 0) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("No hay órdenes");
            dialogo1.setMessage("No hay órdenes en el historial");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        } else {
            Intent i = new Intent(getApplicationContext(), HistorialUltimosServicios.class);
            startActivity(i);

        }
    }


    public void buscar(View v) {


        if (sing.getRazonessocialesDisponibles().size() <= 0) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("No hay órdenes");
            dialogo1.setMessage("No hay órdenes en el historial");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        } else if (fechaHistorial.getText().toString().equals("")) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Fecha");
            dialogo1.setMessage("Selecione una Fecha");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();

        } else if (selecioneCliente.getSelectedItem().toString().equals("") || selecioneCliente.getSelectedItem().toString().equals(null)) {

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Razon social");
            dialogo1.setMessage("Selecione la razon social del cliente");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        } else {

            Calendar calendar = Calendar.getInstance();
            final int year1 = calendar.get(Calendar.YEAR);
            final int month1 = calendar.get(Calendar.MONTH) + 1;
            int day1 = calendar.get(Calendar.DAY_OF_MONTH);
            final String fecha_actual = day1 + "/" + month1 + "/" + year1;

            //String dateInString = fecha_actual;
            try {
                date_1 = sing.dfsimple.parse(fecha_actual);
                //System.out.println(date_1);
                //System.out.println(formatter.format(date_1));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String fechaResivida = "01/" + fechaHistorial.getText().toString();

            try {
                date_2 = sing.dfsimple.parse(fechaResivida);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            int rf = date_1.compareTo(date_2);
            if (rf == -1) {
                System.out.println("mayor");

                AlertDialog.Builder dialog = new AlertDialog.Builder(Tap_3.this);
                dialog.setTitle("Fecha Superior"); // set title
                dialog.setMessage("La fecha ingresada es mayor a la  fecha actual"); // set message
                dialog.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fechaHistorial.setText(month1 + "/" + year1);
                            }
                        });
                dialog.create().show();

            } else {


                sing.filtrarPorFechaYCliente(fechaHistorial.getText().toString(), selecioneCliente.getSelectedItem().toString());
                //   Toast.makeText(getApplicationContext(),"ok"+fechaHistorial.getText().toString()+selecioneCliente.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                // Log.v("ok","ok"+fechaHistorial.getText().toString()+selecioneCliente.getSelectedItem().toString());
                Intent i = new Intent(getApplicationContext(), HistoricoServiciosDetallada.class);
                i.putExtra("fecha", "" + fechaHistorial.getText().toString());
                i.putExtra("razonSocial", "" + selecioneCliente.getSelectedItem().toString());
                startActivity(i);

            }
        }
    }




    @Override
    public void cargoHistorial() {

        //cargando combobox con hashtable
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sing.getRazonessocialesDisponibles());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selecioneCliente.setAdapter(dataAdapter);

    }

    //fecha
    DatePickerDialog.OnDateSetListener mDateSetListner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDate();
        }
    };


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
   /*
    * return new DatePickerDialog(this, mDateSetListner, mYear, mMonth,
    * mDay);
    */
                DatePickerDialog datePickerDialog = this.customDatePicker();
                return datePickerDialog;
        }
        return null;
    }

    protected void updateDate() {
        int localMonth = (mMonth + 1);
        String monthString = localMonth < 10 ? "0" + localMonth : Integer
                .toString(localMonth);
        String localYear = Integer.toString(mYear).substring(0);
        fechaHistorial.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(monthString).append("/").append(localYear).append(" "));
        showDialog(DATE_DIALOG_ID);

    }


    private DatePickerDialog customDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(this, mDateSetListner,
                mYear, mMonth + 1, mDay);
        try {

            Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField
                            .get(dpd);
                    Field datePickerFields[] = datePickerDialogField.getType()
                            .getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if ("mDayPicker".equals(datePickerField.getName())
                                || "mDaySpinner".equals(datePickerField
                                .getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }

            }
        } catch (Exception ex) {
        }
        return dpd;
    }
    //fin fecha


}

