package com.espol.aplicacion_g8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class JuegoMemoriaActivity extends AppCompatActivity {

    // Componentes de UI
    private GridLayout gridLayout;
    private TextView txtIntentos;
    private Button btnReiniciar;
    private Button btnMenuPrincipal;

    // Sonidos
    private SoundPool soundPool;
    private int sonidoFlip, sonidoSuccess, sonidoFail;
    private boolean sonidosCargados = false;

    // Lógica del juego
    private ArrayList<Integer> imagenes;
    private Button primeraCarta, segundaCarta;
    private boolean bloqueado = false;
    private int intentos = 0;
    private int paresEncontrados = 0;
    private final int TOTAL_PARES = 8;
    private Button[] botonesCartas = new Button[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_memoria);

        // Inicializar vistas - ¡SOLO LAS QUE EXISTEN!
        gridLayout = findViewById(R.id.gridCartas);
        txtIntentos = findViewById(R.id.txtIntentos);
        btnReiniciar = findViewById(R.id.btnReiniciar);
        btnMenuPrincipal = findViewById(R.id.btnMenuPrincipal);

        // Configurar sonidos
        inicializarSonidos();

        // Configurar botones - ¡CORREGIDO!
        btnReiniciar.setOnClickListener(v -> reiniciarJuego());
        btnMenuPrincipal.setOnClickListener(v -> volverAlMenu());

        // Iniciar juego
        iniciarJuego();
    }

    private void inicializarSonidos() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();

        // Cargar sonidos
        sonidoFlip = soundPool.load(this, R.raw.flip, 1);
        sonidoSuccess = soundPool.load(this, R.raw.success, 1);
        sonidoFail = soundPool.load(this, R.raw.fail, 1);

        soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
            if (status == 0) {
                sonidosCargados = true;
            }
        });
    }

    private void iniciarJuego() {
        // Reiniciar variables
        gridLayout.removeAllViews();
        bloqueado = false;
        intentos = 0;
        paresEncontrados = 0;
        primeraCarta = null;
        segundaCarta = null;

        // Actualizar estadísticas
        actualizarEstadisticas();

        // Crear y mezclar imágenes
        imagenes = new ArrayList<>();
        int[] recursos = {
                R.drawable.img_agua, R.drawable.img_arbol,
                R.drawable.img_bici, R.drawable.img_foco,
                R.drawable.img_hoja, R.drawable.img_nube,
                R.drawable.img_reciclaje, R.drawable.img_sol
        };

        for (int recurso : recursos) {
            imagenes.add(recurso);
            imagenes.add(recurso); // Duplicar para el par
        }

        Collections.shuffle(imagenes);

        // Calcular tamaño de carta
        int anchoPantalla = getResources().getDisplayMetrics().widthPixels;
        int tamañoCarta = anchoPantalla / 4 - 32;

        // Crear botones/cartas
        for (int i = 0; i < 16; i++) {
            Button carta = new Button(this);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = tamañoCarta;
            params.height = tamañoCarta;
            params.setMargins(8, 8, 8, 8);

            carta.setLayoutParams(params);
            carta.setBackgroundResource(R.drawable.reverso);
            carta.setTag(imagenes.get(i));
            carta.setId(View.generateViewId()); // Generar ID único

            final int posicion = i;
            carta.setOnClickListener(v -> manejarCarta(carta));

            gridLayout.addView(carta);
            botonesCartas[i] = carta;
        }
    }

    private void manejarCarta(Button carta) {
        if (bloqueado || carta == primeraCarta || !carta.isEnabled()) {
            return;
        }

        // Reproducir sonido
        if (sonidosCargados) {
            soundPool.play(sonidoFlip, 1, 1, 1, 0, 1);
        }

        // Mostrar imagen
        carta.setBackgroundResource((int) carta.getTag());

        if (primeraCarta == null) {
            primeraCarta = carta;
        } else {
            segundaCarta = carta;
            bloqueado = true;
            intentos++;

            actualizarEstadisticas();

            // Verificar después de un retraso
            new Handler().postDelayed(this::verificarPareja, 700);
        }
    }

    private void verificarPareja() {
        if (primeraCarta != null && segundaCarta != null) {
            if (primeraCarta.getTag().equals(segundaCarta.getTag())) {
                // ¡Pareja encontrada!
                if (sonidosCargados) {
                    soundPool.play(sonidoSuccess, 1, 1, 1, 0, 1);
                }

                primeraCarta.setEnabled(false);
                segundaCarta.setEnabled(false);
                paresEncontrados++;

                Toast.makeText(this, "¡Par encontrado!", Toast.LENGTH_SHORT).show();

                // Verificar si el juego terminó
                if (paresEncontrados == TOTAL_PARES) {
                    mostrarDialogoCompletado();
                }
            } else {
                // No es pareja
                if (sonidosCargados) {
                    soundPool.play(sonidoFail, 1, 1, 1, 0, 1);
                }

                primeraCarta.setBackgroundResource(R.drawable.reverso);
                segundaCarta.setBackgroundResource(R.drawable.reverso);
            }

            primeraCarta = null;
            segundaCarta = null;
            bloqueado = false;
        }
    }

    private void actualizarEstadisticas() {
        txtIntentos.setText("Intentos: " + intentos + " | Pares: " + paresEncontrados + "/8");
    }

    private void mostrarDialogoCompletado() {
        new AlertDialog.Builder(this)
                .setTitle("¡Felicidades!")
                .setMessage("¡Completaste el juego en " + intentos + " intentos!\n\n¿Qué deseas hacer?")
                .setPositiveButton("Jugar de nuevo", (dialog, which) -> {
                    reiniciarJuego();
                })
                .setNegativeButton("Menú principal", (dialog, which) -> {
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void reiniciarJuego() {
        new AlertDialog.Builder(this)
                .setTitle("Reiniciar Juego")
                .setMessage("¿Estás seguro de que quieres reiniciar el juego?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    iniciarJuego();
                    Toast.makeText(this, "¡Juego reiniciado!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void volverAlMenu() {
        new AlertDialog.Builder(this)
                .setTitle("Volver al Menú")
                .setMessage("¿Estás seguro de que quieres salir del juego?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}