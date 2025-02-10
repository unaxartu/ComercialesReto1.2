package com.example.comercialesreto12;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "comerciales";
    private static final int DB_VERSION = 14; // Incrementa la versión si antes era 6

    // Tablas
    private static final String TABLE_USUARIOS_LOGIN = "usuarios_login";
    private static final String TABLE_CITAS = "citas";
    private static final String TABLE_CLIENTES = "clientes";
    private static final String TABLE_PRODUCTOS = "productos";
    public static final String TABLE_PEDIDOS = "pedidos"; // Ahora contiene los detalles del pedido

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla usuarios_login
        String createUsuariosLoginTable = "CREATE TABLE " + TABLE_USUARIOS_LOGIN + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT)";
        db.execSQL(createUsuariosLoginTable);

        // Crear tabla citas
        String createCitasTable = "CREATE TABLE " + TABLE_CITAS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario_id INTEGER, " +
                "fecha TEXT, " +
                "hora TEXT, " +
                "descripcion TEXT, " +
                "FOREIGN KEY(usuario_id) REFERENCES " + TABLE_USUARIOS_LOGIN + "(id))";
        db.execSQL(createCitasTable);

        // Crear tabla clientes (actualizada según el modelo de la API)
        String createClientesTable = "CREATE TABLE " + TABLE_CLIENTES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "apellido TEXT, " +
                "direccion TEXT, " + // Nuevo campo
                "telefono TEXT, " +
                "email TEXT UNIQUE, " +
                "cif TEXT, " + // Nuevo campo
                "nombre_empresa TEXT, " + // Nuevo campo
                "cliente INTEGER DEFAULT 0" + // Nuevo campo (0 = false, 1 = true)
                ")";
        db.execSQL(createClientesTable);

        // Crear tabla productos
        String createProductosTable = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Campo ID (clave primaria)
                "nombre TEXT NOT NULL, " +                // Campo nombre (cadena de texto, no nulo)
                "descripcion TEXT, " +                    // Campo descripción (cadena de texto, puede ser nulo)
                "precio REAL NOT NULL, " +                // Campo precio (número decimal, no nulo)
                "tipo TEXT NOT NULL " +                   // Campo tipo (cadena de texto, no nulo)
                ");";                                      // Cierre de la declaración SQL
        db.execSQL(createProductosTable);

        // Crear tabla pedidos (incluye detalles del pedido)
        String createPedidosTable = "CREATE TABLE " + TABLE_PEDIDOS + " (" +
                "id_pedido INTEGER PRIMARY KEY AUTOINCREMENT, " + // Clave primaria
                "id_cliente INTEGER, " +                          // Referencia al cliente
                "id_experiencia INTEGER, " +                      // Referencia a la experiencia
                "fecha_pedido TEXT, " +                           // Fecha del pedido
                "cantidad INTEGER, " +                            // Cantidad
                "n_pedido INTEGER, " +                            // Número de pedido
                "FOREIGN KEY(id_cliente) REFERENCES " + TABLE_CLIENTES + "(id), " +
                "FOREIGN KEY(id_experiencia) REFERENCES " + TABLE_PRODUCTOS + "(id)" +
                ")";
        db.execSQL(createPedidosTable);

        // Insertar datos de prueba
        insertTestData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDOS);
        onCreate(db); // Vuelve a crear las tablas con la nueva estructura
    }

    private void insertTestData(SQLiteDatabase db) {
        try {
            db.execSQL("INSERT INTO " + TABLE_USUARIOS_LOGIN + " (username, password) VALUES ('admin', 'admin')");
            db.execSQL("INSERT INTO " + TABLE_CITAS + " (usuario_id, fecha, hora, descripcion) VALUES (1, '2024-06-01', '10:00', 'Reunión con cliente')");
            db.execSQL("INSERT INTO " + TABLE_CITAS + " (usuario_id, fecha, hora, descripcion) VALUES (1, '2024-06-02', '14:30', 'Presentación de producto')");
            db.execSQL("INSERT INTO " + TABLE_CITAS + " (usuario_id, fecha, hora, descripcion) VALUES (2, '2024-06-03', '09:00', 'Llamada de seguimiento')");

            db.execSQL("INSERT INTO " + TABLE_CLIENTES + " (nombre, apellido, email, telefono) VALUES ('Juan', 'Pérez', 'juan@example.com', '123456789')");
            db.execSQL("INSERT INTO " + TABLE_CLIENTES + " (nombre, apellido, email, telefono) VALUES ('María', 'Gómez', 'maria@example.com', '987654321')");
            db.execSQL("INSERT INTO " + TABLE_CLIENTES + " (nombre, apellido, email, telefono) VALUES ('Carlos', 'Rodríguez', 'carlos@example.com', '567890123')");

            // Asegúrate de incluir el campo `tipo` en los productos
            db.execSQL("INSERT INTO " + TABLE_PRODUCTOS + " (nombre, precio, tipo) VALUES ('Producto 1', 10.0, 'Tipo1')");
            db.execSQL("INSERT INTO " + TABLE_PRODUCTOS + " (nombre, precio, tipo) VALUES ('Producto 2', 20.0, 'Tipo2')");
            db.execSQL("INSERT INTO " + TABLE_PRODUCTOS + " (nombre, precio, tipo) VALUES ('Producto 3', 30.0, 'Tipo3')");
        } catch (Exception e) {
            Log.e("DBHandler", "Error al insertar datos de prueba: " + e.getMessage());
        }
    }

    public List<String> getCitasPorUsuarioYFecha(int userId, String fecha) {
        List<String> citas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("DBHandler", "Obteniendo citas para usuario ID: " + userId + " y fecha: " + fecha);

        String query = "SELECT descripcion FROM " + TABLE_CITAS + " WHERE usuario_id = ? AND fecha = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), fecha});

        if (cursor.moveToFirst()) {
            int descripcionIndex = cursor.getColumnIndex("descripcion");
            if (descripcionIndex >= 0) {
                do {
                    citas.add(cursor.getString(descripcionIndex));
                } while (cursor.moveToNext());
            } else {
                Log.d("DBHandler", "Índice de columna 'descripcion' es inválido.");
            }
        } else {
            Log.d("DBHandler", "No se encontraron citas para el usuario y fecha especificados.");
        }

        cursor.close();
        db.close();
        return citas;
    }

    public boolean insertarCliente(String nombre, String apellido, String telefono, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("apellido", apellido);
        values.put("email", email);
        values.put("telefono", telefono);

        long resultado = db.insert(TABLE_CLIENTES, null, values);
        db.close();
        return resultado != -1; // Devuelve true si la inserción fue exitosa
    }

    public boolean existeCliente(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES + " WHERE email = ?", new String[]{email});
        boolean existe = cursor.moveToFirst();  // Si hay resultados, el cliente existe
        cursor.close();
        db.close();
        return existe;
    }

    public Cliente obtenerClientePorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cliente cliente = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES + " WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            cliente = new Cliente(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nombre")),
                    cursor.getString(cursor.getColumnIndex("apellido")),
                    cursor.getString(cursor.getColumnIndex("email")),
                    cursor.getString(cursor.getColumnIndex("telefono"))
            );
        }

        cursor.close();
        db.close();
        return cliente;
    }

    public boolean insertCita(int usuarioId, String fecha, String hora, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuario_id", usuarioId);
        values.put("fecha", fecha);
        values.put("hora", hora);
        values.put("descripcion", descripcion);

        long resultado = db.insert(TABLE_CITAS, null, values);
        db.close();
        return resultado != -1; // Retorna true si la inserción fue exitosa
    }

    public List<String> obtenerProductos() {
        List<String> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM " + TABLE_PRODUCTOS, null);

        if (cursor.moveToFirst()) {
            do {
                productos.add(cursor.getString(0));
            } while (cursor.moveToNext());
        } else {
            Log.d("DBHandler", "No se encontraron productos en la base de datos.");
        }

        cursor.close();
        db.close();
        return productos;
    }

    public List<String> obtenerPedidosPorCliente(int clienteId) {
        List<String> pedidos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, fecha, producto_nombre, cantidad, estado FROM " + TABLE_PEDIDOS + " WHERE cliente_id = ?", new String[]{String.valueOf(clienteId)});

        if (cursor.moveToFirst()) {
            do {
                String pedido = "Pedido ID: " + cursor.getInt(cursor.getColumnIndex("id")) +
                        ", Fecha: " + cursor.getString(cursor.getColumnIndex("fecha")) +
                        ", Producto: " + cursor.getString(cursor.getColumnIndex("producto_nombre")) +
                        ", Cantidad: " + cursor.getInt(cursor.getColumnIndex("cantidad")) +
                        ", Estado: " + cursor.getString(cursor.getColumnIndex("estado"));
                pedidos.add(pedido);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return pedidos;
    }

    public void marcarPedidoComoEnviado(int pedidoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("estado", "enviado");
        db.update(TABLE_PEDIDOS, values, "id = ?", new String[]{String.valueOf(pedidoId)});
        db.close();
    }

    public int obtenerPedidoIdDesdeString(String pedidoString) {
        String[] partes = pedidoString.split(",");
        String idParte = partes[0].trim();  // "Pedido ID: X"
        String pedidoIdString = idParte.split(":")[1].trim();  // Extraemos solo el número del ID
        return Integer.parseInt(pedidoIdString);  // Convertimos el ID a entero
    }

    public boolean insertarPedido(int clienteId, String productoNombre, int cantidad, String fecha, String estado) {
        SQLiteDatabase db = this.getWritableDatabase(); // Abrir la base de datos en modo escritura
        ContentValues values = new ContentValues(); // Crear un objeto ContentValues para almacenar los valores

        // Asignar los valores correspondientes a las columnas de la tabla pedidos
        values.put("cliente_id", clienteId); // ID del cliente asociado al pedido
        values.put("producto_nombre", productoNombre); // Nombre del producto
        values.put("cantidad", cantidad); // Cantidad del producto

        // Si se proporciona una fecha personalizada, usarla; de lo contrario, dejar el valor predeterminado
        if (fecha != null && !fecha.isEmpty()) {
            values.put("fecha", fecha);
        }

        // Si se proporciona un estado personalizado, usarlo; de lo contrario, dejar el valor predeterminado
        if (estado != null && !estado.isEmpty()) {
            values.put("estado", estado);
        }

        // Insertar los valores en la tabla pedidos y obtener el resultado
        long resultado = db.insert(TABLE_PEDIDOS, null, values);
        db.close(); // Cerrar la conexión a la base de datos

        // Devolver true si la inserción fue exitosa (resultado != -1), false en caso contrario
        return resultado != -1;
    }

    public void printAllClientes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                String apellido = cursor.getString(cursor.getColumnIndex("apellido"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String telefono = cursor.getString(cursor.getColumnIndex("telefono"));

                Log.d("DBHandler", "Cliente ID: " + id + ", Nombre: " + nombre + ", Apellido: " + apellido +
                        ", Email: " + email + ", Teléfono: " + telefono);
            } while (cursor.moveToNext());
        } else {
            Log.d("DBHandler", "No hay clientes en la base de datos");
        }

        cursor.close();
        db.close();
    }

    public List<String> obtenerPartners() {
        List<String> partners = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre, apellido, telefono FROM " + TABLE_CLIENTES, null);

        if (cursor.moveToFirst()) {
            do {
                String nombreCompleto = cursor.getString(0) + " " + cursor.getString(1);
                partners.add(nombreCompleto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return partners;
    }

    public boolean insertarCliente(String nombre, String apellido, String direccion, String telefono, String email, String cif, String nombreEmpresa, boolean cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("apellido", apellido);
        values.put("direccion", direccion); // Nuevo campo
        values.put("telefono", telefono);
        values.put("email", email);
        values.put("cif", cif); // Nuevo campo
        values.put("nombre_empresa", nombreEmpresa); // Nuevo campo
        values.put("cliente", cliente ? 1 : 0); // Nuevo campo (convertir boolean a entero)

        long resultado = db.insert(TABLE_CLIENTES, null, values);
        db.close();
        return resultado != -1; // Devuelve true si la inserción fue exitosa
    }

    public Cliente obtenerClientePorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cliente cliente = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES + " WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            int clienteId = cursor.getInt(cursor.getColumnIndex("id"));
            String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            String apellido = cursor.getString(cursor.getColumnIndex("apellido"));
            String direccion = cursor.getString(cursor.getColumnIndex("direccion")); // Nuevo campo
            String telefono = cursor.getString(cursor.getColumnIndex("telefono"));
            String cif = cursor.getString(cursor.getColumnIndex("cif")); // Nuevo campo
            String nombreEmpresa = cursor.getString(cursor.getColumnIndex("nombre_empresa")); // Nuevo campo
            boolean esCliente = cursor.getInt(cursor.getColumnIndex("cliente")) == 1; // Nuevo campo

            cliente = new Cliente(clienteId, nombre, apellido, direccion, telefono, email, cif, nombreEmpresa, esCliente);
        }
        cursor.close();
        db.close();
        return cliente;
    }

    public Cliente obtenerClientePorNombreApellido(String nombre, String apellido) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES + " WHERE nombre = ? AND apellido = ?", new String[]{nombre, apellido});
        Cliente cliente = null;
        if (cursor != null && cursor.moveToFirst()) {
            cliente = new Cliente(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nombre")),
                    cursor.getString(cursor.getColumnIndex("apellido")),
                    cursor.getString(cursor.getColumnIndex("direccion")),
                    cursor.getString(cursor.getColumnIndex("telefono")),
                    cursor.getString(cursor.getColumnIndex("email")),
                    cursor.getString(cursor.getColumnIndex("cif")),
                    cursor.getString(cursor.getColumnIndex("nombre_empresa")),
                    cursor.getInt(cursor.getColumnIndex("cliente")) == 1
            );
            cursor.close();
        }
        db.close();
        return cliente;
    }
    public boolean insertarPedido(int idCliente, int idExperiencia, int cantidad, String fechaPedido, int nPedido) {
        SQLiteDatabase db = this.getWritableDatabase(); // Abrir la base de datos en modo escritura
        ContentValues values = new ContentValues(); // Crear un objeto ContentValues para almacenar los valores

        // Asignar los valores correspondientes a las columnas de la tabla pedidos
        values.put("id_cliente", idCliente);          // ID del cliente asociado al pedido
        values.put("id_experiencia", idExperiencia);  // ID del producto/experiencia
        values.put("fecha_pedido", fechaPedido);      // Fecha del pedido
        values.put("cantidad", cantidad);             // Cantidad del producto
        values.put("n_pedido", nPedido);              // Número de pedido

        // Insertar los valores en la tabla pedidos y obtener el resultado
        long resultado = db.insert(TABLE_PEDIDOS, null, values);
        db.close(); // Cerrar la conexión a la base de datos

        // Devolver true si la inserción fue exitosa (resultado != -1), false en caso contrario
        return resultado != -1;
    }
    public int obtenerIdProductoPorNombre(String nombreProducto) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_PRODUCTOS + " WHERE nombre = ?", new String[]{nombreProducto});
        int idProducto = -1; // Valor predeterminado si no se encuentra el producto
        if (cursor.moveToFirst()) {
            idProducto = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        db.close();
        return idProducto;
    }
    private int generarNumeroPedido() {
        // Generar un número aleatorio entre 1000 y 9999
        return (int) (Math.random() * 9000) + 1000;
    }

    public List<String> obtenerPedidosPendientes() {
        List<String> pedidosPendientes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para obtener todos los pedidos pendientes
        String query = "SELECT id_pedido, fecha_pedido, cantidad, n_pedido FROM " + TABLE_PEDIDOS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int idPedido = cursor.getInt(cursor.getColumnIndex("id_pedido"));
                String fechaPedido = cursor.getString(cursor.getColumnIndex("fecha_pedido"));
                int cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));
                int nPedido = cursor.getInt(cursor.getColumnIndex("n_pedido"));

                // Formatear cada pedido como una cadena legible
                String pedido = "Pedido ID: " + idPedido +
                        ", Fecha: " + fechaPedido +
                        ", Cantidad: " + cantidad +
                        ", Número de Pedido: " + nPedido;
                pedidosPendientes.add(pedido);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return pedidosPendientes;
    }

    /**
     * Método para eliminar un pedido de la base de datos.
     * @param pedidoId El ID del pedido que se desea eliminar.
     */
    public void eliminarPedido(int pedidoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Eliminar el pedido con el ID especificado
            db.delete(TABLE_PEDIDOS, "id = ?", new String[]{String.valueOf(pedidoId)});
            Log.d("DBHandler", "Pedido eliminado con ID: " + pedidoId);
        } catch (Exception e) {
            Log.e("DBHandler", "Error al eliminar el pedido con ID: " + pedidoId, e);
        } finally {
            db.close(); // Cerrar la conexión a la base de datos
        }
    }
}