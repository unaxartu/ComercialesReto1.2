package com.example.comercialesreto12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PartnersActivity extends MainActivity {  // Hereda de MainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);

        // Llamar a la configuración del menú para asegurarse de que el header esté configurado
        setupDrawerMenu();

        // Inicialización de vistas
        initializeViews();
    }

    private void initializeViews() {
        Button consultaPartners = findViewById(R.id.consultaPartners);
        Button altaPartner = findViewById(R.id.altaPartner);

        // Verificar si los botones están correctamente configurados
        if (consultaPartners == null || altaPartner == null) {
            Toast.makeText(this, "¡Error! Los botones no están configurados correctamente.", Toast.LENGTH_LONG).show();
        }

        // Configurar las acciones de los botones
        consultaPartners.setOnClickListener(v -> {
            // Acción para consulta de Partners
            Intent intent = new Intent(PartnersActivity.this, ConsultaPartner.class);
            startActivity(intent);
        });

        altaPartner.setOnClickListener(v -> {
            // Acción para alta de nuevo Partner
            Intent intent = new Intent(PartnersActivity.this, AltaPartner.class);
            startActivity(intent);
        });
    }
}
