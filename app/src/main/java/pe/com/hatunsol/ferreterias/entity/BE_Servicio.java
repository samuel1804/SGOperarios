package pe.com.hatunsol.ferreterias.entity;

/**
 * Created by Samuel on 3/12/2017.
 */

public class BE_Servicio {
    private int IdServicio;
    private String Desc_Serv;


    public int getIdServicio() {
        return IdServicio;
    }

    public void setIdServicio(int idServicio) {
        IdServicio = idServicio;
    }

    public String getDesc_Serv() {
        return Desc_Serv;
    }

    public void setDesc_Serv(String desc_Serv) {
        Desc_Serv = desc_Serv;
    }

    public BE_Servicio(int idServicio, String desc_Serv) {
        IdServicio = idServicio;
        Desc_Serv = desc_Serv;
    }

    public BE_Servicio() {
    }
}
