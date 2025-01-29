package com.example.comercialesreto12;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.comercialesreto12.MainActivity;

public class PedidosActivity extends MainActivity {

    private LinearLayout containerPedidos; // Contenedor para los pedidos
    private int pedidoCount = 1; // Contador para numerar los pedidos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        // Llama al menú lateral
        setupDrawerMenu();

        // Inicializa el contenedor y el botón
        containerPedidos = findViewById(R.id.containerPedidos);
        ImageButton btnAddPedido = findViewById(R.id.ImagebtnAddPedido);

        // Configura el evento de clic para agregar pedidos
        btnAddPedido.setOnClickListener(v -> addNewPedido());
    }

    /**
     * Método para agregar dinámicamente un nuevo pedido
     */
    private void addNewPedido() {
        pedidoCount++; // Incrementa el número de pedidos

        // Crea un nuevo LinearLayout para el pedido
        LinearLayout newPedidoLayout = new LinearLayout(this);
        newPedidoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 16, 0, 16); // Añade un margen entre pedidos
        newPedidoLayout.setLayoutParams(layoutParams);

        // Crea un TextView para el título del pedido
        TextView textViewPedido = new TextView(this);
        textViewPedido.setText("Pedido " + pedidoCount);  // Muestra el número del pedido
        textViewPedido.setTextColor(getResources().getColor(android.R.color.black));
        textViewPedido.setTextSize(18);  // Ajusta el tamaño del texto
        textViewPedido.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Crea un EditText para ingresar los detalles del pedido
        EditText editTextPedido = new EditText(this);
        editTextPedido.setHint("Ingrese el pedido");
        editTextPedido.setHintTextColor(Color.WHITE); // Establece el color del hint a blanco
        editTextPedido.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Crea un EditText para ingresar la cantidad del pedido
        EditText editTextCantidad = new EditText(this);
        editTextCantidad.setHint("Ingrese la cantidad");
        editTextCantidad.setHintTextColor(Color.WHITE); // Establece el color del hint a blanco
        editTextCantidad.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Añade el TextView, el EditText del pedido y la cantidad al nuevo LinearLayout
        newPedidoLayout.addView(textViewPedido);
        newPedidoLayout.addView(editTextPedido);
        newPedidoLayout.addView(editTextCantidad);

        // Añade el nuevo pedido al contenedor de pedidos
        containerPedidos.addView(newPedidoLayout);

        // Verifica si el pedido fue añadido correctamente
        Log.d("PedidosActivity", "Nuevo pedido agregado: Pedido " + pedidoCount);
    }
}
