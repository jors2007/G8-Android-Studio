package com.espol.aplicacion_g8;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.espol.aplicacion_g8.modelo.actividad.Prioridad;
import com.espol.aplicacion_g8.modelo.actividad.SesionEnfoque;
import com.espol.aplicacion_g8.modelo.actividad.TipoActividad;

import java.util.ArrayList;

public class GestionActividadesActivity extends AppCompatActivity {

    private ActividadAdapter adapter;

    // Lista real (se guarda)
    private ArrayList<Actividad> actividadesAll;

    // Lista mostrada (filtrada/ordenada)
    private ArrayList<Actividad> actividadesView;

    // Estado de filtros
    private String filtroCategoria = "Todos";   // ACADEMICA / PERSONAL
    private String ordenSeleccionado = "Nombre (A-Z)";

    // -------------------- LAUNCHERS --------------------

    private final ActivityResultLauncher<Intent> launcherFormulario =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Actividad nueva = (Actividad) result.getData().getSerializableExtra("actividad");
                            if (nueva != null) {
                                actividadesAll.add(nueva);
                                Actividad.guardarActividades(this, actividadesAll);
                                refrescarLista();
                            }
                        }
                    }
            );

    private final ActivityResultLauncher<Intent> launcherAvance =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            int idActividad = result.getData().getIntExtra("idActividad", -1);
                            int nuevoAvance = result.getData().getIntExtra("nuevoAvance", -1);

                            if (idActividad == -1 || nuevoAvance == -1) return;

                            for (Actividad a : actividadesAll) {
                                if (a.getId() == idActividad) {
                                    a.setAvance(nuevoAvance);
                                    break;
                                }
                            }

                            Actividad.guardarActividades(this, actividadesAll);
                            refrescarLista();
                            Toast.makeText(this, "Avance actualizado", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    private final ActivityResultLauncher<Intent> launcherSesion =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                            Actividad actActualizada =
                                    (Actividad) result.getData().getSerializableExtra("actividadActualizada");

                            if (actActualizada == null) return;

                            for (int i = 0; i < actividadesAll.size(); i++) {
                                if (actividadesAll.get(i).getId() == actActualizada.getId()) {
                                    actividadesAll.set(i, actActualizada);
                                    break;
                                }
                            }

                            Actividad.guardarActividades(this, actividadesAll);
                            refrescarLista();
                            Toast.makeText(this, "Sesión guardada", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    // -------------------- ONCREATE --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_actividades);


        // Cargar lista
        actividadesAll = Actividad.cargarActividades(this);
        // Cargar lista precargada SOLO SI ESTA VACIA
        if (actividadesAll == null) actividadesAll = new ArrayList<>();
        if(actividadesAll.isEmpty()){
            // 1 personal: cita médica 20 ene
            Actividad a1 = new Actividad(
                    Actividad.obtenerNuevoID(this),
                    TipoActividad.PERSONAL,
                    "CITA",
                    "Cita médica",
                    "Chequeo general",
                    Prioridad.ALTA,
                    "2026-01-20",
                    1.0
            );

            // 3 académicas:
            // - 1 proyecto (70%, 30 ene, con pomodoro en dos días)
            Actividad a2 = new Actividad(
                    Actividad.obtenerNuevoID(this),
                    TipoActividad.ACADEMICA,
                    "PROYECTO",
                    "Proyecto",
                    "Avanzar entregable del proyecto",
                    Prioridad.MEDIA,
                    "2026-01-30",
                    6.0
            );
            a2.setAvance(70); // ✅ aquí le pones el avance precargado

            // - 1 tarea (19 ene)
            Actividad a3 = new Actividad(
                    Actividad.obtenerNuevoID(this),
                    TipoActividad.ACADEMICA,
                    "TAREA",
                    "Tarea",
                    "Resolver ejercicios",
                    Prioridad.MEDIA,
                    "2026-01-19",
                    2.0
            );

            // - 1 examen (23 ene)
            Actividad a4 = new Actividad(
                    Actividad.obtenerNuevoID(this),
                    TipoActividad.ACADEMICA,
                    "EXAMEN",
                    "Examen",
                    "Repasar temas para el examen",
                    Prioridad.ALTA,
                    "2026-01-23",
                    3.0
            );

            actividadesAll.add(a1);
            actividadesAll.add(a2);
            actividadesAll.add(a3);
            actividadesAll.add(a4);

            SesionEnfoque s1 = new SesionEnfoque("2026-01-19","POMODORO",25);
            SesionEnfoque s2 = new SesionEnfoque("2026-01-17","PODOMORO",15);

            a2.agregarSesion(s1);
            a2.agregarSesion(s2);

            Actividad.guardarActividades(this,actividadesAll);
        }


        // Lista mostrada
        actividadesView = new ArrayList<>(actividadesAll);

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

        adapter = new ActividadAdapter(actividadesView, new OnActividadActionListener() {

            @Override
            public void onEliminar(Actividad actividad) {
                new AlertDialog.Builder(GestionActividadesActivity.this)
                        .setTitle("Eliminar actividad")
                        .setMessage("¿Desea eliminar la actividad:\n" + actividad.getNombre() + "?")
                        .setPositiveButton("Sí", (d, w) -> {
                            eliminarPorId(actividad.getId());
                            Actividad.guardarActividades(GestionActividadesActivity.this, actividadesAll);
                            refrescarLista();
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

        // Spinners
        Spinner spFiltroTipo = findViewById(R.id.spFiltroTipo);     // filtro por categoría
        Spinner spOrdenarPor = findViewById(R.id.spOrdenarPor);     // ordenar

        configurarSpinners(spFiltroTipo, spOrdenarPor);

        // Primera carga
        refrescarLista();
    }

    // -------------------- SPINNERS --------------------

    private void configurarSpinners(Spinner spFiltroTipo, Spinner spOrdenarPor) {

        // ---- Filtro por Categoría (enum TipoActividad) ----
        ArrayList<String> opcionesCategoria = new ArrayList<>();
        opcionesCategoria.add("Todos");
        opcionesCategoria.add("ACADEMICA");
        opcionesCategoria.add("PERSONAL");

        ArrayAdapter<String> adapterCat = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opcionesCategoria
        );
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFiltroTipo.setAdapter(adapterCat);

        // ---- Orden ----
        ArrayList<String> opcionesOrden = new ArrayList<>();
        opcionesOrden.add("Nombre (A-Z)");
        opcionesOrden.add("Fecha límite");
        opcionesOrden.add("Avance");

        ArrayAdapter<String> adapterOrden = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opcionesOrden
        );
        adapterOrden.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrdenarPor.setAdapter(adapterOrden);

        // Listeners
        spFiltroTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtroCategoria = (String) parent.getItemAtPosition(position);
                refrescarLista();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        spOrdenarPor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ordenSeleccionado = (String) parent.getItemAtPosition(position);
                refrescarLista();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    // -------------------- FILTRAR + ORDENAR --------------------

    private void refrescarLista() {
        ArrayList<Actividad> temp = new ArrayList<>();

        // 1) FILTRO por categoria (ACADEMICA/PERSONAL)
        for (Actividad a : actividadesAll) {
            if ("Todos".equalsIgnoreCase(filtroCategoria)) {
                temp.add(a);
            } else {
                TipoActividad cat = a.getCategoria(); // ✅ requiere getter
                if (cat != null && cat.name().equalsIgnoreCase(filtroCategoria)) {
                    temp.add(a);
                }
            }
        }

        // 2) ORDEN
        switch (ordenSeleccionado) {
            case "Nombre (A-Z)":
                temp.sort((a1, a2) -> safeLower(a1.getNombre()).compareTo(safeLower(a2.getNombre())));
                break;

            case "Fecha límite (desc)":
                // funciona si fecha es yyyy-MM-dd
                temp.sort((a1, a2) -> safeFecha(a2.getFechaLimite()).compareTo(safeFecha(a1.getFechaLimite())));
                break;

            case "Avance (desc)":
                temp.sort((a1, a2) -> Integer.compare(a2.getAvance(), a1.getAvance()));
                break;
        }

        // 3) actualizar adapter
        actividadesView.clear();
        actividadesView.addAll(temp);
        adapter.notifyDataSetChanged();
    }

    private String safeLower(String s) {
        return s == null ? "" : s.toLowerCase().trim();
    }

    private String safeFecha(String f) {
        return f == null ? "" : f.trim();
    }

    private void eliminarPorId(int id) {
        for (int i = 0; i < actividadesAll.size(); i++) {
            if (actividadesAll.get(i).getId() == id) {
                actividadesAll.remove(i);
                return;
            }
        }
    }

    // -------------------- PAUSE --------------------

    @Override
    protected void onPause() {
        super.onPause();
        if (actividadesAll != null) {
            Actividad.guardarActividades(this, actividadesAll);
        }
    }
}