package com.espol.aplicacion_g8.modelo.juego;

import com.espol.aplicacion_g8.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class JuegoMemoria {
    private List<Carta> cartas;
    private int intentos;
    private int paresEncontrados;
    private boolean juegoEnCurso;
    private Carta primeraSeleccion;
    private Carta segundaSeleccion;
    private boolean bloqueado;

    public JuegoMemoria() {
        cartas = new ArrayList<>();
        intentos = 0;
        paresEncontrados = 0;
        juegoEnCurso = true;
        bloqueado = false;
        inicializarCartas();
    }

    private void inicializarCartas() {
        cartas.clear();

        // 8 pares = 16 cartas
        int[] imagenes = {
                R.drawable.img_agua, R.drawable.img_arbol,
                R.drawable.img_bici, R.drawable.img_foco,
                R.drawable.img_hoja, R.drawable.img_nube,
                R.drawable.img_reciclaje, R.drawable.img_sol
        };

        String[] nombres = {
                "Ahorro de Agua", "Árbol", "Bicicleta", "Foco Ahorrador",
                "Hoja", "Nube Limpia", "Reciclaje", "Energía Solar"
        };

        String[] descripciones = {
                "Conserva el agua, es vida",
                "Planta árboles, purifican el aire",
                "Transporte sostenible y saludable",
                "Ahorra energía, cuida el planeta",
                "Cuida la naturaleza, es tu hogar",
                "Aire limpio para respirar mejor",
                "Reduce, reusa y recicla",
                "Energía renovable e inagotable"
        };

        // Crear pares
        for (int i = 0; i < 8; i++) {
            cartas.add(new Carta(i*2, imagenes[i], nombres[i], descripciones[i]));
            cartas.add(new Carta(i*2+1, imagenes[i], nombres[i], descripciones[i]));
        }

        mezclarCartas();
    }

    public void mezclarCartas() {
        Collections.shuffle(cartas);
    }

    public boolean seleccionarCarta(int posicion) {
        if (bloqueado || !juegoEnCurso) return false;

        Carta carta = cartas.get(posicion);

        if (carta.isEmparejada() || carta.isVisible()) return false;

        carta.setVisible(true);

        if (primeraSeleccion == null) {
            primeraSeleccion = carta;
            return false; // Necesita segunda selección
        } else {
            segundaSeleccion = carta;
            intentos++;
            bloqueado = true;
            return true; // Listo para verificar
        }
    }

    public boolean verificarPareja() {
        if (primeraSeleccion == null || segundaSeleccion == null) return false;

        boolean sonPareja = primeraSeleccion.getIdImagen() == segundaSeleccion.getIdImagen();

        if (sonPareja) {
            primeraSeleccion.setEmparejada(true);
            segundaSeleccion.setEmparejada(true);
            paresEncontrados++;

            if (paresEncontrados == 8) {
                juegoEnCurso = false;
            }
        } else {
            primeraSeleccion.setVisible(false);
            segundaSeleccion.setVisible(false);
        }

        primeraSeleccion = null;
        segundaSeleccion = null;
        bloqueado = false;

        return sonPareja;
    }

    public void reiniciarJuego() {
        intentos = 0;
        paresEncontrados = 0;
        juegoEnCurso = true;
        bloqueado = false;
        primeraSeleccion = null;
        segundaSeleccion = null;

        for (Carta carta : cartas) {
            carta.setEmparejada(false);
            carta.setVisible(false);
        }

        mezclarCartas();
    }

    // Getters
    public List<Carta> getCartas() { return cartas; }
    public int getIntentos() { return intentos; }
    public int getParesEncontrados() { return paresEncontrados; }
    public boolean isJuegoEnCurso() { return juegoEnCurso; }
    public boolean isBloqueado() { return bloqueado; }
    public boolean isJuegoCompletado() { return paresEncontrados == 8; }
}
