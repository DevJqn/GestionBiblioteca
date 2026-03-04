package com.example.Application.Models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del libro", example = "1")
    private Long libroId;

    @Column(length = 100, nullable = false)
    @Schema(description = "Título del libro", example = "El Quijote", requiredMode = Schema.RequiredMode.REQUIRED)
    private String titulo;

    @Column(length = 100, nullable = false)
    @Schema(description = "Autor del libro", example = "Miguel de Cervantes", requiredMode = Schema.RequiredMode.REQUIRED)
    private String autor;

    @Column(length = 50)
    @Schema(description = "Categoría o género del libro", example = "Novela")
    private String categoria;

    @Column(length = 13, unique = true)
    @Schema(description = "Código ISBN del libro", example = "978-3-16-148410-0")
    private String isbn;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    @Schema(description = "Estado del libro", example = "DISPONIBLE", allowableValues = {"DISPONIBLE", "PRESTADO"})
    private EstadoLibro estado;

    // Getters y setters
    public EstadoLibro getEstado() {
        return estado;
    }

    public void setEstado(EstadoLibro estado) {
        this.estado = estado;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}


