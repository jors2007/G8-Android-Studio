// AppPreferences.java
package com.espol.aplicacion_g8.modelo.actividad;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_CONTADOR_ID = "contadorId";

    private SharedPreferences prefs;

    public AppPreferences(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void guardarContadorId(int contador) {
        prefs.edit().putInt(KEY_CONTADOR_ID, contador).apply();
    }

    public int obtenerContadorId() {
        return prefs.getInt(KEY_CONTADOR_ID, 0); // 0 por defecto
    }
}