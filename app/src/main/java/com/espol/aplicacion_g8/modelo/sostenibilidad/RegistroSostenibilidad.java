package com.espol.aplicacion_g8.modelo.sostenibilidad;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RegistroSostenibilidad {

    private static final String[] ACCIONES_ORDEN = {
            "Usé transporte público, bicicleta o caminé.",
            "No realicé impresiones.",
            "No utilicé envases descartables (usé mi termo/taza).",
            "Separé y reciclé materiales (vidrio, plástico, papel)."
    };

    private final Map<LocalDate, List<AccionSostenible>> registroPorDia;
    private LocalDate fechaReferencia;

    public RegistroSostenibilidad(String fecha) {
        this.fechaReferencia = (fecha == null) ? LocalDate.now() : LocalDate.parse(fecha);
        this.registroPorDia = new LinkedHashMap<>();
    }

    // Registra UNA acción en su fecha
    public void registrarAccion(AccionSostenible nuevaAccion) {
        if (nuevaAccion == null) {
            return;
        }
        LocalDate fecha = (nuevaAccion.getFecha() == null) ? LocalDate.now() : nuevaAccion.getFecha();
        registroPorDia.computeIfAbsent(fecha, f -> new ArrayList<>()).add(nuevaAccion);
    }

    // Registra varias acciones
    public void registrarAcciones(List<AccionSostenible> acciones) {
        if (acciones == null) {
            return;
        }
        for (AccionSostenible accion : acciones) {
            registrarAccion(accion);
        }
    }

    public List<AccionSostenible> getAccionesDia(LocalDate fecha) {
        return registroPorDia.getOrDefault(fecha, new ArrayList<>());
    }

    public Map<LocalDate, List<AccionSostenible>> getRegistroPorDia() {
        return registroPorDia;
    }

    public void mostrarAcciones() {
        if (registroPorDia.isEmpty()) {
            System.out.println("No hay acciones registradas.");
            return;
        }
        System.out.println("=== ACCIONES REGISTRADAS ===");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Map.Entry<LocalDate, List<AccionSostenible>> entry : registroPorDia.entrySet()) {
            System.out.println("Fecha: " + entry.getKey().format(formato));
            for (AccionSostenible a : entry.getValue()) {
                System.out.println("- " + a.getAccion() + " | Puntos: "
                        + a.getPuntosSostenibilidad() + " | Logro: " + a.getLogro());
            }
        }
    }

    // RESUMEN SEMANAL
    public void mostrarEstadisticas() {
        if (registroPorDia.isEmpty()) {
            System.out.println("No hay acciones registradas esta semana.");
            return;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate inicio = hoy.minusDays(6);
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Inicializar frecuencia de cada acción
        Map<String, Integer> frecuenciaAcciones = new LinkedHashMap<>();
        for (String accion : ACCIONES_ORDEN) {
            frecuenciaAcciones.put(accion, 0);
        }

        int diasConAlMenosUnaAccion = 0;
        int diasConCuatroAcciones = 0;

        // Recorremos los días con registro dentro de la semana
        for (Map.Entry<LocalDate, List<AccionSostenible>> entry : registroPorDia.entrySet()) {
            LocalDate fecha = entry.getKey();
            if (fecha.isBefore(inicio) || fecha.isAfter(hoy)) {
                continue;
            }
            List<AccionSostenible> accionesDia = entry.getValue();
            boolean[] presencia = new boolean[ACCIONES_ORDEN.length];

            for (AccionSostenible a : accionesDia) {
                // Sumar frecuencia
                frecuenciaAcciones.merge(a.getAccion(), a.getVecesRealizada(), Integer::sum);
                int idx = indexAccion(a.getAccion());
                if (idx >= 0) {
                    presencia[idx] = true;
                }
            }

            int presentes = 0;
            for (boolean p : presencia) {
                if (p) presentes++;
            }
            if (presentes > 0) {
                diasConAlMenosUnaAccion++;
            }
            if (presentes == ACCIONES_ORDEN.length) {
                diasConCuatroAcciones++;
            }
        }

        System.out.println();
        System.out.println("--- RESUMEN SEMANAL DE SOSTENIBILIDAD ("
                + inicio.format(formatoFecha) + " - " + hoy.format(formatoFecha) + ") ---");
        System.out.println("----------------------------------------------------------------------");
        System.out.println("FRECUENCIA DE ACCIONES");
        System.out.println("----------------------------------------------------------------------");
        System.out.println("ACCIÓN                                       | VECES REALIZADA | LOGRO");
        System.out.println("--------------------------------------------------------------|-----------------|---------");

        // Mostrar cada acción en orden
        for (String accionOrdenada : ACCIONES_ORDEN) {
            int veces = frecuenciaAcciones.getOrDefault(accionOrdenada, 0);
            int vecesCapped = Math.min(veces, 7); // Máximo 7/7 días
            String logro = calcularLogro(vecesCapped, accionOrdenada);
            System.out.printf("%-62s| %-15s | %s%n",
                    accionOrdenada,
                    vecesCapped + " / 7 Días",
                    logro);
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.println("ANÁLISIS ECOLÓGICO");
        System.out.println("----------------------------------------------------------------------");

        int diasTotales = 7;
        int diasAccionCapped = Math.min(diasConAlMenosUnaAccion, diasTotales);
        int diasCuatroCapped = Math.min(diasConCuatroAcciones, diasTotales);
        System.out.println("Días con al menos 1 acción de sostenibilidad: "
                + diasAccionCapped + " de " + diasTotales + " ("
                + (diasAccionCapped * 100 / diasTotales) + "%)");
        System.out.println("Días con las 4 acciones completas: "
                + diasCuatroCapped + " de " + diasTotales + " ("
                + (diasCuatroCapped * 100 / diasTotales) + "%)");
        System.out.println();
        System.out.println("Tip Ecológico de la Semana: Para aumentar tu puntaje de \"Envases descartables\", "
                + "ten siempre tu botella reutilizable a la mano antes de salir.");
        System.out.println("----------------------------------------------------------------------");
    }

    // Decide el texto de LOGRO según la frecuencia semanal
    private String calcularLogro(int vecesSemana, String accion){
        if (vecesSemana >= 7){
            return "Excelente";
        }
        if (vecesSemana >= 5){
            if ("Usé transporte público, bicicleta o caminé.".equals(accion)){
                return "¡Gran Movilidad!";
            }
            return "Muy bien";
        }
        if (vecesSemana >= 4){
            return "Necesita mejorar";
        }
        return "Por mejorar";
    }

    private int indexAccion(String accion){
        for (int i = 0; i < ACCIONES_ORDEN.length; i++){
            if (ACCIONES_ORDEN[i].equals(accion)){
                return i;
            }
        }
        return -1;
    }

    public LocalDate getFechaReferencia() {
        return fechaReferencia;
    }

    public void setFechaReferencia(LocalDate fechaReferencia) {
        this.fechaReferencia = fechaReferencia;
    }
}
