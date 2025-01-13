package com.example.comercialesreto12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);

        Button botonAgenda = findViewById(R.id.agenda);
        Button botonPartners = findViewById(R.id.partners);
        Button botonPedidos = findViewById(R.id.pedidos);
        Button botonEnviarDelegacion = findViewById(R.id.enviardelegacion);
        Button botonLogin = findViewById(R.id.logearse);


        botonAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agenda();
            }
        });

        botonPartners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                partners();
            }
        });

        botonPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pedidos();
            }
        });

        botonEnviarDelegacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarDelegacion();
            }
        });

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });







        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });


        }
    }

    public void agenda() {
        Intent i = new Intent(this, Agenda.class);
        startActivity(i);
    }

    public void partners(){
        Intent i = new Intent(this, Partners.class);
        startActivity(i);
    }

    public void pedidos(){
        Intent i = new Intent(this, Pedidos.class);
        startActivity(i);
    }

    public void enviarDelegacion(){
        Intent i = new Intent(this, EnviarDelegacion.class);
        startActivity(i);
    }

    public void login(){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }


}
