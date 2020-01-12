package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Pasajero;

public class CmdPasajero {


    public interface OnGetPasajero {

        void cargo(Pasajero pasajero);
    }

    public static void getPasajero(final OrdenConductor orden, final OnGetPasajero listener){

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        final Pasajero pasa = orden.pasajeros.get(0);
        DatabaseReference ref = database.getReference("usuarios/" + pasa.getIdPasajero());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                Pasajero pasajero = new Pasajero();
                pasajero.setNombre(snap.child("nombre").getValue().toString());
                pasajero.setApellido(snap.child("apellido").getValue().toString());
                pasajero.setCelular(snap.child("celular").getValue().toString());
                pasajero.setTokenDevice(snap.child("tokenDevice").getValue().toString());
                pasajero.setTipo(pasa.getTipo());
                pasajero.setIdPasajero(pasa.getIdPasajero());

                orden.adicionarNuevoPasajero(pasajero);

                listener.cargo(pasajero);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
