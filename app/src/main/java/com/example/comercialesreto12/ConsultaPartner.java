package com.example.comercialesreto12;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ConsultaPartner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_partner);

        Log.d("ConsultaPartner", "Iniciando actividad...");

        ListView listView = findViewById(R.id.listView);
        ImageButton buttonBack = findViewById(R.id.flechaAtrasConsultaPartners);

        if (listView == null) {
            Log.e("ConsultaPartner", "Error: ListView no encontrado en el layout");
            return;
        }

        // Obtener los clientes desde la base de datos
        DBHandler dbHandler = new DBHandler(this);
        List<String> nombresCompletos = dbHandler.obtenerPartners();

        if (nombresCompletos.isEmpty()) {
            Log.e("ConsultaPartner", "No se encontraron partners en la base de datos");
            Toast.makeText(this, "No hay partners registrados", Toast.LENGTH_SHORT).show();
        }

        // Adaptador para la lista
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombresCompletos);
        listView.setAdapter(adapter);

        // Botón para volver atrás
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> finish());
        }

        Log.d("ConsultaPartner", "Configuración completada con éxito");
    }
}
