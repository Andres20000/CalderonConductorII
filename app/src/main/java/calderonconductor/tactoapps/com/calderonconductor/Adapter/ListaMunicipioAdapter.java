package calderonconductor.tactoapps.com.calderonconductor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Municipio;
import calderonconductor.tactoapps.com.calderonconductor.R;

/**
 * Created by tactomotion on 18/10/16.
 */
public class ListaMunicipioAdapter extends BaseAdapter {

    //Atributos del adaptador
    private final Context mContext;
    AdapterCallback mListener = sDummyCallbacks;
    private List<Municipio> municipios;
    private Modelo sing = Modelo.getInstance();


    //recivimos los datos de la clase informacionServicio
    public ListaMunicipioAdapter(Context mContext, AdapterCallback mListener, String  idServicio){

        this.mContext = mContext;
        this.mListener = mListener;
        //asignamos los valores y se llama la clase principal modelo y llamamos el metodo get ordenes
        // que contiene las ordenes y le pasamos como parametro el idservicio para que nos muetre la
        //informacion del pasajero segun el idServicio
        this.municipios = sing.getOrden(idServicio).municipios;
    }

    @Override
    public int getCount() {
        return municipios.size();
    }

    @Override
    public Object getItem(int position) {
        return municipios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables

        final TextView ciudadpueblos;
        final TextView direccionDestino;


        final Municipio  municipio =  (Municipio) getItem(position);

        // Inflate la vista de la Orden
        LinearLayout itemLayout =  (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.list_destino, parent, false);

        // Toma los elementos a utilizar
        ciudadpueblos = (TextView) itemLayout.findViewById(R.id.ciudadpueblos);
        direccionDestino = (TextView) itemLayout.findViewById(R.id.direccionDestino);
        ciudadpueblos.setText(municipio.getMunicipio());
        direccionDestino.setText(municipio.getDirecion());

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
