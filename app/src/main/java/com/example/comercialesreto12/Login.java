package com.example.comercialesreto12;

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

public class Login extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, showUsersButton;
    private TextView connectionStatus, usersList;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar elementos de la interfaz
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        showUsersButton = findViewById(R.id.showUsersButton);
        connectionStatus = findViewById(R.id.connectionStatus);
        usersList = findViewById(R.id.usersList);

        // Inicializar base de datos
        dbHandler = new DBHandler(this);

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

}
