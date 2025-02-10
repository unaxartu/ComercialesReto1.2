package com.example.comercialesreto12;

public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String direccion; // Nuevo campo
    private String telefono;
    private String email;
    private String cif; // Nuevo campo
    private String nombreEmpresa; // Nuevo campo
    private boolean cliente; // Nuevo campo

    // Constructor
    public Cliente(int id, String nombre, String apellido, String direccion, String telefono, String email, String cif, String nombreEmpresa, boolean cliente) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.cif = cif;
        this.nombreEmpresa = nombreEmpresa;
        this.cliente = cliente;
    }

    public Cliente() {

    }

    public Cliente(int id, String nombre, String apellido, String email, String telefono) {
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public boolean isCliente() {
        return cliente;
    }

    public void setCliente(boolean cliente) {
        this.cliente = cliente;
    }
}