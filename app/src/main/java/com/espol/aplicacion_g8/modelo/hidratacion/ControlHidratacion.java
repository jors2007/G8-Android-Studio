package com.espol.aplicacion_g8.modelo.hidratacion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ControlHidratacion{
    private final List<RegistroHidratacion> registros;
    private int metaDiaria;


    public ControlHidratacion() {
        this.registros = new ArrayList<>();
        this.metaDiaria = 2500;
        //Como se inicia desde 0 ponemos meta por defecto 2500
    }

    public boolean registrarHidratacion(int cantidad){
        if(cantidad<=0){
            return false;
        }
        else{
            registros.add(new RegistroHidratacion(cantidad,LocalDate.now(), LocalTime.now()));
            return true;
        }
    }
    public boolean establecerMetaDiaria(int nuevaMeta) {
        if (nuevaMeta <= 0){
            return false;
        }
        else{
            metaDiaria = nuevaMeta;
            return true;
        }

    }
    public int getAcumuladoHoy() {
        int totalHoy = 0;
        LocalDate hoy = LocalDate.now();
        for (RegistroHidratacion r : registros) {
            if (r.getFecha().equals(hoy)){
                totalHoy += r.getCantidad();
            }
        }
        return totalHoy;
    }

    public double getProgreso() {
        if (metaDiaria == 0) return 0;
        return ((double) getAcumuladoHoy() / metaDiaria) * 100;
    }

    public int getMetaDiaria() {
        return metaDiaria;
    }

    public List<RegistroHidratacion> getRegistrosHoy() {
        LocalDate hoy = LocalDate.now();
        List<RegistroHidratacion> hoyList = new ArrayList<>();
        for (RegistroHidratacion r : registros) {
            if (r.getFecha().equals(hoy)) {
                hoyList.add(r);
            }
        }
        return hoyList;
    }
    public boolean registrarHidratacion(int cantidad, LocalDate fecha, LocalTime hora) {
        if (cantidad <= 0) {
            return false;
        }
        registros.add(new RegistroHidratacion(cantidad, fecha, hora));
        return true;
    }
}