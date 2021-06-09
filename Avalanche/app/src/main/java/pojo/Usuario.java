package pojo;

public class Usuario {
    private String nombre, numero;
    private boolean fruteria;
    public Usuario(){

    }


    public Usuario(boolean fruteria,String nombre, String numero) {
        this.fruteria = fruteria;
        this.nombre = nombre;
        this.numero = numero;

    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isFruteria() {
        return fruteria;
    }

    public void setFruteria(boolean fruteria) {
        this.fruteria = fruteria;
    }

}
