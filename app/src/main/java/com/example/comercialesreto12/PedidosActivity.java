package com.example.comercialesreto12;

import android.os.Bundle;
import android.util.Log;
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

public class PedidosActivity extends MainActivity {
    private LinearLayout containerPedidos; // Contenedor para los pedidos
    private Spinner spinnerCliente; // Spinner para seleccionar cliente
    private Spinner spinnerArticulos; // Spinner para seleccionar productos
    private TextView empresaNombre, contactoNombre, direccionCliente, telefonoCliente, emailCliente;
    private List listaProductos; // Lista de productos disponibles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        // Inicializa los componentes
        initializeViews();
        setupDrawerMenu();

        // Cargar productos en la lista para los pedidos
        DBHandler dbHandler = new DBHandler(this);
        listaProductos = dbHandler.obtenerProductos(); // Obtener productos desde la BD

        // Verifica si la lista de productos está vacía
        if (listaProductos == null || listaProductos.isEmpty()) {
            Toast.makeText(this, "No hay productos disponibles", Toast.LENGTH_SHORT).show();
        } else {
            // Si hay productos, configurar el Spinner de productos
            ArrayAdapter adapterProductos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaProductos);
            adapterProductos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerArticulos.setAdapter(adapterProductos);
        }

        // Configura el evento de clic para agregar pedidos
        ImageButton btnAddPedido = findViewById(R.id.ImagebtnAddPedido);
        btnAddPedido.setOnClickListener(v -> addNewPedido());

        // Configura el evento de clic para enviar el pedido
        Button btnEnviarPedido = findViewById(R.id.botonPedido);
        btnEnviarPedido.setOnClickListener(v -> enviarPedido());

        // Cargar los clientes en el Spinner desde la base de datos
        loadClientes();
    }

    /**
     * Método para inicializar todas las vistas del diseño.
     */
    private void initializeViews() {
        containerPedidos = findViewById(R.id.containerPedidos);
        spinnerCliente = findViewById(R.id.spinnerCliente);
        spinnerArticulos = findViewById(R.id.spinnerArticulos);

        // Verificar si los TextView existen y están inicializados
        empresaNombre = findViewById(R.id.empresaNombre);
        if (empresaNombre == null) {
            Log.e("PedidosActivity", "empresaNombre es null");
        }
        contactoNombre = findViewById(R.id.contactoNombre);
        if (contactoNombre == null) {
            Log.e("PedidosActivity", "contactoNombre es null");
        }
        telefonoCliente = findViewById(R.id.telefonoCliente);
        if (telefonoCliente == null) {
            Log.e("PedidosActivity", "telefonoCliente es null");
        }
    }

    /**
     * Método para cargar los clientes en el Spinner desde la base de datos.
     */
    private void loadClientes() {
        DBHandler dbHandler = new DBHandler(this);
        List nombresClientes = dbHandler.obtenerPartners(); // Obtiene los nombres de clientes

        if (nombresClientes == null || nombresClientes.isEmpty()) {
            Toast.makeText(this, "No hay clientes disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresClientes);
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
     * Método para cargar los detalles del cliente cuando se selecciona del Spinner.
     */
    private void cargarDetallesCliente(String nombreCompleto) {
        DBHandler dbHandler = new DBHandler(this);

        if (nombreCompleto == null || nombreCompleto.isEmpty()) {
            Toast.makeText(this, "Selecciona un cliente válido", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] nombreArray = nombreCompleto.split(" ");
        String nombre = nombreArray[0];
        String apellido = (nombreArray.length > 1) ? nombreArray[1] : "";

        Cliente cliente = dbHandler.obtenerClientePorNombreApellido(nombre, apellido);

        if (cliente == null) {
            Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualizar los TextView con los datos del cliente
        if (empresaNombre != null) {
            empresaNombre.setText(cliente.getNombre());
        }
        if (contactoNombre != null) {
            contactoNombre.setText(cliente.getApellido());
        }
        if (telefonoCliente != null) {
            String telefono = cliente.getTelefono();
            if (telefono == null || telefono.isEmpty()) {
                telefonoCliente.setText("Sin teléfono");
            } else {
                telefonoCliente.setText(telefono);
            }
        }
        if (emailCliente != null) {
            emailCliente.setText(cliente.getEmail());
        }
    }

    /**
     * Método para agregar dinámicamente un nuevo pedido con un Spinner (producto) y un EditText (cantidad).
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
        ArrayAdapter adapterProductos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaProductos);
        adapterProductos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducto.setAdapter(adapterProductos);

        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                0,  // Peso relativo
                LinearLayout.LayoutParams.WRAP_CONTENT,
                2   // Peso de 2 para el Spinner (más espacio)
        );
        spinnerProducto.setLayoutParams(spinnerParams);

        // EditText para ingresar la cantidad (solo números)
        EditText editTextCantidad = new EditText(this);
        editTextCantidad.setHint("Cantidad");
        editTextCantidad.setInputType(android.text.InputType.TYPE_CLASS_NUMBER); // Solo números

        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1   // Peso de 1 para el EditText (menos espacio)
        );
        editTextCantidad.setLayoutParams(editTextParams);

        // Agregar los elementos a la fila
        pedidoRow.addView(spinnerProducto);
        pedidoRow.addView(editTextCantidad);

        // Agregar la fila al contenedor de pedidos
        containerPedidos.addView(pedidoRow);
    }

    /**
     * Método para enviar el pedido (validación básica y guardado en la base de datos).
     */
    private void enviarPedido() {
        boolean valid = true;
        DBHandler dbHandler = new DBHandler(this); // Instancia de la base de datos

        // Obtener el cliente seleccionado
        String nombreCompletoCliente = spinnerCliente.getSelectedItem().toString();
        String[] nombreArray = nombreCompletoCliente.split(" ");
        String nombre = nombreArray[0];
        String apellido = (nombreArray.length > 1) ? nombreArray[1] : "";

        // Buscar el ID del cliente seleccionado
        Cliente cliente = dbHandler.obtenerClientePorNombreApellido(nombre, apellido);
        if (cliente == null || cliente.getId() == 0) {
            Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        int clienteId = cliente.getId();

        // Validar y guardar cada pedido
        for (int i = 0; i < containerPedidos.getChildCount(); i++) {
            LinearLayout pedidoRow = (LinearLayout) containerPedidos.getChildAt(i);

            // Obtener el Spinner (producto) y EditText (cantidad)
            Spinner spinnerProducto = (Spinner) pedidoRow.getChildAt(0);
            EditText editTextCantidad = (EditText) pedidoRow.getChildAt(1);

            String productoSeleccionado = (String) spinnerProducto.getSelectedItem();
            String cantidadStr = editTextCantidad.getText().toString();

            // Validar que no haya campos vacíos
            if (productoSeleccionado == null || cantidadStr.isEmpty()) {
                valid = false;
                break;
            }

            try {
                int cantidad = Integer.parseInt(cantidadStr);

                // Insertar el pedido en la base de datos
                boolean resultado = dbHandler.insertarPedido(clienteId, productoSeleccionado, cantidad);
                if (!resultado) {
                    Toast.makeText(this, "Error al guardar un pedido", Toast.LENGTH_SHORT).show();
                    valid = false;
                    break;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "La cantidad debe ser un número válido", Toast.LENGTH_SHORT).show();
                valid = false;
                break;
            }
        }

        if (valid) {
            Toast.makeText(this, "Pedido enviado correctamente", Toast.LENGTH_SHORT).show();
            limpiarFormulario(); // Limpiar el formulario después de enviar el pedido
        } else {
            Toast.makeText(this, "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para limpiar el formulario después de enviar un pedido.
     */
    private void limpiarFormulario() {
        // Limpiar el contenedor de pedidos
        containerPedidos.removeAllViews();
    }
}