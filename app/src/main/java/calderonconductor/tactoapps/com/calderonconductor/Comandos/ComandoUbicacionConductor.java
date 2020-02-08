package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
        final DatabaseReference ref3 = database.getReference("ubicacionConductor");
        Map<String, Object> ubicacion = new HashMap<>();
        ubicacion.put("I", Arrays.asList(ubicacionConductor.getLat(), ubicacionConductor.getLon()));
        ubicacion.put("asignadoPor", ubicacionConductor.getAsignadoPor());
        ubicacion.put("color", ubicacionConductor.getColor());
        ubicacion.put("estado", ubicacionConductor.getEstado());
        ubicacion.put("marca", ubicacionConductor.getMarca());
        ubicacion.put("matricula", ubicacionConductor.getMatricula());
        ubicacion.put("ofertadaATerceros", ubicacionConductor.getOfertadaATerceros());
        ubicacion.put("referencia", ubicacionConductor.getReferencia());
        ubicacion.put("fechaActualizacion", ubicacionConductor.getFechaHora());
        ubicacion.put("g", "dr5x186m7u");
        if (idConductor == null){
            ref3.child("123456").setValue(ubicacion);
        }else{
            ref3.child(idConductor).setValue(ubicacion);
        }

    }
}
