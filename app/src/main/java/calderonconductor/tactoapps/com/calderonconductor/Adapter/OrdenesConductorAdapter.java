package calderonconductor.tactoapps.com.calderonconductor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.R;

/**
 * Created by tactomotion on 2/08/16.
 */
public class OrdenesConductorAdapter extends BaseAdapter {

    //Atributos del adaptador
    private final Context mContext;
    private List<OrdenConductor> ordenes;
    private Modelo sing = Modelo.getInstance();
    Date date_1,date_2;


    public OrdenesConductorAdapter(Context mContext, ArrayList<OrdenConductor> ordenes){

        this.mContext = mContext;
        this.ordenes = ordenes;
    }

    public void updateOrdenes(List<OrdenConductor> ordenes){
        this.ordenes = ordenes;
   }

    @Override
    public int getCount() {

        return ordenes.size();
    }

    @Override
    public Object getItem(int position) {

        return ordenes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        final ImageView imagen_estado2;
        final TextView text_ciudad_origen;
        final TextView text_ciudad_llegada;
        final TextView texto_fecha;
        final TextView texto_hora;
        final TextView texto_dias;
        final TextView texto_direccion;
        final TextView txt_consecutivo, textoCancelado;
        final FrameLayout item, fondo;
        final  ImageView cron;

        final OrdenConductor orden =  (OrdenConductor) getItem(position);

        // Inflate la vista de la Orden
        FrameLayout itemLayout =  (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.list_row, parent, false);

        // Toma los elementos a utilizar
        imagen_estado2 = (ImageView) itemLayout.findViewById(R.id.imagen_estado);
        text_ciudad_origen = (TextView) itemLayout.findViewById(R.id.text_ciudad_origen);
        text_ciudad_llegada = (TextView) itemLayout.findViewById(R.id.text_ciudad_llegada);
        texto_fecha = (TextView) itemLayout.findViewById(R.id.texto_fecha);
        texto_hora = (TextView) itemLayout.findViewById(R.id.texto_hora);
        texto_dias = (TextView) itemLayout.findViewById(R.id.texto_dias);
        texto_direccion = (TextView) itemLayout.findViewById(R.id.texto_direccion);
        txt_consecutivo = (TextView) itemLayout.findViewById(R.id.txt_consecutivo);
        textoCancelado = itemLayout.findViewById(R.id.textoCancelado);

        item =  itemLayout.findViewById(R.id.item);
        fondo =  itemLayout.findViewById(R.id.fondo);
        cron =  itemLayout.findViewById(R.id.cron);


        if (orden.hayRetrazoAbierto()){
            fondo.setVisibility(View.VISIBLE);
            cron.setVisibility(View.VISIBLE);

        }else {
            fondo.setVisibility(View.GONE);
            cron.setVisibility(View.GONE);
        }

        if (orden.getEstado().equals("Finalizado")){
            //sing.eliminarOrden(orden.getId());
        }

        //if (orden.getEstado().equals("Cancelado")){
          //  textoCancelado.setVisibility(View.VISIBLE);
        //}else{
            textoCancelado.setVisibility(View.GONE);
        //}


        if (orden.getEstado().equals("Asignado")){
            imagen_estado2.setImageResource(R.drawable.estado_confirmado_i5);
        }
        else if (orden.getEstado().equals("En Camino") || (orden.getEstado().equals("EnCamino"))){
            imagen_estado2.setImageResource(R.drawable.estado_en_camino_i5);
        }
        else if (orden.getEstado().equals("Transportando")){
            imagen_estado2.setImageResource(R.drawable.estado_transportando_i5);
        }
        else if (orden.getEstado().equals("Finalizado")){
            imagen_estado2.setImageResource(R.drawable.estado_finalizado_i5);
        }else if (orden.getEstado().equals("Cancelado")){
            imagen_estado2.setImageResource(R.drawable.estado_cancelado_i5);
        }

        else if (orden.getEstado().equals("Anulado")){
            imagen_estado2.setVisibility(View.INVISIBLE);
        }
        else if (orden.getEstado().equals("Cotizar")){
            imagen_estado2.setImageResource(R.drawable.estado_cotizar);
        }

        else if (orden.getEstado().equals("NoAsignado") || orden.getEstado().equals("No Asignado")){
            imagen_estado2.setImageResource(R.drawable.estado_sin_confirmar_i5);
        }
        else{
            //imagen_estado2.setImageResource(R.drawable.estado_cancelado_i5);
            imagen_estado2.setVisibility(View.INVISIBLE);
        }



        if(!orden.getEstado().equals("Anulado") && !orden.getEstado().equals("SinConfirmar")){

            text_ciudad_origen.setText(orden.getOrigen());
            text_ciudad_llegada.setText(orden.getDestino());

            texto_fecha.setText(sing.dfsimple.format(orden.getFechaEnOrigen()));
            texto_hora.setText(orden.getHora());
            //texto_dias.setText(orden.getTiempoRestante()+"");
            texto_direccion.setText(orden.getDireccionDestino());
            txt_consecutivo.setText(orden.getCosecutivoOrden() + " "+  orden.getEstado());
            if (orden.getEstado().equals("EnCamino")){
                txt_consecutivo.setText(orden.getCosecutivoOrden() + " "+  "En Camino");
            }

            //pasamos el dato fecha al metodo  formatearFecha y lo convertimos a formato fecha

            //|| sing.formatearFecha(orden.getFechaEnOrigen()).equals("Pasado Mañana")
            if(sing.formatearFecha(orden.getFechaEnOrigen()).equals("Hoy") || sing.formatearFecha(orden.getFechaEnOrigen()).equals("Mañana")){

                texto_dias.setText(sing.formatearFecha(orden.getFechaEnOrigen()));
            }
            else {
//capturamos la fecha actual
                Calendar calendar = Calendar.getInstance();
                final int year1 = calendar.get(Calendar.YEAR);
                final int month1 = calendar.get(Calendar.MONTH)+1;
                int  day1 = calendar.get(Calendar.DAY_OF_MONTH);
                final String fecha_actual = day1+"/"+month1+"/"+year1;


                try {
                    //convertimos a tipo fecha
                    date_1 = sing.dfsimple.parse(fecha_actual);
                    date_2 = orden.getFechaEnOrigen();

                    //convertimos de date a DAtetime
                    DateTime fechanow = new DateTime(date_1);
                    DateTime fechaOrden = new DateTime(date_2);

                    //capturamos la diferencia de dias
                    Days.daysBetween(fechanow,fechaOrden).getDays();
                    //System.out.println(date_1);
                    //System.out.println(formatter.format(date_1));
                    //validamos si el numero de dias es negativo
                    if (Days.daysBetween(fechanow,fechaOrden).getDays()<0){

                        texto_dias.setText(orden.getFechaEnOrigenEs());
                    }else{
                        texto_dias.setText(Days.daysBetween(fechanow,fechaOrden).getDays()+" Dias");
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else {
            item.setVisibility(View.GONE);
        }


        if (orden.getDestino().equals("ABIERTO")) {


            if (orden.getDiasServicio().equals("") && orden.getHorasServicio().equals("")) {
                texto_direccion.setText("Duración del servicio: Indefinido");
            }

            if (!orden.getDiasServicio().equals("")) {
                String texto = "";

                if (orden.getDiasServicio().equals("1")) {
                    texto = "día";
                } else {
                    texto = "días";
                }
                texto_direccion.setText("Duración del servicio: " + orden.getDiasServicio() + " " + texto);
            }

            if (!orden.getHorasServicio().equals("")) {
                String texto = "";

                if (orden.getHorasServicio().equals("1")) {
                    texto = "hora";
                } else {
                    texto = "horas";
                }
                texto_direccion.setText("Duración del servicio:" + " " + texto);
            }
        }

        return itemLayout;

    }

}
