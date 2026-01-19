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
    public static final String EXTRA_VALOR_ACTUAL = "valor_actual";

    private EditText inputGigante;
    private String modoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_hidratacion);


        TextView txtTitulo = findViewById(R.id.txtTituloFormulario);
        TextView txtSubtitulo = findViewById(R.id.txtSubtituloFormulario);
        inputGigante = findViewById(R.id.etInputValorGigante);
        MaterialButton btnGuardar = findViewById(R.id.btnGuardarFormulario);


        Intent intent = getIntent();
        modoActual = intent.getStringExtra(EXTRA_MODO);
        int valorRecibido = intent.getIntExtra(EXTRA_VALOR_ACTUAL, 0);


        if (MODO_META.equals(modoActual)) {
            txtTitulo.setText("Editar Meta");

            txtSubtitulo.setText("Meta actual: " + valorRecibido + " ml");


            if (valorRecibido > 0) {
                inputGigante.setText(String.valueOf(valorRecibido));

                inputGigante.setSelection(inputGigante.getText().length());
            }

        } else {

            txtTitulo.setText("Registrar Agua");
            txtSubtitulo.setText("Cantidad bebida (ml)");
            inputGigante.setHint("0");
        }


        btnGuardar.setOnClickListener(v -> guardarYSalir());
    }

    private void guardarYSalir() {
        String texto = inputGigante.getText().toString();


        if (TextUtils.isEmpty(texto)) {
            inputGigante.setError("Debes ingresar un número");
            return;
        }

        int valor = Integer.parseInt(texto);


        if (valor <= 0) {
            inputGigante.setError("El valor debe ser mayor a 0");
            return;
        }


        Intent respuesta = new Intent();
        respuesta.putExtra(EXTRA_VALOR, valor);
        respuesta.putExtra(EXTRA_MODO, modoActual);

        setResult(RESULT_OK, respuesta);
        finish();
    }


}
