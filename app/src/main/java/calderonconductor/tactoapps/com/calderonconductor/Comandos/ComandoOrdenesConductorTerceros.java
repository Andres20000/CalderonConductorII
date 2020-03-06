package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.util.Log;
import android.view.Display.Mode;

import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Cotizaciones;
import calderonconductor.tactoapps.com.calderonconductor.Clases.CotizacionesTerceros;
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
public class ComandoOrdenesConductorTerceros {
    Modelo modelo;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    //interface del listener de la actividad interesada
    private OnOrdenesConductorTercerosChangeListener mListener;

    public ComandoOrdenesConductorTerceros(OnOrdenesConductorTercerosChangeListener mListener) {

        this.mListener = mListener;

    }

    public ComandoOrdenesConductorTerceros() {
        // modelo.getOrdenes();

    }


    public void getTodasLasOrdenesTerceros() {

        modelo = Modelo.getInstance();
        DatabaseReference ref = database.getReference("ordenes/pendientes/");

        //condicion por si es tercero o es un cotizar
        Query query = ref.orderByChild("ofertadaATerceros").equalTo(true);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snap, String s) {
                boolean data2 = snap.exists();

                if (data2 == false) {
                    mListener.cargoUnaOrdenesConductorTerceros();
                } else {
                    if (!modelo.estadoConductorPendiente.equals("Activo")) {
                       return;
                    } else {


                        OrdenConductor nuevaOrden = readDatosOrdenConductor(snap);


                        if (nuevaOrden != null) {
                            modelo.adicionarNuevaOrden(nuevaOrden);

                        }
                        mListener.cargoUnaOrdenesConductorTerceros();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snap, String s) {
//aquiLog.v("",""+snap);
                boolean data2 = snap.exists();

                if (data2 == false) {
                    mListener.cargoUnaOrdenesConductorTerceros();
                } else {
                    if (!modelo.estadoConductorPendiente.equals("Activo")) {
                        mListener.cargoUnaOrdenesConductorTerceros();
                    } else {

                        OrdenConductor nuevaOrden = readDatosOrdenConductor(snap);


                        if (nuevaOrden != null) {

                            if (nuevaOrden.getConductor().equals(modelo.uid)) {
                                modelo.adicionarNuevaOrden(nuevaOrden);
                            } else {
                                modelo.eliminarOrden(nuevaOrden.getId());
                            }

                        }
                        mListener.cargoUnaOrdenesConductorTerceros();
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("",""+dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v("",""+dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("",""+databaseError);
            }
        });

        if(modelo.estadoConductorPendiente == "Activo"){
            mListener.cargoUnaOrdenesConductorTerceros();
        }

    }


    public void getTodasLasOrdenesTercerosCotizar() {


        modelo = Modelo.getInstance();
        DatabaseReference ref = database.getReference("ordenes/pendientes/");
        Query query = ref.orderByChild("estado").equalTo("Cotizar");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snap, String s) {
                boolean data2 = snap.exists();
                if (data2 == false) {
                    mListener.cargoUnaOrdenesConductorTercerosCoti();
                } else {
                    if (!modelo.estadoConductorPendiente.equals("Activo")) {
                        mListener.cargoUnaOrdenesConductorTercerosCoti();
                    } else {

                        if (!snap.child("estado").getValue().toString().equals("Cotizar")) {


                            //mListener.cargoUnaOrdenesConductorTercerosCoti();
                            OrdenConductor nuevaOrden = readDatosOrdenConductor(snap);
                            if (!snap.child("estado").getValue().toString().equals("No Asignado") && !snap.child("estado").getValue().toString().equals("Anulado") && !snap.child("estado").getValue().toString().equals("SinConfirmar")) {
                                if (nuevaOrden != null) {
                                    modelo.adicionarNuevaOrden(nuevaOrden);
                                }
                            }

                            mListener.cargoUnaOrdenesConductorTercerosCoti();
                        } else {
                            OrdenConductor nuevaOrden = readDatosOrdenConductor(snap);
                            if (!snap.child("estado").getValue().toString().equals("No Asignado") && !snap.child("estado").getValue().toString().equals("Anulado") && !snap.child("estado").getValue().toString().equals("SinConfirmar")) {
                                if (nuevaOrden != null) {
                                    modelo.adicionarNuevaOrden(nuevaOrden);
                                }

                            }

                            mListener.cargoUnaOrdenesConductorTercerosCoti();
                        }

                    }
                    mListener.cargoUnaOrdenesConductorTercerosCoti();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snap, String s) {

                // metodo que escucha los cambios en la bd
                OrdenConductor nuevaOrden = readDatosOrdenConductor(snap);
                if(nuevaOrden.getConductor() ==  null){
                    modelo.adicionarNuevaOrden(nuevaOrden);
                    mListener.cargoUnaOrdenesConductorTercerosCoti();
                    return;

                }
                if ( nuevaOrden.getConductor().equals(modelo.uid)){
                    modelo.adicionarNuevaOrden(nuevaOrden);
                }else{
                    modelo.eliminarOrden(nuevaOrden.getId());
                }

                mListener.cargoUnaOrdenesConductorTercerosCoti();


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
        mListener.cargoUnaOrdenesConductorTercerosCoti();
    }

    public OrdenConductor readDatosOrdenConductor(DataSnapshot snapT) {



            boolean ofertadaATerceros = false;
            OrdenConductor nuevaOrden = new OrdenConductor(snapT.getKey());

            if (snapT.child("conductor").getValue() == null || snapT.child("conductor").getValue().toString().equals("") || snapT.child("conductor").getValue().toString().equals(modelo.uid) || snapT.child("estado").getValue().toString().equals("Cotizar")) {

                //nuevaOrden.setConductor(snapT.child("conductor").getValue().toString());

                if (snapT.hasChild("conductor")) {
                    nuevaOrden.setConductor(snapT.child("conductor").getValue().toString());
                }
                if (snapT.hasChild("ofertadaATerceros")) {
                    ofertadaATerceros = (boolean) snapT.child("ofertadaATerceros").getValue();
                }

                if (snapT.hasChild("origen")) {
                    nuevaOrden.setOrigen(snapT.child("origen").getValue().toString());
                }

                nuevaOrden.setOfertadaATerceros(ofertadaATerceros);

                if (snapT.hasChild("estado")) {
                    nuevaOrden.setEstado(snapT.child("estado").getValue().toString());
                }

                nuevaOrden.setDestino(snapT.child("destino").getValue().toString());

                if(snapT.child("destino").getValue().toString().equals("ABIERTO")){
                    nuevaOrden.setDiasServicio(snapT.child("diasServicio").getValue().toString());
                    nuevaOrden.setHorasServicio(snapT.child("horasServicio").getValue().toString());
                }

                nuevaOrden.precioHora = Integer.parseInt(snapT.child("precioHora").getValue().toString());
                nuevaOrden.precioKm = Integer.parseInt(snapT.child("precioKm").getValue().toString());
                nuevaOrden.precioHoraOrden = Integer.parseInt(snapT.child("precioHoraOrden").getValue().toString());
                nuevaOrden.precioKmOrden = Integer.parseInt(snapT.child("precioKmOrden").getValue().toString());

                nuevaOrden.setHora(snapT.child("horaEnOrigen").getValue().toString());
                nuevaOrden.setDireccionDestino(snapT.child("direccionDestino").getValue().toString());
                nuevaOrden.setFechaEnOrigenEs(snapT.child("fechaEnOrigen").getValue().toString());
                if (snapT.hasChild("observaciones")) {
                    nuevaOrden.setObservaciones(snapT.child("observaciones").getValue().toString());
                }

                try {
                    Date f = df.parse(snapT.child("fechaEnOrigen").getValue().toString());
                    nuevaOrden.setFechaEnOrigen(f);

                } catch (Exception e) {
                    System.out.println("Error en el formato de la fecha");
                }

                nuevaOrden.setTiempoRestante(snapT.child("fechaGeneracion").getValue().toString());
                if (snapT.child("asignadoPor").getValue() != null) {
                    nuevaOrden.setAsignadoPor(snapT.child("asignadoPor").getValue().toString());
                }

                if (snapT.child("conductor").getValue() != null) {
                    nuevaOrden.setConductor(snapT.child("conductor").getValue().toString());
                }

                if (snapT.hasChild("cosecutivoOrden")) {
                    nuevaOrden.setCosecutivoOrden(snapT.child("cosecutivoOrden").getValue().toString());
                } else {
                    if (snapT.hasChild("consecutivoOrden")) {
                        nuevaOrden.setCosecutivoOrden(snapT.child("consecutivoOrden").getValue().toString());
                    } else {
                        nuevaOrden.setCosecutivoOrden("");
                    }
                }
                nuevaOrden.setDireccionOrigen(snapT.child("direccionOrigen").getValue().toString());
                //nuevaOrden.setFechaEnDestino(snapT.child("fechaEnDestino").getValue().toString());
                //nuevaOrden.setHoraEnDestino(snapT.child("horaEnDestino").getValue().toString());
                nuevaOrden.setHoraGeneracion(snapT.child("horaGeneracion").getValue().toString());
                nuevaOrden.setIdCliente(snapT.child("idCliente").getValue().toString());

                if (snapT.child("matricula").getValue() != null) {
                    nuevaOrden.setMatricula(snapT.child("matricula").getValue().toString());
                }

                nuevaOrden.setRuta(snapT.child("ruta").getValue().toString());

                if (snapT.hasChild("solicitadoPor")) {
                    nuevaOrden.setSolicitadoPor(snapT.child("solicitadoPor").getValue().toString());

                }

                if (snapT.child("tarifa").getValue() != null) {
                    nuevaOrden.setTarifa(snapT.child("tarifa").getValue().toString());
                }

                Long l = (Long) snapT.child("timestamp").getValue();
                nuevaOrden.setTimeStamp(l);
                // Long longTimestamp = Long.valueOf(nuevaOrden.setTimeStamp(snapT.child("timestamp").getValue().toString()));
                // nuevaOrden.setTrayectos(snapT.child("trayectos").getValue().toString());
                nuevaOrden.setId(snapT.getKey());

                //nuevo arbol
                DataSnapshot snapTPasajeros;
                snapTPasajeros = (DataSnapshot) snapT.child("pasajeros");
                for (DataSnapshot pasajero : snapTPasajeros.getChildren()) {
                    Pasajero newPasajero = new Pasajero();
                    newPasajero.setIdPasajero(pasajero.getKey());
                    newPasajero.setTipo(pasajero.getValue().toString());
                    nuevaOrden.pasajeros.add(newPasajero);//  TODO: validacion de evitar duplicados
                }


                if(snapT.child("AutoAsignarDespuesDe").exists()){

                    String numeroOfertas= snapT.child("AutoAsignarDespuesDe/numeroOfertas").getValue().toString();

                    try {
                        nuevaOrden.setNumeroOfertas(Integer.parseInt(numeroOfertas));
                    }catch (Exception ex){
                        nuevaOrden.setNumeroOfertas(-1);

                    }


                }


            DataSnapshot snapRetrasos = (DataSnapshot) snapT.child("retrasos");

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



            //nuevo arbol
            DataSnapshot snapTCotizaciones;
            snapTCotizaciones = (DataSnapshot) snapT.child("cotizaciones");
            if (snapTCotizaciones.exists()) {

                //nuevo arbol

                int contadorCotizaciones = 0;

                CotizacionesTerceros newCotizacionesTerceros = new CotizacionesTerceros();
                newCotizacionesTerceros.setId(snapTCotizaciones.getKey());



                if (snapTCotizaciones.child("contadorCotizaciones").exists()) {
                    contadorCotizaciones = (((Long) snapTCotizaciones.child("contadorCotizaciones").getValue()).intValue());
                }

                newCotizacionesTerceros.setContadorCotizaciones(contadorCotizaciones);

                if (snapTCotizaciones.child("menorValorCotizado").exists()) {
                    newCotizacionesTerceros.setMenorValorCotizado(snapTCotizaciones.child("menorValorCotizado").getValue().toString());

                }
                if (snapTCotizaciones.child("valorMaximo").exists()) {
                    //int valMax = (int) snapTCotizaciones.child("valorMaximo").getValue();
                    int valMax = (((Long) snapTCotizaciones.child("valorMaximo").getValue()).intValue());
                    newCotizacionesTerceros.setValorMaximo(valMax);

                }

                if (snapTCotizaciones.child("valorMinimo").exists()) {
                    //int valMin = (int) snapTCotizaciones.child("valorMinimo").getValue();
                    int valMin = (((Long) snapTCotizaciones.child("valorMinimo").getValue()).intValue());
                    newCotizacionesTerceros.setValorMinimo(valMin);

                }


                // int  vaor = (int) snapTCotizaciones.child("valor").getValue();

                if (snapTCotizaciones.child("datosCotizaciones").exists()) {

                    DataSnapshot snapTDatoscotizaiones;
                    snapTDatoscotizaiones = (DataSnapshot) snapTCotizaciones.child("datosCotizaciones");//municipios
                    for (DataSnapshot coti : snapTDatoscotizaiones.getChildren()) {
                        Cotizaciones cot2 = new Cotizaciones();
                        cot2.setId(coti.getKey());

                        int valor = (((Long) coti.child("valor").getValue()).intValue());
                        cot2.setValor(valor);
                        Long time = (Long) coti.child("timestamp").getValue();
                        cot2.setTimestamp(time);
                        newCotizacionesTerceros.cotizaciones.add(cot2);
                    }

                }

                nuevaOrden.cotizacionesT.add(newCotizacionesTerceros);//  TODO: validacion de evitar duplicados


            }


//municipio

                DataSnapshot snapTMunicipios;
                snapTMunicipios = (DataSnapshot) snapT.child("municipio");//municipios
                for (DataSnapshot municipio : snapTMunicipios.getChildren()) {
                    Municipio newMunicipio = new Municipio();
                    newMunicipio.setId(municipio.getKey());
                    newMunicipio.setMunicipio(municipio.child("ciudad").getValue().toString());
                    newMunicipio.setDirecion(municipio.child("direccion").getValue().toString());
                    nuevaOrden.municipios.add(newMunicipio);//  TODO: validacion de evitar duplicados
                }


                //nuevo arbol snapTUbicacion
                DataSnapshot snapTUbicacion;
                snapTUbicacion = (DataSnapshot) snapT.child("ubicacion");//ubicacion
                //nuevaOrden.ubicacionGPss.clear();
                for (DataSnapshot gps : snapTUbicacion.getChildren()) {
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

            //nuevo arbol snapTUbicacionOrigen
            DataSnapshot snapTUbicacionOrigen;
            snapTUbicacionOrigen = (DataSnapshot) snapT.child("ubicacionOrigen");//ubicacion
            //nuevaOrden.ubicacionGPss.clear();


            if (snapTUbicacionOrigen.exists()) {
                Ubicacion newUbicacionO = new Ubicacion();
                double latdO = (double) snapTUbicacionOrigen.child("lat").getValue();
                newUbicacionO.setLatOrigen(latdO);
                double lonO = (double) snapTUbicacionOrigen.child("lon").getValue();
                newUbicacionO.setLonOrigen(lonO);
                nuevaOrden.ubicacionGPss.add(0, newUbicacionO);


            }

            //nuevo arbol snapTUbicacionDestino
            DataSnapshot snapTUbicacionDestino;
            snapTUbicacionDestino = (DataSnapshot) snapT.child("ubicacionDestino");//ubicacion

            if (snapTUbicacionDestino.exists()) {
                Ubicacion newUbicacionD = new Ubicacion();
                double latdes = (double) snapTUbicacionDestino.child("lat").getValue();
                newUbicacionD.setLatDestino(latdes);
                double lonDes = (double) snapTUbicacionDestino.child("lon").getValue();
                newUbicacionD.setLonDestino(lonDes);
                nuevaOrden.ubicacionGPss.add(0, newUbicacionD);
            }


            return nuevaOrden;
        }
        return null;
    }

    public void getOrdenesConductorTerceros() {
        modelo = Modelo.getInstance();
        // modelo.getOrdenes().clear();
        DatabaseReference ref = database.getReference("ordenes/pendientes/");//ruta path
        //se crea un query filtrado por el id del conductor
        Query query = ref.orderByChild("ofertadaATerceros").equalTo(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snap, String s) {

                boolean ofertadaATerceros = (boolean) snap.child("ofertadaATerceros").getValue();


                OrdenConductor nuevaOrden = new OrdenConductor(snap.getKey());
                nuevaOrden.setOfertadaATerceros(ofertadaATerceros);
                nuevaOrden.setOrigen(snap.child("origen").getValue().toString());
                nuevaOrden.setEstado(snap.child("estado").getValue().toString());
                nuevaOrden.setDestino(snap.child("destino").getValue().toString());

                if(snap.child("destino").getValue().toString().equals("ABIERTO")){
                    nuevaOrden.setDiasServicio(snap.child("diasServicio").getValue().toString());
                    nuevaOrden.setHorasServicio(snap.child("horasServicio").getValue().toString());
                }

                nuevaOrden.setHora(snap.child("horaEnOrigen").getValue().toString());
                nuevaOrden.setDireccionDestino(snap.child("direccionDestino").getValue().toString());
                nuevaOrden.setFechaEnOrigenEs(snap.child("fechaEnOrigen").getValue().toString());
                try {
                    Date f = df.parse(snap.child("fechaEnOrigen").getValue().toString());
                    nuevaOrden.setFechaEnOrigen(f);

                } catch (Exception e) {
                    System.out.println("Error en el formato de la fecha");
                }

                if (snap.hasChild("observaciones")) {
                    nuevaOrden.setObservaciones(snap.child("observaciones").getValue().toString());
                }


                nuevaOrden.setTiempoRestante(snap.child("fechaGeneracion").getValue().toString());
                nuevaOrden.setAsignadoPor(snap.child("asignadoPor").getValue().toString());
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
                nuevaOrden.setTarifa(snap.child("tarifa").getValue().toString());
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

//municipio

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


                String estados = snap.child("estado").getValue().toString();
                if (!estados.equals("Anulado") && !estados.equals("SinConfirmar")) {
                    if (nuevaOrden != null) {
                        modelo.adicionarNuevaOrden(nuevaOrden);
                    }
                }


                mListener.cargoUnaOrdenesConductorTerceros();


            }

            @Override
            public void onChildChanged(DataSnapshot snap, String s) {

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


    //transacciones
    public void asignarTrayectoSeguro(final String idOrden, final String idTercero) {




        DatabaseReference ref = database.getReference("ordenes/pendientes/" + idOrden + "/conductor/");

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() != null) {
                    String idconductor = currentData.getValue().toString();

                    if (idconductor.equals("")) {
                        Log.v("SolicitudAsiganda", "Solicitud ya asignada. " + idOrden);

                        return Transaction.abort();
                    } else {

                        currentData.setValue(idTercero);
                        return Transaction.success(currentData);
                    }
                } else {
                    currentData.setValue(idTercero);
                    return Transaction.success(currentData);

                }

            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (!b) {
                    mListener.resultadoAlAsignarCancel(false);
                } else {
                    mListener.resultadoAlAsignar(true);
                }
            }


        });

    }


    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnOrdenesConductorTercerosChangeListener {

        void cargoUnaOrdenesConductorTerceros();

        void resultadoAlAsignar(boolean exitoso);
        void resultadoAlAsignarCancel(boolean cancel);

        void cargoUnaOrdenesConductorTercerosCoti();


    }

    /**
     * Para evitar nullpointerExeptions
     */
    private static OnOrdenesConductorTercerosChangeListener sDummyCallbacks = new OnOrdenesConductorTercerosChangeListener() {
        @Override
        public void cargoUnaOrdenesConductorTerceros() {
        }

        @Override
        public void resultadoAlAsignar(boolean exitoso) {
        }
        public void resultadoAlAsignarCancel(boolean cancel) {
        }

        @Override
        public void cargoUnaOrdenesConductorTercerosCoti() {

        }
    };

}
