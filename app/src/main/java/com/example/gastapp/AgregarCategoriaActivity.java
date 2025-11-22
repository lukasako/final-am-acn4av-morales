package com.example.gastapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class AgregarCategoriaActivity extends AppCompatActivity {

    private EditText etCategoria;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_categoria);

        // Toolbar correcta (según tu XML)
        MaterialToolbar toolbar = findViewById(R.id.toolbarEditarCategoria);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Título dinámico
        String name = getIntent().getStringExtra("name");
        if (name != null) {
            toolbar.setTitle("Editar Categoría");
        } else {
            toolbar.setTitle("Agregar Categoría");
        }

        etCategoria = findViewById(R.id.etCategoriaNombre);
        btnGuardar = findViewById(R.id.btnGuardarCategoriaFinal);

        // Modo edición
        if (name != null) {
            etCategoria.setText(name);
        }

        btnGuardar.setOnClickListener(v -> {
            Intent data = new Intent();
            data.putExtra("name", etCategoria.getText().toString().trim());
            setResult(RESULT_OK, data);
            finish();
        });
    }
}
