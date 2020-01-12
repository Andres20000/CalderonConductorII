package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Municipio;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Pasajero;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Retraso;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Ubicacion;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;

/**
 * Created by tactomotion on 2/08/16.
 */
public class ComandoOrdenesConductor {
    Modelo modelo;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    //interface del listener de la actividad interesada
    //private OnOrdenesConductorChangeListener mListener;



    public ComandoOrdenesConductor() {
        // modelo.getOrdenes();

    }

    public interface OnEscucharEstados {
        void cambio();
        void cambioError();
    }


    public interface OnFinalizarOrden {
        void finalizo(OrdenConductor orden);
        void fallo(OrdenConductor orden);
    }


    public static void escucharEstados(final OnEscucharEstados listener) {

        final Modelo modelo = Modelo.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("ordenes/pendientes/");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {

                for (DataSnapshot pues : snap.getChildren()) {

                    OrdenConductor nuevaOrden = new OrdenConductor(pues.getKey());

                    if(pues.child("estado").exists()){

                        String estadoOrden = pues.child("estado").getValue().toString();
                        if(estadoOrden.equals("Asignado") ){
                            String conductor = pues.child("conductor").getValue().toString();
                            if(!conductor.equals(modelo.uid)){
                                modelo.eliminarOrden(pues.getKey());

                            }
                        }

                    }

                    listener.cambio();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static void escucharEstadosOrden(final String idOrden,final OnEscucharEstados listener) {

        final Modelo modelo = Modelo.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("ordenes/pendientes/");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {

                for (DataSnapshot pues : snap.getChildren()) {

                    OrdenConductor nuevaOrden = new OrdenConductor(pues.getKey());

                    if(pues.child("estado").exists()){

                        if(idOrden.equals(pues.getKey())){
                            String estadoOrden = pues.child("estado").getValue().toString();
                            if(estadoOrden.equals("Asignado") ){
                                String conductor = pues.child("conductor").getValue().toString();
                                if(!conductor.equals(modelo.uid)){
                                    modelo.eliminarOrden(pues.getKey());
                                    listener.cambioError();
                                }
                                else {
                                    listener.cambio();
                                }
                            }
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void getTodasLasOrdenes() {
        modelo = Modelo.getInstance();

        DatabaseReference ref = database.getReference("ordenes/pendientes/");
        Query query = ref.orderByChild("conductor").equalTo(modelo.uid);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snap, String s) {
                boolean data2 = snap.exists();

                if (data2 == false) {
                    //mListener.cargoUnaOrdenesConductor();
                    //getOrdenesCotizar();
                } else {

                    OrdenConductor nuevaOrden = readDatosOrdennesConductor(snap);
                    modelo.adicionarNuevaOrden(nuevaOrden);
                    //mListener.cargoUnaOrdenesConductor();
                    //getOrdenesCotizar();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot snap, String s) {
//             // metodo que escucha los cambios en la bd
                OrdenConductor nuevaOrden = readDatosOrdennesConductor(snap);
                if (nuevaOrden.getConductor() != null) {
                    if (nuevaOrden.getConductor().equals(modelo.uid)) {
                        modelo.adicionarNuevaOrden(nuevaOrden);
                    } else {
                        modelo.eliminarOrden(nuevaOrden.getId());
                    }
                }

               // mListener.cargoUnaOrdenesConductor();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Log.v("dataSnapshot",dataSnapshot.getKey());
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
        //mListener.cargoUnaOrdenesConductor();
    }


    public OrdenConductor readDatosOrdennesConductor(DataSnapshot snap) {


        OrdenConductor nuevaOrden = new OrdenConductor(snap.getKey());
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
            nuevaOrden.pasajeros.add(newPasajero);//  TODO: validacion de evitar duplicados
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


    public void getOrdenesCotizar() {

        modelo = Modelo.getInstance();
        DatabaseReference ref = database.getReference("ordenes/pendientes/");


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snap, String s) {
                boolean data2 = snap.exists();

                if (data2 == false) {
                    //mListener.cargoUnaOrdenesConductor();
                } else {
                    if (!modelo.estadoConductorPendiente.equals("Activo")) {
                        //mListener.cargoUnaOrdenesConductor();
                    } else {

                        if (!snap.child("estado").getValue().toString().equals("Cotizar")) {
                            //mListener.cargoUnaOrdenesConductor();
                        } else {


                            OrdenConductor nuevaOrden = readDatosOrdennesConductor(snap);
                            modelo.adicionarNuevaOrden(nuevaOrden);
                           // mListener.cargoUnaOrdenesConductor();


                        }

                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot snap, String s) {

                OrdenConductor nuevaOrden = readDatosOrdennesConductor(snap);
                modelo.adicionarNuevaOrden(nuevaOrden);
                //mListener.cargoUnaOrdenesConductor();
            }

            @Override
            public void onChildRemoved(DataSnapshot snap) {

            }

            @Override
            public void onChildMoved(DataSnapshot snap, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void getOrdenesConductor() {
        modelo = Modelo.getInstance();
        // modelo.getOrdenes().clear();
        DatabaseReference ref = database.getReference("ordenes/pendientes/");//ruta path
        //se crea un query filtrado por el id del conductor
        Query query = ref.orderByChild("conductor").equalTo(modelo.uid);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snap, String s) {

                OrdenConductor nuevaOrden = new OrdenConductor(snap.getKey());
                nuevaOrden.setOrigen(snap.child("origen").getValue().toString());
                nuevaOrden.setEstado(snap.child("estado").getValue().toString());
                nuevaOrden.setDestino(snap.child("destino").getValue().toString());

                nuevaOrden.precioHora = Integer.parseInt(snap.child("precioHora").getValue().toString());
                nuevaOrden.precioKm = Integer.parseInt(snap.child("precioKm").getValue().toString());
                nuevaOrden.precioHoraOrden = Integer.parseInt(snap.child("precioHoraOrden").getValue().toString());
                nuevaOrden.precioKmOrden = Integer.parseInt(snap.child("precioKmOrden").getValue().toString());

                if (snap.child("destino").getValue().toString().equals("ABIERTO")) {
                    nuevaOrden.setDiasServicio(snap.child("diasServicio").getValue().toString());
                    nuevaOrden.setHorasServicio(snap.child("horasServicio").getValue().toString());
                }


                nuevaOrden.setHora(snap.child("horaEnOrigen").getValue().toString());
                nuevaOrden.setDireccionDestino(snap.child("direccionDestino").getValue().toString());

                if (snap.hasChild("observaciones")) {
                    nuevaOrden.setObservaciones(snap.child("observaciones").getValue().toString());
                }
                nuevaOrden.setFechaEnOrigenEs(snap.child("fechaEnOrigen").getValue().toString());
                try {
                    Date f = df.parse(snap.child("fechaEnOrigen").getValue().toString());
                    nuevaOrden.setFechaEnOrigen(f);

                } catch (Exception e) {
                    System.out.println("Error en el formato de la fecha");
                }

                nuevaOrden.setTiempoRestante(snap.child("fechaGeneracion").getValue().toString());
                if (snap.hasChild("asignadoPor")) {
                    nuevaOrden.setAsignadoPor(snap.child("asignadoPor").getValue().toString());
                }
                nuevaOrden.setConductor(snap.child("conductor").getValue().toString());
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
                nuevaOrden.setMatricula(snap.child("matricula").getValue().toString());
                nuevaOrden.setRuta(snap.child("ruta").getValue().toString());
                nuevaOrden.setSolicitadoPor(snap.child("solicitadoPor").getValue().toString());
                if (snap.hasChild("tarifa")) {
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
                    nuevaOrden.pasajeros.add(newPasajero);//  TODO: validacion de evitar duplicados
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




                DataSnapshot snapMunicipios;
                snapMunicipios = (DataSnapshot) snap.child("municipio");//municipios
                for (DataSnapshot municipio : snapMunicipios.getChildren()) {
                    Municipio newMunicipio = new Municipio();
                    newMunicipio.setId(municipio.getKey());
                    newMunicipio.setMunicipio(municipio.child("ciudad").getValue().toString());
                    newMunicipio.setDirecion(municipio.child("direccion").getValue().toString());
                    nuevaOrden.municipios.add(newMunicipio);//  TODO: validacion de evitar duplicados
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


                }

                //nuevo arbol snapUbicacionDestino
                DataSnapshot snapUbicacionDestino;
                snapUbicacionDestino = (DataSnapshot) snap.child("ubicacionDestino");//ubicacion

                if (snapUbicacionDestino.exists()) {
                    Ubicacion newUbicacionD = new Ubicacion();
                    double latdes = (double) snapUbicacionDestino.child("lat").getValue();
                    newUbicacionD.setLatDestino(latdes);
                    double lonDes = (double) snapUbicacionDestino.child("lon").getValue();
                    newUbicacionD.setLonDestino(lonDes);
                    nuevaOrden.ubicacionGPss.add(0, newUbicacionD);
                }

                modelo.adicionarNuevaOrden(nuevaOrden);
                //mListener.cargoUnaOrdenesConductor();

            }

            @Override
            public void onChildChanged(DataSnapshot snap, String s) {

                // metodo que escucha los cambios en la bd
                OrdenConductor nuevaOrden = readDatosOrdennesConductor(snap);
                if (nuevaOrden.getConductor() != null) {
                    if (nuevaOrden.getConductor().equals(modelo.uid)) {
                        modelo.adicionarNuevaOrden(nuevaOrden);
                    } else {
                        modelo.eliminarOrden(nuevaOrden.getId());
                    }
                }

                //mListener.cargoUnaOrdenesConductor();

            }

            @Override
            public void onChildRemoved(DataSnapshot snap) {

            }

            @Override
            public void onChildMoved(DataSnapshot snap, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void moverRegistro(final DatabaseReference fromPath, final DatabaseReference toPath, final String idOrden, final OnFinalizarOrden listener) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modelo = Modelo.getInstance();
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, DatabaseReference ref) {
                        if (error != null) {
                            OrdenConductor orden = modelo.getOrden(idOrden);
                            listener.fallo(orden);
                        } else {
                            fromPath.removeValue();
                            OrdenConductor orden = modelo.getOrden(idOrden);
                            modelo.eliminarOrden(idOrden);
                            listener.finalizo(orden);

                        }
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.fallo(null);

                Log.d("firebaseError", "error intentando copiar datos historico");
            }
        });
    }




    public void moverOrdenAlHistorico(String idOrden, OnFinalizarOrden listener) {
        modelo = Modelo.getInstance();

        //colocar metodo update fecha y hora de llegada


        DatabaseReference ref = database.getReference("ordenes/pendientes/" + idOrden);//ruta path
        DatabaseReference refHistorico = database.getReference("ordenes/historial/" + idOrden);//ruta path

        moverRegistro(ref, refHistorico, idOrden, listener);

    }




    //actualizar datos conductor
    public void crearActualizarDateTime(String idOrden) {
        modelo = Modelo.getInstance();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        DatabaseReference ref = database.getReference("ordenes/historial/" + idOrden + "/fechaEnDestino");//ruta path
        ref.setValue("" + dateFormat.format(date));

        DatabaseReference ref2 = database.getReference("ordenes/historial/" + idOrden + "/horaEnDestino");//ruta path
        ref2.setValue("" +modelo.getHora(date));

    }


    public void actualizarEstado(String estado, String idOrden) {


        modelo = Modelo.getInstance();
        OrdenConductor orden = modelo.getOrden(idOrden);
        DatabaseReference ref = database.getReference("ordenes/pendientes/" + idOrden + "/estado");//ruta path

        String fechaEstado = "";
        DatabaseReference reftime;

        //crear la solicitud
        if (estado.equals("No Asignado") || estado.equals("NoAsignado")) {
            ref.setValue("Asignado");
            orden.setEstado("Asignado");

        } else if (estado.equals("Asignado")) {
            ref.setValue("En Camino");

            fechaEstado = "fechaEstadoEnCamino";
            reftime = database.getReference("ordenes/pendientes/" + idOrden + "/" + fechaEstado);
            reftime.setValue("" + modelo.getFechaHora());

            orden.setEstado("En Camino");

        } else if (estado.equals("En Camino") || estado.equals("EnCamino")) {
            ref.setValue("Transportando");

            fechaEstado = "fechaEstadoEnTransportando";
            reftime = database.getReference("ordenes/pendientes/" + idOrden + "/" + fechaEstado);
            reftime.setValue("" + modelo.getFechaHora());
            orden.setEstado("Transportando");
        } else if (estado.equals("Transportando")) {
            ref.setValue("Finalizado");

            fechaEstado = "fechaEstadoEnfinalizado";
            reftime = database.getReference("ordenes/pendientes/" + idOrden + "/" + fechaEstado);
            reftime.setValue("" + modelo.getFechaHora());

            orden.setEstado("Finalizado");


        } else if (estado.equals("Finalizado")) {
            ref.setValue("Finalizado");

            fechaEstado = "fechaEstadoEnfinalizado";
            reftime = database.getReference("ordenes/pendientes/" + idOrden + "/" + fechaEstado);
            reftime.setValue(""+modelo.getFechaHora());


            orden.setEstado("Finalizado");


        }

    }



}
