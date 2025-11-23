package com.example.gastapp.models;

public class Movimiento {
    public String titulo;
    public double monto;
    public String categoria;
    public String fecha;
    public String medioPago;
    public boolean esIngreso;

    public Movimiento() {} // requerido por firebase

    public Movimiento(String titulo, double monto, String categoria, String fecha, String medioPago, boolean esIngreso) {
        this.titulo = titulo;
        this.monto = monto;
        this.categoria = categoria;
        this.fecha = fecha;
        this.medioPago = medioPago;
        this.esIngreso = esIngreso;
    }
}
