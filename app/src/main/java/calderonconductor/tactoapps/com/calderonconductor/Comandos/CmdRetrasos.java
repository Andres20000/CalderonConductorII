package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.util.Log;

import com.firebase.client.ServerValue;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Retraso;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;

public class CmdRetrasos {

    public static void subirRetraso(OrdenConductor orden, Retraso retra) {


        if (retra == null || orden == null || retra.fechaInicio == null){
            return;
        }


        final Modelo modelo = Modelo.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("ordenes/pendientes/" + orden.getId()  + "/retrasos");


        if (retra.id == null){
            String id = ref.push().getKey();
            retra.id = id;
            ref = ref.child(id);
        }
        else{
            ref = ref.child(retra.id);
        }


        Map<String, Object> dicc = new HashMap<String, Object>();
        dicc.put("inicio", Utility.convertDateToFechayHora(retra.fechaInicio));

        if (retra.fechaFin != null) {

            dicc.put("fin", Utility.convertDateToFechayHora(retra.fechaFin));
            dicc.put("endTime", ServerValue.TIMESTAMP);
            long minutos = (retra.fechaFin.getTime() - retra.fechaInicio.getTime()) / 1000 / 60;
            dicc.put("minutosDemora", minutos);
            dicc.put("valorDemora", ( Long.valueOf(minutos) / 60.0) * orden.precioHora);

        }


        if (retra.comentario != null){
            dicc.put("motivo", retra.comentario);
        }


        if (retra.startTime != null && retra.startTime > 0){
            dicc.put("startTime", retra.startTime);
        }



        try {
            ref.setValue(dicc);
        } catch (DatabaseException e){

            Log.i("ERROR al salvar", e.getMessage());
            Log.i("ERROR al salvar", dicc.toString());
        }


    }

}
