package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import calderonconductor.tactoapps.com.calderonconductor.Clases.UbicacionConductor;

public class ComandoUbicacionConductor {

    public static void ActualizaUbicacionConductor(UbicacionConductor ubicacionConductor, String idConductor){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref3 = database.getReference("ubicacionConductor");

        Map<String, Object> ubicacion = new HashMap<>();
        ubicacion.put("lat", ubicacionConductor.getLat());
        ubicacion.put("lon", ubicacionConductor.getLon());
        ubicacion.put("asignadoPor", ubicacionConductor.getAsignadoPor());
        ubicacion.put("color", ubicacionConductor.getColor());
        ubicacion.put("estado", ubicacionConductor.getEstado());
        ubicacion.put("marca", ubicacionConductor.getMarca());
        ubicacion.put("matricula", ubicacionConductor.getMatricula());
        ubicacion.put("ofertadaATerceros", ubicacionConductor.getOfertadaATerceros());
        ubicacion.put("referencia", ubicacionConductor.getReferencia());
        ubicacion.put("fechaActualizacion", ubicacionConductor.getFechaHora());

        ref3.child(idConductor).setValue(ubicacion);
    }
}
