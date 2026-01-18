package com.espol.aplicacion_g8.modelo.actividad;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Actividad implements Serializable {

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
                     Prioridad prioridad, String fechaLimite, double tiempoEstimado) {
        this.categoria = categoria;
        this.tipoActividad = tipoActividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.fechaLimite = fechaLimite;
        this.tiempoEstimado = tiempoEstimado;
        this.avance = 0;
        this.estado = false;
        this.historialSesiones = new ArrayList<>();
        ;
    }
    public Actividad(int id, String nombre, String fechaLimite, Prioridad prioridad,int avance, String tipoActividad){
        this.id = id;
        this.nombre = nombre;
        this.fechaLimite= fechaLimite;
        this.prioridad = prioridad;
        this.avance = avance;
        this.tipoActividad = tipoActividad;
    }

    public Actividad(int id,TipoActividad categoria, String tipoActividad, String nombre, String descripcion,
                     Prioridad prioridad, String fechaLimite, double tiempoEstimado){
        // mucho ojo con el this
        this(categoria,tipoActividad,nombre,descripcion,prioridad,fechaLimite,tiempoEstimado);
        this.id = id;

    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipoActividad;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public boolean getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public double getTiempoEstimado() {
        return tiempoEstimado;
    }

    public int getAvance() {
        return avance;
    }

    public ArrayList<SesionEnfoque> getHistorialSesiones() {
        return this.historialSesiones;
    }

    //Agrega actividades a la seccion
    public void agregarSesion(SesionEnfoque nuevaSesion) {
        this.historialSesiones.add(nuevaSesion);
    }

    // Setters
    public void setAvance(int avance) {
        this.avance = avance;
    }



    //  SERIALIZACION
    public static void guardarActividades(Context context, ArrayList<Actividad> actividades) {
        try {
            FileOutputStream fos = context.openFileOutput("Actividades.ser", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(actividades);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DESERIALIZAR
    public static ArrayList<Actividad> cargarActividades(Context context) {
        ArrayList<Actividad> actividades = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput("Actividades.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            actividades = (ArrayList<Actividad>) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actividades;
    }

    public static int obtenerNuevoID(Context context){
        SharedPreferences prefs =
                context.getSharedPreferences("contador_ids",Context.MODE_PRIVATE);
        int ultimoId = prefs.getInt("ultimo_id",0);
        int nuevoId = ultimoId +1;

        prefs.edit().putInt("ultimo_id", nuevoId).apply();
        return nuevoId;
    }
}