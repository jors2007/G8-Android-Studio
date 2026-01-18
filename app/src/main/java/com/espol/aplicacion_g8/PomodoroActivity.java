package com.espol.aplicacion_g8;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;
import com.espol.aplicacion_g8.modelo.actividad.SesionEnfoque;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PomodoroActivity extends AppCompatActivity {

    private TextView tvActividad, tvTimer;
    private MaterialButton btn25, btn5, btn15, btn10s;
    private MaterialButton btnIniciar, btnPausar, btnReiniciar;

    private android.os.CountDownTimer timer;
    private boolean corriendo = false;

    private long duracionMs = 25 * 60 * 1000L;
    private long restanteMs = duracionMs;

    private Actividad actividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        // ✅ Recibir actividad (NO idActividad)
        actividad = (Actividad) getIntent().getSerializableExtra("actividad");
        if (actividad == null) {
            Toast.makeText(this, "No se recibió la actividad", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvActividad = findViewById(R.id.tvActividadPomodoro);
        tvTimer = findViewById(R.id.tvTimerPomodoro);

        btn25 = findViewById(R.id.btn25);
        btn5  = findViewById(R.id.btn5);
        btn15 = findViewById(R.id.btn15);
        btn10s = findViewById(R.id.btn10s);

        btnIniciar = findViewById(R.id.btnIniciarPomodoro);
        btnPausar = findViewById(R.id.btnPausarPomodoro);
        btnReiniciar = findViewById(R.id.btnReiniciarPomodoro);

        tvActividad.setText("Actividad: " + actividad.getNombre());

        setDuracionMin(25);

        btn25.setOnClickListener(v -> setDuracionMin(25));
        btn5.setOnClickListener(v -> setDuracionMin(5));
        btn15.setOnClickListener(v -> setDuracionMin(15));
        btn10s.setOnClickListener(v -> setDuracionSegundos(10));

        btnIniciar.setOnClickListener(v -> iniciar());
        btnPausar.setOnClickListener(v -> pausar());
        btnReiniciar.setOnClickListener(v -> reiniciar());
    }

    private void setDuracionMin(int minutos) {
        if (corriendo) return;
        duracionMs = minutos * 60 * 1000L;
        restanteMs = duracionMs;
        actualizarUI();
    }

    private void setDuracionSegundos(int segundos) {
        if (corriendo) return;
        duracionMs = segundos * 1000L;
        restanteMs = duracionMs;
        actualizarUI();
    }

    private void iniciar() {
        if (corriendo) return;

        corriendo = true;
        timer = new android.os.CountDownTimer(restanteMs, 1000) {
            @Override public void onTick(long msUntilFinished) {
                restanteMs = msUntilFinished;
                actualizarUI();
            }
            @Override public void onFinish() {
                corriendo = false;
                restanteMs = 0;
                actualizarUI();
                confirmarGuardado();
            }
        }.start();
    }

    private void pausar() {
        if (!corriendo) return;
        corriendo = false;
        if (timer != null) timer.cancel();
    }

    private void reiniciar() {
        if (timer != null) timer.cancel();
        corriendo = false;
        restanteMs = duracionMs;
        actualizarUI();
    }

    private void actualizarUI() {
        long totalSeg = restanteMs / 1000;
        long min = totalSeg / 60;
        long seg = totalSeg % 60;
        tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", min, seg));
    }

    private void confirmarGuardado() {
        new AlertDialog.Builder(this)
                .setTitle("Sesión finalizada")
                .setMessage("¿Deseas guardar esta sesión Pomodoro en el historial?")
                .setPositiveButton("Sí", (d, w) -> {
                    guardarSesionEnHistorial("Pomodoro");
                })
                .setNegativeButton("No", (d, w) -> finish())
                .show();
    }

    private void guardarSesionEnHistorial(String tecnica) {
        ArrayList<Actividad> lista = Actividad.cargarActividades(this);
        if (lista == null) lista = new ArrayList<>();

        for (Actividad a : lista) {
            if (a.getId() == actividad.getId()) {

                String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                int durMin = (int) (duracionMs / 60000);
                if (durMin == 0) durMin = 1; // para el test de 10s

                SesionEnfoque sesion = new SesionEnfoque(fecha, tecnica, durMin);
                a.agregarSesion(sesion);

                Actividad.guardarActividades(this, lista);

                // ✅ devolver actividad actualizada al listado
                Intent data = new Intent();
                data.putExtra("actividadActualizada", a);
                setResult(RESULT_OK, data);

                Toast.makeText(this, "Sesión guardada ✅", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        Toast.makeText(this, "No se encontró la actividad en disco", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}