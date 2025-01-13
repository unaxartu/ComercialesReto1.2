package com.example.comercialesreto12;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Agenda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {  // Cambiar 'OnCreate' a 'onCreate'
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda);  // Asegúrate de que 'agenda.xml' exista en tu carpeta 'res/layout'

        ImageButton buttonBack = findViewById(R.id.flechaAtrasAgenda);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual
            }
        });
    }


}
