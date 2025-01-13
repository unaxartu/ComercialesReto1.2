package com.example.comercialesreto12;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class Pedidos extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {  // Cambiar 'OnCreate' a 'onCreate'
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);  // Asegúrate de que 'agenda.xml' exista en tu carpeta 'res/layout'
        ImageButton buttonBack = findViewById(R.id.flechaAtrasPedidos);
        Button buttonPedido = findViewById(R.id.botonPedido);
        EditText fieldNameEmpresa = findViewById(R.id.fieldNameEmpresa);
        EditText fieldoNombreContacto = findViewById(R.id.fieldNameContacto);
        EditText filedDireccion = findViewById(R.id.fieldDireccion);
        EditText fieldTelefono = findViewById(R.id.fieldTelefono);
        EditText fieldEmail = findViewById(R.id.fieldEmail);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual
            }
        });

        buttonPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(fieldNameEmpresa.getText())) {
                    Toast.makeText(Pedidos.this, "El campo Nombre de la Empresa es obligatorio", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(fieldoNombreContacto.getText())) {
                    Toast.makeText(Pedidos.this, "El campo Nombre del Contacto es obligatorio", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(filedDireccion.getText())) {
                    Toast.makeText(Pedidos.this, "El campo Direccion es obligatorio", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(fieldTelefono.getText())){
                    Toast.makeText(Pedidos.this, "El campo Telefono es obligatorio", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(fieldEmail.getText())){
                    Toast.makeText(Pedidos.this, "El campo Email es obligatorio", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // Aquí se implementaría la lógica para darse de alta.
                        // Si se da de alta con éxito:
                        Toast.makeText(Pedidos.this, "Se ha enviado el pedido con éxito", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        // Si ocurre un error:
                        Toast.makeText(Pedidos.this, "No se ha podido enviar el pedido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
