package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Cliente;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;


/**
 * Created by tacto on 6/07/17.
 */

public class ComandoCompartirUbicacion {


    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference referencia = database.getReference();


    //interface del listener de la actividad interesada
    private OnCompartirUbicacionChangeListener mListener;

    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnCompartirUbicacionChangeListener {

        void cargoUbicacion();

        void actualizoUbicacion();

    }

    public ComandoCompartirUbicacion(OnCompartirUbicacionChangeListener mListener) {

        this.mListener = mListener;

    }


    public void enviarUbicacion(final double latitud, final double longitud, String idServicio) {

        DatabaseReference key = referencia.push();

        final DatabaseReference ref = database.getReference("ordenes/pendientes/" + idServicio + "/ubicacion/" + key.getKey() + "/");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {//addListenerForSingleValueEvent no queda escuchando peticiones
            @Override
            public void onDataChange(DataSnapshot snap) {

                Map<String, Object> enviarUbicacion = new HashMap<String, Object>();
                enviarUbicacion.put("lat", latitud);
                enviarUbicacion.put("lon", longitud);
                enviarUbicacion.put("pasajero", "" + modelo.uid);
                enviarUbicacion.put("timestamp", ServerValue.TIMESTAMP);

                ref.setValue(enviarUbicacion);
                mListener.cargoUbicacion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void actualizarUbicacionFinal(final double latitud, final double longitud, final String idServicio, final long distanciaRecorrida) {


        DatabaseReference ref = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/lat");//ruta path
        ref.setValue(latitud);
        DatabaseReference ref2 = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/lon");//ruta path
        ref2.setValue(longitud);


        DatabaseReference ref3 = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/distanciaRecorrida");//ruta path
        ref3.setValue(distanciaRecorrida);

        modelo.distanciasRecorrida = "" + distanciaRecorrida;


        mListener.actualizoUbicacion();

    }


    public void actualizarUbicacion(final double latitud, final double longitud, final String idServicio, final long distanciaRecorrida) {

        DatabaseReference ref = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/distanciaRecorrida");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long acumulador = 0;
                if (!snapshot.exists() || snapshot.getValue() == null) {
                    acumulador = distanciaRecorrida;
                } else {
                    acumulador = (long) snapshot.getValue() + distanciaRecorrida;
                }


                DatabaseReference ref = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/lat");//ruta path
                ref.setValue(latitud);
                DatabaseReference ref2 = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/lon");//ruta path
                ref2.setValue(longitud);


                DatabaseReference ref3 = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/distanciaRecorrida");//ruta path
                ref3.setValue(acumulador);

                modelo.distanciasRecorrida = "" + acumulador;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mListener.actualizoUbicacion();

    }


    public void actualizarUbicacion1(final double latitud, final double longitud, String idServicio) {


        final DatabaseReference ref = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/" );//ruta path
        Map<String, Object> enviarGps = new HashMap<String, Object>();
        enviarGps.put("lat", latitud);
        enviarGps.put("lon", longitud);
        ref.updateChildren(enviarGps, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.v("okgps","ok gps");
                    return;
                } else {
                    Log.v("error gps","error gps");
                }
            }
        });

    }

    public void actualizarTimeStampInicial(String idServicio) {


        final DatabaseReference ref = database.getReference("ordenes/pendientes/" + idServicio + "/envioPuntoRecogida");
        ref.setValue(true);

        final DatabaseReference ref2 = database.getReference("ordenes/pendientes/" + idServicio + "/fechaPuntoRecogida");
        ref2.setValue(modelo.getFechaHora());


        DatabaseReference ref3 = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/timestampInicio");//ruta path
        ref3.setValue(ServerValue.TIMESTAMP);

    }

    public void actualizarTimeStampFinal(final double latitud, final double longitud, String idServicio) {

        DatabaseReference ref = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/timestampFin");//ruta path
        ref.setValue(ServerValue.TIMESTAMP);

    }


    public void datosRecorreido(final String idServicio, final Cliente empresa, final OrdenConductor orden) {

        DatabaseReference ref = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {


                Long distanciaR = (Long) snap.child("distanciaRecorrida").getValue();
                Long timeI = (Long) snap.child("timestampInicio").getValue();
                Long timeF = (Long) snap.child("timestampFin").getValue();


                if (timeI == null || timeF == null) {
                    return;
                }


                if (distanciaR == null) {
                    distanciaR  = new Long(0);
                }
                long restaTime = timeF - timeI;
                long divTime = restaTime / 1000;
                long minRecorrido = divTime / 60;


                double klRecorridos = distanciaR/1000.0;

                String decimalR = String.format("%.2f", klRecorridos);

                String repazar = decimalR.replaceAll(",",".");
                System.out.println(decimalR);
                double klR = Double.parseDouble(repazar);


                DatabaseReference ref3 = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/tiempoRecorrido");//ruta path
                ref3.setValue(minRecorrido);

                DatabaseReference ref4 = database.getReference("ordenes/historial/" + idServicio + "/tiempoRecorrido/");//ruta path
                ref4.setValue(minRecorrido);

                DatabaseReference ref5 = database.getReference("ordenes/historial/" + idServicio + "/distanciaRecorrida/");//ruta path
                ref5.setValue(klR);

             /*   DatabaseReference ref6 = database.getReference("ordenes/historial/" + idServicio + "/precioHora/");//ruta path
                ref6.setValue(orden.precioHora);

                DatabaseReference ref7 = database.getReference("ordenes/historial/" + idServicio + "/precioKm/");//ruta path
                ref7.setValue(orden.precioKm);

                DatabaseReference ref10 = database.getReference("ordenes/historial/" + idServicio + "/precioHoraOrden/");//ruta path
                ref6.setValue(orden.precioHoraOrden);

                DatabaseReference ref11 = database.getReference("ordenes/historial/" + idServicio + "/precioKmOrden/");//ruta path
                ref7.setValue(orden.precioKmOrden);*/

                DatabaseReference ref8 = database.getReference("ordenes/historial/" + idServicio + "/tarifaMinima/");//ruta path
                ref8.setValue(empresa.tarifaMinima);

                DatabaseReference ref9 = database.getReference("ordenes/historial/" + idServicio + "/tarifaTaximetro/");//ruta path
                ref9.setValue("" + calcularTarifa(empresa, minRecorrido, distanciaR, orden));


                modelo.eliminarOrden(idServicio);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static int calcularTarifa(Cliente empresa, long minutos, long metros, OrdenConductor orden) {

        int valorXDistancia = (int) ((double)metros * (orden.precioKmOrden / 1000.0));
        int valorXTiempo = (int) ((double) minutos * (orden.precioHoraOrden / 60.0));

        return Math.max(valorXDistancia, Math.max(valorXTiempo, empresa.tarifaMinima));

    }


    /**
     * Para evitar nullpointerExeptions
     */
    private static OnCompartirUbicacionChangeListener sDummyCallbacks = new OnCompartirUbicacionChangeListener() {
        @Override
        public void cargoUbicacion() {
        }


        @Override
        public void actualizoUbicacion() {
        }


    };

}
