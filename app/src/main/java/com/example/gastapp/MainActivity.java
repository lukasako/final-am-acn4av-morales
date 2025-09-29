package com.example.gastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import java.util.Random;
import java.text.NumberFormat;
import java.util.Locale;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

public class MainActivity extends AppCompatActivity{
    private boolean showingSaldo = true;
    private LinearLayout layoutSaldo, layoutGrafico;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //saludos dinamicos
        TextView tvGreeting = findViewById(R.id.tvGreeting);
        String[] greetings = getResources().getStringArray(R.array.greetings);
        int randomIndex = new Random().nextInt(greetings.length);
        tvGreeting.setText(greetings[randomIndex]);

        //imagen billetera dinamica
        TextView tvSaldoAmount = findViewById(R.id.tvSaldoAmount);
        String saldoStr = getString(R.string.saldo_inicial);
        double saldo = Double.parseDouble(saldoStr);
        //formateo de numero a moneda
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        String saldoFormateado = currencyFormat.format(saldo);
        tvSaldoAmount.setText(saldoFormateado);
        //imagen de billetera dinamica
        ImageView imgLogo = findViewById(R.id.imgLogo);
        if (saldo >= 100000){
            imgLogo.setImageResource(R.drawable.ic_wallet);
        } else{
            imgLogo.setImageResource(R.drawable.ic_wallet_empty);
        }

        //funcion de saldo y grafico
        CardView cardSaldo = findViewById(R.id.cardSaldo);
        layoutSaldo = findViewById(R.id.layoutSaldo);
        layoutGrafico = findViewById(R.id.layoutGrafico);
        pieChart = findViewById(R.id.pieChart);
        setupPieChart();

        cardSaldo.setOnClickListener(v -> toggleCardView());
    }

    private void toggleCardView() {
        if (showingSaldo) {
            layoutSaldo.setVisibility(View.GONE);
            layoutGrafico.setVisibility(View.VISIBLE);
            showingSaldo = false;
        } else {
            layoutSaldo.setVisibility(View.VISIBLE);
            layoutGrafico.setVisibility(View.GONE);
            showingSaldo = true;
        }
    }

    private void setupPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(70f, "Ingresos"));
        entries.add(new PieEntry(30f, "Egresos"));
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{android.graphics.Color.GREEN, android.graphics.Color.RED});
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(true);
        pieChart.invalidate();
    }
}
