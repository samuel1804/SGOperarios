package pe.com.hatunsol.ferreterias.entity;

/**
 * Created by Samuel on 15/12/2017.
 */

public class BE_EncuestaRepuesta {
    private String Criterio;
    private Double Puntaje;

    public String getCriterio() {
        return Criterio;
    }

    public void setCriterio(String criterio) {
        Criterio = criterio;
    }

    public Double getPuntaje() {
        return Puntaje;
    }

    public void setPuntaje(Double puntaje) {
        Puntaje = puntaje;
    }
}
