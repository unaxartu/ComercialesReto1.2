package com.example.comercialesreto12;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnviarDelegacion extends MainActivity {
    private DBHandler dbHandler;
    private ListView listViewPedidos;
    private Button botonDelegacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_delegacion);

        setupDrawerMenu();
        setupInfoButton();
        setupCompanyIconButton();
        setButtonActions();

        dbHandler = new DBHandler(this);

        listViewPedidos = findViewById(R.id.listViewPedidos);
        botonDelegacion = findViewById(R.id.botonDelegacion);

        refrescarListaPedidos();

        listViewPedidos.setOnItemClickListener((parent, view, position, id) -> {
            String pedidoSeleccionado = (String) parent.getItemAtPosition(position);
            Toast.makeText(EnviarDelegacion.this, "Pedido seleccionado: " + pedidoSeleccionado, Toast.LENGTH_SHORT).show();
        });

        botonDelegacion.setOnClickListener(v -> enviarDelegacionAPedidosAPI());
    }

    private List<String> obtenerPedidosFormateados() {
        List<String> pedidosFormateados = new ArrayList<>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT id_pedido, id_cliente, fecha_pedido, id_experiencia, cantidad, n_pedido FROM " + DBHandler.TABLE_PEDIDOS;
            cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                int idPedido = cursor.getInt(cursor.getColumnIndexOrThrow("id_pedido"));
                int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente"));
                int idExperiencia = cursor.getInt(cursor.getColumnIndexOrThrow("id_experiencia"));
                String fechaPedido = cursor.getString(cursor.getColumnIndexOrThrow("fecha_pedido"));
                int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));
                int nPedido = cursor.getInt(cursor.getColumnIndexOrThrow("n_pedido"));

                String pedido = "Pedido ID: " + idPedido +
                        ", Cliente ID: " + idCliente +
                        ", Fecha: " + fechaPedido +
                        ", Experiencia ID: " + idExperiencia +
                        ", Cantidad: " + cantidad +
                        ", Número de Pedido: " + nPedido;
                pedidosFormateados.add(pedido);
            }
        } catch (Exception e) {
            Log.e("EnviarDelegacion", "Error al obtener pedidos", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return pedidosFormateados;
    }

    private void enviarDelegacionAPedidosAPI() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT id_pedido, id_cliente, fecha_pedido, id_experiencia, cantidad, n_pedido FROM " + DBHandler.TABLE_PEDIDOS;
            cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                int idPedido = cursor.getInt(cursor.getColumnIndexOrThrow("id_pedido"));
                int idCliente = 1;
                int idExperiencia = cursor.getInt(cursor.getColumnIndexOrThrow("id_experiencia"));
                String fechaPedido = convertirFecha(cursor.getString(cursor.getColumnIndexOrThrow("fecha_pedido")));
                int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));
                int nPedido = cursor.getInt(cursor.getColumnIndexOrThrow("n_pedido"));

                PedidoRequest pedidoRequest = new PedidoRequest(idCliente, idExperiencia, fechaPedido, cantidad, nPedido);
                enviarPedidoAPedidosAPI(pedidoRequest, idPedido);
            }
        } catch (Exception e) {
            Log.e("EnviarDelegacion", "Error al leer pedidos", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    private void enviarPedidoAPedidosAPI(PedidoRequest pedidoRequest, int pedidoId) {
        Log.d("EnviarDelegacion", "Enviando pedido: " +
                " Cliente: " + pedidoRequest.getIdCliente() +
                " Experiencia: " + pedidoRequest.getIdExperiencia() +
                " Fecha: " + pedidoRequest.getFechaPedido() +
                " Cantidad: " + pedidoRequest.getCantidad() +
                " N_Pedido: " + pedidoRequest.getNPedido()
        );

        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.enviarPedido(pedidoRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    dbHandler.eliminarPedido(pedidoId);
                    Log.d("EnviarDelegacion", "Pedido enviado y eliminado: " + pedidoId);
                    Toast.makeText(EnviarDelegacion.this, "Pedido enviado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Log.e("EnviarDelegacion", "Error al enviar pedido. Código: " + response.code() +
                                " - " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("EnviarDelegacion", "Error al leer el error", e);
                    }
                    Toast.makeText(EnviarDelegacion.this, "Error al enviar el pedido", Toast.LENGTH_SHORT).show();
                }
                refrescarListaPedidos();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("EnviarDelegacion", "Fallo en el envío", t);
                Toast.makeText(EnviarDelegacion.this, "Error al enviar el pedido", Toast.LENGTH_SHORT).show();
                refrescarListaPedidos();
            }
        });
    }

    private void refrescarListaPedidos() {
        List<String> pedidos = obtenerPedidosFormateados();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pedidos);
        listViewPedidos.setAdapter(adapter);
    }
    private String convertirFecha(String fechaOriginal) {
        try {
            // Intentar detectar el formato original
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return formatoSalida.format(formatoEntrada.parse(fechaOriginal));
        } catch (Exception e) {
            Log.e("EnviarDelegacion", "Error al convertir la fecha", e);
            return "2025-01-01"; // Valor por defecto para evitar errores
        }
    }
}
