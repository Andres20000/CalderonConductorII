package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Cliente;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

/**
 * Created by tactomotion on 1/09/16.
 */
public class ComandoCliente {


    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    //interface del listener de la actividad interesada
    private OnClienteChangeListener mListener;

    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnClienteChangeListener {

        void cargoCliente();

    }

    public ComandoCliente(OnClienteChangeListener mListener) {

        this.mListener = mListener;

    }

    public ComandoCliente() {

    }


    //metodo que me trae la razon social
    //se guarda en clase modelo
    public void getRazonSocialCliente(final String idCliente, final String idServicio) {

        DatabaseReference ref = database.getReference("clientes/" + idCliente);//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                int t = modelo.getOrdenes().size();
                Log.v("idServicio", "idServicio" + idServicio);
                String nombreEmpres = snap.child("razonSocial").getValue().toString();
                Cliente cliente = modelo.getOrden(idServicio).getCliente();
                cliente.setRazonSocial(nombreEmpres);


                if(snap.child("precioHora").exists()){
                    cliente.precioHora = Integer.parseInt(snap.child("precioHora").getValue().toString());
                }
                else{
                    cliente.precioHora = 25000;
                }

                if(snap.child("precioKm").exists()){
                    cliente.precioKm = Integer.parseInt(snap.child("precioKm").getValue().toString());
                }else{
                    cliente.precioKm = 1000;
                }

                if(snap.child("tarifaMinima").exists()){
                    cliente.tarifaMinima = ((Long)snap.child("tarifaMinima").getValue()).intValue();
                }else{
                    cliente.tarifaMinima = 8000;
                }




                modelo.cliente = cliente;
                mListener.cargoCliente();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("Error :X",""+databaseError.getMessage());

            }
        });

    }


    //metodo que me trae la razon social
    //se guarda en clase modelo
    public void getRazonSocialClienteHistorial(String idCliente, final String idServicio) {

        DatabaseReference ref = database.getReference("clientes/" + idCliente);//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                Cliente cliente = new Cliente();
                modelo.getOrdenHistorial(idServicio).getCliente().setRazonSocial(snap.child("razonSocial").getValue().toString());
                mListener.cargoCliente();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    /**
     * Para evitar nullpointerExeptions
     */
    private static OnClienteChangeListener sDummyCallbacks = new OnClienteChangeListener() {
        @Override
        public void cargoCliente() {
        }


    };
}
