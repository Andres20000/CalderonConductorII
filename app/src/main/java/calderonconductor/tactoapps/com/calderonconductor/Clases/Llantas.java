package calderonconductor.tactoapps.com.calderonconductor.Clases;

/**
 * Created by tacto on 1/10/17.
 */

public class Llantas {

    private boolean Delantera_derecha;
    private boolean Delantera_izquierda;
    private boolean Trasera_derecha;
    private boolean Trasera_izquierda;
    private boolean repuesto;


    public boolean getDelantera_derecha() {
        return Delantera_derecha;
    }

    public void setDelantera_derecha(boolean delantera_derecha) {
        Delantera_derecha = delantera_derecha;
    }

    public boolean getDelantera_izquierda() {
        return Delantera_izquierda;
    }

    public void setDelantera_izquierda(boolean delantera_izquierda) {
        Delantera_izquierda = delantera_izquierda;
    }

    public boolean getTrasera_derecha() {
        return Trasera_derecha;
    }

    public void setTrasera_derecha(boolean trasera_derecha) {
        Trasera_derecha = trasera_derecha;
    }

    public boolean getTrasera_izquierda() {
        return Trasera_izquierda;
    }

    public void setTrasera_izquierda(boolean trasera_izquierda) {
        Trasera_izquierda = trasera_izquierda;
    }

    public boolean getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(boolean repuesto) {
        this.repuesto = repuesto;
    }
}
