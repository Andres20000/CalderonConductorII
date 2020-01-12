package calderonconductor.tactoapps.com.calderonconductor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.R;

/**
 * Created by tactomotion on 4/09/16.
 */
public class HistorialAdapter extends BaseAdapter {

    //Atributos del adaptador
    private final Context mContext;
    AdapterCallback mListener = sDummyCallbacks;
    private List<OrdenConductor> ordenes ;
    private Modelo sing = Modelo.getInstance();



    public HistorialAdapter(Context mContext, AdapterCallback mListener){

        this.mContext = mContext;
        this.mListener = mListener;
        this.ordenes = sing.getHistorial();

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
        final TextView texto_emprea;
        final TextView texto_fecha;
        final TextView txt_consecutivo_h;


        final OrdenConductor orden =  (OrdenConductor) getItem(position);

        // Inflate la vista de la Orden
        LinearLayout itemLayout =  (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.historialservicios, parent, false);

        // Toma los elementos a utilizar
        imagen_estado2 = (ImageView) itemLayout.findViewById(R.id.imagen_estado);
        text_ciudad_origen = (TextView) itemLayout.findViewById(R.id.text_ciudad_origen);
        text_ciudad_llegada = (TextView) itemLayout.findViewById(R.id.text_ciudad_llegada);
        texto_emprea = (TextView) itemLayout.findViewById(R.id.texto_emprea);
        texto_fecha = (TextView) itemLayout.findViewById(R.id.texto_fecha);
        txt_consecutivo_h = (TextView) itemLayout.findViewById(R.id.txt_consecutivo_h);


        if (orden.getEstado().equals("Almacenado")){
            imagen_estado2.setImageResource(R.drawable.estado_finalizado_i5);
        }
        else{
            imagen_estado2.setImageResource(R.drawable.estado_finalizado_i5);
        }

        txt_consecutivo_h.setText(""+orden.getCosecutivoOrden()+" "+ orden.getEstado());
        text_ciudad_origen.setText(orden.getOrigen());
        text_ciudad_llegada.setText(orden.getDestino());
        texto_emprea.setText(""+sing.razonesSociales.get(orden.getIdCliente()));

        texto_fecha.setText(sing.dfsimple.format(orden.getFechaEnOrigen()));
        //pasamos el dato fecha al metodo  formatearFecha y lo convertimos a formato fecha


        return itemLayout;

    }



    /* Se define la interfaz del callback para poder informar a la actividad de alguna accion o dato.
   */
    public interface AdapterCallback
    {

    }

    /**
     * Para evitar nullpointerExceptions
     */
    private static AdapterCallback sDummyCallbacks = new AdapterCallback()
    {


    };

}
