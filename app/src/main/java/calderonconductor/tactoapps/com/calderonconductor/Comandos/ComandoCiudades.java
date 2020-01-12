package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

/**
 * Created by tactomotion on 13/09/16.
 */
public class ComandoCiudades {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();



    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnCiudadesChangeListener {

        void cargoCiudades();

    }


    //interface del listener de la actividad interesada
    private OnCiudadesChangeListener mListener;

    public ComandoCiudades(OnCiudadesChangeListener mListener){

        this.mListener = mListener;

    }

    public  ComandoCiudades(){

    }

    public void getCiudades(){

        final DatabaseReference ref = database.getReference("trayectos/");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {//addListenerForSingleValueEvent no queda escuchando peticiones
            @Override
            public void onDataChange(DataSnapshot snap) {
                ref.setValue("");
                mListener.cargoCiudades();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Para evitar nullpointerExeptions
     */
    private static OnCiudadesChangeListener sDummyCallbacks = new OnCiudadesChangeListener()
    {
        @Override
        public void cargoCiudades()
        {}



    };
}
