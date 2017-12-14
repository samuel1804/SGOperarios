package pe.com.hatunsol.ferreterias.entity;

/**
 * Created by EfrainSamuelFloresHe on 14/12/2017.
 */

public class BE_Encuesta {
    private int IdEncuesta;
    private int IdCliente;
    private int IdEmpleado;
    private String Comentario;
    private String Nombre;
    private String Fecha;

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getIdEncuesta() {
        return IdEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        IdEncuesta = idEncuesta;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int idCliente) {
        IdCliente = idCliente;
    }

    public int getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        IdEmpleado = idEmpleado;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }
}
