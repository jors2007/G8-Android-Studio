package com.espol.aplicacion_g8;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;

public interface OnSesionClickListener {
    void onPomodoroClick(Actividad actividad);
    void onDeepWorkClick(Actividad actividad);
}