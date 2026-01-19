package com.espol.aplicacion_g8;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.espol.aplicacion_g8.controlador.ControlHidratacion;
import com.espol.aplicacion_g8.controlador.RegistroHidratacion;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HydrationActivity extends AppCompatActivity {
    private ControlHidratacion controlHidratacion;
    private ProgressBar progressBar;
    private TextView txtPorcentaje, txtMeta, txtTotal;
    private LinearLayout listaRegistrosContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_hidratacion);

        // 1. Inicializar Modelo
        controlHidratacion = new ControlHidratacion();
        cargarDatosIniciales();

        // 2. Vincular vistas
        progressBar = findViewById(R.id.progressBarAgua);
        txtPorcentaje = findViewById(R.id.txtPorcentaje);
        txtMeta = findViewById(R.id.txtMetaDiaria);
        txtTotal = findViewById(R.id.txtTotalConsumido);
        listaRegistrosContainer = findViewById(R.id.layoutListaRegistros);
        Button btnRegistrar = findViewById(R.id.btnRegistrarAgua);
        Button btnMeta = findViewById(R.id.btnEstablecerMeta);

        //  Botón: Registrar Agua
        btnRegistrar.setOnClickListener(v -> mostrarDialogoRegistrarAgua());

        //  Botón: Cambiar Meta
        btnMeta.setOnClickListener(v -> mostrarDialogoMeta());

        //  Mostrar datos iniciales
        actualizarPantalla();
    }


    private void cargarDatosIniciales() {
        controlHidratacion.registrarHidratacion(500, LocalDate.of(2025, 1, 19), LocalTime.of(10, 0));
        controlHidratacion.registrarHidratacion(300, LocalDate.of(2025, 1, 21), LocalTime.of(15, 30));

    }

    private void actualizarPantalla() {
        int totalHoy = controlHidratacion.getAcumuladoHoy();
        int meta = controlHidratacion.getMetaDiaria();
        double progreso = controlHidratacion.getProgreso();

        // Actualizar Textos y Barra
        txtMeta.setText("Meta diaria: " + meta + " ml");
        txtTotal.setText("Total consumido: " + totalHoy + " ml");
        txtPorcentaje.setText((int)progreso + "%");
        progressBar.setMax(100);
        progressBar.setProgress((int)progreso);


        listaRegistrosContainer.removeAllViews();


        for (RegistroHidratacion r : controlHidratacion.getRegistrosHoy()) {
            TextView renglon = new TextView(this);
            String horaFormato = r.getHora().format(DateTimeFormatter.ofPattern("HH:mm"));
            renglon.setText(r.getCantidad() + " ml - " + horaFormato);
            renglon.setTextSize(16);
            renglon.setPadding(0, 10, 0, 10);
            listaRegistrosContainer.addView(renglon);
        }
    }


    private void mostrarDialogoRegistrarAgua() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registrar Toma de Agua");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Ej: 250");
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String texto = input.getText().toString();
            if (!texto.isEmpty()) {
                int cantidad = Integer.parseInt(texto);
                boolean exito = controlHidratacion.registrarHidratacion(cantidad);
                if (exito) {
                    actualizarPantalla();
                    Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error: cantidad inválida", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Ventana cambiar meta
    private void mostrarDialogoMeta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Meta Diaria");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Nueva meta (ml)");
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String texto = input.getText().toString();
            if (!texto.isEmpty()) {
                int nuevaMeta = Integer.parseInt(texto);
                controlHidratacion.establecerMetaDiaria(nuevaMeta);
                actualizarPantalla();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}