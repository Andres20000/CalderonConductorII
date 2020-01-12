package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.app.Activity;
import android.content.pm.PackageInfo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Params;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Sistema;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdPasajero.OnGetPasajero;


/**
 * Created by andres on 3/24/18.
 */

public class CmdParams {



    public interface onGetParams{
        void cargoParams();
        void cargoOpcionesGenerales();

    }


    public static  void getParams(final onGetParams listenerP){

        final Modelo modelo = Modelo.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        DatabaseReference ref = database.getReference("params");//ruta path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                modelo.params =  snap.getValue(Params.class);
                listenerP.cargoParams();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference ref2 = database.getReference("listados/opcionesGenerales/");//ruta path
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                if (snap.hasChild("observaciones")){
                    modelo.params.hasObservaciones = (boolean)snap.child("observaciones").getValue();
                }

                if (snap.hasChild("paradasServicio")){
                    modelo.params.hasParadasServicio = (boolean)snap.child("paradasServicio").getValue();
                }

                if (snap.hasChild("pasajeros")){
                    modelo.params.hasPasajerosAdicionales = (boolean)snap.child("pasajeros").getValue();
                }

                if (snap.hasChild("registroInmediato")){
                    modelo.params.hasRegistroInmediato = (boolean)snap.child("registroInmediato").getValue();
                }

                if (snap.hasChild("servicioAbierto")){
                    modelo.params.hasServiciosAbiertos = (boolean)snap.child("servicioAbierto").getValue();
                }

                if (snap.hasChild("subastas")){
                    modelo.params.hasSubastas = (boolean)snap.child("subastas").getValue();
                }

                if (snap.hasChild("tiposVehiculo")){
                    modelo.params.hasTiposVehiculos = (boolean)snap.child("tiposVehiculo").getValue();
                }

                if (snap.hasChild("ocultarBotonRegistroEnConductor")){
                    modelo.params.ocultarBotonRegistroEnConductor = (boolean)snap.child("ocultarBotonRegistroEnConductor").getValue();
                }

                if (snap.hasChild("diseñoRegistroTax1")){  //ESto no es que sea solo para Tax1 es "para todas las apps que quieran tener el registro del conductor estilo  taxone "
                    modelo.params.diseñoRegistroTax1 = (boolean)snap.child("diseñoRegistroTax1").getValue();
                }

                if (snap.hasChild("registroConductorRequiereAprobacionAdmin")){
                    modelo.params.registroConductorRequiereAprobacionAdmin = (boolean)snap.child("registroConductorRequiereAprobacionAdmin").getValue();
                }



                listenerP.cargoOpcionesGenerales();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }


    public interface OnSystemListener {

        void estaActualizado();
        void puedeActualizar();
        void debeActualizar();

    }



    public static void getSytem(final Activity act , final OnSystemListener listener){


        final Modelo modelo = Modelo.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("system/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {


                Long androideBuildlong = (Long)snap.child("androideBuildConductor").getValue();
                modelo.sistema.setBuild(androideBuildlong.intValue());

                modelo.sistema.setLink(snap.child("androideLinkConductor").getValue().toString());
                boolean double1 = (boolean)snap.child("androideUpdateMandatorioConductor").getValue();
                modelo.sistema.setUpdateMandatorio(double1);

                Long  entero2 = (Long) snap.child("androideVersionConductor").getValue();
                modelo.sistema.setVersion(entero2);

                Sistema miSis = tomarDatosSistema(act);

                if (miSis.getBuild() < modelo.sistema.getBuild()) {
                    if (modelo.sistema.getUpdateMandatorio()){
                        listener.debeActualizar();
                    }
                    else{
                        listener.puedeActualizar();
                    }
                } else {
                    listener.estaActualizado();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }


    public static Sistema tomarDatosSistema(Activity act) {
        Sistema sis = new Sistema();
        try {
            PackageInfo info = act.getPackageManager().getPackageInfo(act.getPackageName(), 0);

            sis.setBuild(Integer.parseInt("" + info.versionCode));

        } catch (Exception e) {
            sis.setBuild(1);
            sis.setVersion(1);
        }

        return sis;
    }





}
