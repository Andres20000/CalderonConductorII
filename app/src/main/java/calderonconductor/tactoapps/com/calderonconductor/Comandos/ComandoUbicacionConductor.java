package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
import calderonconductor.tactoapps.com.calderonconductor.Clases.UbicacionConductor;

public class ComandoUbicacionConductor {

    public static void ActualizaUbicacionConductor(UbicacionConductor ubicacionConductor, String idConductor){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref3 = database.getReference("ubicacionConductor");

        Map<String, Double> ubicacion = new HashMap<>();
        ubicacion.put("lat", ubicacionConductor.getLat());
        ubicacion.put("lon", ubicacionConductor.getLon());
        ref3.child(idConductor).setValue(ubicacion);
    }
}
