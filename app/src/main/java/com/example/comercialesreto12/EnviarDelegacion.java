package com.example.comercialesreto12;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class EnviarDelegacion extends MainActivity {

    private DBHandler dbHandler;
    private ListView listViewPedidos;
    private Button botonDelegacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_delegacion);

        // Inicializar el DBHandler
        try {
            dbHandler = new DBHandler(this);
        } catch (Exception e) {
            Log.e("EnviarDelegacion", "Error al inicializar la base de datos", e);
            Toast.makeText(this, "Error al inicializar la base de datos", Toast.LENGTH_SHORT).show();
            return;  // Detener el proceso si no se puede inicializar la base de datos
        }

        // Configurar el ListView
        listViewPedidos = findViewById(R.id.listViewPedidos);
        botonDelegacion = findViewById(R.id.botonDelegacion);

        // Obtener los pedidos pendientes desde la base de datos
        List<String> pedidosPendientes = null;
        try {
            pedidosPendientes = dbHandler.obtenerPedidosPendientes();
        } catch (Exception e) {
            Log.e("EnviarDelegacion", "Error al obtener los pedidos pendientes", e);
            Toast.makeText(this, "Error al obtener los pedidos", Toast.LENGTH_SHORT).show();
        }

        // Verifica que la lista no sea null o vacía
        if (pedidosPendientes == null || pedidosPendientes.isEmpty()) {
            // Si es null o vacía, crea una lista vacía
            pedidosPendientes = new ArrayList<>();
            Log.w("EnviarDelegacion", "No hay pedidos pendientes para mostrar.");
        }

        // Hacer pedidosPendientes final
        final List<String> finalPedidosPendientes = pedidosPendientes;

        // Crear el adaptador para el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, finalPedidosPendientes);
        listViewPedidos.setAdapter(adapter);

        // Configurar el click en un item de la lista
        listViewPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Aquí puedes manejar lo que pasa cuando seleccionas un pedido
                String pedidoSeleccionado = finalPedidosPendientes.get(position);
                Toast.makeText(EnviarDelegacion.this, "Pedido seleccionado: " + pedidoSeleccionado, Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el botón para enviar la delegación
        botonDelegacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para enviar la delegación
                enviarDelegacion(finalPedidosPendientes);
            }
        });
    }

    // Método para enviar la delegación
    private void enviarDelegacion(List<String> pedidosPendientes) {
        if (pedidosPendientes.isEmpty()) {
            // Si no hay pedidos pendientes, muestra un mensaje
            Toast.makeText(this, "No hay pedidos pendientes para enviar.", Toast.LENGTH_SHORT).show();
        } else {
            // Aquí puedes implementar la lógica para enviar la delegación
            // Esto puede incluir marcar los pedidos como enviados en la base de datos

            for (String pedido : pedidosPendientes) {
                try {
                    // Extraer el pedidoId desde el string
                    int pedidoId = dbHandler.obtenerPedidoIdDesdeString(pedido);

                    // Verifica si el pedidoId es válido
                    if (pedidoId != -1) {
                        // Aquí puedes actualizar el estado del pedido en la base de datos
                        dbHandler.marcarPedidoComoEnviado(pedidoId);
                        Log.d("EnviarDelegacion", "Pedido enviado: " + pedidoId);
                    } else {
                        Log.w("EnviarDelegacion", "Pedido no encontrado en la base de datos: " + pedido);
                    }

                } catch (Exception e) {
                    Log.e("EnviarDelegacion", "Error al procesar el pedido: " + pedido, e);
                    Toast.makeText(this, "Error al procesar el pedido: " + pedido, Toast.LENGTH_SHORT).show();
                }
            }

            // Mostrar mensaje de éxito
            Toast.makeText(this, "Delegación enviada con éxito.", Toast.LENGTH_SHORT).show();
        }
    }
}
