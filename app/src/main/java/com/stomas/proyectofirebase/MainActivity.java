package com.stomas.proyectofirebase;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText etCodigoChip, etNombreMascota, etNombreDueno, etDireccionDueno;
    private Spinner spTipoMascota;
    private Button btnEnviarDatos, btnCargarDatos;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes
        etCodigoChip = findViewById(R.id.etCodigoChip);
        etNombreMascota = findViewById(R.id.etNombreMascota);
        spTipoMascota = findViewById(R.id.spTipoMascota);
        etNombreDueno = findViewById(R.id.etNombreDueno);
        etDireccionDueno = findViewById(R.id.etDireccionDueno);
        btnEnviarDatos = findViewById(R.id.btnEnviarDatos);
        btnCargarDatos = findViewById(R.id.btnCargarDatos);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Configurar botones
        btnEnviarDatos.setOnClickListener(v -> enviarDatos());
        btnCargarDatos.setOnClickListener(v -> cargarDatos());
    }

    private void enviarDatos() {
        // Obtener datos del formulario
        String codigoChip = etCodigoChip.getText().toString();
        String nombreMascota = etNombreMascota.getText().toString();
        String tipoMascota = spTipoMascota.getSelectedItem().toString();
        String nombreDueno = etNombreDueno.getText().toString();
        String direccionDueno = etDireccionDueno.getText().toString();

        // Validar campos
        if (codigoChip.isEmpty() || nombreMascota.isEmpty() || nombreDueno.isEmpty() || direccionDueno.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear mapa con los datos
        Map<String, Object> mascota = new HashMap<>();
        mascota.put("codigoChip", codigoChip);
        mascota.put("nombreMascota", nombreMascota);
        mascota.put("tipoMascota", tipoMascota);
        mascota.put("nombreDueno", nombreDueno);
        mascota.put("direccionDueno", direccionDueno);

        // Enviar datos a Firestore
        db.collection("mascotas").add(mascota)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Datos enviados correctamente", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al enviar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void cargarDatos() {
        // Leer datos de Firestore
        db.collection("mascotas").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    StringBuilder datos = new StringBuilder();
                    queryDocumentSnapshots.forEach(document -> {
                        String codigoChip = document.getString("codigoChip");
                        String nombreMascota = document.getString("nombreMascota");
                        String tipoMascota = document.getString("tipoMascota");
                        String nombreDueno = document.getString("nombreDueno");
                        String direccionDueno = document.getString("direccionDueno");
                        datos.append("Chip: ").append(codigoChip)
                                .append(", Mascota: ").append(nombreMascota)
                                .append(", Tipo: ").append(tipoMascota)
                                .append(", Dueño: ").append(nombreDueno)
                                .append(", Dirección: ").append(direccionDueno)
                                .append("\n");
                    });
                    Toast.makeText(this, datos.toString(), Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al cargar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
