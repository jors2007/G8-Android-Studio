package com.espol.aplicacion_g8.modelo.juego;

public class Carta {

    private int id;
    private String contenido;
    private boolean descubierta;
    private boolean emparejada;

    //Constructor
    public Carta(int id, String contenido){
        this.id = id;
        this.contenido = contenido;
        this.descubierta = false; //Se debe iniciar cubierta
        this.emparejada = false; //se debe iniciar no emparejada
    }

    //Getters && Setters
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getContenido(){
        return contenido;
    }

    public void setContenido(String contenido){
        this.contenido = contenido;
    }

    public boolean isDescubierta(){
        return descubierta;
    }

    public void setDescubierta(boolean descubierta){
        this.descubierta = descubierta;
    }

    public boolean isEmparejada(){
        return emparejada;
    }

    public void setEmparejada(boolean emparejada){
        this.emparejada = emparejada;
    }

}