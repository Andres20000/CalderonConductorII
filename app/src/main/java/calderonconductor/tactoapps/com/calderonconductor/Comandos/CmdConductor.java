package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Conductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Vehiculo;

public class CmdConductor {




    public interface OnCrearConductor {

        void creoConductor();

        void error();

    }

    public static void crearConductor(final String userId, Conductor conductor, final Vehiculo vehi, String token, final OnCrearConductor listener) {
        Modelo modelo;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference referencia = database.getReference();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final DatabaseReference ref = database.getReference("empresa/conductores/" + userId + "/");//ruta path

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activo", true);
        map.put("apellido", conductor.getApellido());
        map.put("cedula", conductor.cedula);
        map.put("celular", conductor.getCelular());
        map.put("correo", conductor.getCorreo());
        map.put("direccion", conductor.getDireccion());
        map.put("estado", "Pendiente");
        map.put("nombre", conductor.getNombre());
        map.put("tokenDevice", token);
        map.put("ocupado", false);

        final Map<String, Object> veh = new HashMap<String, Object>();
        veh.put("marca", vehi.getMarca());
        veh.put("referencia", vehi.referencia);
        veh.put("modelo", vehi.modelo);
        veh.put("color", "Amarillo");
        veh.put("activo", true);
        veh.put("alcance", "Local");
        veh.put("cupoMaximo", "4");
        veh.put("cupoMinimo", "1");
        veh.put("disponibilidad", true);
        veh.put("proximaRevisionTM", "11/5/2020");
        veh.put("ultimaRevisionTM", "11/5/2019");
        veh.put("tipo", "Taxi Estándar");




        final Map<String, Object> bigveh = new HashMap<String, Object>();
        bigveh.put(vehi.getPlaca(), veh);

        map.put("vehiculo", bigveh);



        ref.setValue(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                    final DatabaseReference ref = database.getReference("empresa/vehiculos/" + vehi.getPlaca() + "/");//ruta path
                    ref.setValue(veh);

                    listener.creoConductor();
                } else {
                    listener.error();
                }

            }
        });








    }

}