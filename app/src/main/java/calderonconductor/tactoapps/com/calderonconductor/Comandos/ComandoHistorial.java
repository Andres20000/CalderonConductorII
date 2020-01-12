package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Calificacion;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Municipio;
import calderonconductor.tactoapps.com.calderonconductor.Clases.OrdenConductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Pasajero;

/**
 * Created by tactomotion on 4/09/16.
 */
public class ComandoHistorial {

    Modelo modelo;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");



    //interface del listener de la actividad interesada
    private OnOrdenesHistorialChangeListener mListener;

    public ComandoHistorial(OnOrdenesHistorialChangeListener mListener){

        this.mListener = mListener;

    }

    public  ComandoHistorial(){
        modelo.getOrdenes();

    }


    public void getTodasLasOrdenesHistorial(){
        modelo = Modelo.getInstance();

        modelo.getHistorial().clear();
        DatabaseReference ref = database.getReference("ordenes/historial/");//ruta path
        //se crea un query filtrado por el id del conductor
        Query query = ref.orderByChild("conductor").equalTo(modelo.uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {

                for (DataSnapshot orden : snap.getChildren()){

                    OrdenConductor nuevaOrden = new OrdenConductor(snap.getKey());
                    nuevaOrden.setOrigen(orden.child("origen").getValue().toString());
                    nuevaOrden.setEstado(orden.child("estado").getValue().toString());
                    nuevaOrden.setDestino(orden.child("destino").getValue().toString());

                    if(orden.child("destino").getValue().toString().equals("ABIERTO")){
                        nuevaOrden.setDiasServicio(orden.child("diasServicio").getValue().toString());
                        nuevaOrden.setHorasServicio(orden.child("horasServicio").getValue().toString());
                    }

                    nuevaOrden.setHora(orden.child("horaEnOrigen").getValue().toString());
                    nuevaOrden.setDireccionDestino(orden.child("direccionDestino").getValue().toString());

                    try
                    {
                        Date f = df.parse(orden.child("fechaEnOrigen").getValue().toString());
                        nuevaOrden.setFechaEnOrigen(f);

                    }
                    catch (Exception e)
                    {
                        System.out.println("Error en el formato de la fecha");
                    }

                    nuevaOrden.setTiempoRestante(orden.child("fechaGeneracion").getValue().toString());


                    if(orden.child("asignadoPor").getValue() != null){
                        nuevaOrden.setAsignadoPor(orden.child("asignadoPor").getValue().toString());
                    }


                    if(orden.child("conductor").getValue() != null) {
                        nuevaOrden.setConductor(orden.child("conductor").getValue().toString());
                    }
                    if(orden.hasChild("cosecutivoOrden")){
                        nuevaOrden.setCosecutivoOrden(orden.child("cosecutivoOrden").getValue().toString());
                    }else{
                        if(orden.hasChild("consecutivoOrden")){
                            nuevaOrden.setCosecutivoOrden(orden.child("consecutivoOrden").getValue().toString());
                        }else{
                            nuevaOrden.setCosecutivoOrden("");
                        }
                    }
                    nuevaOrden.setDireccionOrigen(orden.child("direccionOrigen").getValue().toString());
                    //nuevaOrden.setFechaEnDestino(snap.child("fechaEnDestino").getValue().toString());
                    //nuevaOrden.setHoraEnDestino(snap.child("horaEnDestino").getValue().toString());
                    nuevaOrden.setHoraGeneracion(orden.child("horaGeneracion").getValue().toString());
                    nuevaOrden.setIdCliente(orden.child("idCliente").getValue().toString());

                    if(orden.child("matricula").getValue() != null){
                        nuevaOrden.setMatricula(orden.child("matricula").getValue().toString());
                    }

                    nuevaOrden.setRuta(orden.child("ruta").getValue().toString());
                    nuevaOrden.setSolicitadoPor(orden.child("solicitadoPor").getValue().toString());

                    if(orden.child("tarifa").getValue() != null){
                        nuevaOrden.setTarifa(orden.child("tarifa").getValue().toString());
                    }

                    if (snap.hasChild("observaciones")) {
                        nuevaOrden.setObservaciones(snap.child("observaciones").getValue().toString());
                    }

                    Long l  = (Long)orden.child("timestamp").getValue();
                    nuevaOrden.setTimeStamp(l);
                    nuevaOrden.setId(orden.getKey());

                    getRazonSocial(nuevaOrden.getIdCliente());


                    //nuevo arbol pasajero
                    DataSnapshot snapPasajeros;
                    snapPasajeros = (DataSnapshot) orden.child("pasajeros");
                    for (DataSnapshot pasajero : snapPasajeros.getChildren()){
                        Pasajero newPasajero = new Pasajero();
                        newPasajero.setIdPasajero(pasajero.getKey());
                        nuevaOrden.pasajeros.add(newPasajero);//  TODO: validacion de evitar duplicados
                    }

                    //nuevo arbol calificacion
                    DataSnapshot snapCalificaciones;
                    snapCalificaciones = (DataSnapshot) orden.child("calificacion");//calificaciones
                    for (DataSnapshot calificacion : snapCalificaciones.getChildren()){
                        Calificacion newCalificacion = new Calificacion();
                        newCalificacion.setId(calificacion.getKey());
                        newCalificacion.setObervacion(calificacion.child("observacion").getValue().toString());
                        newCalificacion.setValor(calificacion.child("valor").getValue().toString());
                        nuevaOrden.calificaciones.add(newCalificacion);//  TODO: validacion de evitar duplicados
                    }

                    //municipio

                    DataSnapshot snapMunicipios;
                    snapMunicipios = (DataSnapshot) orden.child("municipio");//municipios
                    for (DataSnapshot municipio : snapMunicipios.getChildren()){
                        Municipio newMunicipio = new Municipio();
                        newMunicipio.setId(municipio.getKey());
                        newMunicipio.setMunicipio(municipio.child("ciudad").getValue().toString());
                        newMunicipio.setDirecion(municipio.child("direccion").getValue().toString());
                        nuevaOrden.municipios.add(newMunicipio);//  TODO: validacion de evitar duplicados
                    }


                    if(orden.child("estado").getValue().toString().equals("Finalizado")){
                        modelo.adicionarNuevaOrdenHistorial(nuevaOrden);

                    }
                    mListener.cargoHistorial();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void getOrdenesHistorial(){
        modelo = Modelo.getInstance();
        DatabaseReference ref = database.getReference("ordenes/historial/");//ruta path
        //se crea un query filtrado por el id del conductor
        Query query = ref.orderByChild("conductor").equalTo(modelo.uid);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snap, String s) {

                OrdenConductor nuevaOrden = new OrdenConductor(snap.getKey());
                nuevaOrden.setOrigen(snap.child("origen").getValue().toString());
                nuevaOrden.setEstado(snap.child("estado").getValue().toString());
                nuevaOrden.setDestino(snap.child("destino").getValue().toString());

                if(snap.child("destino").getValue().toString().equals("ABIERTO")){
                    nuevaOrden.setDiasServicio(snap.child("diasServicio").getValue().toString());
                    nuevaOrden.setHorasServicio(snap.child("horasServicio").getValue().toString());
                }

                nuevaOrden.setHora(snap.child("horaEnOrigen").getValue().toString());
                nuevaOrden.setDireccionDestino(snap.child("direccionDestino").getValue().toString());

                try
                {
                    Date f = df.parse(snap.child("fechaEnOrigen").getValue().toString());
                    nuevaOrden.setFechaEnOrigen(f);

                }
                catch (Exception e)
                {
                    System.out.println("Error en el formato de la fecha");
                }

                nuevaOrden.setTiempoRestante(snap.child("fechaGeneracion").getValue().toString());

                if(snap.child("asignadoPor").getValue() != null){
                    nuevaOrden.setAsignadoPor(snap.child("asignadoPor").getValue().toString());
                }




                if(snap.child("conductor").getValue() != null) {
                    nuevaOrden.setConductor(snap.child("conductor").getValue().toString());
                }

                if(snap.hasChild("cosecutivoOrden")){
                    nuevaOrden.setCosecutivoOrden(snap.child("cosecutivoOrden").getValue().toString());
                }else{
                    if(snap.hasChild("consecutivoOrden")){
                        nuevaOrden.setCosecutivoOrden(snap.child("consecutivoOrden").getValue().toString());
                    }else{
                        nuevaOrden.setCosecutivoOrden("");
                    }
                }
                nuevaOrden.setDireccionOrigen(snap.child("direccionOrigen").getValue().toString());
                //nuevaOrden.setFechaEnDestino(snap.child("fechaEnDestino").getValue().toString());
                //nuevaOrden.setHoraEnDestino(snap.child("horaEnDestino").getValue().toString());
                nuevaOrden.setHoraGeneracion(snap.child("horaGeneracion").getValue().toString());
                nuevaOrden.setIdCliente(snap.child("idCliente").getValue().toString());

                if(snap.child("matricula").getValue() != null){
                    nuevaOrden.setMatricula(snap.child("matricula").getValue().toString());
                }

                nuevaOrden.setRuta(snap.child("ruta").getValue().toString());
                nuevaOrden.setSolicitadoPor(snap.child("solicitadoPor").getValue().toString());

                if(snap.child("tarifa").getValue() != null){
                    nuevaOrden.setTarifa(snap.child("tarifa").getValue().toString());
                }

                if (snap.hasChild("observaciones")) {
                    nuevaOrden.setObservaciones(snap.child("observaciones").getValue().toString());
                }

                Long l  = (Long)snap.child("timestamp").getValue();
                nuevaOrden.setTimeStamp(l);
                nuevaOrden.setId(snap.getKey());

                getRazonSocial(nuevaOrden.getIdCliente());


                //nuevo arbol pasajero
                DataSnapshot snapPasajeros;
                snapPasajeros = (DataSnapshot) snap.child("pasajeros");
                for (DataSnapshot pasajero : snapPasajeros.getChildren()){
                    Pasajero newPasajero = new Pasajero();
                    newPasajero.setIdPasajero(pasajero.getKey());
                    newPasajero.setTipo(pasajero.getValue().toString());
                    nuevaOrden.pasajeros.add(newPasajero);//  TODO: validacion de evitar duplicados
                }

               //nuevo arbol calificacion
                DataSnapshot snapCalificaciones;
                snapCalificaciones = (DataSnapshot) snap.child("calificacion");//calificaciones
                for (DataSnapshot calificacion : snapCalificaciones.getChildren()){
                    Calificacion newCalificacion = new Calificacion();
                    newCalificacion.setId(calificacion.getKey());
                    newCalificacion.setObervacion(calificacion.child("observacion").getValue().toString());
                    newCalificacion.setValor(calificacion.child("valor").getValue().toString());
                    nuevaOrden.calificaciones.add(newCalificacion);//  TODO: validacion de evitar duplicados
                }

                //municipio

               DataSnapshot snapMunicipios;
                snapMunicipios = (DataSnapshot) snap.child("municipio");//municipios
                for (DataSnapshot municipio : snapMunicipios.getChildren()){
                    Municipio newMunicipio = new Municipio();
                    newMunicipio.setId(municipio.getKey());
                    newMunicipio.setMunicipio(municipio.child("ciudad").getValue().toString());
                    newMunicipio.setDirecion(municipio.child("direccion").getValue().toString());
                    nuevaOrden.municipios.add(newMunicipio);//  TODO: validacion de evitar duplicados
                }


                if(snap.child("estado").getValue().toString().equals("Finalizado")){
                    modelo.adicionarNuevaOrdenHistorial(nuevaOrden);

                }

                mListener.cargoHistorial();

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



    public void getRazonSocial(final String idCliente){
        modelo = Modelo.getInstance();

        DatabaseReference ref = database.getReference("clientes/"+idCliente+"/razonSocial");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                //modelo.getOrden(idServicio).getCliente().setRazonSocial(snap.getValue().toString());
                modelo.razonesSociales.put(idCliente,snap.getValue().toString());
                mListener.cargoHistorial();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnOrdenesHistorialChangeListener {

        void cargoHistorial();


    }

    /**
     * Para evitar nullpointerExeptions
     */
    private static OnOrdenesHistorialChangeListener sDummyCallbacks = new OnOrdenesHistorialChangeListener()
    {
        @Override
        public void cargoHistorial()
        {}


    };




}
