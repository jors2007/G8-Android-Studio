package com.espol.aplicacion_g8.controlador;

import java.util.ArrayList;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;
import com.espol.aplicacion_g8.modelo.actividad.ActividadAcademica;
import com.espol.aplicacion_g8.modelo.actividad.ActividadPersonal;
import com.espol.aplicacion_g8.modelo.actividad.SesionEnfoque;
import com.espol.aplicacion_g8.modelo.actividad.TipoAcademico;
import com.espol.aplicacion_g8.modelo.actividad.TipoActividad;
import com.espol.aplicacion_g8.modelo.actividad.TipoPersonal;
import com.espol.aplicacion_g8.modelo.actividad.Prioridad;

public class ControladorActividades  {

    // LISTA DE ACTIVIDADES
    static ArrayList<Actividad> listaActividades = new ArrayList<>();
    public static ArrayList<Actividad> getListaActividades() {
        return listaActividades;
    }

    // LISTA DE SESIONES DE ENFOQUE
    static ArrayList<SesionEnfoque> listaSesionesEnfoques = new ArrayList<>();
    public static ArrayList<SesionEnfoque> getListaSesionesEnfoques() {
        return listaSesionesEnfoques;
    }

    // OBTENER ACTIVIDADES
    public Actividad obtenerActividad(int ID) {
        for (Actividad actividad : listaActividades) {
            if (actividad.getId() == ID) {
                return actividad;
            }
        }
        return null;
    }

    // CREACION DE ACTIVIDADES
    public void creacionDeActividadAcademica(String tipo,String nombre,String asignatura, String descripcion,
                                             Prioridad prioridad, String fechaLimite, double tiempoEstimado){
        ActividadAcademica actividad = new ActividadAcademica(TipoActividad.ACADEMICA,tipo,nombre,asignatura,descripcion,prioridad,fechaLimite,tiempoEstimado);
        añadirActividad(actividad);
    }

    public void creacionDeActividadPersonal(String tipo, String nombre, String detalles,
                                            Prioridad prioridad, String fechaLimite, double tiempoEstimado, String lugar){
        ActividadPersonal actividad = new ActividadPersonal(TipoActividad.PERSONAL,tipo,nombre,detalles,prioridad,fechaLimite,tiempoEstimado,lugar);
        añadirActividad(actividad);
    }

    // AÑADE NUEVA ACTIVIDAD
    public void añadirActividad(Actividad actividad) {
        listaActividades.add(actividad);
    }

    // ELIMINA ACTIVIDAD
    public void eliminarActividad(int ID) {
        listaActividades.removeIf(actividad -> ID == actividad.getId());
    }

    // ENCUENTRA LA ACTIVIDAD POR EL ID
    public static Actividad encuentraActividad(ArrayList<Actividad> actividades, int id) {
        Actividad actividadEncontrada = null;
        for (Actividad actividad : actividades) {
            if (actividad.getId() == id) {
                actividadEncontrada = actividad;
                return actividadEncontrada;
            }
        }
        return actividadEncontrada;
    }

    // IDENTIFICADOR DEL TIPO DE ACTIVIDAD
    public void verificarTipoDeActividad(int ID, ArrayList<Actividad> listaActividad) {
        Actividad actividad = null;
        for (Actividad actividadSeleccionada : listaActividad) {
            if (actividadSeleccionada.getId() == ID) {
                actividad = actividadSeleccionada;
            }
        }
    }

    // ETIQUETAS DE AVANCE
    public static String verificarAvance(double avance) {
        if (avance != 100) {
            return "En curso";
        } else {
            return "Finalizado";
        }
    }

    // MODIFICADOR DE AVANCE
    public void modificadorAvance(Actividad actividad, int avance) {
        actividad.setAvance(avance);
    }
}
