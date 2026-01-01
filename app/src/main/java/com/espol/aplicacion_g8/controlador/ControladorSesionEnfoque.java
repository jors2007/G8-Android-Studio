package com.espol.aplicacion_g8.controlador;

import java.util.ArrayList;
import com.espol.aplicacion_g8.modelo.actividad.Actividad;
import com.espol.aplicacion_g8.modelo.actividad.SesionEnfoque;

public class ControladorSesionEnfoque {

    public void sesionPomodoro(Actividad actividad){
        SesionEnfoque sesion = new SesionEnfoque(SesionEnfoque.Tecnicas.POMODORO, 25);
        actividad.agregarSesion(sesion);
        actividad.setAvance(100);
    }
    public void sesionDeepwork(Actividad actividad){
        SesionEnfoque sesion = new SesionEnfoque(SesionEnfoque.Tecnicas.DEEPWORK, 90);
        actividad.agregarSesion(sesion);
        actividad.setAvance(100);
    }
}
