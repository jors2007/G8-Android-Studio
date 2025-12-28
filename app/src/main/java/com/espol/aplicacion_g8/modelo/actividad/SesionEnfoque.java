package com.espol.aplicacion_g8.modelo.actividad;

import java.time.LocalDate;

public class SesionEnfoque{
    private Tecnicas tecnicaDeEnfoque;
    private LocalDate fechaDeSesion;
    private int tiempo; //
    private int ciclos;

    public enum Tecnicas {
        POMODORO,
        DEEPWORK,
    }

    public SesionEnfoque(Tecnicas tecnicaDeEnfoque , int duración){
        this.tecnicaDeEnfoque = tecnicaDeEnfoque;
        this.fechaDeSesion = LocalDate.now(); //Entrega la fecha de hoy directamente
        this.tiempo = duración;
        this.ciclos = 1;
    }
    public Tecnicas getTipoEnfoque(){return tecnicaDeEnfoque;}
    public int getTiempoEnfoque(){return tiempo;}
    public LocalDate getFechaDeSesion(){return fechaDeSesion; }
    public int getCiclos(){return ciclos;}
    public void setCiclos(int ciclos){this.ciclos = ciclos;}
}