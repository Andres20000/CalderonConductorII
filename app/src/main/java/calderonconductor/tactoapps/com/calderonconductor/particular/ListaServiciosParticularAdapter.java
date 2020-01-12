package calderonconductor.tactoapps.com.calderonconductor.particular;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Pasajero;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdPasajero;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdPasajero.OnGetPasajero;
import calderonconductor.tactoapps.com.calderonconductor.R;


public class ListaServiciosParticularAdapter  extends BaseAdapter {

    private final Context mContext;
    private List<OrdenConductor> ordenes;

    private int posNoMias = -1;

    public ListaServiciosParticularAdapter(Context mContext, ArrayList<OrdenConductor> ordenes){

        this.mContext = mContext;
        this.ordenes = ordenes;
    }


    public void updateOrdenes(List<OrdenConductor> ordenes){
        posNoMias = -1;
        this.ordenes = ordenes;

        int pos = 0 ;
        for (OrdenConductor orden : ordenes) {
            if (orden.getEstado().equals("NoAsignado")){
                posNoMias = pos;
                break;
            }
            pos++;
        }
    }


    @Override
    public int getCount() {
        return ordenes.size();
    }

    @Override
    public Object getItem(int i) {
        return ordenes.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ImageView foto;
        final TextView origen, destino, nombre, distancia, fechaProgramar;
        final LinearLayout layProgramar;
        final LinearLayout layOtros;
        final TextView textoCancelado;



        final OrdenConductor orden =  (OrdenConductor) getItem(position);

        FrameLayout itemLayout =  (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.item_lista_particular, parent, false);

        if ( position != posNoMias ){
            itemLayout =  (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.item_lista_particular, parent, false);
        }else {
            itemLayout =  (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.item_lista_particular_con_titulo, parent, false);
        }


        foto = itemLayout.findViewById(R.id.foto);
        nombre = itemLayout.findViewById(R.id.nombre);
        origen = itemLayout.findViewById(R.id.origen);
        destino = itemLayout.findViewById(R.id.destino);
        distancia = itemLayout.findViewById(R.id.distancia);
        fechaProgramar = itemLayout.findViewById(R.id.fechaprogramar);
        layProgramar = itemLayout.findViewById(R.id.layerProgramar);
        layOtros = itemLayout.findViewById(R.id.layerOtros);
        textoCancelado = itemLayout.findViewById(R.id.franjaRoja);



        if (!orden.servicioInmediato) {
            layProgramar.setVisibility(View.VISIBLE);
            fechaProgramar.setText(Utility.getFotmatoFechaDestino(orden.getFechaEnOrigen()) + " " +orden.getHora());
        }else{
            layProgramar.setVisibility(View.GONE);
        }

        if ( position != posNoMias ){
            layOtros.setVisibility(View.GONE);

        }else {
            layOtros.setVisibility(View.VISIBLE);
        }

        if (orden.getEstado().equals("Cancelado")){
            textoCancelado.setVisibility(View.VISIBLE);
        }else{
            textoCancelado.setVisibility(View.GONE);
        }



        origen.setText(orden.getDireccionOrigen());
        destino.setText(orden.getDireccionDestino());

        Pasajero pasajero = orden.pasajeros.get(0);
        nombre.setText(pasajero.getNombreCompleto() + " | "  + orden.getHoraGeneracion());


        if (orden.distanciaConduciendo == -1){
            distancia.setText("...");
            return itemLayout;
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        distancia.setText(df.format(orden.distanciaConduciendo / 1000.0));


        return itemLayout;
    }
}
