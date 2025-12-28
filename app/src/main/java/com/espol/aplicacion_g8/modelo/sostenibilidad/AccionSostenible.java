package com.espol.aplicacion_g8.modelo.sostenibilidad;

import java.time.LocalDate;

public class AccionSostenible {
    private String accion;
    private int puntosSostenibilidad;
    private int vecesRealizada;
    private String logro;
    private LocalDate fecha;

    public AccionSostenible() {
        this.fecha = LocalDate.now();
    }

    public AccionSostenible(String accion, int puntosSostenibilidad, String logro, LocalDate fecha) {
        this.accion = accion;
        this.puntosSostenibilidad = puntosSostenibilidad;
        this.vecesRealizada = 1;
        this.logro = logro;
        this.fecha = (fecha == null) ? LocalDate.now() : fecha;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public int getPuntosSostenibilidad() {
        return puntosSostenibilidad;
    }

    public void setPuntosSostenibilidad(int puntosSostenibilidad) {
        this.puntosSostenibilidad = puntosSostenibilidad;
    }

    public int getVecesRealizada() {
        return vecesRealizada;
    }

    public void setVecesRealizada(int vecesRealizada) {
        this.vecesRealizada = vecesRealizada;
    }

    public String getLogro() {
        return logro;
    }

    public void setLogro(String logro) {
        this.logro = logro;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
