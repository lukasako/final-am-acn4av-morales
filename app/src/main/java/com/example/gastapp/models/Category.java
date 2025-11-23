package com.example.gastapp.models;

public class Category {
    private String id;
    private String nombre;
    private long createdAt;

    public Category() {} // necesario Firestore

    public Category(String id, String nombre, long createdAt) {
        this.id = id;
        this.nombre = nombre;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public long getCreatedAt() { return createdAt; }
}
