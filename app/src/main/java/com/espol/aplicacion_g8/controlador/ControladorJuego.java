package com.espol.aplicacion_g8.controlador;


import com.espol.aplicacion_g8.R;
import com.espol.aplicacion_g8.modelo.juego.Carta;
import com.espol.aplicacion_g8.modelo.juego.JuegoMemoria;

import java.util.List;


import android.content.Context;
import android.media.SoundPool;
import android.media.AudioAttributes;



public class ControladorJuego {
    private JuegoMemoria model;
    private SoundPool soundPool;
    private int sonidoFlip, sonidoSuccess, sonidoFail;
    private boolean sonidosCargados = false;
    private GameListener listener;

    public interface GameListener {
        void onCartaVolteada(int posicion);
        void onParejaEncontrada(List<Carta> cartas);
        void onParejaFallida(List<Carta> cartas);
        void onJuegoCompletado(int intentos);
        void onEstadisticasActualizadas(int intentos, int pares);
    }

    public ControladorJuego(Context context, GameListener listener) {
        this.model = new JuegoMemoria();
        this.listener = listener;
        inicializarSonidos(context);
    }

    private void inicializarSonidos(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();

        sonidoFlip = soundPool.load(context, R.raw.flip, 1);
        sonidoSuccess = soundPool.load(context, R.raw.success, 1);
        sonidoFail = soundPool.load(context, R.raw.fail, 1);

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) {
                sonidosCargados = true;
            }
        });
    }

    public void seleccionarCarta(int posicion) {
        if (model.seleccionarCarta(posicion)) {
            // Reproducir sonido de volteo
            if (sonidosCargados) {
                soundPool.play(sonidoFlip, 1, 1, 1, 0, 1);
            }

            listener.onCartaVolteada(posicion);

            // Verificar después de un breve retraso
            android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(this::verificarSeleccion, 700);
        } else if (model.getCartas().get(posicion).isVisible()) {
            // Primera carta seleccionada
            if (sonidosCargados) {
                soundPool.play(sonidoFlip, 1, 1, 1, 0, 1);
            }
            listener.onCartaVolteada(posicion);
        }
    }

    private void verificarSeleccion() {
        boolean esPareja = model.verificarPareja();

        // Obtener las últimas cartas seleccionadas
        List<Carta> cartas = model.getCartas();
        int pos1 = -1, pos2 = -1;
        for (int i = 0; i < cartas.size(); i++) {
            Carta carta = cartas.get(i);
            if (carta.isVisible() && !carta.isEmparejada()) {
                if (pos1 == -1) pos1 = i;
                else pos2 = i;
            }
        }

        if (esPareja) {
            if (sonidosCargados) {
                soundPool.play(sonidoSuccess, 1, 1, 1, 0, 1);
            }

            if (listener != null) {
                listener.onParejaEncontrada(model.getCartas());
            }

            if (model.isJuegoCompletado()) {
                listener.onJuegoCompletado(model.getIntentos());
            }
        } else {
            if (sonidosCargados) {
                soundPool.play(sonidoFail, 1, 1, 1, 0, 1);
            }

            if (listener != null) {
                listener.onParejaFallida(model.getCartas());
            }
        }

        // Actualizar estadísticas
        actualizarEstadisticas();
    }

    private void actualizarEstadisticas() {
        if (listener != null) {
            listener.onEstadisticasActualizadas(
                    model.getIntentos(),
                    model.getParesEncontrados()
            );
        }
    }

    public void reiniciarJuego() {
        model.reiniciarJuego();
        if (listener != null) {
            actualizarEstadisticas();
        }
    }

    // Getters para la vista
    public List<Carta> getCartas() {
        return model.getCartas();
    }

    public int getIntentos() {
        return model.getIntentos();
    }

    public int getParesEncontrados() {
        return model.getParesEncontrados();
    }

    public boolean isJuegoEnCurso() {
        return model.isJuegoEnCurso();
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}