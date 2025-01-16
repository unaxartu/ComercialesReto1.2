package com.example.comercialesreto12;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {  // Cambiar 'OnCreate' a 'onCreate'
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Asegúrate de que 'agenda.xml' exista en tu carpeta 'res/layout'
    }
}