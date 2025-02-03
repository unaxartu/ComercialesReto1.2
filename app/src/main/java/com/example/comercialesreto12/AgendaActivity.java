package com.example.comercialesreto12;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgendaActivity extends MainActivity {

    private TextView txtSelectedDate;
    private Button btnSelectDate, btnAddEvent;
    private ListView eventListView;
    private DBHandler dbHandler;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnAddEvent = findViewById(R.id.add_event_button);
        eventListView = findViewById(R.id.event_list_view);

        dbHandler = new DBHandler(this);

        // Obtener el userId de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            txtSelectedDate.setText("Error: No se encontró el ID de usuario.");
            return;
        }

        btnSelectDate.setOnClickListener(v -> showDatePickerDialog());
        btnAddEvent.setOnClickListener(v -> showAddEventDialog());

        setupDrawerMenu();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" +
                            String.format("%02d", selectedMonth + 1) + "-" +
                            String.format("%02d", selectedDay);

                    txtSelectedDate.setText("Fecha seleccionada: " + selectedDate);
                    loadEvents(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showAddEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_event_layout, null);
        builder.setView(dialogView);

        EditText edtEventTitle = dialogView.findViewById(R.id.edtEventTitle);
        Button btnSelectEventDate = dialogView.findViewById(R.id.btnSelectEventDate);
        TextView txtEventDate = dialogView.findViewById(R.id.txtEventDate);
        Button btnSaveEvent = dialogView.findViewById(R.id.btnSaveEvent);
        EditText edtEventHour = dialogView.findViewById(R.id.edtEventHour); // Campo para la hora

        final String[] selectedDate = {""};

        btnSelectEventDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedDate[0] = selectedYear + "-" +
                                String.format("%02d", selectedMonth + 1) + "-" +
                                String.format("%02d", selectedDay);
                        txtEventDate.setText("Fecha: " + selectedDate[0]);
                    }, year, month, day);

            datePickerDialog.show();
        });

        AlertDialog dialog = builder.create();

        btnSaveEvent.setOnClickListener(v -> {
            String eventTitle = edtEventTitle.getText().toString().trim();
            String eventHour = edtEventHour.getText().toString().trim(); // Obtener la hora

            if (eventTitle.isEmpty() || selectedDate[0].isEmpty() || eventHour.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar evento en la base de datos con la hora incluida
            boolean insertSuccess = dbHandler.insertCita(userId, selectedDate[0], eventHour, eventTitle);

            if (insertSuccess) {
                Toast.makeText(this, "Cita guardada en la base de datos", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadEvents(selectedDate[0]);
            } else {
                Toast.makeText(this, "Error al guardar la cita", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void loadEvents(String fecha) {
        List<String> citas = dbHandler.getCitasPorUsuarioYFecha(userId, fecha);

        if (citas.isEmpty()) {
            citas = new ArrayList<>();
            citas.add("No hay citas para esta fecha.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, citas);
        eventListView.setAdapter(adapter);
    }
}
