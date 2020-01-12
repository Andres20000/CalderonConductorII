package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;

/**
 * Created by tacto on 5/10/17.
 */

public class ComandoActualizarOfertaTerceros {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    OrdenConductor ordenConductor ;

    int menorValor = 0;
    int newMmenorValor = 0;
    int cantidad = 1;
    String idMenorValor = "";


    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnActualizarOfertaTercerosChangeListener {

        void cargoActualizarOfertaTerceros(String idOrden);

        void resultadoAlAsignar(boolean exitoso);

        void cargoCotizar();


    }


    public interface OnIntentoDeCotizar {

        void cotizacionExitosa();

        void cotizacionFallida();
    }

    //interface del listener de la actividad interesada
    private OnActualizarOfertaTercerosChangeListener mListener;

    public ComandoActualizarOfertaTerceros(OnActualizarOfertaTercerosChangeListener mListener) {

        this.mListener = mListener;

    }


    public interface OnActulizarOrden {

        void exito();

        void fallo();
    }

    public void actualizarOrden(final String idServicio) {

        OrdenConductor orden = modelo.getOrden(idServicio);
        DatabaseReference ref = database.getReference("ordenes/pendientes/" + idServicio );


        orden.setEstado("Asignado");

        Map<String, Object> mapa = new HashMap<String, Object>();
        mapa.put("asignadoPor", "autoAsignado");
        mapa.put("matricula", modelo.placa);
        mapa.put("ofertadaATerceros", false);
        mapa.put("estado", "Asignado");
        if (modelo.params.hasRegistroInmediato){
            mapa.put("color",modelo.vehiculo.ccolor );
            mapa.put("referencia", modelo.vehiculo.referencia);
            mapa.put("marca", modelo.vehiculo.getMarca());
            mapa.put("matricula", modelo.vehiculo.getPlaca());
            mapa.put("estado", "Asignado");
        }

        ref.updateChildren(mapa);

        mListener.cargoActualizarOfertaTerceros(idServicio);


    }

    public void enviarCotizacion(final int opcion, final String idServicio, final int precio) {

/////////////////////////////


        final DatabaseReference ref = database.getReference("ordenes/pendientes/" + idServicio + "/cotizaciones/");//ruta path


        Map<String, Object> cotizacion = new HashMap<String, Object>();
        cotizacion.put("valor", precio);
        cotizacion.put("placa", modelo.placa);
        cotizacion.put("timestamp", ServerValue.TIMESTAMP);

        Map<String, Object> datosCotizacion = new HashMap<String, Object>();
        datosCotizacion.put("datosCotizaciones/" + modelo.uid, cotizacion);



        if (!idMenorValor.equals("")) {

            datosCotizacion.put("menorValorCotizado",idMenorValor);
        }

        ref.updateChildren(datosCotizacion);


}


