package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

/**
 * Created by tactomotion on 3/09/16.
 */
public class ComandoCancelarServicio  {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();



    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnCancelacionChangeListener {

        void cargoCancelacion();

    }


    //interface del listener de la actividad interesada
    private OnCancelacionChangeListener mListener;

    public ComandoCancelarServicio(OnCancelacionChangeListener mListener){

        this.mListener = mListener;

    }

    public  ComandoCancelarServicio(){

    }


    //actualizar el conductor a vacio
    public void getCamioConductor(String idServicio){

        final DatabaseReference ref = database.getReference("ordenes/pendientes/"+idServicio+"/conductor");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {//addListenerForSingleValueEvent no queda escuchando peticiones
            @Override
            public void onDataChange(DataSnapshot snap) {
                ref.setValue("");
                mListener.cargoCancelacion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //actualizar estadi por motivo de cancelacion
    public void getCambioEstado(String idServicio){

        final DatabaseReference ref = database.getReference("ordenes/pendientes/"+idServicio+"/estado");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {//addListenerForSingleValueEvent no queda escuchando peticiones
            @Override
            public void onDataChange(DataSnapshot snap) {
                ref.setValue("NoAsignado");
                mListener.cargoCancelacion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //actualizar estadi por motivo de cancelacion matricula
    public void getCambioMatricula(String idServicio){

        final DatabaseReference ref = database.getReference("ordenes/pendientes/"+idServicio+"/matricula");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {//addListenerForSingleValueEvent no queda escuchando peticiones
            @Override
            public void onDataChange(DataSnapshot snap) {
                ref.setValue("");
                mListener.cargoCancelacion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //cancelar servicio
    public void getCancelacionServicio(String idServicio, String idConductor, final String motivoCancelacion){
        final DatabaseReference ref = database.getReference("ordenes/pendientes/"+idServicio+"/cancelacionesConductor/"+idConductor);//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {//addListenerForSingleValueEvent no queda escuchando peticiones
            @Override
            public void onDataChange(DataSnapshot snap) {
                ref.setValue(motivoCancelacion);
                mListener.cargoCancelacion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Para evitar nullpointerExeptions
     */
    private static OnCancelacionChangeListener sDummyCallbacks = new OnCancelacionChangeListener()
    {
        @Override
        public void cargoCancelacion()
        {}



    };
}
