package com.espol.aplicacion_g8;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;

public interface OnActividadActionListener {
    void onDetalles(Actividad a);
    void onRegistrarAvance(Actividad a);
    void onEliminar(Actividad a);
}