package com.espol.aplicacion_g8;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;
import com.espol.aplicacion_g8.modelo.actividad.ActividadAcademica;
import com.espol.aplicacion_g8.modelo.actividad.ActividadPersonal;
import com.espol.aplicacion_g8.modelo.actividad.SesionEnfoque;

import java.util.ArrayList;

public class DetalleActividadActivity extends AppCompatActivity {

    private TextView tvTitulo, tvNombre, tvTipo, tvAsignaturaLugar,
            tvPrioridad, tvEstado, tvFecha, tvTiempoEstimado, tvAvance;

    private LinearLayout layoutHistorial;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad);

        initViews();

        Actividad recibida = (Actividad) getIntent().getSerializableExtra("actividad");
        if (recibida == null) {
            finish();
            return;
        }

        // ✅ SIEMPRE cargar la más actual desde disco
        Actividad actividad = buscarActividadEnDisco(recibida.getId());
        if (actividad == null) actividad = recibida;

        cargarDatos(actividad);
        pintarHistorial(actividad);

        btnVolver.setOnClickListener(v -> finish());
    }

    private void initViews() {
        tvTitulo = findViewById(R.id.tvTitulo);
        tvNombre = findViewById(R.id.tvNombre);
        tvTipo = findViewById(R.id.tvTipo);
        tvAsignaturaLugar = findViewById(R.id.tvAsignaturaLugar);
        tvPrioridad = findViewById(R.id.tvPrioridad);
        tvEstado = findViewById(R.id.tvEstado);
        tvFecha = findViewById(R.id.tvFecha);
        tvTiempoEstimado = findViewById(R.id.tvTiempoEstimado);
        tvAvance = findViewById(R.id.tvAvance);

        layoutHistorial = findViewById(R.id.layoutHistorial);

        btnVolver = findViewById(R.id.btnVolver);
    }

    private void cargarDatos(Actividad a) {
        tvTitulo.setText("DETALLES DE LA ACTIVIDAD (ID " + a.getId() + ")");

        tvNombre.setText("Nombre: " + a.getNombre());

        String tipo = (a instanceof ActividadAcademica) ? "ACADÉMICA" : "PERSONAL";
        tvTipo.setText("Tipo: " + tipo);

        tvPrioridad.setText("Prioridad: " + a.getPrioridad());
        tvFecha.setText("Fecha límite: " + a.getFechaLimite());
        tvTiempoEstimado.setText("Tiempo Estimado Total: " + a.getTiempoEstimado() + " minutos");
        tvAvance.setText("Avance Actual: " + a.getAvance() + "%");

        String estado = a.getAvance() >= 100 ? "Completada" : "En curso";
        tvEstado.setText("Estado: " + estado);

        if (a instanceof ActividadAcademica) {
            ActividadAcademica aa = (ActividadAcademica) a;
            tvAsignaturaLugar.setText("Asignatura: " + aa.getAsignatura());
        } else if (a instanceof ActividadPersonal) {
            ActividadPersonal ap = (ActividadPersonal) a;
            tvAsignaturaLugar.setText("Lugar: " + ap.getLugar());
        }
    }

    private void pintarHistorial(Actividad a) {
        layoutHistorial.removeAllViews();

        ArrayList<SesionEnfoque> sesiones = a.getHistorialSesiones();
        if (sesiones == null || sesiones.isEmpty()) {
            TextView vacio = new TextView(this);
            vacio.setText("No hay sesiones registradas todavía.");
            vacio.setPadding(8, 12, 8, 12);
            layoutHistorial.addView(vacio);
            return;
        }

        for (SesionEnfoque s : sesiones) {
            LinearLayout fila = new LinearLayout(this);
            fila.setOrientation(LinearLayout.HORIZONTAL);
            fila.setPadding(0, 8, 0, 8);

            TextView colFecha = new TextView(this);
            colFecha.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            colFecha.setText(s.getFecha());

            TextView colTecnica = new TextView(this);
            colTecnica.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            colTecnica.setText(s.getTecnica());

            TextView colDur = new TextView(this);
            colDur.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            colDur.setGravity(Gravity.END);
            colDur.setText(s.getDuracionMin() + " min");

            fila.addView(colFecha);
            fila.addView(colTecnica);
            fila.addView(colDur);

            layoutHistorial.addView(fila);
        }
    }

    private Actividad buscarActividadEnDisco(int id) {
        ArrayList<Actividad> lista = Actividad.cargarActividades(this);
        if (lista == null) return null;

        for (Actividad a : lista) {
            if (a.getId() == id) return a;
        }
        return null;
    }
}