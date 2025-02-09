package com.example.comercialesreto12;

public class PedidoRequest {
    private int id_cliente;
    private int id_experiencia;
    private String fecha_pedido;
    private int cantidad;
    private int n_pedido;

    public PedidoRequest(int id_cliente, int id_experiencia, String fecha_pedido, int cantidad, int n_pedido) {
        this.id_cliente = id_cliente;
        this.id_experiencia = id_experiencia;
        this.fecha_pedido = fecha_pedido;
        this.cantidad = cantidad;
        this.n_pedido = n_pedido;
    }

    public int getIdCliente() { return id_cliente; }
    public int getIdExperiencia() { return id_experiencia; }
    public String getFechaPedido() { return fecha_pedido; }
    public int getCantidad() { return cantidad; }
    public int getNPedido() { return n_pedido; }
}
