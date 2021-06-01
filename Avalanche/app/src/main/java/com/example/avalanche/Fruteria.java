package com.example.avalanche;

public class Fruteria {
    private String direccion;
    private String email;
    private String nombre;
    private String usuario;

    public Fruteria() {
    }

    public Fruteria(String direccion, String email, String nombre, String usuario) {
        this.direccion = direccion;
        this.email = email;
        this.nombre = nombre;
        this.usuario = usuario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
