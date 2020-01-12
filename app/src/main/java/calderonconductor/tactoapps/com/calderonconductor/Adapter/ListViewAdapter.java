package calderonconductor.tactoapps.com.calderonconductor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import calderonconductor.tactoapps.com.calderonconductor.R;


public class ListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    int[] imagen_estado;
    String[] ciudad_inicio;
    String[] ciudad_fin;
    String[] fecha;
    String[] hora;
    String[] tiempo_restantes;
    String[] direccion;

    LayoutInflater inflater;

    public ListViewAdapter(Context context, int[] imagen_estado,String[] ciudad_inicio, String[] ciudad_fin, String[] fecha,String[] hora,String[] tiempo_restantes, String[] direccion) {
        this.context = context;
        this.imagen_estado = imagen_estado;
        this.ciudad_inicio = ciudad_inicio;
        this.ciudad_fin = ciudad_fin;
        this.fecha = fecha;
        this.hora = hora;
        this.tiempo_restantes = tiempo_restantes;
        this.direccion = direccion;

    }

    @Override
    public int getCount() {
        return ciudad_inicio.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        ImageView imagen_estado2;
        TextView text_ciudad_origen;
        TextView text_ciudad_llegada;
        TextView texto_fecha;
        TextView texto_hora;
        TextView texto_dias;
        TextView texto_direccion;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_row, parent, false);

        // Locate the TextViews in listview_item.xml
        imagen_estado2 = (ImageView) itemView.findViewById(R.id.imagen_estado);
        text_ciudad_origen = (TextView) itemView.findViewById(R.id.text_ciudad_origen);
        text_ciudad_llegada = (TextView) itemView.findViewById(R.id.text_ciudad_llegada);
        texto_fecha = (TextView) itemView.findViewById(R.id.texto_fecha);
        texto_hora = (TextView) itemView.findViewById(R.id.texto_hora);
        texto_dias = (TextView) itemView.findViewById(R.id.texto_dias);
        texto_direccion = (TextView) itemView.findViewById(R.id.texto_direccion);

        // Capture position and set to the TextViews

        imagen_estado2.setImageResource(imagen_estado[position]);
        text_ciudad_origen.setText(ciudad_inicio[position]);
        text_ciudad_llegada.setText(ciudad_fin[position]);
        texto_fecha.setText(fecha[position]);
        texto_hora.setText(hora[position]);
        texto_dias.setText(tiempo_restantes[position]);
        texto_direccion.setText(direccion[position]);

        return itemView;
    }
}