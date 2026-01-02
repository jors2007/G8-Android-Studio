package com.espol.aplicacion_g8;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnGestion = findViewById(R.id.button_actividad);
        btnGestion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GestionActividadesActivity.class);
            startActivity(intent);
        });

        Button btnHidratacion = findViewById(R.id.button_hidratacion);
        btnHidratacion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ControlHidratacionActivity.class);
            startActivity(intent);
        });

        Button btnSostenibilidad = findViewById(R.id.button_sostenibilidad);
        btnSostenibilidad.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SostenibilidadActivity.class);
            startActivity(intent);
        });

        Button btnJuego = findViewById(R.id.button_juegoMemoria);
        btnJuego.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, JuegoActivity.class);
            startActivity(intent);
        });
    }
}