package com.espol.aplicacion_g8.modelo.actividad;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Actividad implements Serializable {
    public static int contadorId = 0;
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
        controlId();
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

        // AÑADE ESTA LÍNEA PARA ACTUALIZAR EL CONTADOR:
        if (id > contadorId) {
            contadorId = id;
        }
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

    // METODOS
    public void controlId() {
        contadorId++;
        this.id = contadorId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void reducirContadorId() {
        contadorId -= 1;
    }

    public static int obtenerContadorId(){
        return contadorId;
    }

    //  SERIALIZA
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

    // DESERIALIZA
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
}