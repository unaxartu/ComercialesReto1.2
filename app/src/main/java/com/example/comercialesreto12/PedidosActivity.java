package com.example.comercialesreto12;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PedidosActivity extends AppCompatActivity {

    private LinearLayout containerPedidos; // Contenedor para los pedidos
    private Spinner spinnerCliente; // Spinner para seleccionar cliente
    private Spinner spinnerArticulos; // Spinner para seleccionar productos
    private TextView empresaNombre, contactoNombre, direccionCliente, telefonoCliente, emailCliente;
    private List<String> listaProductos; // Lista de productos disponibles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        // Inicializa los componentes
        containerPedidos = findViewById(R.id.containerPedidos);
        spinnerCliente = findViewById(R.id.spinnerCliente);
        spinnerArticulos = findViewById(R.id.spinnerArticulos);
        empresaNombre = findViewById(R.id.empresaNombre);
        contactoNombre = findViewById(R.id.contactoNombre);
        direccionCliente = findViewById(R.id.direccionCliente);
        telefonoCliente = findViewById(R.id.telefonoCliente);
        emailCliente = findViewById(R.id.emailCliente);
        ImageButton btnAddPedido = findViewById(R.id.ImagebtnAddPedido);
        Button btnEnviarPedido = findViewById(R.id.botonPedido);

        // Cargar productos en la lista para los pedidos
        DBHandler dbHandler = new DBHandler(this);
        listaProductos = dbHandler.obtenerProductos(); // Obtener productos desde la BD

        // Verifica si la lista de productos está vacía
        if (listaProductos.isEmpty()) {
            Toast.makeText(this, "No hay productos disponibles", Toast.LENGTH_SHORT).show();
        } else {
            // Si hay productos, configurar el Spinner de productos
            ArrayAdapter<String> adapterProductos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaProductos);
            adapterProductos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerArticulos.setAdapter(adapterProductos);
        }

        // Configura el evento de clic para agregar pedidos
        btnAddPedido.setOnClickListener(v -> addNewPedido());

        // Configura el evento de clic para enviar el pedido
        btnEnviarPedido.setOnClickListener(v -> enviarPedido());

        // Cargar los clientes en el Spinner desde la base de datos
        loadClientes();
    }

    /**
     * Método para cargar los clientes en el Spinner desde la base de datos
     */
    private void loadClientes() {
        DBHandler dbHandler = new DBHandler(this);
        List<String> nombresClientes = dbHandler.obtenerPartners(); // Obtiene los nombres de clientes

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresClientes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCliente.setAdapter(adapter);

        // Evento de selección para cargar los datos del cliente
        spinnerCliente.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View view, int position, long id) {
                String nombreCompleto = (String) parentView.getItemAtPosition(position);
                cargarDetallesCliente(nombreCompleto);

                // Mostrar el Spinner de productos cuando se selecciona un cliente
                spinnerArticulos.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // No hacer nada si no hay selección
            }
        });
    }

    /**
     * Método para cargar los detalles del cliente cuando se selecciona del Spinner
     */
    private void cargarDetallesCliente(String nombreCompleto) {
        DBHandler dbHandler = new DBHandler(this);

        // Validar que haya al menos un nombre
        String[] nombreArray = nombreCompleto.split(" ");
        String nombre = nombreArray[0];
        String apellido = (nombreArray.length > 1) ? nombreArray[1] : "";

        Cliente cliente = dbHandler.obtenerClientePorNombreApellido(nombre, apellido);

        if (cliente != null) {
            empresaNombre.setText(cliente.getNombre());
            contactoNombre.setText(cliente.getApellido());
            telefonoCliente.setText(cliente.getTelefono());
            emailCliente.setText(cliente.getEmail());
        }
    }

    /**
     * Método para agregar dinámicamente un nuevo pedido con un Spinner (producto) y un EditText (cantidad)
     */
    private void addNewPedido() {
        LinearLayout pedidoRow = new LinearLayout(this);
        pedidoRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.setMargins(0, 16, 0, 16); // Margen entre filas
        pedidoRow.setLayoutParams(rowParams);

        // Spinner para seleccionar el producto
        Spinner spinnerProducto = new Spinner(this);
        ArrayAdapter<String> adapterProductos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaProductos);
        adapterProductos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducto.setAdapter(adapterProductos);
        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                0,  // Peso relativo
                LinearLayout.LayoutParams.WRAP_CONTENT,
                2  // Peso de 2 para el Spinner (más espacio)
        );
        spinnerProducto.setLayoutParams(spinnerParams);

        // EditText para ingresar la cantidad (solo números)
        EditText editTextCantidad = new EditText(this);
        editTextCantidad.setHint("Cantidad");
        editTextCantidad.setInputType(android.text.InputType.TYPE_CLASS_NUMBER); // Solo números
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1  // Peso de 1 para el EditText (menos espacio)
        );
        editTextCantidad.setLayoutParams(editTextParams);

        // Agregar los elementos a la fila
        pedidoRow.addView(spinnerProducto);
        pedidoRow.addView(editTextCantidad);

        // Agregar la fila al contenedor de pedidos
        containerPedidos.addView(pedidoRow);
    }

    /**
     * Método para enviar el pedido (validación básica)
     */
    private void enviarPedido() {
        boolean valid = true;

        // Validar que cada pedido tenga un producto y una cantidad
        for (int i = 0; i < containerPedidos.getChildCount(); i++) {
            LinearLayout pedidoRow = (LinearLayout) containerPedidos.getChildAt(i);
            Spinner spinnerProducto = (Spinner) pedidoRow.getChildAt(0);
            EditText editTextCantidad = (EditText) pedidoRow.getChildAt(1);

            String productoSeleccionado = (String) spinnerProducto.getSelectedItem();
            String cantidad = editTextCantidad.getText().toString();

            // Validar que la cantidad no esté vacía y que se haya seleccionado un producto
            if (productoSeleccionado == null || cantidad.isEmpty()) {
                valid = false;
                break;
            }
        }

        if (valid) {
            Toast.makeText(this, "Pedido enviado correctamente", Toast.LENGTH_SHORT).show();
            limpiarFormulario(); // Limpia el formulario después de enviar el pedido
        } else {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para limpiar el formulario después de enviar un pedido
     */
    private void limpiarFormulario() {
        // Limpiar el contenedor de pedidos
        containerPedidos.removeAllViews();
    }
}
