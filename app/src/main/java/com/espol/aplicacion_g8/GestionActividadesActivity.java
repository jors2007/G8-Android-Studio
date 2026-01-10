package com.espol.aplicacion_g8;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // 1️⃣ Cargar layout (UNA SOLA VEZ)
        setContentView(R.layout.activity_gestion_actividades);

        // 2️⃣ Insets (solo padding)
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

        // 3️⃣ RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView_actividad);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 4️⃣ Datos
        ArrayList<Actividad> actividades = new ArrayList<>();
        actividades.add(
                new Actividad(
                        1,
                        "Estudiar Física",
                        "2026-01-15",
                        Prioridad.BAJA,
                        50,
                        "TAREA"
                )
        );

        // 5️⃣ Adapter
        ActividadAdapter adapter = new ActividadAdapter(actividades);
        recyclerView.setAdapter(adapter);
    }
}