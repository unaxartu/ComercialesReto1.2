package com.example.comercialesreto12;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AltaPartner extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alta_partner);  // Asegúrate de que 'alta_partner.xml' exista en tu carpeta 'res/layout'

        ImageButton buttonBack = findViewById(R.id.flechaAtras);
        Button buttonDarseDeAlta = findViewById(R.id.buttonDarseDeAlta);
        EditText fieldName = findViewById(R.id.fieldName);
        EditText fielApellidos = findViewById(R.id.fielApellidos);
        EditText filedDireccion = findViewById(R.id.fieldDireccion);
        EditText fieldTelefono = findViewById(R.id.fieldTelefono);
        EditText fieldEmail = findViewById(R.id.fieldEmail);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual
            }
        });

        buttonDarseDeAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(fieldName.getText())) {
                    Toast.makeText(AltaPartner.this, "El campo Nombre es obligatorio", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(fielApellidos.getText())) {
                    Toast.makeText(AltaPartner.this, "El campo Apellidos es obligatorio", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(filedDireccion.getText())) {
                    Toast.makeText(AltaPartner.this, "El campo Direccion es obligatorio", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(fieldTelefono.getText())){
                    Toast.makeText(AltaPartner.this, "El campo Telefono es obligatorio", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(fieldEmail.getText())){
                    Toast.makeText(AltaPartner.this, "El campo Email es obligatorio", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // Aquí se implementaría la lógica para darse de alta.
                        // Si se da de alta con éxito:
                        Toast.makeText(AltaPartner.this, "Se ha dado de alta con éxito", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        // Si ocurre un error:
                        Toast.makeText(AltaPartner.this, "No se ha podido dar de alta", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
