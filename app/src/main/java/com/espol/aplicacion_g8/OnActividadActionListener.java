package com.espol.aplicacion_g8;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;

public interface OnActividadActionListener {
    void onEliminar(Actividad actividad);
    void onDetalles(Actividad actividad);
    void onRegistrarAvance(Actividad actividad);

    // ✅ nuevos
    void onPomodoro(Actividad actividad);
    void onDeepWork(Actividad actividad);
}