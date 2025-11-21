package com.example.gastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.NumberFormat;
import java.util.Locale;

public class MisMovimientosActivity extends AppCompatActivity {

    private LinearLayout llMovimientosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_movimientos);

        Toolbar toolbar = findViewById(R.id.toolbarMovimientos);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        llMovimientosList = findViewById(R.id.llMovimientosList);

        // Ejemplos
        addMovementItem("Sueldo", 300000.0, "Ingreso mensual", "05/09/25", "Efectivo", true);
        addMovementItem("Gimnasio", -45500.0, "Suscripciones", "02/10/25", "Banco Galicia", false);
    }

    private void addMovementItem(String titulo, double monto, String categoria, String fecha, String medioPago, boolean esIngreso) {

        View item = getLayoutInflater().inflate(R.layout.item_movimiento, llMovimientosList, false);

        TextView tvTitulo = item.findViewById(R.id.tvTitulo);
        TextView tvMonto = item.findViewById(R.id.tvMonto);
        TextView tvCategoria = item.findViewById(R.id.tvCategoria);
        TextView tvFecha = item.findViewById(R.id.tvFecha);
        TextView tvMedioPago = item.findViewById(R.id.tvMedioPago);

        tvTitulo.setText(titulo);

        // Formateo de monto
        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        tvMonto.setText(formato.format(Math.abs(monto)));
        tvMonto.setTextColor(getResources().getColor(esIngreso ? R.color.accent_green : R.color.danger));

        tvCategoria.setText(categoria);
        tvFecha.setText(fecha);
        tvMedioPago.setText(medioPago);

        llMovimientosList.addView(item, 0);
    }
}
