package com.espol.aplicacion_g8.utils;

import android.content.Context;
import android.media.MediaPlayer;
import com.espol.aplicacion_g8.R;

public class SonidoManager {
    private MediaPlayer mediaPlayerFlip;
    private MediaPlayer mediaPlayerSuccess;
    private MediaPlayer mediaPlayerFail;
    private Context context;
    private boolean sonidosActivados = true;

    public SonidoManager(Context context) {
        this.context = context;
        inicializarSonidos();
    }

    private void inicializarSonidos() {
        mediaPlayerFlip = MediaPlayer.create(context, R.raw.flip);
        mediaPlayerSuccess = MediaPlayer.create(context, R.raw.success);
        mediaPlayerFail = MediaPlayer.create(context, R.raw.fail);
    }

    public void playFlipSound() {
        if (sonidosActivados && mediaPlayerFlip != null) {
            try {
                mediaPlayerFlip.seekTo(0);
                mediaPlayerFlip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playSuccessSound() {
        if (sonidosActivados && mediaPlayerSuccess != null) {
            try {
                mediaPlayerSuccess.seekTo(0);
                mediaPlayerSuccess.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playFailSound() {
        if (sonidosActivados && mediaPlayerFail != null) {
            try {
                mediaPlayerFail.seekTo(0);
                mediaPlayerFail.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSonidosActivados(boolean activados) {
        this.sonidosActivados = activados;
    }

    public boolean areSonidosActivados() {
        return sonidosActivados;
    }

    public void release() {
        if (mediaPlayerFlip != null) {
            mediaPlayerFlip.release();
            mediaPlayerFlip = null;
        }
        if (mediaPlayerSuccess != null) {
            mediaPlayerSuccess.release();
            mediaPlayerSuccess = null;
        }
        if (mediaPlayerFail != null) {
            mediaPlayerFail.release();
            mediaPlayerFail = null;
        }
    }
}