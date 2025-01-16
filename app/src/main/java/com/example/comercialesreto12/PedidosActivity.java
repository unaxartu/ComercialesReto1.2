package com.example.comercialesreto12;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class PedidosActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        // Llamar a la configuración del menú para asegurarse de que el header esté configurado
        setupDrawerMenu();

    }
}
