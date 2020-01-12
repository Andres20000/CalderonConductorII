package calderonconductor.tactoapps.com.calderonconductor.Clases;

import java.util.ArrayList;

/**
 * Created by tacto on 29/05/18.
 */

public class CotizacionesTerceros {

    String id;
    int contadorCotizaciones = 0;
    String idConductorMenorValorCotizado;
    int valorMaximo;
    int valorMinimo;
    public ArrayList<Cotizaciones> cotizaciones = new ArrayList<Cotizaciones>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getContadorCotizaciones() {
        return contadorCotizaciones;
    }

    public void setContadorCotizaciones(int contadorCotizaciones) {
        this.contadorCotizaciones = contadorCotizaciones;
    }

    public String getMenorValorCotizado() {
        return idConductorMenorValorCotizado;
    }

    public void setMenorValorCotizado(String idConductorMenorValorCotizado) {
        this.idConductorMenorValorCotizado = idConductorMenorValorCotizado;
    }

    public int getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(int valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public int getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(int valorMinimo) {
        this.valorMinimo = valorMinimo;
    }


}
