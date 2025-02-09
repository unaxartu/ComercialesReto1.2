package com.example.comercialesreto12;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AltaPartner extends PartnersActivity {
    private EditText fieldName, fieldApellidos, fieldDireccion, fieldTelefono, fieldEmail, fieldCif, fieldNombreEmpresa;
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
        fieldApellidos = findViewById(R.id.fieldApellidos);
        fieldDireccion = findViewById(R.id.fieldDireccion); // Nuevo campo
        fieldTelefono = findViewById(R.id.fieldTelefono);
        fieldEmail = findViewById(R.id.fieldEmail);
        fieldCif = findViewById(R.id.fieldCif); // Nuevo campo
        fieldNombreEmpresa = findViewById(R.id.fieldNombreEmpresa); // Nuevo campo
    }

    private void procesarRegistro() {
        if (validarCampos()) {
            String nombre = fieldName.getText().toString().trim();
            String apellidos = fieldApellidos.getText().toString().trim();
            String direccion = fieldDireccion.getText().toString().trim(); // Nuevo campo
            String telefono = fieldTelefono.getText().toString().trim();
            String email = fieldEmail.getText().toString().trim();
            String cif = fieldCif.getText().toString().trim(); // Nuevo campo
            String nombreEmpresa = fieldNombreEmpresa.getText().toString().trim(); // Nuevo campo
            boolean cliente = true; // Por defecto, asumimos que es un cliente

            // Verificar si el cliente ya existe
            boolean existe = dbHandler.existeCliente(email);
            if (existe) {
                Toast.makeText(this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
                return;  // Detiene el flujo si el cliente ya existe
            }

            // Crear un objeto Cliente para enviar a la API
            Cliente clienteObj = new Cliente();
            clienteObj.setNombre(nombre);
            clienteObj.setApellido(apellidos);
            clienteObj.setDireccion(direccion); // Nuevo campo
            clienteObj.setTelefono(telefono);
            clienteObj.setEmail(email);
            clienteObj.setCif(cif); // Nuevo campo
            clienteObj.setNombreEmpresa(nombreEmpresa); // Nuevo campo
            clienteObj.setCliente(cliente); // Nuevo campo

            // Realizar la solicitud POST a la API
            postCliente(clienteObj);
        }
    }

    /**
     * Realiza una solicitud POST a la API para registrar un nuevo cliente.
     */
    private void postCliente(Cliente cliente) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.132:8000/") // URL base de la API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<Cliente> call = apiService.postCliente(cliente);
        call.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Cliente registrado exitosamente en la API
                    Cliente clienteRegistrado = response.body();
                    Toast.makeText(AltaPartner.this, "Cliente registrado en la API", Toast.LENGTH_SHORT).show();

                    // Guardar el cliente en la base de datos local
                    boolean insertado = dbHandler.insertarCliente(
                            clienteRegistrado.getNombre(),
                            clienteRegistrado.getApellido(),
                            clienteRegistrado.getDireccion(), // Nuevo campo
                            clienteRegistrado.getTelefono(),
                            clienteRegistrado.getEmail(),
                            clienteRegistrado.getCif(), // Nuevo campo
                            clienteRegistrado.getNombreEmpresa(), // Nuevo campo
                            clienteRegistrado.isCliente() // Nuevo campo
                    );

                    if (insertado) {
                        Toast.makeText(AltaPartner.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AltaPartner.this, PartnersActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AltaPartner.this, "Error al guardar el cliente localmente", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AltaPartner.this, "Error al registrar el cliente en la API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                Toast.makeText(AltaPartner.this, "Fallo al conectar con la API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        if (TextUtils.isEmpty(fieldDireccion.getText().toString().trim())) { // Validar nuevo campo
            mostrarMensajeError("El campo Dirección es obligatorio");
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
        if (TextUtils.isEmpty(fieldCif.getText().toString().trim())) { // Validar nuevo campo
            mostrarMensajeError("El campo CIF es obligatorio");
            return false;
        }
        if (TextUtils.isEmpty(fieldNombreEmpresa.getText().toString().trim())) { // Validar nuevo campo
            mostrarMensajeError("El campo Nombre de Empresa es obligatorio");
            return false;
        }
        return true;
    }

    private void mostrarMensajeError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}