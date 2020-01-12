package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

/**
 * Created by tacto on 2/10/17.
 */

public class ComandoValidarUsuario {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    //interface del listener de la actividad interesada
    private OnValidarUsuarioChangeListener mListener;

    public ComandoValidarUsuario(OnValidarUsuarioChangeListener mListener){

        this.mListener = mListener;

    }

    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnValidarUsuarioChangeListener {

        void validandoConductorOK();
        void validandoConductorTerceroOK();
        void validandoConductorError();
        void getListadoVehiculos();

    }


    public void validandoUsuario(){
        DatabaseReference ref = database.getReference("empresa/conductores/"+modelo.uid+"/");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {


                if(snap.exists()){
                    mListener.validandoConductorOK();
                }else{
                    mListener.validandoConductorError();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void validandoUsuarioTercero(){
        DatabaseReference ref = database.getReference("empresa/conductoresTerceros/"+modelo.uid+"/");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {

                boolean  data2 = snap.exists();

                if(!data2){
                    validandoUsuario();
                }else{

                    boolean ocupado = (boolean) snap.child("ocupado").getValue();
                    modelo.ocupado = ocupado;
                    mListener.validandoConductorTerceroOK();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getVehiculos(){
        modelo.tiposVehiculosTerceros.clear();
        DatabaseReference ref = database.getReference("empresa/tiposVehiculosTerceros/");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snap) {

                if (snap.getValue()== null){
                    return;
                }
                modelo.tiposVehiculosTerceros.clear();
                for (DataSnapshot listaVehiculos : snap.getChildren()) {
                    modelo.tiposVehiculosTerceros.add(listaVehiculos.getKey());
                }

                mListener.getListadoVehiculos();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Para evitar nullpointerExeptions
     */
    private static OnValidarUsuarioChangeListener sDummyCallbacks = new OnValidarUsuarioChangeListener()
    {
        @Override
        public void validandoConductorOK()
        {}

        @Override
        public void validandoConductorTerceroOK()
        {}

        @Override
        public void validandoConductorError()
        {}

        @Override
        public void getListadoVehiculos()
        {}



    };

}
