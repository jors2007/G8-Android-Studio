package com.espol.aplicacion_g8;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.espol.aplicacion_g8.controlador.ControlHidratacion;
import com.espol.aplicacion_g8.controlador.RegistroHidratacion;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HydrationActivity extends AppCompatActivity {

    private ControlHidratacion controlHidratacion;
    private ProgressBar progressBar;
    private TextView txtPorcentaje, txtMeta, txtTotal;
    private LinearLayout listaRegistrosContainer;

    // Lanzador para recibir datos del Formulario
    private final ActivityResultLauncher<Intent> lanzador = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    int valor = result.getData().getIntExtra(FormularioActivity.EXTRA_VALOR, 0);
                    String modo = result.getData().getStringExtra(FormularioActivity.EXTRA_MODO);

                    if (FormularioActivity.MODO_META.equals(modo)) {
                        controlHidratacion.establecerMetaDiaria(valor);
                        Toast.makeText(this, "Meta actualizada", Toast.LENGTH_SHORT).show();
                    } else {
                        controlHidratacion.registrarHidratacion(valor);
                        Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                    }
                    actualizarPantalla();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_hidratacion);

        // 1. Inicializar el Controlador
        controlHidratacion = new ControlHidratacion();

        // 2. Conectar Vistas
        progressBar = findViewById(R.id.progressBarAgua);
        txtPorcentaje = findViewById(R.id.txtPorcentaje);
        txtMeta = findViewById(R.id.txtMetaDiaria);
        txtTotal = findViewById(R.id.txtTotalConsumido);
        listaRegistrosContainer = findViewById(R.id.layoutListaRegistros);

        // 3. CARGAR DATOS REALES (Persistencia)
        cargarPreferencias();

        // 4. Configurar Botón REGISTRAR
        Button btnRegistrar = findViewById(R.id.btnRegistrarAgua);
        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormularioActivity.class);
            intent.putExtra(FormularioActivity.EXTRA_MODO, FormularioActivity.MODO_TOMA);
            lanzador.launch(intent);
        });

        // 5. Configurar Botón META
        Button btnMeta = findViewById(R.id.btnEstablecerMeta);
        btnMeta.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormularioActivity.class);
            intent.putExtra(FormularioActivity.EXTRA_MODO, FormularioActivity.MODO_META);
            // Enviamos la meta actual para que aparezca en el formulario
            intent.putExtra(FormularioActivity.EXTRA_VALOR_ACTUAL, controlHidratacion.getMetaDiaria());
            lanzador.launch(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Guardamos todo automáticamente al salir
        guardarPreferencias();
    }

    private void actualizarPantalla() {
        int totalHoy = controlHidratacion.getAcumuladoHoy();
        int meta = controlHidratacion.getMetaDiaria();
        double progreso = controlHidratacion.getProgreso();

        txtMeta.setText("Meta diaria: " + meta + " ml");
        txtTotal.setText("Total Hoy: " + totalHoy + " ml");
        txtPorcentaje.setText((int) progreso + "%");

        progressBar.setMax(100);
        progressBar.setProgress((int) progreso);

        // Actualizar la lista visual
        listaRegistrosContainer.removeAllViews();

        // Usamos tu clase RegistroHidratacion para llenar la lista
        for (RegistroHidratacion r : controlHidratacion.getRegistrosHoy()) {
            TextView renglon = new TextView(this);
            String horaFormato = r.getHora().format(DateTimeFormatter.ofPattern("HH:mm"));
            renglon.setText(r.getCantidad() + " ml - " + horaFormato);
            renglon.setTextSize(16);
            renglon.setPadding(0, 10, 0, 10);
            renglon.setTextColor(getResources().getColor(android.R.color.darker_gray));
            listaRegistrosContainer.addView(renglon);
        }
    }

    // -----------------------------------------------------------
    // MÉTODOS DE PERSISTENCIA (GUARDAR Y CARGAR)
    // -----------------------------------------------------------

    private void guardarPreferencias() {
        SharedPreferences prefs = getSharedPreferences("MisDatosAgua", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // 1. Guardar la Meta y la Fecha
        editor.putInt("meta_guardada", controlHidratacion.getMetaDiaria());
        String hoy = LocalDate.now().toString();
        editor.putString("fecha_guardada", hoy);

        // 2. Guardar el historial como texto simple (Ej: "500,200,300")
        StringBuilder historialString = new StringBuilder();
        for (RegistroHidratacion r : controlHidratacion.getRegistrosHoy()) {
            historialString.append(r.getCantidad()).append(",");
        }
        editor.putString("historial_tomas", historialString.toString());

        editor.apply();
    }

    private void cargarPreferencias() {
        SharedPreferences prefs = getSharedPreferences("MisDatosAgua", Context.MODE_PRIVATE);

        // 1. Recuperar Meta
        int metaGuardada = prefs.getInt("meta_guardada", 2500);
        controlHidratacion.establecerMetaDiaria(metaGuardada);

        // 2. Verificar fecha para saber si resetear
        String fechaGuardada = prefs.getString("fecha_guardada", "");
        String hoy = LocalDate.now().toString();

        if (fechaGuardada.equals(hoy)) {
            // Es el mismo día: Recuperamos las tomas
            String historial = prefs.getString("historial_tomas", "");
            if (!historial.isEmpty()) {
                String[] tomas = historial.split(",");
                for (String cantidadStr : tomas) {
                    if (!cantidadStr.isEmpty()) {
                        try {
                            int cantidad = Integer.parseInt(cantidadStr);
                            // Volvemos a registrar en el controlador
                            controlHidratacion.registrarHidratacion(cantidad);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        // Si es otro día, no hacemos nada (el controlador inicia vacío)

        actualizarPantalla();
    }
}