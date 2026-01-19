package com.espol.aplicacion_g8;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.espol.aplicacion_g8.modelo.sostenibilidad.AccionSostenible;
import com.espol.aplicacion_g8.modelo.sostenibilidad.RegistroSostenibilidad;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RegistroSostenibilidadActivity extends AppCompatActivity {

    private CheckBox chkTransporte, chkImpresiones, chkEnvases, chkReciclaje;
    private RegistroSostenibilidad registro;
    private LocalDate fechaHoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_sostenibilidad);

        registro = RegistroSostenibilidad.cargar(this);
        fechaHoy = LocalDate.now();

        // Vincular con la Interfaz de Usuario (XML)
        TextView tvFecha = findViewById(R.id.tvFechaActual);
        tvFecha.setText("(" + fechaHoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");

        chkTransporte = findViewById(R.id.chkTransporte);
        chkImpresiones = findViewById(R.id.chkImpresiones);
        chkEnvases = findViewById(R.id.chkEnvases);
        chkReciclaje = findViewById(R.id.chkReciclaje);

        Button btnGuardar = findViewById(R.id.btnGuardarRegistro);
        Button btnCancelar = findViewById(R.id.btnCancelarRegistro);

        cargarEstadoPrevio();

        btnGuardar.setOnClickListener(v -> guardarDatos());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarEstadoPrevio() {
        List<AccionSostenible> accionesHoy = registro.getAccionesDia(fechaHoy);

        for (AccionSostenible accion : accionesHoy) {
            String nombreAccion = accion.getAccion();

            if (nombreAccion.equals(RegistroSostenibilidad.ACCIONES_ORDEN[0])) {
                chkTransporte.setChecked(true);
            } else if (nombreAccion.equals(RegistroSostenibilidad.ACCIONES_ORDEN[1])) {
                chkImpresiones.setChecked(true);
            } else if (nombreAccion.equals(RegistroSostenibilidad.ACCIONES_ORDEN[2])) {
                chkEnvases.setChecked(true);
            } else if (nombreAccion.equals(RegistroSostenibilidad.ACCIONES_ORDEN[3])) {
                chkReciclaje.setChecked(true);
            }
        }
    }

    private void guardarDatos() {
        // 1. Creamos una lista
        List<AccionSostenible> listaParaGuardar = new ArrayList<>();

        // Transporte
        if (chkTransporte.isChecked()) {
            listaParaGuardar.add(new AccionSostenible(
                    RegistroSostenibilidad.ACCIONES_ORDEN[0],
                    1, "¡Gran Movilidad!", fechaHoy));
        }

        // Impresiones
        if (chkImpresiones.isChecked()) {
            listaParaGuardar.add(new AccionSostenible(
                    RegistroSostenibilidad.ACCIONES_ORDEN[1],
                    1, "Excelente", fechaHoy));
        }

        // Envases
        if (chkEnvases.isChecked()) {
            listaParaGuardar.add(new AccionSostenible(
                    RegistroSostenibilidad.ACCIONES_ORDEN[2],
                    1, "Necesita mejorar", fechaHoy));
        }

        // Reciclaje
        if (chkReciclaje.isChecked()) {
            listaParaGuardar.add(new AccionSostenible(
                    RegistroSostenibilidad.ACCIONES_ORDEN[3],
                    1, "Muy bien", fechaHoy));
        }

        registro.actualizarAccionesDia(fechaHoy, listaParaGuardar);

        // GUARDAR EN ARCHIVO
        registro.guardar(this);

        Toast.makeText(this, "Registro actualizado correctamente", Toast.LENGTH_SHORT).show();
        finish(); // Al cerrarse, SostenibilidadActivity ejecutará onResume() y leerá los cambios.
    }

    // Método auxiliar para evitar duplicados en el día
    private boolean yaExiste(String nombreAccion) {
        List<AccionSostenible> accionesHoy = registro.getAccionesDia(fechaHoy);
        for (AccionSostenible a : accionesHoy) {
            if (a.getAccion().equals(nombreAccion)) {
                return true;
            }
        }
        return false;
    }
}
