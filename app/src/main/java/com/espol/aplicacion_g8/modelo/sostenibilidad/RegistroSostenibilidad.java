package com.espol.aplicacion_g8.modelo.sostenibilidad;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RegistroSostenibilidad implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String NOMBRE_ARCHIVO = "sostenibilidad_g8.dat";

    public static final String[] ACCIONES_ORDEN = {
            "Usé transporte público, bicicleta o caminé.",
            "No realicé impresiones.",
            "No utilicé envases descartables (usé mi termo/taza).",
            "Separé y reciclé materiales (vidrio, plástico, papel)."
    };

    private final Map<LocalDate, List<AccionSostenible>> registroPorDia;
    private LocalDate fechaReferencia;

    public RegistroSostenibilidad() {
        this.fechaReferencia = LocalDate.now();
        this.registroPorDia = new LinkedHashMap<>();
    }


    public void registrarAccion(AccionSostenible nuevaAccion) {
        if (nuevaAccion == null) return;

        LocalDate fecha = (nuevaAccion.getFecha() == null) ? LocalDate.now() : nuevaAccion.getFecha();

        // Si no existe la lista para ese día, se crea
        if (!registroPorDia.containsKey(fecha)) {
            registroPorDia.put(fecha, new ArrayList<>());
        }

        registroPorDia.get(fecha).add(nuevaAccion);
    }

    public void registrarAcciones(List<AccionSostenible> acciones) {
        if (acciones == null) return;
        for (AccionSostenible accion : acciones) {
            registrarAccion(accion);
        }
    }

    public List<AccionSostenible> getAccionesDia(LocalDate fecha) {
        if (registroPorDia.containsKey(fecha)) {
            return registroPorDia.get(fecha);
        }
        return new ArrayList<>();
    }

    public Map<LocalDate, List<AccionSostenible>> getRegistroPorDia() {
        return registroPorDia;
    }

    // Permite sobrescribir las acciones de un día específico
    public void actualizarAccionesDia(LocalDate fecha, List<AccionSostenible> nuevasAcciones) {
        if (fecha != null) {
            registroPorDia.put(fecha, nuevasAcciones);
        }
    }


    public void guardar(Context context) {
        try {
            File archivo = new File(context.getFilesDir(), NOMBRE_ARCHIVO);
            FileOutputStream fos = new FileOutputStream(archivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this); // Guarda ESTE objeto completo
            oos.close();
            fos.close();
            Log.d("Sostenibilidad", "Datos guardados correctamente en " + archivo.getAbsolutePath());
        } catch (Exception e) {
            Log.e("Sostenibilidad", "Error al guardar: " + e.getMessage());
        }
    }

    public static RegistroSostenibilidad cargar(Context context) {
        RegistroSostenibilidad registro = null;
        File archivo = new File(context.getFilesDir(), NOMBRE_ARCHIVO);

        if (archivo.exists()) {
            try {
                FileInputStream fis = new FileInputStream(archivo);
                ObjectInputStream ois = new ObjectInputStream(fis);
                registro = (RegistroSostenibilidad) ois.readObject();
                ois.close();
                fis.close();
                Log.d("Sostenibilidad", "Datos cargados exitosamente.");
            } catch (Exception e) {
                Log.e("Sostenibilidad", "Error al leer archivo: " + e.getMessage());
            }
        }

        // Si no hay archivo (la primera vez) o falló la carga, crear uno nuevo con datos iniciales
        if (registro == null) {
            registro = new RegistroSostenibilidad();
            registro.inicializarApp(); // Cargar requerimientos del PDF
            registro.guardar(context); // Guardar inmediatamente
        }

        return registro;
    }

    // DATOS DE PRUEBA
    public void inicializarApp() {
        int year = LocalDate.now().getYear();

        // Registro del 17 de Enero (Cumple especificación)
        LocalDate fecha17 = LocalDate.of(year, 1, 17);
        this.registrarAccion(new AccionSostenible(ACCIONES_ORDEN[0], 1, "¡Gran Movilidad!", fecha17));

        // Registro del 18 de Enero (Cumple especificación)
        LocalDate fecha18 = LocalDate.of(year, 1, 18);
        this.registrarAccion(new AccionSostenible(ACCIONES_ORDEN[1], 1, "Excelente", fecha18));

        Log.d("Sostenibilidad", "inicializarApp: Datos del 17 y 18 de Enero creados.");
    }
}
