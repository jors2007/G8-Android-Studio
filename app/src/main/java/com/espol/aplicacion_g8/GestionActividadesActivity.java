package com.espol.aplicacion_g8;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;

import java.util.ArrayList;

public class GestionActividadesActivity extends AppCompatActivity {

    private ActividadAdapter adapter;
    private ArrayList<Actividad> actividades;

    // Formulario: crea actividad
    private final ActivityResultLauncher<Intent> launcherFormulario =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Actividad nueva = (Actividad) result.getData().getSerializableExtra("actividad");
                            if (nueva != null) {
                                actividades.add(nueva);
                                adapter.notifyItemInserted(actividades.size() - 1);
                                Actividad.guardarActividades(this, actividades);
                            }
                        }
                    }
            );

    // Registrar avance
    private final ActivityResultLauncher<Intent> launcherAvance =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            int idActividad = result.getData().getIntExtra("idActividad", -1);
                            int nuevoAvance = result.getData().getIntExtra("nuevoAvance", -1);

                            if (idActividad == -1 || nuevoAvance == -1) return;

                            for (int i = 0; i < actividades.size(); i++) {
                                if (actividades.get(i).getId() == idActividad) {
                                    actividades.get(i).setAvance(nuevoAvance);
                                    adapter.notifyItemChanged(i);
                                    Actividad.guardarActividades(this, actividades);
                                    Toast.makeText(this, "Avance actualizado", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                    }
            );

    // Sesión enfoque (Pomodoro/DeepWork): regresa actividad completa actualizada
    private final ActivityResultLauncher<Intent> launcherSesion =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                            Actividad actActualizada =
                                    (Actividad) result.getData().getSerializableExtra("actividadActualizada");

                            if (actActualizada == null) return;

                            // Reemplazar actividad en el ArrayList
                            for (int i = 0; i < actividades.size(); i++) {
                                if (actividades.get(i).getId() == actActualizada.getId()) {
                                    actividades.set(i, actActualizada);
                                    adapter.notifyItemChanged(i);
                                    break;
                                }
                            }

                            // Guardar persistente
                            Actividad.guardarActividades(this, actividades);

                            Toast.makeText(this, "Sesión guardada", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_actividades);

        // Cargar SOLO UNA VEZ
        actividades = Actividad.cargarActividades(this);
        if (actividades == null) actividades = new ArrayList<>();

        Button btnAgregar = findViewById(R.id.button_agregarActividad);
        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormularioActividadActivity.class);
            launcherFormulario.launch(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView_actividad);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ActividadAdapter(actividades, new OnActividadActionListener() {

            @Override
            public void onEliminar(Actividad actividad) {
                new AlertDialog.Builder(GestionActividadesActivity.this)
                        .setTitle("Eliminar actividad")
                        .setMessage("¿Desea eliminar la actividad:\n" + actividad.getNombre() + "?")
                        .setPositiveButton("Sí", (d, w) -> {
                            actividades.remove(actividad);
                            adapter.notifyDataSetChanged();
                            Actividad.guardarActividades(GestionActividadesActivity.this, actividades);
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }

            @Override
            public void onDetalles(Actividad actividad) {
                Intent intent = new Intent(GestionActividadesActivity.this, DetalleActividadActivity.class);
                intent.putExtra("actividad", actividad);
                startActivity(intent);
            }

            @Override
            public void onRegistrarAvance(Actividad actividad) {
                Intent intent = new Intent(GestionActividadesActivity.this, RegistrarAvanceActivity.class);
                intent.putExtra("actividad", actividad);
                launcherAvance.launch(intent);
            }

            @Override
            public void onPomodoro(Actividad actividad) {
                Intent intent = new Intent(GestionActividadesActivity.this, PomodoroActivity.class);
                intent.putExtra("actividad", actividad);
                launcherSesion.launch(intent);
            }

            @Override
            public void onDeepWork(Actividad actividad) {
                Intent intent = new Intent(GestionActividadesActivity.this, DeepWorkActivity.class);
                intent.putExtra("actividad", actividad);
                launcherSesion.launch(intent);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (actividades != null) {
            Actividad.guardarActividades(this, actividades);
        }
    }
}