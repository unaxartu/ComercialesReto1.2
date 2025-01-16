package com.example.comercialesreto12;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
public class EnviarDelegacion extends MainActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_delegacion);

        // Llamar a la configuración del menú para asegurarse de que el header esté configurado
        setupDrawerMenu();

    }
}
