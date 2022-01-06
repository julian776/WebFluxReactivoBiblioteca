package com.sofka.biblioteca.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;

@Document(collection = "recursos")
public class Recurso {

    @Id
    private String id = UUID.randomUUID().toString();

    private String fechaSalida;
    private boolean isPrestado = false;
    private String titulo;
    private String tipo;

    public Recurso(String fechaSalida, String titulo, String tipo){
        this.fechaSalida = Objects.requireNonNull(fechaSalida);
        this.titulo = Objects.requireNonNull(titulo);
        this.tipo = Objects.requireNonNull(tipo);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public boolean isPrestado() {
        return isPrestado;
    }

    public String getTipo() {
        return tipo;
    }

    public Recurso setPrestado(boolean prestado) {
        this.isPrestado = prestado;
        return this;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
