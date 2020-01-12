package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

/**
 * Created by tactomotion on 30/08/16.
 */
public class ComandoListadoPasajeros {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    //interface del listener de la actividad interesada
    private OnComandoPasajerosChangeListener mListener;

    public ComandoListadoPasajeros(OnComandoPasajerosChangeListener mListener) {

        this.mListener = mListener;

    }

    public ComandoListadoPasajeros() {

    }

    //metodo que me trae los datos del pasajero
    public void getListadoPasajerosConductor(final String tipo,final String idPasejero, final String idOrden, final String solicitadoPor) {


        //DatabaseReference ref = database.getReference("usuarios/" + idPasejero);//ruta path


        DatabaseReference ref;
        if(tipo.equals("particular")){
            ref = database.getReference("usuarios/" + solicitadoPor+"/misPasajeros/"+idPasejero);//ruta path
        }else{
            ref = database.getReference("usuarios/" + idPasejero);//ruta path
        }
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {

                if (snap.getValue() == null) {

                    if(tipo.equals("particular")){

                        modelo.getOrden(idOrden).getPasajero(idPasejero).setNombre(snap.child("nombre").getValue().toString());
                        modelo.getOrden(idOrden).getPasajero(idPasejero).setCelular(snap.child("celular").getValue().toString());
                        modelo.getOrden(idOrden).getPasajero(idPasejero).setApellido(snap.child("apellido").getValue().toString());

                    }else{
                        return;
                    }

                }
                if (modelo.getOrdenes().size() > 0) {

                    if(modelo.getOrden(idOrden) == null  || (modelo.getOrden(idOrden).getPasajero(idPasejero)  == null)){
                        mListener.cargoPasajeroCero();
                        return;
                    }
                    //asignamos los datos de firebase a la clase
                    modelo.getOrden(idOrden).getPasajero(idPasejero).setNombre(snap.child("nombre").getValue().toString());
                    Log.i("PASA:", "Si hay  pasajeros asignados en comando: " + snap.child("nombre").getValue().toString() + modelo.getOrden(idOrden).getId());

                    modelo.getOrden(idOrden).getPasajero(idPasejero).setCelular(snap.child("celular").getValue().toString());
                    modelo.getOrden(idOrden).getPasajero(idPasejero).setApellido(snap.child("apellido").getValue().toString());
                    if (snap.child("tokenDevice").getValue() != null) {
                        modelo.getOrden(idOrden).getPasajero(idPasejero).setTokenDevice(snap.child("tokenDevice").getValue().toString());
                    }

                    mListener.cargoPasajero(snap.child("nombre").getValue().toString());
                } else {
                    mListener.cargoPasajeroCero();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //metodo que me trae los datos del pasajero
    public void getListadoPasajerosConductorHistorial(final String tipo,final String idPasejero, final String idOrden, final String solicitadoPor) {

        DatabaseReference ref;
        if(tipo.equals("particular")){
             ref = database.getReference("usuarios/" + solicitadoPor+"/misPasajeros/"+idPasejero);//ruta path
        }else{
             ref = database.getReference("usuarios/" + idPasejero);//ruta path
        }


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {

                if (snap.getValue() == null) {

                    if(tipo.equals("particular")){

                        modelo.getOrdenHistorial(idOrden).getPasajero(idPasejero).setNombre(snap.child("nombre").getValue().toString());
                        modelo.getOrdenHistorial(idOrden).getPasajero(idPasejero).setCelular(snap.child("celular").getValue().toString());
                        modelo.getOrdenHistorial(idOrden).getPasajero(idPasejero).setApellido(snap.child("apellido").getValue().toString());

                    }else{
                        return;
                    }

                }

                if (modelo.getOrdenHistorial(idOrden).pasajeros.size() > 0) {
                    //asignamos los datos de firebase a la clase
                    modelo.getOrdenHistorial(idOrden).getPasajero(idPasejero).setNombre(snap.child("nombre").getValue().toString());
                    modelo.getOrdenHistorial(idOrden).getPasajero(idPasejero).setCelular(snap.child("celular").getValue().toString());
                    modelo.getOrdenHistorial(idOrden).getPasajero(idPasejero).setApellido(snap.child("apellido").getValue().toString());


                    mListener.cargoPasajero(snap.child("nombre").getValue().toString());
                } else {
                    mListener.cargoPasajero(snap.child("nombre").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnComandoPasajerosChangeListener {

        void cargoPasajero(String nombre);//TODO: cambiar el nombre por cargo pasajero

        void cargoPasajeroCero();

    }

    /**
     * Para evitar nullpointerExeptions
     */
    private static OnComandoPasajerosChangeListener sDummyCallbacks = new OnComandoPasajerosChangeListener() {
        @Override
        public void cargoPasajero(String nombre) {
        }


        @Override
        public void cargoPasajeroCero() {
        }

    };
}
