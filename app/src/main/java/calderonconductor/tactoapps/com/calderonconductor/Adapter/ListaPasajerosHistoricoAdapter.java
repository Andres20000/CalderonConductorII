package calderonconductor.tactoapps.com.calderonconductor.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Pasajero;
import calderonconductor.tactoapps.com.calderonconductor.R;

/**
 * Created by tactomotion on 30/08/16.
 */
public class ListaPasajerosHistoricoAdapter extends BaseAdapter {

    //Atributos del adaptador
    private final Context mContext;
    AdapterCallback mListener = sDummyCallbacks;
    private List<Pasajero> pasajeros;
    private Modelo sing = Modelo.getInstance();


    //recivimos los datos de la clase informacionServicio
    public ListaPasajerosHistoricoAdapter(Context mContext, AdapterCallback mListener, String idServicio) {

        this.mContext = mContext;
        this.mListener = mListener;
        //asignamos los valores y se llama la clase principal modelo y llamamos el metodo get ordenes
        // que contiene las ordenes y le pasamos como parametro el idservicio para que nos muetre la
        //informacion del pasajero segun el idServicio
        this.pasajeros = sing.getOrdenHistorial(idServicio).pasajeros;
    }

    @Override
    public int getCount() {
        return pasajeros.size();
    }

    @Override
    public Object getItem(int position) {
        return pasajeros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables

        final TextView nombre;
        final TextView celular;


        final Pasajero pasajero = (Pasajero) getItem(position);
        LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.adapter_lista_detalla_servicios, parent, false);



            // Inflate la vista de la Orden

            // Toma los elementos a utilizar
            nombre = (TextView) itemLayout.findViewById(R.id.nombre);
            celular = (TextView) itemLayout.findViewById(R.id.celular);


       /* if (pasajero.misPasajeros.size() > 0) {
            for (int i = 0; i < pasajero.misPasajeros.size(); i++) {
                nombre.setText(pasajero.misPasajeros.get(i).getNombre() + " " + pasajero.misPasajeros.get(i).getApellido());
                celular.setText(pasajero.misPasajeros.get(i).getCelular());
            }
        }*/

            nombre.setText(pasajero.getNombre() + " " + pasajero.getApellido());
            celular.setText(pasajero.getCelular());




            celular.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + pasajero.getCelular()));
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mContext.startActivity(callIntent);

                }
            });





        return itemLayout;

    }


    /* Se define la interfaz del callback para poder informar a la actividad de alguna accion o dato.
   */
    public interface AdapterCallback {

    }

    /**
     * Para evitar nullpointerExceptions
     */
    private static AdapterCallback sDummyCallbacks = new AdapterCallback() {


    };

}
