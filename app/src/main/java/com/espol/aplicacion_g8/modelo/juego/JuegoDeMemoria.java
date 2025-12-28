package com.espol.aplicacion_g8.modelo.juego;

import java.util.ArrayList;

public class JuegoDeMemoria{
    // en el controlador, aqui solo van los atributos y pues el constructor getters y setters, el controlador se encarga de eso
    private int intentos;
    private Carta primeraCarta;
    private Carta segundaCarta;
    private int paresEncontrados;
    private ArrayList<Carta> cartas;
    private boolean juegoTerminado;
    private static final int TOTAL_PARES = 8;


    //Constructor
    public JuegoDeMemoria(){
        this.cartas = new ArrayList<>();
        this.primeraCarta = null;
        this.segundaCarta = null;
        this.intentos = 0;
        this.paresEncontrados = 0;
        this.juegoTerminado = false;
    }

    // Getters && setters
    public ArrayList<Carta> getCartas(){
        return cartas;
    }

    public void setCartas(ArrayList<Carta> cartas){
        this.cartas = cartas;
    }

    public int getIntentos(){
        return intentos;
    }

    public void setIntentos(int intentos){
        this.intentos = intentos;
    }

    public int getParesEncontrados(){
        return paresEncontrados;
    }

    public void setParesEncontrados(int paresEncontrados){
        this.paresEncontrados = paresEncontrados;
    }

    public boolean isJuegoTerminado(){
        return juegoTerminado;
    }

    public void setJuegoTerminado(boolean juegoTerminado){
        this.juegoTerminado = juegoTerminado;
    }

    public Carta getPrimeraCarta(){
        return primeraCarta;
    }

    public Carta getSegundaCarta(){
        return segundaCarta;
    }

    public void setPrimeraCarta(Carta primeraCarta){
        this.primeraCarta = primeraCarta;
    }

    public void setSegundaCarta(Carta segundaCarta){
        this.segundaCarta = segundaCarta;
    }

    public int getTotalCartas(){
        return cartas.size();
    }

    public static int getTOTAL_PARES(){
        return TOTAL_PARES;
    }
}

