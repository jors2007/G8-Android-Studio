package com.espol.aplicacion_g8.modelo.juego;


public class Carta {
    private int id;
    private int idImagen;
    private String nombre;
    private String descripcion;
    private boolean emparejada;
    private boolean visible;

    public Carta(int id, int idImagen, String nombre, String descripcion) {
        this.id = id;
        this.idImagen = idImagen;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.emparejada = false;
        this.visible = false;
    }

    // Getters y Setters
    public int getId() { return id; }
    public int getIdImagen() { return idImagen; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public boolean isEmparejada() { return emparejada; }
    public boolean isVisible() { return visible; }

    public void setEmparejada(boolean emparejada) { this.emparejada = emparejada; }
    public void setVisible(boolean visible) { this.visible = visible; }
}
