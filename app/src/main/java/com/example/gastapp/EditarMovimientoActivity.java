package com.example.gastapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditarMovimientoActivity extends AppCompatActivity {

    private EditText etCategoria, etNombre, etMedioPago, etMonto;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_movimiento);

        Toolbar toolbar = findViewById(R.id.toolbarEditarMov);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        etCategoria = findViewById(R.id.etCategoria);
        etNombre = findViewById(R.id.etNombre);
        etMedioPago = findViewById(R.id.etMedioPago);
        etMonto = findViewById(R.id.etMonto);
        btnConfirm = findViewById(R.id.btnConfirm);

        // recibir datos enviados
        Intent data = getIntent();
        etNombre.setText(data.getStringExtra("titulo"));
        etCategoria.setText(data.getStringExtra("categoria"));
        etMedioPago.setText(data.getStringExtra("medioPago"));
        etMonto.setText(String.valueOf(data.getDoubleExtra("monto", 0)));

        // guardar cambios
        btnConfirm.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("titulo", etNombre.getText().toString());
            result.putExtra("categoria", etCategoria.getText().toString());
            result.putExtra("medioPago", etMedioPago.getText().toString());
            result.putExtra("monto", Double.parseDouble(etMonto.getText().toString()));
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
