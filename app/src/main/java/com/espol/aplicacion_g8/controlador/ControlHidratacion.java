package com.espol.aplicacion_g8.controlador;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
public class ControlHidratacion {
    private final List<RegistroHidratacion> registros;
    private int metaDiaria;

    public ControlHidratacion() {
        this.registros = new ArrayList<>();
        this.metaDiaria = 2500;
    }

    // Método para carga de datos iniciales
    public boolean registrarHidratacion(int cantidad, LocalDate fecha, LocalTime hora) {
        if (cantidad <= 0) return false;
        registros.add(new RegistroHidratacion(cantidad, fecha, hora));
        return true;
    }

    // Método para registrar HOY
    public boolean registrarHidratacion(int cantidad) {
        return registrarHidratacion(cantidad, LocalDate.now(), LocalTime.now());
    }

    public boolean establecerMetaDiaria(int nuevaMeta) {
        if (nuevaMeta <= 0) return false;
        this.metaDiaria = nuevaMeta;
        return true;
    }

    public int getMetaDiaria() { return metaDiaria; }

    public int getAcumuladoHoy() {
        int total = 0;
        LocalDate hoy = LocalDate.now();
        for (RegistroHidratacion r : registros) {
            if (r.getFecha().equals(hoy)) {
                total += r.getCantidad();
            }
        }
        return total;
    }

    public double getProgreso() {
        if (metaDiaria == 0) return 0;
        return ((double) getAcumuladoHoy() / metaDiaria) * 100;
    }

    public List<RegistroHidratacion> getRegistrosHoy() {
        List<RegistroHidratacion> hoyList = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        for (RegistroHidratacion r : registros) {
            if (r.getFecha().equals(hoy)) {
                hoyList.add(r);
            }
        }
        return hoyList;
    }

}
