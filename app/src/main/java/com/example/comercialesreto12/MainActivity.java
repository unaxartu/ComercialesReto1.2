package com.example.comercialesreto12;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

public class MainActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawerMenu();
    }

    public void setupDrawerMenu() {
        drawerLayout = findViewById(R.id.drawer_layout);
        ImageButton menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        setButtonActions();
    }

    public void setButtonActions() {
        Button agendaButton = findViewById(R.id.agendaButton);
        Button ordersButton = findViewById(R.id.ordersButton);
        Button partnersButton = findViewById(R.id.partnersButton);
        Button sendDelegationsButton = findViewById(R.id.sendDelegationsButton);

        agendaButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgendaActivity.class);
            startActivity(intent);
        });

        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PedidosActivity.class);
            startActivity(intent);
        });

        partnersButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PartnersActivity.class);
            startActivity(intent);
        });

        sendDelegationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EnviarDelegacion.class);
            startActivity(intent);
        });

        // Cerrar el menú cuando se presiona el botón "Cerrar Menú"
        ImageButton closeMenuButton = findViewById(R.id.closeMenuButton);
        closeMenuButton.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));

        // Acción para abrir la aplicación de correo
        ImageButton emailIcon = findViewById(R.id.email_icon);  // Referencia al ImageButton de correo
        emailIcon.setOnClickListener(v -> openEmailApp());

        // Acción para abrir la ubicación en Google Maps
        ImageButton locationIcon = findViewById(R.id.location_icon);  // Referencia al ImageButton de ubicación
        locationIcon.setOnClickListener(v -> openGoogleMaps());

        // Acción para abrir la aplicación del teléfono y realizar una llamada
        ImageButton phoneButton = findViewById(R.id.phoneButton); // Referencia al ImageButton del teléfono
        phoneButton.setOnClickListener(v -> makePhoneCall());
    }

    public void openEmailApp() {
        String email = "ejemplo@correo.com";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Asunto predeterminado");
        intent.putExtra(Intent.EXTRA_TEXT, "Cuerpo del correo");

        try {
            startActivity(Intent.createChooser(intent, "Selecciona una aplicación de correo"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "No se encontró una aplicación de correo", Toast.LENGTH_SHORT).show();
        }
    }

    public void openGoogleMaps() {
        double lat = 43.30501518366967;
        double lon = -2.0169580764405652;
        Uri location = Uri.parse("geo:" + lat + "," + lon + "?q=" + lat + "," + lon + "(Green Getaway)");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(MainActivity.this, "No se pudo abrir Google Maps", Toast.LENGTH_SHORT).show();
        }
    }

    public void makePhoneCall() {
        String phoneNumber = "tel:666666666";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(phoneNumber));

        try {
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Abriendo aplicación de teléfono...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error al abrir la aplicación de teléfono", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void showInfoDialog() {
        CharSequence[] items = new CharSequence[]{
                "Lorem ipsum dolor sit amet",
                "Consectetur adipiscing elit",
                "Integer nec odio",
                "Praesent libero",
                "Sed cursus ante dapibus diam"
        };

        new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                .setTitle("Información")
                .setItems(items, null)
                .setPositiveButton("Cerrar", null)
                .show();
    }

    public void openWebsite() {
        String url = "http://remoto.cebanc.com:20203";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "No hay navegador disponible", Toast.LENGTH_SHORT).show();
        }
    }
}
