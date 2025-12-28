package com.espol.aplicacion_g8.modelo.hidratacion;

import java.time.LocalDate;
import java.time.LocalTime;


public class RegistroHidratacion{
    private int cantidad;
    private  LocalDate fecha;
    private  LocalTime hora;


    public RegistroHidratacion(int cantidad, LocalDate fecha, LocalTime hora) {
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getCantidad(){
        return cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }


}

