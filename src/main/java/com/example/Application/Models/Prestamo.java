package com.example.Application.Models;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del préstamo", example = "1")
    private Long prestamoId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "socio_id", nullable = false)
    @Schema(description = "Socio que realiza el préstamo")
    private Socio socio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "libro_id", nullable = false)
    @Schema(description = "Libro prestado")
    private Libro libro;

    @Transient
    @Schema(description = "ID del libro (solo para JSON)", example = "1")
    private Long libroId;

    @Transient
    @Schema(description = "ID del socio (solo para JSON)", example = "1")
    private Long socioId;

    @Schema(description = "Fecha del préstamo", example = "2025-12-16")
    private LocalDate fechaPrestamo;

    @Schema(description = "Fecha límite de devolución", example = "2025-12-30")
    private LocalDate fechaLimite;

    @Schema(description = "Fecha de devolución real (null si no devuelto)", example = "2025-12-28")
    private LocalDate fechaDevolucion;

    // Getters y setters

    public Long getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(Long prestamoId) {
        this.prestamoId = prestamoId;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public Long getSocioId() {
        return socioId;
    }

    public void setSocioId(Long socioId) {
        this.socioId = socioId;
    }
}


