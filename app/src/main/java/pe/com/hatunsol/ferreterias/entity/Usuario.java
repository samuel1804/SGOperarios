package pe.com.hatunsol.ferreterias.entity;

/**
 * Created by Sistemas on 02/03/2015.
 */
public class Usuario {

private int UserId;
    private String UserLogin;
    private String UserPassword;
    private Rol Rol;
    private int EmpleadoId;
    private Boolean IsActive;
    private String Celular;
    private String Nombre;

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }

    public Boolean getActive() {
        return IsActive;
    }

    public void setActive(Boolean active) {
        IsActive = active;
    }

    public int getEmpleadoId() {
        return EmpleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        EmpleadoId = empleadoId;
    }


    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserLogin() {
        return UserLogin;
    }

    public void setUserLogin(String userLogin) {
        UserLogin = userLogin;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public pe.com.hatunsol.ferreterias.entity.Rol getRol() {
        return Rol;
    }

    public void setRol(pe.com.hatunsol.ferreterias.entity.Rol rol) {
        Rol = rol;
    }

    @Override
    public String toString() {
        /*return "Distrito{reniec='" + reniec + '\'' +
                ", inei='" + inei + "\', nombre='" + nombre + '\'' +
                ", nombreCompleto='" + nombreCompleto + "\'}";*/
        return UserPassword;
    }
}
