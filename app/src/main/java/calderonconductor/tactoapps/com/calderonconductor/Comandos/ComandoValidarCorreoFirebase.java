package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.FirebaseDatabase;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

/**
 * Created by tacto on 4/10/17.
 */

public class ComandoValidarCorreoFirebase {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    //interface del listener de la actividad interesada
    private OnValidarCorreoFirebaseChangeListener mListener;

    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnValidarCorreoFirebaseChangeListener {

        void cargoValidarCorreoFirebase();
        void cargoValidarCorreoFirebaseEroor();

    }

    public ComandoValidarCorreoFirebase(OnValidarCorreoFirebaseChangeListener mListener){

        this.mListener = mListener;

    }

    public  ComandoValidarCorreoFirebase(){

    }

    public void checkAccountEmailExistInFirebase(String email) {
        String  correo = email;
        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                if (task.getResult().getProviders().size() > 0) {
                    mListener.cargoValidarCorreoFirebaseEroor();
                }else{
                    mListener.cargoValidarCorreoFirebase();
                }
            }
        });
    }


    /**
     * Para evitar nullpointerExeptions
     */
    private static OnValidarCorreoFirebaseChangeListener sDummyCallbacks = new OnValidarCorreoFirebaseChangeListener()
    {
        @Override
        public void cargoValidarCorreoFirebase()
        {}

        @Override
        public void cargoValidarCorreoFirebaseEroor()
        {}



    };
}
