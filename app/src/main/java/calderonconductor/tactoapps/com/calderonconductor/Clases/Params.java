package calderonconductor.tactoapps.com.calderonconductor.Clases;

/**
 * Created by andres on 4/5/18.
 */

public class Params {

    public int precioKilometro = 1500;
    public String tiempoRespuestaMinutos = "50";
    public int horasRespuestaGenerico = 1;
    public String terminos;
    public boolean ocultarAsignacionTerceros = false;
    public int ocultarAsignacionTercerosMinutos = 0;


    public boolean hasObservaciones = true;
    public boolean hasParadasServicio = true;
    public boolean hasPasajerosAdicionales = true;
    public boolean hasRegistroInmediato = false;    //Autoline  (Pasajero queda tipo A de una y los servicio se publican de una a los conductores)
    public boolean hasServiciosAbiertos = true;
    public boolean hasSubastas = true;
    public boolean hasTiposVehiculos = true;

    public int radioNormal = 10000;
    public int radioProgramadas = 90000;


    public boolean ocultarBotonRegistroEnConductor = false;
    public boolean diseñoRegistroTax1 = false;
    public  boolean registroConductorRequiereAprobacionAdmin = false;

    public  boolean mostrarAlertasCambioEstados = false;
    public  boolean mostrarInfoSensible = false;
    public  boolean autoAsignarServicios = false;

    public long tiempoEsperaPreAsignadoConductorEnSegundos = 0;

}