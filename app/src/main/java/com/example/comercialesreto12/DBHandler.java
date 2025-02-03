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
    private static final int DB_VERSION = 6;
    private static final String TABLE_USUARIOS_LOGIN = "usuarios_login";
    private static final String TABLE_CITAS = "citas";
    private static final String TABLE_CLIENTES = "clientes";
    private static final String TABLE_PRODUCTOS = "productos";
    private static final String TABLE_PEDIDOS = "pedidos";
    private static final String TABLE_DETALLE_PEDIDO = "detalle_pedido";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsuariosLoginTable = "CREATE TABLE " + TABLE_USUARIOS_LOGIN + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT)";
        db.execSQL(createUsuariosLoginTable);

        String createCitasTable = "CREATE TABLE " + TABLE_CITAS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario_id INTEGER, " +
                "fecha TEXT, " +
                "hora TEXT, " +
                "descripcion TEXT, " +
                "FOREIGN KEY(usuario_id) REFERENCES " + TABLE_USUARIOS_LOGIN + "(id))";
        db.execSQL(createCitasTable);

        String createClientesTable = "CREATE TABLE " + TABLE_CLIENTES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "apellido TEXT, " +
                "email TEXT UNIQUE, " +
                "telefono TEXT)";
        db.execSQL(createClientesTable);

        String createProductosTable = "CREATE TABLE " + TABLE_PRODUCTOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "precio REAL)";
        db.execSQL(createProductosTable);

        // Crear tablas de pedidos y detalle de pedido
        String createPedidosTable = "CREATE TABLE " + TABLE_PEDIDOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cliente_id INTEGER, " +
                "fecha TEXT, " +
                "FOREIGN KEY(cliente_id) REFERENCES " + TABLE_CLIENTES + "(id))";
        db.execSQL(createPedidosTable);

        String createDetallePedidoTable = "CREATE TABLE " + TABLE_DETALLE_PEDIDO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pedido_id INTEGER, " +
                "producto_nombre TEXT, " +
                "cantidad INTEGER, " +
                "FOREIGN KEY(pedido_id) REFERENCES " + TABLE_PEDIDOS + "(id))";
        db.execSQL(createDetallePedidoTable);

        insertTestData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETALLE_PEDIDO);

        onCreate(db);  // Vuelve a crear las tablas y recarga los datos
    }

    private void insertTestData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_USUARIOS_LOGIN + " (username, password) VALUES ('admin', 'admin123')");
        db.execSQL("INSERT INTO " + TABLE_USUARIOS_LOGIN + " (username, password) VALUES ('usuario1', 'pass123')");

        db.execSQL("INSERT INTO " + TABLE_CITAS + " (usuario_id, fecha, hora, descripcion) VALUES (1, '2024-06-01', '10:00', 'Reunión con cliente')");
        db.execSQL("INSERT INTO " + TABLE_CITAS + " (usuario_id, fecha, hora, descripcion) VALUES (1, '2024-06-02', '14:30', 'Presentación de producto')");
        db.execSQL("INSERT INTO " + TABLE_CITAS + " (usuario_id, fecha, hora, descripcion) VALUES (2, '2024-06-03', '09:00', 'Llamada de seguimiento')");

        db.execSQL("INSERT INTO " + TABLE_CLIENTES + " (nombre, apellido, email, telefono) VALUES ('Juan', 'Pérez', 'juan@example.com', '123456789')");
        db.execSQL("INSERT INTO " + TABLE_CLIENTES + " (nombre, apellido, email, telefono) VALUES ('María', 'Gómez', 'maria@example.com', '987654321')");
        db.execSQL("INSERT INTO " + TABLE_CLIENTES + " (nombre, apellido, email, telefono) VALUES ('Carlos', 'Rodríguez', 'carlos@example.com', '567890123')");

        db.execSQL("INSERT INTO " + TABLE_PRODUCTOS + " (nombre, precio) VALUES ('Producto 1', 10.0)");
        db.execSQL("INSERT INTO " + TABLE_PRODUCTOS + " (nombre, precio) VALUES ('Producto 2', 20.0)");
        db.execSQL("INSERT INTO " + TABLE_PRODUCTOS + " (nombre, precio) VALUES ('Producto 3', 30.0)");
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

    public List<String> obtenerPartners() {
        List<String> partners = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT nombre, apellido FROM " + TABLE_CLIENTES, null);

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
            int clienteId = cursor.getInt(cursor.getColumnIndex("id"));
            String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            String apellido = cursor.getString(cursor.getColumnIndex("apellido"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String telefono = cursor.getString(cursor.getColumnIndex("telefono"));

            cliente = new Cliente(clienteId, nombre, apellido, email, telefono);
        }

        cursor.close();
        db.close();

        return cliente;
    }

    public Cliente obtenerClientePorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cliente cliente = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES + " WHERE email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            int clienteId = cursor.getInt(cursor.getColumnIndex("id"));
            String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            String apellido = cursor.getString(cursor.getColumnIndex("apellido"));
            String telefono = cursor.getString(cursor.getColumnIndex("telefono"));

            cliente = new Cliente(clienteId, nombre, apellido, email, telefono);
        }

        cursor.close();
        db.close();

        return cliente;
    }

    public Cliente obtenerClientePorNombreApellido(String nombre, String apellido) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES + " WHERE nombre = ? AND apellido = ?", new String[]{nombre, apellido});

        if (cursor != null && cursor.moveToFirst()) {
            Cliente cliente = new Cliente(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nombre")),
                    cursor.getString(cursor.getColumnIndex("apellido")),
                    cursor.getString(cursor.getColumnIndex("email")),
                    cursor.getString(cursor.getColumnIndex("telefono"))
            );
            cursor.close();
            db.close();
            return cliente;
        }

        cursor.close();
        db.close();
        return null;
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

    // Métodos para manejar pedidos
    public boolean insertarPedido(int clienteId, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cliente_id", clienteId);
        values.put("fecha", fecha);

        long resultado = db.insert(TABLE_PEDIDOS, null, values);
        db.close();
        return resultado != -1;
    }

    public boolean insertarDetallePedido(int pedidoId, String productoNombre, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pedido_id", pedidoId);
        values.put("producto_nombre", productoNombre);
        values.put("cantidad", cantidad);

        long resultado = db.insert(TABLE_DETALLE_PEDIDO, null, values);
        db.close();
        return resultado != -1;
    }

    public List<String> obtenerPedidosPorCliente(int clienteId) {
        List<String> pedidos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PEDIDOS + " WHERE cliente_id = ?", new String[]{String.valueOf(clienteId)});

        if (cursor.moveToFirst()) {
            do {
                String pedido = "Pedido ID: " + cursor.getInt(cursor.getColumnIndex("id")) +
                        ", Fecha: " + cursor.getString(cursor.getColumnIndex("fecha"));
                pedidos.add(pedido);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pedidos;
    }

    public List<String> obtenerDetallePedido(int pedidoId) {
        List<String> detalle = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DETALLE_PEDIDO + " WHERE pedido_id = ?", new String[]{String.valueOf(pedidoId)});

        if (cursor.moveToFirst()) {
            do {
                String detalleStr = "Producto: " + cursor.getString(cursor.getColumnIndex("producto_nombre")) +
                        ", Cantidad: " + cursor.getInt(cursor.getColumnIndex("cantidad"));
                detalle.add(detalleStr);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return detalle;
    }

    // Modificado para que acepte el pedidoId como int
    public void marcarPedidoComoEnviado(int pedidoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("estado", "enviado");

        // Actualizar el estado del pedido en la base de datos
        db.update(TABLE_PEDIDOS, values, "id = ?", new String[]{String.valueOf(pedidoId)});
        db.close();
    }
    // Método para extraer el ID del pedido desde el String
    public int obtenerPedidoIdDesdeString(String pedidoString) {
        String[] partes = pedidoString.split(",");  // Asumimos que el formato es "Pedido ID: X, Fecha: Y"
        String idParte = partes[0];  // "Pedido ID: X"
        String pedidoIdString = idParte.split(":")[1].trim();  // Extraemos solo el número del ID
        return Integer.parseInt(pedidoIdString);  // Convertimos el ID a entero
    }

    public List<String> obtenerPedidosPendientes() {
        List<String> pedidosPendientes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Obtener pedidos donde el estado es 'pendiente'
        String query = "SELECT id, fecha FROM " + TABLE_PEDIDOS + " WHERE estado = 'pendiente'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int pedidoId = cursor.getInt(cursor.getColumnIndex("id"));
                String fecha = cursor.getString(cursor.getColumnIndex("fecha"));
                // Crear el String en el formato adecuado: "Pedido ID: X, Fecha: Y"
                pedidosPendientes.add("Pedido ID: " + pedidoId + ", Fecha: " + fecha);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pedidosPendientes;
    }

}
