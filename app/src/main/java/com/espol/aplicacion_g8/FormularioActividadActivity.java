package com.espol.aplicacion_g8;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;
import com.espol.aplicacion_g8.modelo.actividad.ActividadAcademica;
import com.espol.aplicacion_g8.modelo.actividad.ActividadPersonal;
import com.espol.aplicacion_g8.modelo.actividad.Prioridad;
import com.espol.aplicacion_g8.modelo.actividad.TipoAcademico;
import com.espol.aplicacion_g8.modelo.actividad.TipoActividad;
import com.espol.aplicacion_g8.modelo.actividad.TipoPersonal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class FormularioActividadActivity extends AppCompatActivity {

    // Views
    private AutoCompleteTextView autoCompleteTipoActividad;
    private AutoCompleteTextView autoCompleteTipoEspecifico;
    private AutoCompleteTextView autoCompletePrioridad;
    private TextInputEditText etNombre, etDescripcion, etFecha, etTiempoEstimado;
    private TextInputEditText etAsignatura, etLugar;

    // Layouts
    private TextInputLayout layoutTipoEspecifico, layoutAsignatura, layoutLugar;

    // Botones
    private MaterialButton btnCancelar, btnGuardar;

    // Selecciones
    private TipoActividad tipoActividadSeleccionado;
    private String tipoEspecificoSeleccionado;
    private Prioridad prioridadSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_actividad);

        initViews();
        setupDropdowns();
        setupFechaPicker();
        setupListeners();
    }

    private void initViews() {
        autoCompleteTipoActividad = findViewById(R.id.autoCompleteTipoActividad);
        autoCompleteTipoEspecifico = findViewById(R.id.autoCompleteTipoEspecifico);
        autoCompletePrioridad = findViewById(R.id.autoCompletePrioridad);

        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFecha = findViewById(R.id.etFecha);
        etTiempoEstimado = findViewById(R.id.etTiempoEstimado);
        etAsignatura = findViewById(R.id.etAsignatura);
        etLugar = findViewById(R.id.etLugar);

        layoutTipoEspecifico = findViewById(R.id.layoutTipoEspecifico);
        layoutAsignatura = findViewById(R.id.layoutAsignatura);
        layoutLugar = findViewById(R.id.layoutLugar);

        btnCancelar = findViewById(R.id.btnCancelar);
        btnGuardar = findViewById(R.id.btnGuardar);
    }

    private void setupDropdowns() {
        autoCompleteTipoActividad.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line,
                        new String[]{"ACADÉMICA", "PERSONAL"})
        );

        autoCompletePrioridad.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line,
                        Prioridad.values())
        );
    }

    private void setupFechaPicker() {
        etFecha.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, y, m, d) ->
                            etFecha.setText(String.format("%04d-%02d-%02d", y, m + 1, d)),
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void setupListeners() {

        autoCompleteTipoActividad.setOnItemClickListener((p, v, pos, id) -> {
            layoutTipoEspecifico.setVisibility(View.VISIBLE);

            if (pos == 0) { // ACADÉMICA
                tipoActividadSeleccionado = TipoActividad.ACADEMICA;
                layoutAsignatura.setVisibility(View.VISIBLE);
                layoutLugar.setVisibility(View.GONE);

                autoCompleteTipoEspecifico.setAdapter(
                        new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line,
                                getNombresAcademicos())
                );

            } else { // PERSONAL
                tipoActividadSeleccionado = TipoActividad.PERSONAL;
                layoutAsignatura.setVisibility(View.GONE);
                layoutLugar.setVisibility(View.VISIBLE);

                autoCompleteTipoEspecifico.setAdapter(
                        new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line,
                                getNombresPersonales())
                );
            }
        });

        autoCompleteTipoEspecifico.setOnItemClickListener(
                (p, v, pos, id) -> tipoEspecificoSeleccionado = (String) p.getItemAtPosition(pos)
        );

        autoCompletePrioridad.setOnItemClickListener(
                (p, v, pos, id) -> prioridadSeleccionada = (Prioridad) p.getItemAtPosition(pos)
        );

        btnCancelar.setOnClickListener(v -> finish());

        btnGuardar.setOnClickListener(v -> {
            if (validarFormulario()) {
                Actividad actividad = crearActividad();
                if (actividad != null) {
                    Intent intent = new Intent();
                    intent.putExtra("actividad", actividad);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private boolean validarFormulario() {
        if (tipoActividadSeleccionado == null ||
                tipoEspecificoSeleccionado == null ||
                prioridadSeleccionada == null ||
                etNombre.getText().toString().trim().isEmpty() ||
                etDescripcion.getText().toString().trim().isEmpty() ||
                etFecha.getText().toString().trim().isEmpty() ||
                etTiempoEstimado.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Actividad crearActividad() {
        int id = Actividad.obtenerNuevoID(this);
        double tiempo = Double.parseDouble(etTiempoEstimado.getText().toString());

        if (tipoActividadSeleccionado == TipoActividad.ACADEMICA) {
            return new ActividadAcademica(
                    id,
                    TipoActividad.ACADEMICA,
                    tipoEspecificoSeleccionado,
                    etNombre.getText().toString(),
                    etAsignatura.getText().toString(),
                    etDescripcion.getText().toString(),
                    prioridadSeleccionada,
                    etFecha.getText().toString(),
                    tiempo
            );
        } else {
            return new ActividadPersonal(
                    id,
                    TipoActividad.PERSONAL,
                    tipoEspecificoSeleccionado,
                    etNombre.getText().toString(),
                    etDescripcion.getText().toString(),
                    prioridadSeleccionada,
                    etFecha.getText().toString(),
                    tiempo,
                    etLugar.getText().toString()
            );
        }
    }

    private String[] getNombresAcademicos() {
        TipoAcademico[] valores = TipoAcademico.values();
        String[] nombres = new String[valores.length];
        for (int i = 0; i < valores.length; i++) nombres[i] = valores[i].getNombre();
        return nombres;
    }

    private String[] getNombresPersonales() {
        TipoPersonal[] valores = TipoPersonal.values();
        String[] nombres = new String[valores.length];
        for (int i = 0; i < valores.length; i++) nombres[i] = valores[i].getNombre();
        return nombres;
    }
}