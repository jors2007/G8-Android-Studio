package com.espol.aplicacion_g8.modelo.actividad;
import java.util.ArrayList;

public class Actividad {
    protected static int contadorId = 0;
    protected int id = 0;
    protected String nombre;
    protected String descripcion;
    protected String tipoActividad;
    protected Prioridad prioridad;
    protected boolean estado;
    protected String fechaLimite;
    protected double tiempoEstimado;
    protected int avance;
    protected TipoActividad categoria;
    protected ArrayList<SesionEnfoque> historialSesiones;

    // Constructor del Padre
    public Actividad(TipoActividad categoria, String tipoActividad, String nombre, String descripcion,
                     Prioridad prioridad,String fechaLimite, double tiempoEstimado){
        controlId();
        this.id = id++;
        this.categoria = categoria;
        this.tipoActividad = tipoActividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.fechaLimite = fechaLimite;
        this.tiempoEstimado = tiempoEstimado;
        this.avance = 0;
        this.estado = false;
        this.historialSesiones = new ArrayList<>();;
    }

    // Getters
    public int getId(){ return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipoActividad; }
    public Prioridad getPrioridad() { return prioridad; }
    public boolean getEstado() { return estado; }
    public String getDescripcion() { return descripcion; }
    public String getFechaLimite() {return fechaLimite; }
    public double getTiempoEstimado() { return tiempoEstimado; }
    public int getAvance() { return avance; }
    public ArrayList<SesionEnfoque> getHistorialSesiones() { return this.historialSesiones;}

    //Agrega actividades a la seccion
    public void agregarSesion(SesionEnfoque nuevaSesion) {
        this.historialSesiones.add(nuevaSesion);
    }

    // Setters
    public void setAvance(int avance){
        this.avance = avance;
    }
    // METODOS
    public void controlId(){
        this.id = ++contadorId;
    }

    public void setId(int id){
        this.id = id;
    }

    public static void reducirContadorId(){
        contadorId -= 1;
    }

}
