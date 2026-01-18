package com.espol.aplicacion_g8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class JuegoMemoriaActivity extends AppCompatActivity {

    // Componentes de UI
    private GridLayout gridLayout;
    private TextView txtIntentos;
    private TextView txtDescripcion;
    private Button btnReiniciar;
    private Button btnMenuPrincipal;
    private LinearLayout mainLayout;

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

    // Variables para mantener el estado del juego
    private boolean juegoIniciado = false;
    private int[] estadoCartas = new int[16]; // 0 = carta_cerrada, 1-8 = imágenes
    private boolean[] cartasEmparejadas = new boolean[16];
    private int[] imagenesGuardadas = new int[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_memoria);

        // Inicializar vistas
        mainLayout = findViewById(R.id.mainLayout);
        gridLayout = findViewById(R.id.gridCartas);
        txtIntentos = findViewById(R.id.txtIntentos);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        btnReiniciar = findViewById(R.id.btnReiniciar);
        btnMenuPrincipal = findViewById(R.id.btnMenuPrincipal);

        // Aplicar tema inicial
        aplicarTema();

        // Configurar sonidos
        inicializarSonidos();

        // Configurar botones
        btnReiniciar.setOnClickListener(v -> reiniciarJuego());
        btnMenuPrincipal.setOnClickListener(v -> finish());

        // Iniciar juego
        iniciarJuego();
    }

    private void aplicarTema() {
        int nightModeFlags = getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Modo oscuro
            mainLayout.setBackgroundColor(0xFF191A1C);
            txtDescripcion.setTextColor(0xFFFFFFFF);
            txtIntentos.setTextColor(0xFF009688);
            txtIntentos.setBackgroundColor(0xFF2D2D2D);
        } else {
            // Modo claro
            mainLayout.setBackgroundColor(0xFFF5F5F5);
            txtDescripcion.setTextColor(0xFF000000);
            txtIntentos.setTextColor(0xFF009688);
            txtIntentos.setBackgroundColor(0xFFE8F5E9);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Solo aplicar el tema, NO reiniciar el juego
        aplicarTema();

        // IMPORTANTE: Mantener el estado actual del juego
        // No llamamos a iniciarJuego() aquí
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
        juegoIniciado = true;

        // Reiniciar variables
        gridLayout.removeAllViews();
        bloqueado = false;
        intentos = 0;
        paresEncontrados = 0;
        primeraCarta = null;
        segundaCarta = null;

        // Reiniciar arrays de estado
        for (int i = 0; i < 16; i++) {
            estadoCartas[i] = 0; // Todas cerradas
            cartasEmparejadas[i] = false;
            imagenesGuardadas[i] = 0;
        }

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
            imagenes.add(recurso);
        }

        Collections.shuffle(imagenes);

        // Guardar las imágenes mezcladas
        for (int i = 0; i < 16; i++) {
            imagenesGuardadas[i] = imagenes.get(i);
        }

        // Calcular tamaño de carta
        int anchoPantalla = getResources().getDisplayMetrics().widthPixels;
        int tamañoCarta = anchoPantalla / 4 - 32;

        // Crear botones/cartas
        for (int i = 0; i < 16; i++) {
            Button carta = new Button(this);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = tamañoCarta;
            params.height = tamañoCarta;
            params.setMargins(6, 6, 6, 6);

            carta.setLayoutParams(params);

            // Usar carta_cerrada.xml
            carta.setBackgroundResource(R.drawable.carta_cerrada);

            // IMPORTANTE: Limpiar cualquier estilo de botón por defecto
            carta.setText("");
            carta.setAllCaps(false);

            // Guardar la imagen frontal como tag
            carta.setTag(imagenes.get(i));

            final int posicion = i;
            carta.setOnClickListener(v -> manejarCarta(carta));

            gridLayout.addView(carta);
        }
    }

    private void restaurarEstadoJuego() {
        // Este método podría usarse para restaurar el estado si fuera necesario
        // Pero no lo estamos usando porque no queremos reiniciar el juego
    }

    private void manejarCarta(Button carta) {
        if (bloqueado || carta == primeraCarta || !carta.isEnabled()) {
            return;
        }

        // Reproducir sonido
        if (sonidosCargados) {
            soundPool.play(sonidoFlip, 1, 1, 1, 0, 1);
        }

        // Mostrar imagen frontal
        carta.setBackgroundResource((int) carta.getTag());

        if (primeraCarta == null) {
            primeraCarta = carta;
        } else {
            segundaCarta = carta;
            bloqueado = true;
            intentos++;

            actualizarEstadisticas();

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

                if (paresEncontrados == TOTAL_PARES) {
                    new Handler().postDelayed(() -> {
                        mostrarMensajeCompletado();
                    }, 500);
                }
            } else {
                // No es pareja
                if (sonidosCargados) {
                    soundPool.play(sonidoFail, 1, 1, 1, 0, 1);
                }

                // Volver a mostrar carta_cerrada.xml
                primeraCarta.setBackgroundResource(R.drawable.carta_cerrada);
                segundaCarta.setBackgroundResource(R.drawable.carta_cerrada);
            }

            primeraCarta = null;
            segundaCarta = null;
            bloqueado = false;
        }
    }

    private void actualizarEstadisticas() {
        txtIntentos.setText("Intentos: " + intentos + " | Pares: " + paresEncontrados + "/8");
    }

    private void mostrarMensajeCompletado() {
        String mensaje = "¡Felicidades! Completaste el juego en " + intentos + " intentos";

        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_LONG);
        toast.setGravity(android.view.Gravity.CENTER, 0, 0);
        toast.show();

        if (sonidosCargados) {
            new Handler().postDelayed(() -> {
                soundPool.play(sonidoSuccess, 1, 1, 1, 0, 1);
            }, 300);
        }
    }

    private void reiniciarJuego() {
        iniciarJuego();
        Toast.makeText(this, "¡Juego reiniciado!", Toast.LENGTH_SHORT).show();
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
