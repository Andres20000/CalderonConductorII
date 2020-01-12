package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Vehiculo;

/**
 * Created by tactomotion on 27/07/16.
 */
public class ComandoConductor {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    public ComandoConductor() {

    }


    public interface OnConductorListener {

        void cargoConductor();

    }


    //asignamos los datos a conductor
    public static void getConductor(final OnConductorListener listener) {
        final Modelo modelo = Modelo.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref;
        if (modelo.tipoConductor.equals("conductor")) {
            ref = database.getReference("empresa/conductores/" + modelo.uid);//ruta path
        } else {
            ref = database.getReference("empresa/conductoresTerceros/" + modelo.uid);//ruta path
        }

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {

                boolean activo = (boolean) snap.child("activo").getValue();

               /* if(activo == true){
                    modelo.estadoConductorPendiente = "Activo";
                }else{
                    modelo.estadoConductorPendiente = "Pendiente";
                }*/
                modelo.conductor.setNombre(snap.child("nombre").getValue().toString());
                modelo.conductor.setApellido(snap.child("apellido").getValue().toString());
                modelo.conductor.setCelular(snap.child("celular").getValue().toString());
                if (snap.child("vehiculo/placa").getValue() != null) {

                    modelo.placa = snap.child("vehiculo/placa").getValue().toString();
                }

                if (snap.child("estado").exists()) {
                    modelo.conductor.setEstado(snap.child("estado").getValue().toString());
                } else {
                    modelo.conductor.setEstado("Pendiente");
                }

                if (snap.child("ocupado").exists()) {
                    modelo.ocupado = ((boolean)snap.child("ocupado").getValue());
                } else {
                    modelo.ocupado = true;
                }


                if (snap.child("activo").exists()) {
                    modelo.conductor.activo = ((boolean)snap.child("activo").getValue());
                } else {
                    modelo.conductor.activo = false;
                }


                if (modelo.params.hasRegistroInmediato){
                    if (snap.child("vehiculo").exists()) {

                        Vehiculo carro = new Vehiculo();

                        for (DataSnapshot snapCarro : snap.child("vehiculo").getChildren()) {
                            if ((boolean)(snapCarro.child("activo").getValue())){
                                carro.ccolor = snapCarro.child("color").getValue().toString();
                                carro.setMarca(snapCarro.child("marca").getValue().toString());
                                carro.referencia = snapCarro.child("referencia").getValue().toString();
                                carro.setPlaca(snapCarro.getKey());

                            }
                            modelo.vehiculo = carro;
                        }

                    }

                }


                listener.cargoConductor();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void setConductorToken(String token) {

        DatabaseReference ref = database.getReference("empresa/conductores/" + modelo.uid + "/tokenDevice");//ruta path

        ref.setValue("" + token);

    }


    public interface OnTerceroEstadoListener {

        void aprobado();
        void pendiente();
        void rechazado();

    }

    public static void getEstadoTercero(String id, final OnTerceroEstadoListener listener){

        final Modelo modelo = Modelo.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("empresa/conductoresTerceros/" + modelo.uid + "/estado");//ruta path

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {

                modelo.conductor.setEstado(snap.getValue().toString());

                if (snap.getValue().toString().equals("Aprobado")) {

                    listener.aprobado();
                }

                if (snap.getValue().toString().equals("Pendiente")) {
                    listener.pendiente();
                }

                if (snap.getValue().toString().equals("Rechazado")) {
                    listener.rechazado();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public static void getEstadoNormal(String id, final OnTerceroEstadoListener listener){

        final Modelo modelo = Modelo.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("empresa/conductores/" + modelo.uid + "/estado");//ruta path

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {

                modelo.conductor.setEstado(snap.getValue().toString());

                if (snap.getValue().toString().equals("Aprobado")) {

                    listener.aprobado();
                }

                if (snap.getValue().toString().equals("Pendiente")) {
                    listener.pendiente();
                }

                if (snap.getValue().toString().equals("Rechazado")) {
                    listener.rechazado();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public static void actualizarInicioSesion(final String id, final Context context, final String tioConductor) {

        if (id == null || id.equals("")) {
            return;
        }
        Modelo model = Modelo.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {


                if (!task.isSuccessful()) {

                    final DatabaseReference ref3 = database.getReference("empresa/" + tioConductor + "/" + id + "/ultimoLogeo");
                    ref3.setValue(Utility.getFechaHora());
                    final DatabaseReference ref4 = database.getReference("empresa/" + tioConductor + "/" + "systemDevice");
                    ref4.setValue("Android");

                    return;
                }

                String token = task.getResult().getToken();
                final DatabaseReference ref3 = database.getReference("empresa/" + tioConductor + "/" + id + "/ultimoLogeo");
                ref3.setValue(Utility.getFechaHora());
                final DatabaseReference ref4 = database.getReference("empresa/" + tioConductor + "/" + id + "/systemDevice");
                ref4.setValue("Android");
                final DatabaseReference ref5 = database.getReference("empresa/" + tioConductor + "/" + id + "/tokenDevice");
                ref5.setValue(token);
                final DatabaseReference ref6 = database.getReference("empresa/" + tioConductor + "/" + id + "/version");
                ref6.setValue(Utility.getVersionParaUsuario(context));


            }

        });


    }



}
