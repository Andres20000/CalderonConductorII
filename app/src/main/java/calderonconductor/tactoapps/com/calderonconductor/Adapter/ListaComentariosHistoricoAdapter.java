package calderonconductor.tactoapps.com.calderonconductor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Calificacion;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.R;

/**
 * Created by tactomotion on 30/08/16.
 */
public class ListaComentariosHistoricoAdapter extends BaseAdapter {

    //Atributos del adaptador
    private final Context mContext;

    private List<Calificacion> calificaciones;
    private Modelo sing = Modelo.getInstance();


    //recivimos los datos de la clase informacionServicio
    public ListaComentariosHistoricoAdapter(Context mContext, String  idServicio){

        this.mContext = mContext;
        //informacion del pasajero segun el idServicio
        this.calificaciones = sing.getOrdenHistorial(idServicio).calificaciones;

    }

    @Override
    public int getCount() {
        return calificaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return calificaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables

        final TextView comentario;


        final Calificacion  calificacion =  (Calificacion) getItem(position);

        // Inflate la vista de la Orden
        LinearLayout itemLayout =  (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.adapter_comentario, parent, false);

        // Toma los elementos a utilizar
        comentario = (TextView) itemLayout.findViewById(R.id.comentario);
        comentario.setText(calificacion.getObervacion());

        return itemLayout;

    }



}
