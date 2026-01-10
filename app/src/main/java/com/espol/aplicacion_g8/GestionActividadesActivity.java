package com.espol.aplicacion_g8;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
import com.espol.aplicacion_g8.modelo.actividad.Prioridad;

import java.util.ArrayList;

public class GestionActividadesActivity extends AppCompatActivity {
    private ActividadAdapter adapter;
    private ArrayList<Actividad> actividades;

    private ActivityResultLauncher<Intent> launcherAgregarActividad =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Actividad nueva = (Actividad) result.getData().getSerializableExtra("actividad");
                            if (adapter != null && nueva != null) {
                                adapter.agregarActividad(nueva);
                                Actividad.guardarActividades(GestionActividadesActivity.this, actividades);
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_gestion_actividades);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
            return insets;
        });

        // CARGAR ACTIVIDADES GUARDADAS
        actividades = Actividad.cargarActividades(this);

        // SI ESTÁ VACÍO, agregar ejemplo
        if (actividades.isEmpty()) {
            actividades.add(new Actividad(
                    1,
                    "Estudiar Física",
                    "2026-01-15",
                    Prioridad.BAJA,
                    50,
                    "TAREA"
            ));
            Actividad.guardarActividades(this, actividades);
        }

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView_actividad);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ActividadAdapter(actividades);
        recyclerView.setAdapter(adapter);

        Button btnAgregar = findViewById(R.id.button_agregarActividad);

        btnAgregar.setOnClickListener(v ->{
            Intent intent = new Intent(this,FormularioActividadActivity.class);
            launcherAgregarActividad.launch(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (actividades != null) {
            Actividad.guardarActividades(this, actividades);
        }
    }
}