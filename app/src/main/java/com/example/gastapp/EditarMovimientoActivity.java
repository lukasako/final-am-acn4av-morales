package com.example.gastapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gastapp.models.Movimiento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditarMovimientoActivity extends AppCompatActivity {

    private EditText etNombreEdit, etMontoEdit, etFechaEdit;
    private AutoCompleteTextView categoriaPickerEdit, medioPagoPickerEdit, tipoPickerEdit, esFijoPickerEdit;
    private Button btnConfirm;

    private FirebaseFirestore db;
    private String docId;
    private String uid;

    private List<String> categoriasIds = new ArrayList<>();
    private List<String> categoriasNombres = new ArrayList<>();
    private List<String> cuentasIds = new ArrayList<>();
    private List<String> cuentasNombres = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_movimiento);

        Toolbar toolbar = findViewById(R.id.toolbarEditarMov);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());

        categoriaPickerEdit = findViewById(R.id.categoriaPickerEdit);
        medioPagoPickerEdit = findViewById(R.id.medioPagoPickerEdit);
        tipoPickerEdit = findViewById(R.id.tipoPickerEdit);
        esFijoPickerEdit = findViewById(R.id.esFijoPickerEdit);

        etNombreEdit = findViewById(R.id.etNombreEdit);
        etMontoEdit = findViewById(R.id.etMontoEdit);
        etFechaEdit = findViewById(R.id.etFechaEdit);

        btnConfirm = findViewById(R.id.btnConfirm);

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        docId = getIntent().getStringExtra("docId");

        setupStaticPickers();
        loadCategorias();
        loadCuentas();

        cargarDatosMovimiento();
        configurarPickerFecha();

        btnConfirm.setOnClickListener(v -> actualizarMovimiento());
    }

    private void setupStaticPickers() {
        tipoPickerEdit.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                new String[]{"Ingreso", "Egreso"}));

        esFijoPickerEdit.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                new String[]{"Sí", "No"}));
    }

    private void cargarDatosMovimiento() {
        if (docId == null) return;

        db.collection("usuarios").document(uid).collection("movimientos")
                .document(docId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) return;

                    Movimiento m = doc.toObject(Movimiento.class);
                    if (m == null) return;

                    etNombreEdit.setText(m.getTitulo());
                    etMontoEdit.setText(String.valueOf(m.getMonto()));
                    etFechaEdit.setText(m.getFecha());

                    tipoPickerEdit.setText(m.isEsIngreso() ? "Ingreso" : "Egreso", false);
                    esFijoPickerEdit.setText(m.isEsFijo() ? "Sí" : "No", false);

                    if (m.getCategoriaId() != null) {
                        db.collection("usuarios").document(uid).collection("categorias")
                                .document(m.getCategoriaId())
                                .get()
                                .addOnSuccessListener(c -> {
                                    if (c.exists())
                                        categoriaPickerEdit.setText(c.getString("nombre"), false);
                                });
                    }

                    if (m.getMedioPagoId() != null) {
                        db.collection("usuarios").document(uid).collection("cuentas")
                                .document(m.getMedioPagoId())
                                .get()
                                .addOnSuccessListener(c -> {
                                    if (c.exists())
                                        medioPagoPickerEdit.setText(c.getString("nombre"), false);
                                });
                    }
                });
    }

    private void loadCategorias() {
        db.collection("usuarios").document(uid).collection("categorias")
                .get()
                .addOnSuccessListener(qs -> {
                    categoriasIds.clear();
                    categoriasNombres.clear();
                    for (DocumentSnapshot d : qs) {
                        categoriasIds.add(d.getId());
                        categoriasNombres.add(d.getString("nombre"));
                    }
                    categoriaPickerEdit.setAdapter(new ArrayAdapter<>(
                            this, android.R.layout.simple_dropdown_item_1line, categoriasNombres));
                });
    }

    private void loadCuentas() {
        db.collection("usuarios").document(uid).collection("cuentas")
                .get()
                .addOnSuccessListener(qs -> {
                    cuentasIds.clear();
                    cuentasNombres.clear();
                    for (DocumentSnapshot d : qs) {
                        cuentasIds.add(d.getId());
                        cuentasNombres.add(d.getString("nombre"));
                    }
                    medioPagoPickerEdit.setAdapter(new ArrayAdapter<>(
                            this, android.R.layout.simple_dropdown_item_1line, cuentasNombres));
                });
    }

    private void configurarPickerFecha() {
        etFechaEdit.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> {
                String f = String.format(Locale.getDefault(),
                        "%02d/%02d/%02d", d, m + 1, y % 100);
                etFechaEdit.setText(f);
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void actualizarMovimiento() {
        String nombre = etNombreEdit.getText().toString().trim();
        String montoStr = etMontoEdit.getText().toString().trim();
        String fecha = etFechaEdit.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(montoStr)) {
            Toast.makeText(this, "Completá nombre y monto", Toast.LENGTH_SHORT).show();
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (Exception e) {
            Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean esIngreso = tipoPickerEdit.getText().toString().equals("Ingreso");
        boolean esFijo = esFijoPickerEdit.getText().toString().equals("Sí");

        String categoriaName = categoriaPickerEdit.getText().toString().trim();
        String categoriaId = categoriasNombres.contains(categoriaName)
                ? categoriasIds.get(categoriasNombres.indexOf(categoriaName))
                : null;

        String cuentaName = medioPagoPickerEdit.getText().toString().trim();
        String cuentaId = cuentasNombres.contains(cuentaName)
                ? cuentasIds.get(cuentasNombres.indexOf(cuentaName))
                : null;

        db.collection("usuarios").document(uid).collection("movimientos")
                .document(docId)
                .update(
                        "titulo", nombre,
                        "monto", monto,
                        "fecha", fecha,
                        "esIngreso", esIngreso,
                        "esFijo", esFijo,
                        "categoriaId", categoriaId,
                        "medioPagoId", cuentaId
                )
                .addOnSuccessListener(a -> {
                    Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
