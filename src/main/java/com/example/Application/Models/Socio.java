package com.example.Application.Models;

import java.time.LocalDate;

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
@Table(name = "socio")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del socio", example = "1")
    private Long socioId;

    @Column(name = "numero_socio", unique = true, nullable = false)
    @Schema(description = "Número de socio único", example = "SOC001")
    private String numeroSocio;

    @Column(length = 50, nullable = false)
    @Schema(description = "Nombre del socio", example = "Luis", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(length = 50, nullable = false)
    @Schema(description = "Apellidos del socio", example = "Navarro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apellidos;

    @Column(length = 100, nullable = false, unique = true)
    @Schema(description = "Correo electrónico del socio", example = "luis@mail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Fecha de nacimiento del socio", example = "1990-05-20")
    private LocalDate fechaNacimiento;

    @Schema(description = "Fecha de alta del socio", example = "2025-12-01")
    private LocalDate fechaAlta;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    @Schema(description = "Estado del socio", example = "ACTIVO", allowableValues = {"ACTIVO", "SANCIONADO"})
    private EstadoSocio estado;

    @Schema(description = "Fecha de fin de sanción, si aplica", example = "2025-12-31")
    private LocalDate fechaFinPenalizacion;

    public enum EstadoSocio {
        ACTIVO,
        SANCIONADO
    }

    // Getters y setters

    public Long getSocioId() {
        return socioId;
    }

    public String getNumeroSocio() { return numeroSocio; }
    public void setNumeroSocio(String numeroSocio) { this.numeroSocio = numeroSocio; }

    public void setSocioId(Long socioId) {
        this.socioId = socioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public EstadoSocio getEstado() {
        return estado;
    }

    public void setEstado(EstadoSocio estado) {
        this.estado = estado;
    }

    public LocalDate getFechaFinPenalizacion() {
        return fechaFinPenalizacion;
    }

    public void setFechaFinPenalizacion(LocalDate fechaFinPenalizacion) {
        this.fechaFinPenalizacion = fechaFinPenalizacion;
    }
}

