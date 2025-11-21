package com.example.gastapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.NumberFormat;
import java.util.Locale;

public class MisMovimientosActivity extends AppCompatActivity {

    public static final int REQ_EDIT = 2001;

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
        addMovementItem("Café", -250.0, "Gasto diario", "29/09/25", "Mercado Pago", false);
        addMovementItem("Sueldo", 300000.0, "Ingreso mensual", "05/09/25", "Efectivo", true);
    }

    private void addMovementItem(String titulo, double monto, String categoria,
                                 String fecha, String medioPago, boolean esIngreso) {

        View item = getLayoutInflater().inflate(R.layout.item_movimiento, llMovimientosList, false);

        TextView tvTitulo = item.findViewById(R.id.tvTitulo);
        TextView tvMonto = item.findViewById(R.id.tvMonto);
        TextView tvCategoria = item.findViewById(R.id.tvCategoria);
        TextView tvFecha = item.findViewById(R.id.tvFecha);
        TextView tvMedioPago = item.findViewById(R.id.tvMedioPago);

        ImageView ivDelete = item.findViewById(R.id.ivDelete);
        ImageView ivEdit = item.findViewById(R.id.ivEdit);

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        tvTitulo.setText(titulo);
        tvMonto.setText(nf.format(Math.abs(monto)));
        tvMonto.setTextColor(getResources().getColor(esIngreso ? R.color.accent_green : R.color.danger));
        tvCategoria.setText(categoria);
        tvFecha.setText(fecha);
        tvMedioPago.setText(medioPago);

        // borrar movimiento
        ivDelete.setOnClickListener(v -> llMovimientosList.removeView(item));

        // editar movimiento → nueva activity
        ivEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditarMovimientoActivity.class);
            intent.putExtra("titulo", titulo);
            intent.putExtra("monto", monto);
            intent.putExtra("categoria", categoria);
            intent.putExtra("fecha", fecha);
            intent.putExtra("medioPago", medioPago);
            intent.putExtra("esIngreso", esIngreso);

            startActivity(intent);
        });

        llMovimientosList.addView(item, 0);
    }
}
