package com.example.comercialesreto12;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView connectionStatus;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar elementos de la interfaz
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        connectionStatus = findViewById(R.id.connectionStatus);

        // Inicializar base de datos
        dbHandler = new DBHandler(this);

        // Llamar a la API para obtener experiencias
        fetchExperienciasFromApi();

        // Llamar a la API para obtener empleados
        fetchEmpleadosFromApi();

        // Llamar a la API para obtener clientes
        fetchClientesFromApi();

        // Botón de Login
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                if (checkLogin(username, password)) {
                    Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    connectionStatus.setText("Conectado como " + username);

                    // Redirigir a MainActivity
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("username", username); // Enviar username a MainActivity
                    startActivity(intent);
                    finish(); // Cierra la actividad de login para que no se pueda volver atrás
                } else {
                    Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    connectionStatus.setText("Error en el login");
                }
            }
        });
    }

    /**
     * Verifica las credenciales del usuario en la base de datos local.
     */
    private boolean checkLogin(String username, String password) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String query = "SELECT id FROM usuarios_login WHERE username=? AND password=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor.moveToFirst()) { // Si hay un resultado
            int userId = cursor.getInt(0); // Obtener el ID del usuario

            // Guardar userId en SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", userId);
            editor.apply(); // Guarda los cambios

            cursor.close();
            db.close();
            return true; // Inicio de sesión exitoso
        }

        cursor.close();
        db.close();
        return false; // Usuario o contraseña incorrectos
    }

    /**
     * Realiza una solicitud GET a la API para obtener experiencias y las guarda en la base de datos.
     */
    private void fetchExperienciasFromApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.132:8000/") // URL base de la API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getExperiencias().enqueue(new Callback<List<Experiencia>>() {
            @Override
            public void onResponse(Call<List<Experiencia>> call, Response<List<Experiencia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Experiencia> experiencias = response.body();
                    saveExperienciasToDatabase(experiencias);
                    Toast.makeText(Login.this, "Datos de experiencias guardados", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_ERROR", "Error en la respuesta de la API: " + response.code());
                    Toast.makeText(Login.this, "Error al cargar datos de la API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Experiencia>> call, Throwable t) {
                Log.e("API_ERROR", "Fallo al conectar con la API: " + t.getMessage());
                Toast.makeText(Login.this, "No se pudo conectar con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Guarda las experiencias obtenidas de la API en la tabla 'productos' de la base de datos.
     */
    private void saveExperienciasToDatabase(List<Experiencia> experiencias) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.execSQL("DELETE FROM productos"); // Limpiar la tabla antes de insertar nuevos datos

        for (Experiencia experiencia : experiencias) {
            ContentValues values = new ContentValues();
            values.put("id", experiencia.getId());
            values.put("nombre", experiencia.getNombre());
            values.put("descripcion", experiencia.getDescripcion());
            values.put("precio", experiencia.getPrecio());

            // Verificar si el campo 'tipo' no es nulo antes de insertarlo
            String tipo = experiencia.getTipo() != null ? experiencia.getTipo() : "";
            values.put("tipo", tipo);

            db.insert("productos", null, values);
        }

        db.close();
        Log.d("DATABASE", "Datos de experiencias guardados en la base de datos");
    }

    /**
     * Realiza una solicitud GET a la API para obtener empleados y los guarda en la tabla 'usuarios_login'.
     */
    private void fetchEmpleadosFromApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.132:8000/") // URL base de la API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getEmpleados().enqueue(new Callback<List<Empleado>>() {
            @Override
            public void onResponse(Call<List<Empleado>> call, Response<List<Empleado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Empleado> empleados = response.body();
                    saveEmpleadosToDatabase(empleados);
                    Toast.makeText(Login.this, "Datos de empleados guardados", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_ERROR", "Error en la respuesta de la API: " + response.code());
                    Toast.makeText(Login.this, "Error al cargar datos de empleados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Empleado>> call, Throwable t) {
                Log.e("API_ERROR", "Fallo al conectar con la API: " + t.getMessage());
                Toast.makeText(Login.this, "No se pudo conectar con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Guarda los empleados obtenidos de la API en la tabla 'usuarios_login'.
     */
    private void saveEmpleadosToDatabase(List<Empleado> empleados) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        // Limpiar la tabla antes de insertar nuevos datos
        db.execSQL("DELETE FROM usuarios_login");

        for (Empleado empleado : empleados) {
            ContentValues values = new ContentValues();

            // Combinar nombre y apellido para crear username y password
            String username = empleado.getNombre()  + empleado.getApellido();
            String password = username; // Usar el mismo valor para el password

            values.put("username", username.trim()); // Eliminar espacios adicionales
            values.put("password", password.trim());

            db.insert("usuarios_login", null, values);
        }

        db.close();
        Log.d("DATABASE", "Datos de empleados guardados en la base de datos");
    }

    /**
     * Realiza una solicitud GET a la API para obtener clientes y los guarda en la tabla 'clientes'.
     */
    private void fetchClientesFromApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.132:8000/") // URL base de la API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getClientes().enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cliente> clientes = response.body();
                    Log.d("API_CLIENTES", "Número de clientes recibidos: " + clientes.size()); // Agregar este log
                    for (Cliente cliente : clientes) {
                        Log.d("API_CLIENTES", "Cliente recibido: " + cliente.getNombre() + " " + cliente.getApellido());
                    }
                    saveClientesToDatabase(clientes);
                    Toast.makeText(Login.this, "Datos de clientes guardados", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_ERROR", "Error en la respuesta de la API: " + response.code());
                    Toast.makeText(Login.this, "Error al cargar datos de clientes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Log.e("API_ERROR", "Fallo al conectar con la API: " + t.getMessage());
                Toast.makeText(Login.this, "No se pudo conectar con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Guarda los clientes obtenidos de la API en la tabla 'clientes'.
     */
    private void saveClientesToDatabase(List<Cliente> clientes) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        // Limpiar la tabla antes de insertar nuevos datos
        db.execSQL("DELETE FROM clientes");

        Log.d("DATABASE", "Número de clientes a insertar: " + clientes.size()); // Agregar este log

        for (Cliente cliente : clientes) {
            ContentValues values = new ContentValues();
            values.put("nombre", cliente.getNombre());
            values.put("apellido", cliente.getApellido());
            values.put("email", cliente.getEmail());
            values.put("telefono", cliente.getTelefono());

            long resultado = db.insert("clientes", null, values); // Insertar el cliente
            Log.d("DATABASE", "Resultado de inserción para cliente " + cliente.getNombre() + ": " + resultado);

            if (resultado == -1) {
                Log.e("DATABASE", "Error al insertar cliente: " + cliente.getNombre());
            }
        }

        db.close();
        Log.d("DATABASE", "Datos de clientes guardados en la base de datos");
    }
}