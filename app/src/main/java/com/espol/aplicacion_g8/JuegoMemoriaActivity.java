package com.espol.aplicacion_g8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private List<Integer> imagenesIds; // Lista de IDs de imágenes
    private List<Button> cartasButtons; // Lista de botones-cartas
    private Button primeraCarta = null;
    private Button segundaCarta = null;
    private boolean bloqueado = false;
    private int intentos = 0;
    private int paresEncontrados = 0;
    private static final int TOTAL_PARES = 8;
    private static final int TOTAL_CARTAS = TOTAL_PARES * 2; // 16

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

        // Inicializar listas
        imagenesIds = new ArrayList<>();
        cartasButtons = new ArrayList<>();

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
        aplicarTema();
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

        // Limpiar listas y vistas
        cartasButtons.clear();
        imagenesIds.clear();
        gridLayout.removeAllViews();

        // Reiniciar variables
        bloqueado = false;
        intentos = 0;
        paresEncontrados = 0;
        primeraCarta = null;
        segundaCarta = null;

        // Actualizar estadísticas
        actualizarEstadisticas();

        // 1. DEFINIR LOS 8 PARES DE IMÁGENES
        int[] imagenesBase = {
                R.drawable.img_agua,
                R.drawable.img_arbol,
                R.drawable.img_bici,
                R.drawable.img_foco,
                R.drawable.img_hoja,
                R.drawable.img_nube,
                R.drawable.img_reciclaje,
                R.drawable.img_sol
        };


        // 2. CREAR 16 CARTAS (8 PARES)
        for (int imagenId : imagenesBase) {
            // Agregar DOS veces cada imagen para formar el par
            imagenesIds.add(imagenId);
            imagenesIds.add(imagenId);
        }

        if (imagenesIds.size() != 16) {
            Toast.makeText(this, "Error: No hay suficientes imágenes", Toast.LENGTH_LONG).show();
            return;
        }

        // 3. MEZCLAR LAS CARTAS
        Collections.shuffle(imagenesIds);

        // 4. CALCULAR TAMAÑO DE CARTA
        int anchoPantalla = getResources().getDisplayMetrics().widthPixels;
        int tamañoCarta = anchoPantalla / 4 - 32;

        // 5. CREAR LOS BOTONES-CARTAS
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            Button carta = new Button(this);

            // Configurar layout
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = tamañoCarta;
            params.height = tamañoCarta;
            params.setMargins(6, 6, 6, 6);
            carta.setLayoutParams(params);

            // Asignar imagen de reverso (carta cerrada)
            carta.setBackgroundResource(R.drawable.carta_cerrada);

            // Limpiar estilos de botón
            carta.setText("");
            carta.setAllCaps(false);

            // Guardar el ID de la imagen frontal como tag
            int imagenId = imagenesIds.get(i);
            carta.setTag(imagenId);

            // DEBUG: Mostrar qué imagen tiene cada carta
            String nombreImagen = getResources().getResourceName(imagenId);

            // Configurar click listener
            final int posicion = i;
            carta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manejarCarta((Button) v);
                }
            });

            // Agregar a la vista y a la lista
            gridLayout.addView(carta);
            cartasButtons.add(carta);
        }

    }

    private void manejarCarta(Button carta) {
        // Validaciones
        if (bloqueado) {
            return;
        }

        if (carta == primeraCarta) {
            return;
        }

        if (!carta.isEnabled()) {
            return;
        }

        // Reproducir sonido de volteo
        if (sonidosCargados) {
            soundPool.play(sonidoFlip, 1, 1, 1, 0, 1);
        }

        // Mostrar imagen frontal
        int imagenId = (int) carta.getTag();
        carta.setBackgroundResource(imagenId);

        // Gestionar selección
        if (primeraCarta == null) {
            // Primera carta seleccionada
            primeraCarta = carta;
        } else {
            // Segunda carta seleccionada
            segundaCarta = carta;
            bloqueado = true;
            intentos++;


            actualizarEstadisticas();

            // Esperar y verificar
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    verificarPareja();
                }
            }, 700);
        }
    }

    private void verificarPareja() {
        if (primeraCarta == null || segundaCarta == null) {
            return;
        }

        // Obtener los IDs de las imágenes
        int idImagen1 = (int) primeraCarta.getTag();
        int idImagen2 = (int) segundaCarta.getTag();

        String nombre1 = getResources().getResourceName(idImagen1);
        String nombre2 = getResources().getResourceName(idImagen2);


        boolean sonPareja = (idImagen1 == idImagen2);

        if (sonPareja) {
            // ¡PAREJA ENCONTRADA!
            if (sonidosCargados) {
                soundPool.play(sonidoSuccess, 1, 1, 1, 0, 1);
            }

            // Deshabilitar las cartas emparejadas
            primeraCarta.setEnabled(false);
            segundaCarta.setEnabled(false);

            // Incrementar contador
            paresEncontrados++;

            //CORRECCIÓN: ACTUALIZAR ESTADÍSTICAS DESPUÉS DE ENCONTRAR UN PAR
            actualizarEstadisticas();
            
            Toast.makeText(this, "¡Par encontrado!", Toast.LENGTH_SHORT).show();

            // Verificar si el juego terminó
            if (paresEncontrados >= TOTAL_PARES) {
              
                // Contar cartas emparejadas para verificación
                int cartasEmparejadas = 0;
                for (Button carta : cartasButtons) {
                    if (!carta.isEnabled()) {
                        cartasEmparejadas++;
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mostrarMensajeCompletado();
                    }
                }, 500);
            }
        } else {
            // NO ES PAREJA
            if (sonidosCargados) {
                soundPool.play(sonidoFail, 1, 1, 1, 0, 1);
            }


            // Volver a mostrar carta cerrada
            primeraCarta.setBackgroundResource(R.drawable.carta_cerrada);
            segundaCarta.setBackgroundResource(R.drawable.carta_cerrada);
        }

        // Limpiar selección
        primeraCarta = null;
        segundaCarta = null;
        bloqueado = false;
    }

    private void actualizarEstadisticas() {
        String texto = "Intentos: " + intentos + " | Pares: " + paresEncontrados + "/8";
        txtIntentos.setText(texto);
    }

    private void mostrarMensajeCompletado() {
        String mensaje = "¡Felicidades! Completaste el juego en " + intentos + " intentos";
    

        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_LONG);
        toast.setGravity(android.view.Gravity.CENTER, 0, 0);
        toast.show();

        if (sonidosCargados) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    soundPool.play(sonidoSuccess, 1, 1, 1, 0, 1);
                }
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