    //transacciones cotizacion o subastas
    @SuppressLint("HandlerLeak")
    public void setCotizacion(final String idOrden, final String valorCotizado, final OnIntentoDeCotizar listener) {

        final int valCotizado = Integer.parseInt(valorCotizado);

        ordenConductor = modelo.getOrden(idOrden);


        DatabaseReference ref = database.getReference("ordenes/pendientes/" + idOrden + "/cotizaciones/contadorCotizaciones");

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Object o = mutableData.getValue();

                if (o == null) {
                    return Transaction.success(mutableData);
                }

                int numero = Integer.valueOf(o.toString());


                if (ordenConductor.cotizacionPorTiempo()) {
                    mutableData.setValue(numero + 1);
                    return Transaction.success(mutableData);
                }

                if (numero < ordenConductor.getNumeroOfertas()) {
                    mutableData.setValue(numero + 1);
                    return Transaction.success(mutableData);
                }

                return Transaction.abort();
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean commited, DataSnapshot snap) {

                if (commited) {
                    // mListener.resultadoAlAsignar(true);

                    //ingressamos cotizacio para menor valor

                    //DatabaseReference ref2 = database.getReference("ordenes/pendientes/" + idOrden + "/cotizaciones/menorValorCotizado/");

                    if (ordenConductor.cotizacionesT.size() > 0) {
                        if (ordenConductor.cotizacionesT.get(0).cotizaciones.size() > 0) {
                            if (valCotizado >= ordenConductor.cotizacionesT.get(0).getValorMinimo() && valCotizado <= ordenConductor.cotizacionesT.get(0).getValorMaximo()) {


                                String idConductorMenorValor = ordenConductor.cotizacionesT.get(0).getMenorValorCotizado();

                                if (idConductorMenorValor.equals("")) {
                                    if (valCotizado >= ordenConductor.cotizacionesT.get(0).getValorMinimo() && valCotizado <= ordenConductor.cotizacionesT.get(0).getValorMaximo()) {
                                        idMenorValor = modelo.uid;
                                        enviarCotizacion(1, idOrden, valCotizado);
                                        return;
                                    } else {
                                        enviarCotizacion(0, idOrden, valCotizado);
                                        return;
                                    }
                                } else {
                                    for (int i = 0; i < ordenConductor.cotizacionesT.get(0).cotizaciones.size(); i++) {

                                        if (ordenConductor.cotizacionesT.get(0).cotizaciones.get(i).getId().equals(idConductorMenorValor)) {
                                            menorValor = ordenConductor.cotizacionesT.get(0).cotizaciones.get(i).getValor();
                                            idMenorValor = ordenConductor.cotizacionesT.get(0).cotizaciones.get(i).getId();
                                            break;
                                        }


                                    }


                                    if (valCotizado < menorValor) {
                                        newMmenorValor = valCotizado;

                                        /// transaccion

                                        DatabaseReference ref = database.getReference("ordenes/pendientes/" + idOrden + "/cotizaciones/menorValorCotizado");


                                        ref.runTransaction(new Transaction.Handler() {
                                            @Override
                                            public Transaction.Result doTransaction(MutableData mutableData) {
                                                Object o = mutableData.getValue();

                                                if (o == null) {
                                                    return Transaction.success(mutableData);
                                                }

                                                String numero = o.toString();

                                                mutableData.setValue(modelo.uid);


                                                return Transaction.success(mutableData);
                                            }

                                            @Override
                                            public void onComplete(DatabaseError databaseError, boolean commited, DataSnapshot snap) {
                                                if (commited) {
                                                    idMenorValor = modelo.uid;
                                                    enviarCotizacion(1, idOrden, newMmenorValor);

                                                }

                                            }
                                        });

                                    } else {

                                        enviarCotizacion(0, idOrden, valCotizado);
                                    }
                                }


                            } else {
                                enviarCotizacion(0, idOrden, valCotizado);
                            }
                        } else {
                            if (valCotizado >= ordenConductor.cotizacionesT.get(0).getValorMinimo() && valCotizado <= ordenConductor.cotizacionesT.get(0).getValorMaximo()) {

                                idMenorValor = modelo.uid;
                                enviarCotizacion(1, idOrden, valCotizado);

                            } else {
                                enviarCotizacion(0, idOrden, valCotizado);
                            }
                        }
                    } else {

                        if (valCotizado >= ordenConductor.cotizacionesT.get(0).getValorMinimo() && valCotizado <= ordenConductor.cotizacionesT.get(0).getValorMaximo()) {

                            idMenorValor = modelo.uid;
                            enviarCotizacion(1, idOrden, valCotizado);

                        } else {
                            enviarCotizacion(0, idOrden, valCotizado);
                        }

                    }

                    listener.cotizacionExitosa();
                } else {

                    listener.cotizacionFallida();
                }

            }
        });


    }


    /**
     * Para evitar nullpointerExeptions
     */
    private static OnActualizarOfertaTercerosChangeListener sDummyCallbacks = new OnActualizarOfertaTercerosChangeListener() {
        @Override
        public void cargoActualizarOfertaTerceros(String id) {
        }

        @Override
        public void resultadoAlAsignar(boolean exitoso) {
        }

        @Override
        public void cargoCotizar() {
        }


    };
}
