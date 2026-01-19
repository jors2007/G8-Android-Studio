package com.espol.aplicacion_g8.modelo.actividad;

public class ActividadAcademica extends Actividad {
    private String asignatura;
    public ActividadAcademica(int id,
                              TipoActividad categoria,
                              String tipo,
                              String nombre,
                              String asignatura,
                              String descripcion,
                              Prioridad prioridad,
                              String fechaLimite,
                              double tiempoEstimado
    ){
        super(id,TipoActividad.ACADEMICA, tipo, nombre, descripcion, prioridad, fechaLimite, tiempoEstimado);
        this.asignatura = asignatura;
    }
    public String getAsignatura(){
        return asignatura;
    }
}