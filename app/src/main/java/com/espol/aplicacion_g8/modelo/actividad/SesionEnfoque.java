package com.espol.aplicacion_g8.modelo.actividad;

import java.io.Serializable;

public class SesionEnfoque implements Serializable {
    private String fecha;
    private String tecnica;
    private int duracionMin;

    public SesionEnfoque(String fecha, String tecnica, int duracionMin) {
        this.fecha = fecha;
        this.tecnica = tecnica;
        this.duracionMin = duracionMin;
    }

    public String getFecha() { return fecha; }
    public String getTecnica() { return tecnica; }
    public int getDuracionMin() { return duracionMin; }
}