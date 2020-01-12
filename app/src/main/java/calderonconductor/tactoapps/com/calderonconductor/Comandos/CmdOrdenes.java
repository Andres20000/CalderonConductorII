package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Municipio;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Pasajero;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Retraso;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Ubicacion;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdPasajero.OnGetPasajero;

public class CmdOrdenes {



    Modelo modelo;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnOrdenesListener {

        void nueva();
        void modificada(String idServicio);
        void eliminada();

    }


    public interface OnOrdenesDescargaListener {

        void termino();

    }


    public interface OnCheckEstado {

        void finalizado();
        void trasnportando();

    }



    public static void checkTodasLasOrdenes(final OnOrdenesDescargaListener listener) {

        final Modelo modelo;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        modelo = Modelo.getInstance();

        DatabaseReference ref = database.getReference("ordenes/pendientes/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.termino();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    public static void getTodasLasOrdenes(final OnOrdenesListener listener) {

        final Modelo modelo;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        modelo = Modelo.getInstance();

        DatabaseReference ref = database.getReference("ordenes/pendientes/");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snap, String s) {
                boolean data2 = snap.exists();

                if (data2 == false) {
                } else {
                   final OrdenConductor nuevaOrden = readDatosOrdennesConductor(snap);

                   if (nuevaOrden == null) {
                       return;
                   }

                   if (modelo.params.hasRegistroInmediato) {
                       CmdPasajero.getPasajero( nuevaOrden, new OnGetPasajero() {
                           @Override
                           public void cargo(Pasajero pasajero) {
                               modelo.adicionarNuevaOrden(nuevaOrden);
                               listener.nueva();
                           }
                       });

                   }else {
                       modelo.adicionarNuevaOrden(nuevaOrden);
                       listener.nueva();
                   }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot snap, String s) {

                final OrdenConductor nuevaOrden = readDatosOrdennesConductor(snap);

                if (nuevaOrden == null) {
                    return;
                }

                if (modelo.params.hasRegistroInmediato) {
                    if (nuevaOrden.getEstado().equals("NoAtendido") || nuevaOrden.esAsignadaDeOtro(modelo.uid)){
                        modelo.eliminarOrden(nuevaOrden.getId());
                        listener.eliminada();
                        return;
                    }
                    CmdPasajero.getPasajero(nuevaOrden, new OnGetPasajero() {
                        @Override
                        public void cargo(Pasajero pasajero) {
                            modelo.adicionarNuevaOrden(nuevaOrden);
                            listener.modificada(nuevaOrden.getId());
                        }
                    });
                }else {
                    modelo.adicionarNuevaOrden(nuevaOrden);
                    listener.modificada(nuevaOrden.getId());
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot snap) {
                OrdenConductor nuevaOrden = readDatosOrdennesConductor(snap);

                if (nuevaOrden == null) {
                    return;
                }

                modelo.eliminarOrden(nuevaOrden.getId());
                listener.eliminada();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v("dataSnapshot",dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("dataSnapshot",databaseError.getMessage());
            }
        });


    }


    public interface OnCheckCancelanda {

        void cancelado(String idServicio);

    }

    public static void escucharSiCancelanOrden(final String idServicio, final OnCheckCancelanda listener) {

        final Modelo modelo;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        modelo = Modelo.getInstance();

        DatabaseReference ref = database.getReference("ordenes/pendientes/" + idServicio + "/estado");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                if (snap.getValue() != null && snap.getValue().toString().equals("Cancelado")) {
                    listener.cancelado(idServicio);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }





    private static OrdenConductor readDatosOrdennesConductor(DataSnapshot snap) {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");


        OrdenConductor nuevaOrden = new OrdenConductor(snap.getKey());

        if (!snap.hasChild("origen")) {

            return null;
        }

        nuevaOrden.setOrigen(snap.child("origen").getValue().toString());
        nuevaOrden.setEstado(snap.child("estado").getValue().toString());
        nuevaOrden.setDestino(snap.child("destino").getValue().toString());

        if (snap.hasChild("precioHora")) {
            nuevaOrden.precioHora = Integer.parseInt(snap.child("precioHora").getValue().toString());
        }else {
            nuevaOrden.precioHora = 40000;
        }
        if (snap.hasChild("precioKm")) {
            nuevaOrden.precioKm = Integer.parseInt(snap.child("precioKm").getValue().toString());
        }else {
            nuevaOrden.precioKm = 2600;
        }


        if (snap.hasChild("precioHoraOrden")) {
            nuevaOrden.precioHoraOrden = Integer.parseInt(snap.child("precioHoraOrden").getValue().toString());
        }else {
            nuevaOrden.precioHoraOrden = 40000;
        }
        if (snap.hasChild("precioKmOrden")) {
            nuevaOrden.precioKmOrden = Integer.parseInt(snap.child("precioKmOrden").getValue().toString());
        }else {
            nuevaOrden.precioKmOrden = 2600;
        }


        if (snap.hasChild("ofertadaATerceros")){
            nuevaOrden.setOfertadaATerceros((boolean)snap.child("ofertadaATerceros").getValue());
        }


        if (snap.hasChild("envioPuntoRecogida")){
            nuevaOrden.envioPuntoRecogida =  (boolean)snap.child("envioPuntoRecogida").getValue();
        }



        if (snap.child("destino").getValue().toString().equals("ABIERTO")) {
            nuevaOrden.setDiasServicio(snap.child("diasServicio").getValue().toString());
            nuevaOrden.setHorasServicio(snap.child("horasServicio").getValue().toString());
        }


        nuevaOrden.setHora(snap.child("horaEnOrigen").getValue().toString());
        if (snap.hasChild("observaciones")) {
            nuevaOrden.setObservaciones(snap.child("observaciones").getValue().toString());
        }
        nuevaOrden.setDireccionDestino(snap.child("direccionDestino").getValue().toString());
        nuevaOrden.setFechaEnOrigenEs(snap.child("fechaEnOrigen").getValue().toString());
        try {
            Date f = df.parse(snap.child("fechaEnOrigen").getValue().toString());
            nuevaOrden.setFechaEnOrigen(f);

        } catch (Exception e) {
            System.out.println("Error en el formato de la fecha");
        }

        nuevaOrden.setTiempoRestante(snap.child("fechaGeneracion").getValue().toString());

        if (snap.child("asignadoPor").getValue() != null) {
            nuevaOrden.setAsignadoPor(snap.child("asignadoPor").getValue().toString());
        }

        if (snap.child("conductor").getValue() != null) {
            nuevaOrden.setConductor(snap.child("conductor").getValue().toString());
        }

        if (snap.child("servicioInmediato").getValue() != null) {
            nuevaOrden.servicioInmediato = (boolean)snap.child("servicioInmediato").getValue();
        }else {
            nuevaOrden.servicioInmediato = false;
        }


        if (snap.hasChild("cosecutivoOrden")) {
            nuevaOrden.setCosecutivoOrden(snap.child("cosecutivoOrden").getValue().toString());
        } else {
            if (snap.hasChild("consecutivoOrden")) {
                nuevaOrden.setCosecutivoOrden(snap.child("consecutivoOrden").getValue().toString());
            } else {
                nuevaOrden.setCosecutivoOrden("");
            }
        }
        nuevaOrden.setDireccionOrigen(snap.child("direccionOrigen").getValue().toString());
        //nuevaOrden.setFechaEnDestino(snap.child("fechaEnDestino").getValue().toString());
        //nuevaOrden.setHoraEnDestino(snap.child("horaEnDestino").getValue().toString());
        nuevaOrden.setHoraGeneracion(snap.child("horaGeneracion").getValue().toString());
        nuevaOrden.setIdCliente(snap.child("idCliente").getValue().toString());

        if (snap.child("matricula").getValue() != null) {
            nuevaOrden.setMatricula(snap.child("matricula").getValue().toString());
        }



        if (snap.child("conSonidoDireccion").getValue() != null) {

            nuevaOrden.conSonidoDireccion = (boolean)snap.child("conSonidoDireccion").getValue();

        }


        nuevaOrden.setRuta(snap.child("ruta").getValue().toString());
        nuevaOrden.setSolicitadoPor(snap.child("solicitadoPor").getValue().toString());
        if (snap.child("tarifa").getValue() != null) {
            nuevaOrden.setTarifa(snap.child("tarifa").getValue().toString());
        }
        Long l = (Long) snap.child("timestamp").getValue();
        nuevaOrden.setTimeStamp(l);
        // Long longTimestamp = Long.valueOf(nuevaOrden.setTimeStamp(snap.child("timestamp").getValue().toString()));
        // nuevaOrden.setTrayectos(snap.child("trayectos").getValue().toString());
        nuevaOrden.setId(snap.getKey());

        //nuevo arbol
        DataSnapshot snapPasajeros;
        snapPasajeros = (DataSnapshot) snap.child("pasajeros");
        for (DataSnapshot pasajero : snapPasajeros.getChildren()) {
            Pasajero newPasajero = new Pasajero();
            newPasajero.setIdPasajero(pasajero.getKey());
            newPasajero.setTipo(pasajero.getValue().toString());
            nuevaOrden.pasajeros.add(newPasajero);//
        }



        DataSnapshot snapRetrasos = (DataSnapshot) snap.child("retrasos");

        for (DataSnapshot retraso : snapRetrasos.getChildren()) {
            Retraso newRetraso = new Retraso();
            newRetraso.id = retraso.getKey();
            newRetraso.fechaInicio = Utility.convertStringConHoraToDate(retraso.child("inicio").getValue().toString());
            if (retraso.hasChild("fin")) {
                newRetraso.fechaFin = Utility.convertStringConHoraToDate(retraso.child("fin").getValue().toString());
            }
            newRetraso.startTime = (long) retraso.child("startTime").getValue();
            if (retraso.hasChild("motivo")) {
                newRetraso.comentario = retraso.child("motivo").getValue().toString();
            }
            nuevaOrden.retrasos.add(newRetraso);
        }


//municipio

        DataSnapshot snapMunicipios;
        snapMunicipios = (DataSnapshot) snap.child("municipio");//municipios
        for (DataSnapshot municipio : snapMunicipios.getChildren()) {
            Municipio newMunicipio = new Municipio();
            newMunicipio.setId(municipio.getKey());
            newMunicipio.setMunicipio(municipio.child("ciudad").getValue().toString());
            newMunicipio.setDirecion(municipio.child("direccion").getValue().toString());
            nuevaOrden.municipios.add(newMunicipio);//  TODO: validacion de evitar duplicados
        }


        //nuevo arbol snapUbicacion
        DataSnapshot snapUbicacion;
        snapUbicacion = (DataSnapshot) snap.child("ubicacion");//ubicacion
        nuevaOrden.ubicacionGPss.clear();
        for (DataSnapshot gps : snapUbicacion.getChildren()) {
            Ubicacion newUbicacion = new Ubicacion();
            newUbicacion.setPathUbicacion(gps.getKey());
            double latd = (double) gps.child("lat").getValue();
            newUbicacion.setLat(latd);
            double lon = (double) gps.child("lon").getValue();
            newUbicacion.setLon(lon);
            Long lu = (Long) gps.child("timestamp").getValue();
            newUbicacion.setTimestamp(lu);
            newUbicacion.setPasajero(gps.child("pasajero").getValue().toString());

            nuevaOrden.ubicacionGPss.add(0, newUbicacion);
        }


        //nuevo arbol snapUbicacionOrigen
        DataSnapshot snapUbicacionOrigen;
        snapUbicacionOrigen = (DataSnapshot) snap.child("ubicacionOrigen");//ubicacion
        //nuevaOrden.ubicacionGPss.clear();


        if (snapUbicacionOrigen.exists()) {
            Ubicacion newUbicacionO = new Ubicacion();
            double latdO = (double) snapUbicacionOrigen.child("lat").getValue();
            newUbicacionO.setLatOrigen(latdO);
            double lonO = (double) snapUbicacionOrigen.child("lon").getValue();
            newUbicacionO.setLonOrigen(lonO);
            nuevaOrden.ubicacionGPss.add(0, newUbicacionO);

            Ubicacion ubiOrigen = new Ubicacion();
            ubiOrigen.setLat(latdO);
            ubiOrigen.setLon(lonO);

            nuevaOrden.ubiOrigen = ubiOrigen;

        }


        //nuevo arbol snapUbicacionDestino
        DataSnapshot snapUbicacionDestino;
        snapUbicacionDestino = (DataSnapshot) snap.child("ubicacionDestino");//ubicacion

        if (snapUbicacionDestino.exists()) {
            Ubicacion newUbicacionD = new Ubicacion();

            String valor = ""+snapUbicacionDestino.child("lat").getValue();

            double latdes = 0.0;
            double lonDes = 0.0;
            if (!snapUbicacionDestino.child("lat").getValue().toString().equals("0")){
                latdes = (double) snapUbicacionDestino.child("lat").getValue();
            }
            //double latdes = (double) snapUbicacionDestino.child("lat").getValue();

            if (!snapUbicacionDestino.child("lon").getValue().toString().equals("0")){
                lonDes = (double) snapUbicacionDestino.child("lon").getValue();
            }
            newUbicacionD.setLatDestino(latdes);
            //double lonDes = (double) snapUbicacionDestino.child("lon").getValue();
            newUbicacionD.setLonDestino(lonDes);
            nuevaOrden.ubicacionGPss.add(0, newUbicacionD);
        }


        return nuevaOrden;

    }



    public static void checkEstadoOrden(final String idServicio, final OnCheckEstado listener) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("ordenes/pendientes/" + idServicio + "/estado");//ruta path

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {

                if (snap.getValue() == null) {
                    return;
                }
                if (snap.getValue().toString().equals("Finalizado") ||
                        snap.getValue().toString().equals("Cancelado")) {
                    listener.finalizado();
                }

                if (snap.getValue().toString().equals("Transportando")) {
                    listener.trasnportando();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public interface OnCheckDistancia {

        void avance(int acumulado);

    }





    public static void checkDistancia(final String idServicio, final OnCheckDistancia listener) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("ubicacionOrdenes/" + idServicio + "/ubicacionConductor/distanciaRecorrida" );

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {

                if (snap.getValue() == null) {
                    return;
                }
                long l = (Long)snap.getValue();
                listener.avance((int)l);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public interface OnSetEstado {

        void cambio(String nuevoEstado);
        void fallo(String nuevoEstado);

    }

    public static void actualizarEstado(String estado, String idOrden, final OnSetEstado listener) {


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("ordenes/pendientes/" + idOrden );//ruta path


        final String nuevaEstado = getNuevoEstado(estado);

        Map<String, Object> mapa = new HashMap<String, Object>();
        mapa.put(getFechaNombreEstado(estado), Modelo.getFechaHora());
        mapa.put("estado", nuevaEstado);

        ref.updateChildren(mapa, new CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    listener.fallo(nuevaEstado);
                } else {
                    listener.cambio(nuevaEstado);
                }
            }
        });





    }


    private static String getNuevoEstado(String estado){

        if (estado.equals("No Asignado") || estado.equals("NoAsignado")) {
            return "Asignado";

        }
        if (estado.equals("Asignado")) {
            return "En Camino";
        }
        if (estado.equals("En Camino") || estado.equals("EnCamino")) {
            return "Transportando";

        }
        if (estado.equals("Transportando")) {
            return "Finalizado";
        }

        if (estado.equals("Finalizado")) {
            return "Finalizado";
        }

        return "";

    }

    private static String getFechaNombreEstado(String estado){

        if (estado.equals("No Asignado") || estado.equals("NoAsignado")) {
            return "fechaEstadoAsignado";

        }
        if (estado.equals("Asignado")) {
            return "fechaEstadoEnCamino";
        }
        if (estado.equals("En Camino") || estado.equals("EnCamino")) {
            return "fechaEstadoEnTransportando";

        }
        if (estado.equals("Transportando")) {
            return "fechaEstadoEnfinalizado";
        }

        return "fechaEstadoEnfinalizado";

    }




}