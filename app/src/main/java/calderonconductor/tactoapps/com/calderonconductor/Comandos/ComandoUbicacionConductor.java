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
        final DatabaseReference ref3 = database.getReference("ubicacionConductores");
        Map<String, Object> ubicacion = new HashMap<>();
/*
        ubicacion.put("asignadoPor", ubicacionConductor.getAsignadoPor());
        ubicacion.put("color", ubicacionConductor.getColor());
        ubicacion.put("estado", ubicacionConductor.getEstado());
        ubicacion.put("marca", ubicacionConductor.getMarca());
        ubicacion.put("matricula", ubicacionConductor.getMatricula());
        ubicacion.put("ofertadaATerceros", ubicacionConductor.getOfertadaATerceros());
        ubicacion.put("referencia", ubicacionConductor.getReferencia());
        ubicacion.put("fechaActualizacion", ubicacionConductor.getFechaHora());
        ubicacion.put("conductor", idConductor);*/
        ubicacion.put(".priority", "d2g6fdwhkr");
        ubicacion.put("g", "d2g6fdwhkr");
        ubicacion.put("l", Arrays.asList(ubicacionConductor.getLat(), ubicacionConductor.getLon()));
        if (idConductor == null){
            ref3.child("123456").setValue(ubicacion);
        }else{
            ref3.child(idConductor).setValue(ubicacion);
        }

    }
}
