package com.example.comercialesreto12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Configuración de botones del menú
        Button agendaButton = findViewById(R.id.agendaButton);
        agendaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AgendaActivity.class);
                startActivity(intent);
            }
        });

        Button partnersButton = findViewById(R.id.partnersButton);
        partnersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar lógica para navegar a PartnersActivity si es necesario
            }
        });

        Button ordersButton = findViewById(R.id.ordersButton);
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar lógica para navegar a OrdersActivity si es necesario
            }
        });
    }
}
