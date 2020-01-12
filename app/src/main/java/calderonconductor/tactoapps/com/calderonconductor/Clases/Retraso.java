package calderonconductor.tactoapps.com.calderonconductor.Clases;

import java.util.Date;

public class Retraso {

    public String id;
    public Date fechaInicio;
    public Date fechaFin;
    public String comentario ;
    public Long startTime = 0L;

    public boolean puedeTerminar(){

        if (fechaInicio != null && comentario != null &&  comentario.length() > 3 ) {
            return  true;
        }

        return  false;

    }

    public boolean yaTerminado(){

        if (fechaInicio!= null && fechaFin != null && comentario != null &&  comentario.length() > 1 ) {
            return  true;
        }

        return  false;

    }

    public boolean estaContando(){

        if (fechaInicio != null && fechaFin == null) {
            return  true;
        }

        return  false;

    }



}
