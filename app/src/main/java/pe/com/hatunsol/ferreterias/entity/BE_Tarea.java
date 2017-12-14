package pe.com.hatunsol.ferreterias.entity;

/**
 * Created by EfrainSamuelFloresHe on 13/12/2017.
 */

public class BE_Tarea {
    private int IdTarea;
    private String Nombre_Tarea;

    public int getIdTarea() {
        return IdTarea;
    }

    public void setIdTarea(int idTarea) {
        IdTarea = idTarea;
    }

    public String getNombre_Tarea() {
        return Nombre_Tarea;
    }

    public void setNombre_Tarea(String nombre_Tarea) {
        Nombre_Tarea = nombre_Tarea;
    }

    @Override
    public String toString() {
        return Nombre_Tarea;
    }


    public BE_Tarea() {
    }

    public BE_Tarea(int idTarea, String nombre_Tarea) {
        IdTarea = idTarea;
        Nombre_Tarea = nombre_Tarea;
    }
}
