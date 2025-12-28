package com.espol.aplicacion_g8.modelo.actividad;

public class ActividadPersonal extends Actividad {
    private String lugar;

    public ActividadPersonal(TipoActividad categoria,
                             String tipo,
                             String nombre,
                             String descripcion,
                             Prioridad prioridad,
                             String fechaLimite,
                             double tiempoEstimado,
                             String lugar){
        super(TipoActividad.PERSONAL, tipo, nombre, descripcion, prioridad, fechaLimite, tiempoEstimado);
        this.lugar = lugar;
    }

    public String getLugar(){
        return lugar;
    }


}
