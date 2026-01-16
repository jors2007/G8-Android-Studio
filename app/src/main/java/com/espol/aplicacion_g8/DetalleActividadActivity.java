package com.espol.aplicacion_g8;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;
import com.espol.aplicacion_g8.modelo.actividad.ActividadAcademica;
import com.espol.aplicacion_g8.modelo.actividad.ActividadPersonal;

public class DetalleActividadActivity extends AppCompatActivity {

    private TextView tvTitulo, tvNombre, tvTipo, tvAsignaturaLugar,
            tvPrioridad, tvEstado, tvFecha, tvTiempoEstimado, tvAvance;

    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad);

        initViews();

        Actividad actividad = (Actividad) getIntent().getSerializableExtra("actividad");

        if (actividad != null) {
            cargarDatos(actividad);
        }

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

        btnVolver = findViewById(R.id.btnVolver);
    }

    private void cargarDatos(Actividad a) {
        tvTitulo.setText("DETALLES DE LA ACTIVIDAD (ID " + a.getId() + ")");

        tvNombre.setText("Nombre: " + a.getNombre());
        String tipo;

        if (a instanceof ActividadAcademica) {
            tipo = "ACADÉMICA";
        } else {
            tipo = "PERSONAL";
        }

        tvTipo.setText("Tipo: " + tipo);
        tvPrioridad.setText("Prioridad: " + a.getPrioridad());
        tvFecha.setText("Fecha límite: " + a.getFechaLimite());
        tvTiempoEstimado.setText("Tiempo Estimado Total: " + a.getTiempoEstimado() + " minutos");
        tvAvance.setText("Avance Actual: " + a.getAvance() + "%");

        // Estado simple basado en avance
        String estado = a.getAvance() >= 100 ? "Completada" : "En curso";
        tvEstado.setText("Estado: " + estado);

        // Diferenciar académica / personal
        if (a instanceof ActividadAcademica) {
            ActividadAcademica aa = (ActividadAcademica) a;
            tvAsignaturaLugar.setText("Asignatura: " + aa.getAsignatura());
        } else if (a instanceof ActividadPersonal) {
            ActividadPersonal ap = (ActividadPersonal) a;
            tvAsignaturaLugar.setText("Lugar: " + ap.getLugar());
        }
    }
}