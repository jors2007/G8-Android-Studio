package com.espol.aplicacion_g8;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.espol.aplicacion_g8.R;
import com.google.android.material.button.MaterialButton;

public class FormularioActivity extends AppCompatActivity {

    public static final String EXTRA_MODO = "modo";
    public static final String MODO_META = "meta";
    public static final String MODO_TOMA = "toma";
    public static final String EXTRA_VALOR = "valor";

    private EditText inputGigante;
    private String modoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_formulario_hidratacion);


        TextView titulo = findViewById(R.id.txtTituloFormulario);
        TextView subtitulo = findViewById(R.id.txtSubtituloFormulario);
        inputGigante = findViewById(R.id.etInputValorGigante);
        MaterialButton btnGuardar = findViewById(R.id.btnGuardarFormulario);


        modoActual = getIntent().getStringExtra(EXTRA_MODO);


        if (MODO_META.equals(modoActual)) {
            titulo.setText("Editar Meta");
            subtitulo.setText("Nueva meta diaria (ml)");
        } else {

            titulo.setText("Registrar Agua");
            subtitulo.setText("Cantidad bebida (ml)");
        }


        btnGuardar.setOnClickListener(v -> {
            String texto = inputGigante.getText().toString();


            if (TextUtils.isEmpty(texto)) {
                inputGigante.setError("Escribe un número");
                return;
            }

            int valor = Integer.parseInt(texto);


            Intent respuesta = new Intent();
            respuesta.putExtra(EXTRA_VALOR, valor);
            respuesta.putExtra(EXTRA_MODO, modoActual);

            setResult(RESULT_OK, respuesta);
            finish();
        });
    }
}
