package pe.com.hatunsol.ferreterias.entity;

/**
 * Created by Samuel on 2/12/2017.
 */

public class BE_Empleado {
    private int IdEmpleado;
    private String Nombres_Operario;
    private String Telefono_Operario;
    private String Latitud;
    private String Longitud;
    private String Disponibilidad;
    private String Distrito;
    private int Experiencia_Operario;
    private String NumDocId_Operario;
    private int IdServicio;

    public int getIdServicio() {
        return IdServicio;
    }

    public void setIdServicio(int idServicio) {
        IdServicio = idServicio;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    private String Foto;

    public String getNumDocId_Operario() {
        return NumDocId_Operario;
    }

    public void setNumDocId_Operario(String numDocId_Operario) {
        NumDocId_Operario = numDocId_Operario;
    }

    public int getExperiencia_Operario() {
        return Experiencia_Operario;
    }

    public void setExperiencia_Operario(int experiencia_Operario) {
        Experiencia_Operario = experiencia_Operario;
    }

    public String getDistrito() {
        return Distrito;
    }

    public void setDistrito(String distrito) {
        Distrito = distrito;
    }

    public String getDisponibilidad() {
        return Disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        Disponibilidad = disponibilidad;
    }

    public String getServicio() {
        return Servicio;
    }

    public void setServicio(String servicio) {
        Servicio = servicio;
    }

    private String Servicio;

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public int getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        IdEmpleado = idEmpleado;
    }

    public String getNombres_Operario() {
        return Nombres_Operario;
    }

    public void setNombres_Operario(String nombres_Operario) {
        Nombres_Operario = nombres_Operario;
    }

    public String getTelefono_Operario() {
        return Telefono_Operario;
    }

    public void setTelefono_Operario(String telefono_Operario) {
        Telefono_Operario = telefono_Operario;
    }
}
