package com.example.comercialesreto12;

public class DetallePedido {
    private String productoNombre;
    private int cantidad;

    public DetallePedido(String productoNombre, int cantidad) {
        this.productoNombre = productoNombre;
        this.cantidad = cantidad;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public int getCantidad() {
        return cantidad;
    }
}

