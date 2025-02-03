package com.example.comercialesreto12;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
        setupInfoButton();
        setupCompanyIconButton();
    }

    public void setupCompanyIconButton() {
        // Encuentra el ImageButton del icono de la empresa
        ImageButton companyIconButton = findViewById(R.id.companyIconButton);

        // Establece un OnClickListener para regresar a MainActivity cuando se presione el icono
        companyIconButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent); // Inicia MainActivity
            finish(); // Finaliza AgendaActivity (para evitar que se quede en la pila)
        });
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

        ImageButton closeMenuButton = findViewById(R.id.closeMenuButton);
        closeMenuButton.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));
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

    public void setupInfoButton() {
        // Encuentra el ImageButton de información
        ImageButton infoButton = findViewById(R.id.infoButton);

        // Establece un OnClickListener para mostrar el pop-up cuando se presione el botón
        infoButton.setOnClickListener(v -> showInfoDialog());
    }

    public void showInfoDialog() {
        // Los elementos que aparecerán en el pop-up
        CharSequence[] items = new CharSequence[]{
                "Lorem ipsum dolor sit amet",
                "Consectetur adipiscing elit",
                "Integer nec odio",
                "Praesent libero",
                "Sed cursus ante dapibus diam"
        };

        // Crea y muestra el diálogo de información
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Información")
                .setItems(items, null) // Mostrar la lista de elementos
                .setPositiveButton("Cerrar", null) // Botón para cerrar el pop-up
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
