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

    // Layouts para mostrar/ocultar
    private TextInputLayout layoutTipoEspecifico, layoutAsignatura, layoutLugar;

    // Botones
    private MaterialButton btnCancelar, btnGuardar;

    // Variables para almacenar selecciones
    private TipoActividad tipoActividadSeleccionado = null;
    private String tipoEspecificoSeleccionado = null;
    private Prioridad prioridadSeleccionada = null;

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
        // Dropdowns
        autoCompleteTipoActividad = findViewById(R.id.autoCompleteTipoActividad);
        autoCompleteTipoEspecifico = findViewById(R.id.autoCompleteTipoEspecifico);
        autoCompletePrioridad = findViewById(R.id.autoCompletePrioridad);

        // Campos de texto
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFecha = findViewById(R.id.etFecha);
        etTiempoEstimado = findViewById(R.id.etTiempoEstimado);
        etAsignatura = findViewById(R.id.etAsignatura);
        etLugar = findViewById(R.id.etLugar);

        // Layouts
        layoutTipoEspecifico = findViewById(R.id.layoutTipoEspecifico);
        layoutAsignatura = findViewById(R.id.layoutAsignatura);
        layoutLugar = findViewById(R.id.layoutLugar);

        // Botones
        btnCancelar = findViewById(R.id.btnCancelar);
        btnGuardar = findViewById(R.id.btnGuardar);
    }

    private void setupDropdowns() {
        // 1. TIPO DE ACTIVIDAD (ACADÉMICA/PERSONAL)
        String[] tiposActividad = {"ACADÉMICA", "PERSONAL"};
        ArrayAdapter<String> adapterTipoActividad = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                tiposActividad
        );
        autoCompleteTipoActividad.setAdapter(adapterTipoActividad);
        autoCompleteTipoActividad.setThreshold(1);

        // 2. PRIORIDAD
        ArrayAdapter<Prioridad> adapterPrioridad = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                Prioridad.values()
        );
        autoCompletePrioridad.setAdapter(adapterPrioridad);
        autoCompletePrioridad.setThreshold(1);
    }

    private void setupFechaPicker() {
        etFecha.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Formatear como YYYY-MM-DD (como usas en tu constructor)
                        String fecha = String.format("%04d-%02d-%02d",
                                selectedYear,
                                selectedMonth + 1,
                                selectedDay
                        );
                        etFecha.setText(fecha);
                    },
                    year, month, day
            );

            // Establecer fecha mínima como hoy
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();
        });
    }

    private void setupListeners() {
        // LISTENER para cuando se selecciona un tipo de actividad
        autoCompleteTipoActividad.setOnItemClickListener((parent, view, position, id) -> {
            String seleccion = (String) parent.getItemAtPosition(position);

            // Mostrar el dropdown de tipo específico
            layoutTipoEspecifico.setVisibility(View.VISIBLE);

            if (seleccion.equals("ACADÉMICA")) {
                tipoActividadSeleccionado = TipoActividad.ACADEMICA;

                // Cargar tipos académicos
                String[] tiposAcademicos = new String[TipoAcademico.values().length];
                for (int i = 0; i < TipoAcademico.values().length; i++) {
                    tiposAcademicos[i] = TipoAcademico.values()[i].getNombre();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        tiposAcademicos
                );
                autoCompleteTipoEspecifico.setAdapter(adapter);

                // Mostrar campo de asignatura, ocultar lugar
                layoutAsignatura.setVisibility(View.VISIBLE);
                layoutLugar.setVisibility(View.GONE);

            } else if (seleccion.equals("PERSONAL")) {
                tipoActividadSeleccionado = TipoActividad.PERSONAL;

                // Cargar tipos personales
                String[] tiposPersonales = new String[TipoPersonal.values().length];
                for (int i = 0; i < TipoPersonal.values().length; i++) {
                    tiposPersonales[i] = TipoPersonal.values()[i].getNombre();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        tiposPersonales
                );
                autoCompleteTipoEspecifico.setAdapter(adapter);

                // Mostrar campo de lugar, ocultar asignatura
                layoutAsignatura.setVisibility(View.GONE);
                layoutLugar.setVisibility(View.VISIBLE);
            }

            autoCompleteTipoEspecifico.setThreshold(1);
        });

        // LISTENER para tipo específico
        autoCompleteTipoEspecifico.setOnItemClickListener((parent, view, position, id) -> {
            tipoEspecificoSeleccionado = (String) parent.getItemAtPosition(position);
        });

        // LISTENER para prioridad
        autoCompletePrioridad.setOnItemClickListener((parent, view, position, id) -> {
            prioridadSeleccionada = (Prioridad) parent.getItemAtPosition(position);
        });

        // BOTÓN CANCELAR
        btnCancelar.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        // BOTÓN GUARDAR
        btnGuardar.setOnClickListener(v -> {
            if (validarFormulario()) {
                Actividad actividad = crearActividad();
                if (actividad != null) {
                    enviarResultado(actividad);
                }
            }
        });
    }

    private boolean validarFormulario() {
        // Validar tipo de actividad
        if (tipoActividadSeleccionado == null) {
            Toast.makeText(this, "Seleccione un tipo de actividad", Toast.LENGTH_SHORT).show();
            autoCompleteTipoActividad.requestFocus();
            return false;
        }

        // Validar tipo específico
        if (tipoEspecificoSeleccionado == null) {
            Toast.makeText(this, "Seleccione un tipo específico", Toast.LENGTH_SHORT).show();
            autoCompleteTipoEspecifico.requestFocus();
            return false;
        }

        // Validar nombre
        if (etNombre.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show();
            etNombre.requestFocus();
            return false;
        }

        // Validar descripción
        if (etDescripcion.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese una descripción", Toast.LENGTH_SHORT).show();
            etDescripcion.requestFocus();
            return false;
        }

        // Validar fecha
        if (etFecha.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Seleccione una fecha", Toast.LENGTH_SHORT).show();
            etFecha.requestFocus();
            return false;
        }

        // Validar prioridad
        if (prioridadSeleccionada == null) {
            Toast.makeText(this, "Seleccione una prioridad", Toast.LENGTH_SHORT).show();
            autoCompletePrioridad.requestFocus();
            return false;
        }

        // Validar tiempo estimado
        if (etTiempoEstimado.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese el tiempo estimado", Toast.LENGTH_SHORT).show();
            etTiempoEstimado.requestFocus();
            return false;
        }

        // Validaciones específicas por tipo
        if (tipoActividadSeleccionado == TipoActividad.ACADEMICA) {
            if (etAsignatura.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Ingrese la asignatura", Toast.LENGTH_SHORT).show();
                etAsignatura.requestFocus();
                return false;
            }
        }

        return true;
    }

    private Actividad crearActividad() {
        try {
            // Obtener valores comunes
            String nombre = etNombre.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String fechaLimite = etFecha.getText().toString().trim();
            double tiempoEstimado = Double.parseDouble(etTiempoEstimado.getText().toString().trim());

            // Crear actividad según el tipo
            if (tipoActividadSeleccionado == TipoActividad.ACADEMICA) {
                String asignatura = etAsignatura.getText().toString().trim();

                return new ActividadAcademica(
                        TipoActividad.ACADEMICA,
                        tipoEspecificoSeleccionado,
                        nombre,
                        asignatura,
                        descripcion,
                        prioridadSeleccionada,
                        fechaLimite,
                        tiempoEstimado
                );

            } else { // PERSONAL
                String lugar = etLugar.getText().toString().trim();

                return new ActividadPersonal(
                        TipoActividad.PERSONAL,
                        tipoEspecificoSeleccionado,
                        nombre,
                        descripcion,
                        prioridadSeleccionada,
                        fechaLimite,
                        tiempoEstimado,
                        lugar
                );
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Tiempo estimado debe ser un número válido", Toast.LENGTH_SHORT).show();
            etTiempoEstimado.requestFocus();
            return null;
        } catch (Exception e) {
            Toast.makeText(this, "Error al crear actividad: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void enviarResultado(Actividad actividad) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("actividad", actividad);
        setResult(RESULT_OK, resultIntent);
        finish();

        Toast.makeText(this, "Actividad creada exitosamente", Toast.LENGTH_SHORT).show();
    }
}