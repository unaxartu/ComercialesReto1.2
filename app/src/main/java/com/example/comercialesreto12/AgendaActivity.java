package com.example.comercialesreto12;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AgendaActivity extends MainActivity {  // Hereda de MainActivity

    private CalendarView calendarView;
    private ListView eventListView;
    private Button addEventButton;

    // Estructura para guardar los eventos por fecha
    private HashMap<String, List<String>> eventsByDate;
    private List<String> selectedDateEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        // Llamar a la configuración del menú para asegurarse de que el header esté configurado
        setupDrawerMenu();

        // Inicialización de vistas y eventos
        initializeViews();
        initializeEvents();

    }

    private void initializeViews() {
        calendarView = findViewById(R.id.calendar_view);
        eventListView = findViewById(R.id.event_list_view);
        addEventButton = findViewById(R.id.add_event_button);
        selectedDateEvents = new ArrayList<>();

        // Listener para manejar la selección de fechas
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = getFormattedDate(year, month, dayOfMonth);
            loadEventsForDate(selectedDate);
        });

        // Listener para agregar un nuevo evento
        addEventButton.setOnClickListener(v -> showAddEventDialog());
    }

    private void initializeEvents() {
        eventsByDate = new HashMap<>();

        // Cargar eventos de ejemplo
        String today = getTodayDate();
        eventsByDate.put(today, new ArrayList<>());
        eventsByDate.get(today).add("Reunión con cliente");
        eventsByDate.get(today).add("Llamada con proveedor");

        // Cargar eventos para la fecha seleccionada inicialmente
        loadEventsForDate(today);
    }

    private void loadEventsForDate(String date) {
        selectedDateEvents.clear();
        if (eventsByDate.containsKey(date)) {
            selectedDateEvents.addAll(eventsByDate.get(date));
        } else {
            Toast.makeText(this, "No hay eventos para esta fecha", Toast.LENGTH_SHORT).show();
        }

        // Actualizar el adaptador de la lista de eventos
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedDateEvents);
        eventListView.setAdapter(adapter);
    }

    private void showAddEventDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this);
        dialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            String selectedDate = getFormattedDate(year, month, dayOfMonth);

            // Agregar un evento a la fecha seleccionada
            if (!eventsByDate.containsKey(selectedDate)) {
                eventsByDate.put(selectedDate, new ArrayList<>());
            }
            eventsByDate.get(selectedDate).add("Nuevo Evento");

            // Recargar los eventos para la fecha seleccionada
            loadEventsForDate(selectedDate);
            Toast.makeText(this, "Evento agregado", Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return getFormattedDate(year, month, day);
    }

    private String getFormattedDate(int year, int month, int dayOfMonth) {
        // Formato de fecha "YYYY-MM-DD"
        return year + "-" + (month + 1) + "-" + dayOfMonth;
    }

}
