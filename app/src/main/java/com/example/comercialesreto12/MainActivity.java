package com.example.comercialesreto12;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.insets.Insets;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configuración del DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout); // Referencia al DrawerLayout
        ImageButton menuButton = findViewById(R.id.menuButton); // Referencia al botón de menú

        // Abrir y cerrar el menú lateral al hacer clic en el botón de menú
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // Navegación al menú principal al hacer clic en un botón
        Button agendaButton = findViewById(R.id.agendaButton); // Usando un botón del menú lateral
        agendaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        // Cerrar el menú cuando se presiona el botón "Cerrar Menú"
        ImageButton closeMenuButton = findViewById(R.id.closeMenuButton);
        closeMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);  // Cierra el menú lateral
            }
        });

        // Acción para abrir la aplicación de correo
        ImageButton emailIcon = findViewById(R.id.email_icon);  // Referencia al ImageButton de correo
        emailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un intent para abrir la aplicación de correo con un correo predeterminado
                String email = "ejemplo@correo.com"; // Reemplaza con tu correo
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822"); // Especifica que es un correo
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Asunto predeterminado");
                intent.putExtra(Intent.EXTRA_TEXT, "Cuerpo del correo");

                // Verificar si existe alguna aplicación de correo instalada
                try {
                    startActivity(Intent.createChooser(intent, "Selecciona una aplicación de correo"));
                } catch (android.content.ActivityNotFoundException ex) {
                    // Si no se encuentra ninguna aplicación de correo, muestra un mensaje de error
                    Toast.makeText(MainActivity.this, "No se encontró una aplicación de correo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Acción para abrir la ubicación en Google Maps
        ImageButton locationIcon = findViewById(R.id.location_icon);  // Referencia al ImageButton de ubicación
        locationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = 43.30501518366967;
                double lon = -2.0169580764405652;

                Uri location = Uri.parse("geo:" + lat + "," + lon + "?q=" + lat + "," + lon + "(Green Getaway)"); // Reemplaza "Mi Empresa" con el nombre de tu empresa
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                mapIntent.setPackage("com.google.android.apps.maps");  // Aseguramos que se use Google Maps

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent); // Abre la ubicación en Google Maps
                } else {
                    Toast.makeText(MainActivity.this, "No se pudo abrir Google Maps", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Acción para abrir la aplicación del teléfono y realizar una llamada
        ImageButton phoneButton = findViewById(R.id.phoneButton); // Referencia al ImageButton del teléfono
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Número de teléfono al que deseas llamar
                String phoneNumber = "tel:+123456789";  // Reemplaza con el número al que deseas llamar

                // Intent para abrir la aplicación de teléfono con el número predefinido
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));

                // Verifica si existe alguna actividad para manejar la acción y luego la ejecuta
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);  // Abre la aplicación del teléfono
                } else {
                    // Si no se puede encontrar una aplicación de teléfono, muestra un mensaje
                    Toast.makeText(MainActivity.this, "No se pudo abrir la aplicación de teléfono", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Ajustar la ventana para los márgenes del sistema
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    // Método para ir a la agenda
    public void agenda() {
        Intent i = new Intent(this, Agenda.class);
        startActivity(i);
    }
}
