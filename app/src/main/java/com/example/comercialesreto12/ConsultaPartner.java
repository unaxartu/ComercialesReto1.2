package com.example.comercialesreto12;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ConsultaPartner extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {  // Cambiar 'OnCreate' a 'onCreate'
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consulta_partner);  // Asegúrate de que 'agenda.xml' exista en tu carpeta 'res/layout'
        ListView listView = findViewById(R.id.listView);

        // Datos para el ListView
        String[] nombres = {"Javi", "Unax", "Yeray", "Aitor"};
        String[] apellidos = {"Carmona", "Artuzamonoa", "Martínez", "Lopez"};

        // Crear un array combinado
        String[] nombresCompletos = new String[nombres.length];
        for (int i = 0; i < nombres.length; i++) {
            nombresCompletos[i] = nombres[i] + "                              " + apellidos[i];
        }

        // Crear un ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombresCompletos);

        // Asignar el adaptador al ListView
        listView.setAdapter(adapter);

        ImageButton buttonBack = findViewById(R.id.flechaAtrasConsultaPartners);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual
            }
        });

    }
}
