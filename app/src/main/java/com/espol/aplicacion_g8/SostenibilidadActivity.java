package com.espol.aplicacion_g8;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.espol.aplicacion_g8.modelo.sostenibilidad.AccionSostenible;
import com.espol.aplicacion_g8.modelo.sostenibilidad.RegistroSostenibilidad;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SostenibilidadActivity extends AppCompatActivity {

    private RegistroSostenibilidad registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sostenibilidad);

        // Configurar botón
        Button btnRegistrar = findViewById(R.id.btnRegistrarDia);
        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(SostenibilidadActivity.this, RegistroSostenibilidadActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registro = RegistroSostenibilidad.cargar(this);
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        if (registro == null) return;

        LocalDate hoy = LocalDate.now();
        LocalDate inicio = hoy.minusDays(6);

        //día/mes/año
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");

        TextView tvRango = findViewById(R.id.tvRangoFechas);
        tvRango.setText("(" + inicio.format(fmt) + " - " + hoy.format(fmt) + ")");



        int[] rowsIds = {R.id.row_transporte, R.id.row_impresiones, R.id.row_envases, R.id.row_reciclaje};
        String[] nombresUI = {
                "Usé transporte público/bici", "No realicé impresiones",
                "No utilicé envases descartables", "Separé y reciclé materiales"
        };

        String[] clavesModelo = {
                "Usé transporte público, bicicleta o caminé.",
                "No realicé impresiones.",
                "No utilicé envases descartables (usé mi termo/taza).",
                "Separé y reciclé materiales (vidrio, plástico, papel)."
        };

        int[] conteos = new int[4];
        int diasConAccion = 0;
        int diasCompletos = 0;

        // conteo (recorrer últimos 7 días)
        for (int i = 0; i < 7; i++) {
            LocalDate fecha = inicio.plusDays(i);
            List<AccionSostenible> accionesDelDia = registro.getAccionesDia(fecha);

            if (!accionesDelDia.isEmpty()) diasConAccion++;
            if (accionesDelDia.size() >= 4) diasCompletos++; // Asumiendo que 4 es completar todo

            for (AccionSostenible acc : accionesDelDia) {
                for (int j = 0; j < 4; j++) {
                    if (acc.getAccion().equals(clavesModelo[j])) {
                        conteos[j]++;
                    }
                }
            }
        }

        // Actualizar UI fila por fila
        for (int i = 0; i < 4; i++) {
            View row = findViewById(rowsIds[i]);
            TextView tvNombre = row.findViewById(R.id.tvNombreAccion);
            TextView tvCount = row.findViewById(R.id.tvConteo);
            TextView tvLogro = row.findViewById(R.id.tvLogro);

            tvNombre.setText(nombresUI[i]);
            tvCount.setText(conteos[i] + "/7");

            configurarLogro(tvLogro, conteos[i], i);
        }

        // Cálculo de Porcentajes
        int porc1 = (diasConAccion * 100) / 7;
        int porc2 = (diasCompletos * 100) / 7;

        TextView tvAnalisis1 = findViewById(R.id.tvAnalisis1);
        TextView tvAnalisis2 = findViewById(R.id.tvAnalisis2);

        // Porcentaje
        tvAnalisis1.setText("Días con al menos 1 acción: " + diasConAccion + " de 7 (" + porc1 + "%)");
        tvAnalisis2.setText("Días con las 4 acciones: " + diasCompletos + " de 7 (" + porc2 + "%)");
    }

    private void configurarLogro(TextView tv, int cantidad, int indexAccion) {
        String texto;
        String colorHex;

        if (cantidad == 7) {
            texto = "Excelente";
            colorHex = "#2E7D32"; // Verde Oscuro
        } else if (cantidad >= 5) {
            if (indexAccion == 0) { // Transporte
                texto = "¡Gran Movilidad!";
                colorHex = "#4CAF50"; // Verde Brillante
            } else {
                texto = "Muy bien";
                colorHex = "#66BB6A"; // Verde Medio
            }
        } else if (cantidad == 4) {
            texto = "Necesita mejorar";
            colorHex = "#FF9800"; // Naranja
        } else {
            texto = "Bajo";
            colorHex = "#F44336"; // Rojo
        }

        tv.setText(texto);

        tv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor(colorHex)));
    }
}