package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.firebase.client.ServerValue;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import calderonconductor.tactoapps.com.calderonconductor.Clases.UbicacionConductor;

public class ComandoUbicacionConductor {

    public static void ActualizaUbicacionConductor(UbicacionConductor ubicacionConductor, String idConductor){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("ubicacionConductor");
        Map<String, Object> ubicacion = new HashMap<>();

        ubicacion.put("lat", ubicacionConductor.getLat() );
        ubicacion.put("lon", ubicacionConductor.getLon() );
        ubicacion.put("timestamp",  ServerValue.TIMESTAMP);

        if (idConductor != null){
            ref.child(idConductor).setValue(ubicacion);
        }
    }
}
