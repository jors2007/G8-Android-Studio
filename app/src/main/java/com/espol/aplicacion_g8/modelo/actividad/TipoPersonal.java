package com.espol.aplicacion_g8.modelo.actividad;

public enum TipoPersonal {
    CITAS("CITA"),
    EJERCICIO("EJERCICIO"),
    HOBBIES("HOBBIES");

    private final String nombre;
    TipoPersonal(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }
}
