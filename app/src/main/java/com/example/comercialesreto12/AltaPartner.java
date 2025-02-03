package com.example.comercialesreto12;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AltaPartner extends PartnersActivity {

    private EditText fieldName, fieldApellidos, fieldTelefono, fieldEmail;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_partners);

        dbHandler = new DBHandler(this); // Inicializar base de datos correctamente

        inicializarVistas();

        ImageButton buttonBack = findViewById(R.id.flechaAtras);
        Button buttonDarseDeAlta = findViewById(R.id.buttonDarseDeAlta);

        buttonBack.setOnClickListener(v -> finish());

        buttonDarseDeAlta.setOnClickListener(v -> procesarRegistro());
    }

    private void inicializarVistas() {
        fieldName = findViewById(R.id.fieldName);
        fieldApellidos = findViewById(R.id.fieldApellidos); // Corregí el ID
        fieldTelefono = findViewById(R.id.fieldTelefono);
        fieldEmail = findViewById(R.id.fieldEmail);
    }

    private void procesarRegistro() {
        if (validarCampos()) {
            String nombre = fieldName.getText().toString().trim();
            String apellidos = fieldApellidos.getText().toString().trim();
            String telefono = fieldTelefono.getText().toString().trim();
            String email = fieldEmail.getText().toString().trim();

            // Verificar si el cliente ya existe
            boolean existe = dbHandler.existeCliente(email);
            if (existe) {
                Toast.makeText(this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
                return;  // Detiene el flujo si el cliente ya existe
            }

            // Si no existe, insertar el nuevo cliente
            boolean insertado = dbHandler.insertarCliente(nombre, apellidos, telefono, email);

            if (insertado) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AltaPartner.this, PartnersActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error al registrar el cliente", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private boolean validarCampos() {
        if (TextUtils.isEmpty(fieldName.getText().toString().trim())) {
            mostrarMensajeError("El campo Nombre es obligatorio");
            return false;
        }
        if (TextUtils.isEmpty(fieldApellidos.getText().toString().trim())) {
            mostrarMensajeError("El campo Apellidos es obligatorio");
            return false;
        }
        if (TextUtils.isEmpty(fieldTelefono.getText().toString().trim())) {
            mostrarMensajeError("El campo Teléfono es obligatorio");
            return false;
        }
        if (TextUtils.isEmpty(fieldEmail.getText().toString().trim())) {
            mostrarMensajeError("El campo Email es obligatorio");
            return false;
        }
        return true;
    }

    private void mostrarMensajeError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
