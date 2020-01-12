package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

/**
 * Created by tacto on 23/07/17.
 */

public class ComandoNotificaciones {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference referencia = database.getReference();


    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnNotificacionesChangeListener {

        void cargoNotificaciones();

    }


    //interface del listener de la actividad interesada
    private OnNotificacionesChangeListener mListener;

    public ComandoNotificaciones(OnNotificacionesChangeListener mListener){

        this.mListener = mListener;

    }

    public void notificacionVoyEnCamino(String arrayuid[]){
        final List nameList = new ArrayList<String>(Arrays.asList(arrayuid));

        DatabaseReference key = referencia.push();

        final DatabaseReference ref = database.getReference("mensajes/"+key.getKey()+"/" );//ruta path

                Map<String, Object> msmEnCamino = new HashMap<String, Object>();
                msmEnCamino.put("titulo", "¡Estado del Servicio!");
                msmEnCamino.put("mensaje", "Su servicio cambió de Estado, se encuentra En Camino");

                Map<String, Object> tokens = new HashMap<String, Object>();

                for (int i =0; i< nameList.size();i++) {
                    tokens.put(""+nameList.get(i),true);
                }
                msmEnCamino.put("tokens",tokens);

                //ref.updateChildren(updateConductor);
                //ref.setValue(msmEnCamino);

        ref.setValue(msmEnCamino, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                    mListener.cargoNotificaciones();
                    modelo.contadorNotificaciones = 1;
                    return;
                } else {
                   Log.v("error notificacion","error notificacion");
                }
            }
        });

    }

    public void enviaoDeMensajeAlPAsajero(String arrayuid[], final String titulo, final String descripcion){

        final List nameList = new ArrayList<String>(Arrays.asList(arrayuid));

        DatabaseReference key = referencia.push();

        final DatabaseReference ref = database.getReference("mensajes/"+key.getKey()+"/" );//ruta path


                Map<String, Object> enviarMsm = new HashMap<String, Object>();
                enviarMsm.put("titulo", ""+titulo);
                enviarMsm.put("mensaje", ""+descripcion);


                Map<String, Object> tokens = new HashMap<String, Object>();

                for (int i =0; i< nameList.size();i++) {
                    tokens.put(""+nameList.get(i),true);
                }
                enviarMsm.put("tokens",tokens);


        ref.setValue(enviarMsm, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    mListener.cargoNotificaciones();
                    return;
                } else {
                    Log.v("error notificacion","error notificacion");
                }
            }
        });
                //ref.setValue(enviarMsm);
               // mListener.cargoNotificaciones();


    }



    /**
     * Para evitar nullpointerExeptions
     */
    private static OnNotificacionesChangeListener sDummyCallbacks = new OnNotificacionesChangeListener()
    {
        @Override
        public void cargoNotificaciones()
        {}



    };
}
