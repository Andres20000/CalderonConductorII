package calderonconductor.tactoapps.com.calderonconductor.Comandos;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

/**
 * Created by tacto on 2/10/17.
 */

public class ComandoConductorDocumentosTerceros {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference referencia = database.getReference();

    public interface OnComandoConductorDocumentosTercerosChangeListener {

        void setConductorDocumentosTercerosListener();
        void errorSetConductorDocumentosTerceros();
    }

    //interface del listener de la actividad interesada
    private OnComandoConductorDocumentosTercerosChangeListener mListener;

    public ComandoConductorDocumentosTerceros(OnComandoConductorDocumentosTercerosChangeListener mListener){

        this.mListener = mListener;
    }


    public void setConductorDocumentosTerceros(String userId){
        DatabaseReference key = referencia.push();
        final DatabaseReference ref = database.getReference("empresa/conductoresTerceros/"+userId+"/");//ruta path

        Map<String, Object> enviarRegistroConductorTerceros = new HashMap<String, Object>();
        enviarRegistroConductorTerceros.put("activo", true);
        enviarRegistroConductorTerceros.put("apellido", modelo.conductoresTerceros.getApellido());
        enviarRegistroConductorTerceros.put("cedula", modelo.conductoresTerceros.getCedula());
        enviarRegistroConductorTerceros.put("celular", modelo.conductoresTerceros.getCelular());
        enviarRegistroConductorTerceros.put("correo", modelo.conductoresTerceros.getCorreo());
        enviarRegistroConductorTerceros.put("direccion", modelo.conductoresTerceros.getDireccion());
        enviarRegistroConductorTerceros.put("estado", "Pendiente");
        enviarRegistroConductorTerceros.put("foto", modelo.conductoresTerceros.getFoto());
        enviarRegistroConductorTerceros.put("nombre", modelo.conductoresTerceros.getNombre());
        enviarRegistroConductorTerceros.put("ocupado", false);
        enviarRegistroConductorTerceros.put("tokenDevice", modelo.conductoresTerceros.getTokenDevice());
        enviarRegistroConductorTerceros.put("categoriaLicencia", modelo.conductoresTerceros.getCategoria_licencia());
        enviarRegistroConductorTerceros.put("licenciaDeTransito", modelo.conductoresTerceros.getLicencia_de_transito());

        //documentos
        Map<String,Object> documentos = new HashMap<String,Object>();

        documentos.put("expedicionTarjetaDePropiedad",modelo.conductorDocumentosTerceros.getTxt_tarjetaexpedicion());

        Map<String,Object> soat = new HashMap<String,Object>();
        soat.put("desde",modelo.conductorDocumentosTerceros.getTxt_soat_hasta());
        soat.put("hasta",modelo.conductorDocumentosTerceros.getTxt_soat_hasta());
        documentos.put("soat",soat);

        Map<String,Object> polizaContractual = new HashMap<String,Object>();
        polizaContractual.put("desde",modelo.conductorDocumentosTerceros.getTxt_poliza_contractual_hasta());
        polizaContractual.put("hasta",modelo.conductorDocumentosTerceros.getTxt_poliza_contractual_hasta());
        documentos.put("polizaContractual",polizaContractual);

        Map<String,Object> polizaTodoRiesgo = new HashMap<String,Object>();
        polizaTodoRiesgo.put("desde",modelo.conductorDocumentosTerceros.getTxt_poliza_riesgo_desde());
        polizaTodoRiesgo.put("hasta",modelo.conductorDocumentosTerceros.getTxt_poliza_riesgo_hasta());
        documentos.put("polizaTodoRiesgo",polizaTodoRiesgo);

        Map<String,Object> tarjetaOperacion = new HashMap<String,Object>();
        tarjetaOperacion.put("desde",modelo.conductorDocumentosTerceros.getTxt_tarjeta_desde());
        tarjetaOperacion.put("hasta",modelo.conductorDocumentosTerceros.getTxt_tarjeta_hasta());
        documentos.put("tarjetaOperacion",tarjetaOperacion);

        Map<String,Object> revisionTecnicoMecanica = new HashMap<String,Object>();
        revisionTecnicoMecanica.put("desde",modelo.conductorDocumentosTerceros.getTxt_revicion_desde());
        revisionTecnicoMecanica.put("hasta",modelo.conductorDocumentosTerceros.getTxt_revicion_hasta());
        documentos.put("revisionTecnicoMecanica",revisionTecnicoMecanica);

        enviarRegistroConductorTerceros.put("documentos",documentos);

        //propietario
        Map<String,Object> propietario = new HashMap<String,Object>();
        propietario.put("direccion",modelo.propietarioVehiculo.getDireccion());
        propietario.put("id",modelo.propietarioVehiculo.getNit_cc());
        propietario.put("telefono",modelo.propietarioVehiculo.getTelefono());
        propietario.put("tipo",modelo.propietarioVehiculo.getTipo());
        if(modelo.propietarioVehiculo.getTipo().equals("Empresa")){
            propietario.put("empresa",modelo.propietarioVehiculo.getNombre());
        }
        if(modelo.propietarioVehiculo.getTipo().equals("Propietario")){
            propietario.put("nombre",modelo.propietarioVehiculo.getNombrePropietario());
            propietario.put("apellido",modelo.propietarioVehiculo.getApelidoPropietario());
        }
        enviarRegistroConductorTerceros.put("propietario",propietario);

        //vehiculo
        Map<String,Object> vehiculo = new HashMap<String,Object>();
        vehiculo.put("modelo",modelo.vehiculo.getFecha_Expedicion_Tarjeta_Propiedad());
        vehiculo.put("marca",modelo.vehiculo.getMarca());
        vehiculo.put("placa",modelo.vehiculo.getPlaca());
        vehiculo.put("tipo",modelo.vehiculo.getTipo());

        //Llantas
        Map<String,Object> Llantas = new HashMap<String,Object>();
        Llantas.put("delanteraDerecha",modelo.llantas.getDelantera_derecha());
        Llantas.put("delanteraIzquierda",modelo.llantas.getDelantera_izquierda());
        Llantas.put("traseraDerecha",modelo.llantas.getTrasera_derecha());
        Llantas.put("traseraIzquierda",modelo.llantas.getTrasera_izquierda());
        Llantas.put("repuesto",modelo.llantas.getRepuesto());
        vehiculo.put("llantas",Llantas);

        //informacionGeneral
        Map<String,Object> informacionGeneral = new HashMap<String,Object>();
        informacionGeneral.put("aireAcondicionado",modelo.informacionGeneral.getAire_Acondicionado());
        informacionGeneral.put("botiquin",modelo.informacionGeneral.getBotiquin());
        informacionGeneral.put("estadoMecanico",modelo.informacionGeneral.getEstado_Mecanico());
        informacionGeneral.put("iluminacionExterna",modelo.informacionGeneral.getIluminacion_Externa());
        informacionGeneral.put("iluminacionInterna",modelo.informacionGeneral.getIluminación_Interna());
        informacionGeneral.put("kitDeCarretera",modelo.informacionGeneral.getKit_de_carretera());
        informacionGeneral.put("latoneriaPintura",modelo.informacionGeneral.getLatoneria_y_Pintura());
        vehiculo.put("informacionGeneral",informacionGeneral);

        enviarRegistroConductorTerceros.put("vehiculo",vehiculo);

        ref.setValue(enviarRegistroConductorTerceros, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                    mListener.setConductorDocumentosTercerosListener();
                    return;
                } else {
                    mListener.errorSetConductorDocumentosTerceros();
                }
            }
        });
    }


    /**
     * Para evitar nullpointerExeptions
     */
    private static OnComandoConductorDocumentosTercerosChangeListener sDummyCallbacks = new OnComandoConductorDocumentosTercerosChangeListener()
    {
        @Override
        public void setConductorDocumentosTercerosListener()
        {}

        @Override
        public void errorSetConductorDocumentosTerceros()
        {}

    };

}
