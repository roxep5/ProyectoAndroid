package com.example.avalanche;

public class Usuario {
    private String email, nombre, numero;
    private boolean fruteria;
    public Usuario(){

    }


    public Usuario(boolean fruteria,String nombre, String numero) {
        this.fruteria = fruteria;
        this.nombre = nombre;
        this.numero = numero;

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
