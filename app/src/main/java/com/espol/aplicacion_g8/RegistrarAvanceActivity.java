package com.espol.aplicacion_g8;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegistrarAvanceActivity extends AppCompatActivity {

    private Actividad actividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_avance);

        // Views
        TextView tvId = findViewById(R.id.tvIdActividad);
        TextView tvNombre = findViewById(R.id.tvNombreActividad);
        TextView tvAvanceActual = findViewById(R.id.tvAvanceActual);

        TextInputEditText etNuevoAvance = findViewById(R.id.etNuevoAvance);
        MaterialButton btnGuardar = findViewById(R.id.btnGuardarAvance);
        MaterialButton btnCancelar = findViewById(R.id.btnCancelarAvance);

        // Recibir actividad
        actividad = (Actividad) getIntent().getSerializableExtra("actividad");
        if (actividad == null) {
            Toast.makeText(this, "Error: no se recibió la actividad", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Pintar datos
        tvId.setText("ID: " + actividad.getId());
        tvNombre.setText("Nombre: " + actividad.getNombre());
        tvAvanceActual.setText("Avance actual: " + actividad.getAvance() + "%");

        // (opcional) poner el valor actual por defecto
        etNuevoAvance.setText(String.valueOf(actividad.getAvance()));

        btnCancelar.setOnClickListener(v -> finish());

        btnGuardar.setOnClickListener(v -> {
            String txt = etNuevoAvance.getText() != null ? etNuevoAvance.getText().toString().trim() : "";

            if (txt.isEmpty()) {
                Toast.makeText(this, "Ingresa el nuevo avance", Toast.LENGTH_SHORT).show();
                etNuevoAvance.requestFocus();
                return;
            }
            int nuevoAvance;
            try {
                nuevoAvance = Integer.parseInt(txt);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "El avance debe ser un número", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nuevoAvance < 0 || nuevoAvance > 100) {
                Toast.makeText(this, "El avance debe estar entre 0 y 100", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Confirmar")
                    .setMessage("¿Seguro que deseas registrar el avance en " + nuevoAvance + "%?")
                    .setPositiveButton("Sí", (d, w) -> {
                        Intent data = new Intent();
                        data.putExtra("idActividad", actividad.getId());
                        data.putExtra("nuevoAvance", nuevoAvance);
                        setResult(RESULT_OK, data);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}