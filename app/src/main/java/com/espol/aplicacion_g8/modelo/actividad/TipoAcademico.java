package com.espol.aplicacion_g8.modelo.actividad;

public enum TipoAcademico {
    TAREA("TAREA"),
    EXAMEN("EXAMEN"),
    PROYECTO("PROYECTO");
    private final String nombre;
    TipoAcademico(String nombre){
        this.nombre = nombre;
    }
    public String getNombre(){
        return nombre;
    }
}

