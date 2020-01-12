package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.util.Log;
import android.widget.Toast;

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
 * Created by tactomotion on 16/09/16.
 */
public class ComandoEnviarMensaje {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference referencia = database.getReference();

    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnMensajeChangeListener {

        void cargoMensaje();
        void cargoMensajeNoti();

    }


    //interface del listener de la actividad interesada
    private OnMensajeChangeListener mListener;

    public ComandoEnviarMensaje(OnMensajeChangeListener mListener){

        this.mListener = mListener;

    }

    public  void enviarMensaje(final String idServicio, final String motivoMensaje){

        DatabaseReference key = referencia.push();

        final DatabaseReference ref = database.getReference("ordenes/pendientes/"+idServicio+"/mensajes/"+key.getKey()+"/" );//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {//addListenerForSingleValueEvent no queda escuchando peticiones
            @Override
            public void onDataChange(DataSnapshot snap) {

                //ref.setValue(motivoMensaje);
                //mListener.cargoCancelacion();

                if(idServicio != null && !idServicio.equals("") ){

                    Date date = new Date();
                    //Caso 1: obtener la hora y salida por pantalla con formato:
                    DateFormat hourFormat = new SimpleDateFormat("hh:mm a");
                    System.out.println("Hora: "+hourFormat.format(date));
//Caso 2: obtener la fecha y salida por pantalla con formato:
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    System.out.println("Fecha: "+dateFormat.format(date));
//Caso 3: obtenerhora y fecha y salida por pantalla con formato:
                    DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                    System.out.println("Hora y fecha: "+hourdateFormat.format(date));

                    Map<String, Object> enviarMensaje = new HashMap<String, Object>();
                    enviarMensaje.put("fecha", dateFormat.format(date));
                    enviarMensaje.put("hora", ""+ modelo.getHora(date));
                    enviarMensaje.put("texto", ""+motivoMensaje);
                    enviarMensaje.put("timestamp", ServerValue.TIMESTAMP);
                    //ref.updateChildren(updateConductor);
                    ref.setValue(enviarMensaje);
                    mListener.cargoMensaje();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void notificacionMensaje(String arrayuid[], final String titulo, final String descripcion){

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
                    mListener.cargoMensajeNoti();
                    return;
                } else {
                    Log.v("error notificacion","error notificacion");
                }
            }
        });


    }



    public  void enviarCiudad(String idServicio, final String ciudad, final String direccion){
        DatabaseReference key = referencia.push();

        final DatabaseReference referencia = database.getReference("ordenes/pendientes/"+idServicio+"/municipio/"+key.getKey()+"/");//ruta path
        referencia.addListenerForSingleValueEvent(new ValueEventListener() {//addListenerForSingleValueEvent no queda escuchando peticiones
            @Override
            public void onDataChange(DataSnapshot snap) {


                Map<String, Object> enviarCiudad = new HashMap<String, Object>();

                enviarCiudad.put("ciudad", ciudad);
                enviarCiudad.put("direccion",direccion);
                enviarCiudad.put("timestamp", ServerValue.TIMESTAMP);

                referencia.setValue(enviarCiudad);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Para evitar nullpointerExeptions
     */
    private static OnMensajeChangeListener sDummyCallbacks = new OnMensajeChangeListener()
    {
        @Override
        public void cargoMensaje()
        {}

        @Override
        public void cargoMensajeNoti()
        {}



    };



}
