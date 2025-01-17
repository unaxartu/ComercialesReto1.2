package com.example.comercialesreto12;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class EnviarDelegacion extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_delegacion);  // Cargar el layout específico para esta actividad

        // Llamar a la configuración del menú heredado
        setupDrawerMenu();
    }
}
