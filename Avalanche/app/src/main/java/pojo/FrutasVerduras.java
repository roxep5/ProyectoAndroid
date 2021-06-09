package pojo;

public class FrutasVerduras {
    private String imagen;
    private String nombre;
    private float precio;

    public FrutasVerduras() {

    }

    public FrutasVerduras(String imagen, String nombre, float precio) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
