package com.example.comercialesreto12;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
public class Partners extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {  // Cambiar 'OnCreate' a 'onCreate'
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partners);  // Asegúrate de que 'agenda.xml' exista en tu carpeta 'res/layout'
        Button botonConsultaPartner = findViewById(R.id.consultaPartners);
        Button botonAltaPartner = findViewById(R.id.altaPartner);
        botonConsultaPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultaparner();
            }
        });

        botonAltaPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                altapartner();
            }
        });
        ImageButton buttonBack = findViewById(R.id.flechaAtrasPartners);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual
            }
        });
    }



    public void consultaparner(){
        Intent i = new Intent(this, ConsultaPartner.class);
        startActivity(i);
    }

    public void altapartner(){
        Intent i = new Intent(this, AltaPartner.class);
        startActivity(i);
    }


}
