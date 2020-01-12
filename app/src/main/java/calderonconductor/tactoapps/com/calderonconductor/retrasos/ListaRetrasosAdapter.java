package calderonconductor.tactoapps.com.calderonconductor.retrasos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Retraso;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdRetrasos;
import calderonconductor.tactoapps.com.calderonconductor.R;

class ListaRetrasosAdapter extends BaseAdapter {



    private Context mContext;
    private LayoutInflater mInflater;
    Modelo modelc = Modelo.getInstance();
    ArrayList<Retraso> retrasos;

    TextView cron;
    TextView inicio;
    TextView fin, lbDuracion;

    Retraso retra;

    Timer timer;

    OrdenConductor orden;



    public ListaRetrasosAdapter(Context context, OrdenConductor orden, ArrayList<Retraso> retrasos){

        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.retrasos = retrasos;
        this.orden =  orden;

    }



    @Override
    public int getCount() {

        return retrasos.size();
    }

    @Override
    public Object getItem(int i) {

        return retrasos.get(i);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public void parar(){
        if (timer != null){
            timer.cancel();
            timer = null;
        }

    }


    @Override
    public View getView(final int i, View view, ViewGroup parent) {

        final View rowView = mInflater.inflate(R.layout.item_lista_retraso, parent, false);


        final Retraso retraTemp = retrasos.get(i);

        inicio = rowView.findViewById(R.id.inicio);
        fin = rowView.findViewById(R.id.fin);
        cron = rowView.findViewById(R.id.cron);
        lbDuracion = rowView.findViewById(R.id.lbDuracion);
        final Button boton = rowView.findViewById(R.id.boton);
        final EditText motivo = rowView.findViewById(R.id.motivo);



        ///Solo la pintura....
        motivo.setText(retraTemp.comentario);
        motivo.setEnabled(true);
        if (retraTemp.fechaInicio != null){
            inicio.setText(convertDateToFechaToHora(retraTemp.fechaInicio));
        }

        if (retraTemp.fechaFin != null){
            fin.setText(convertDateToFechaToHora(retraTemp.fechaFin));
            motivo.setEnabled(false);
        }

        if (retraTemp.yaTerminado()){   // Si presionan en uno ya terminado
            boton.setVisibility(View.GONE);
            lbDuracion.setVisibility(View.VISIBLE);
            motivo.setEnabled(false);

            long lapso = retraTemp.fechaFin.getTime() - retraTemp.fechaInicio.getTime();

            cron.setText(getTiempoFinal(lapso));

        }

        if (retraTemp.estaContando()){
            retra =  retrasos.get(i);
            iniciarCron(cron);
            boton.setText("Finalizar");

        }




        boton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                inicio = rowView.findViewById(R.id.inicio);
                fin = rowView.findViewById(R.id.fin);
                cron = rowView.findViewById(R.id.cron);
                lbDuracion = rowView.findViewById(R.id.lbDuracion);
                final EditText motivo = rowView.findViewById(R.id.motivo);

                hideKeyboard((Activity) mContext);
                motivo.clearFocus();
                view.requestFocus();

                Retraso retraTemp = retrasos.get(i);

                if (retraTemp.yaTerminado()){   // Si presionan en uno ya terminado
                    return;
                }
                if (retraTemp.startTime == 0 ){   //Si presionan en uno sin comenzar(debe ser uno recien creado)
                    retra = retrasos.get(i);
                    retra.startTime = System.currentTimeMillis();
                    cron.setText("Iniciando");
                    iniciarCron(cron);
                    inicio.setText(Utility.getHora());

                    boton.setText("Finalizar");

                    retra.fechaInicio = new Date();
                    retra.comentario = motivo.getText().toString();

                    CmdRetrasos.subirRetraso(orden, retra);
                    return;

                }

                if (retraTemp.startTime > 0 ){   //Si presionan en uno ya iniciado (deberia ser el unico)

                    if (motivo.getText().length() < 3) {
                        motivo.setError("Debe ingresar el motivo del retraso");
                        return;
                    }

                    timer.cancel();
                    boton.setVisibility(View.GONE);
                    lbDuracion.setVisibility(View.VISIBLE);
                    motivo.setEnabled(false);
                    fin.setText(Utility.getHora());
                    retra = retrasos.get(i);
                    retra.fechaFin = new Date();
                    retra.comentario = motivo.getText().toString();
                    CmdRetrasos.subirRetraso(orden, retra);
                    return;
                }


            }
        });


        return rowView;
    }


    public void iniciarCron(final TextView crono){


        if (timer!= null){
            timer.cancel();
            timer = null;
        }

        timer  = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {


                final Long spentTime = (System.currentTimeMillis() - retra.startTime);

                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String res = String.format("%02d:%02d:%02d ",
                                TimeUnit.MILLISECONDS.toHours(spentTime),
                                TimeUnit.MILLISECONDS.toMinutes(spentTime),
                                TimeUnit.MILLISECONDS.toSeconds(spentTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(spentTime))
                        );
                        crono.setText("" + res);


                    }
                });

            }
        },0, 1);

    }


    public String convertDateToFechaToHora(Date indate)
    {
        String fecha = null;
        DateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy h:mm a");

        fecha = sdfr.format( indate );
        fecha = fecha.replace("a. m.","AM");
        fecha = fecha.replace("p. m.","PM");
        fecha = fecha.replace("a.m.","AM");
        fecha = fecha.replace("p.m.","PM");
        fecha = fecha.replace("am","AM");
        fecha = fecha.replace("pm","PM");

        String[] parts = fecha.split(" ");

        if (parts.length < 3){
            return "00:00 AM";
        }

        return parts[1] +  " " + parts[2];
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public String getTiempoFinal(Long spentTime){

        String res = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(spentTime),
                TimeUnit.MILLISECONDS.toMinutes(spentTime));

        return res;

    }



}
