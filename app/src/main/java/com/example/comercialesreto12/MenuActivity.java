package com.example.comercialesreto12;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_lateral);

        // Verifica si el botón ordersButton existe
        Button ordersButton = findViewById(R.id.ordersButton);
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pedidos();
            }
        });
    }

    private void pedidos() {
        Log.d(TAG, "Navigating to PedidosActivity");
        Intent i = new Intent(MenuActivity.this, PedidosActivity.class);
        startActivity(i);
    }
}
