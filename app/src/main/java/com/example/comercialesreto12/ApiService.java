package com.example.comercialesreto12;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("api/experiencias/")
    Call<List<Experiencia>> getExperiencias();

    @GET("api/empleados/")
    Call<List<Empleado>> getEmpleados();

    @GET("api/clientes/")
    Call<List<Cliente>> getClientes();

    @POST("api/clientes/") // Endpoint para crear un nuevo cliente
    Call<Cliente> postCliente(@Body Cliente cliente);

    @POST("api/pedidos/") // Asegúrate de que esta ruta sea correcta
    Call<Void> enviarPedido(@Body PedidoRequest pedido);

}