package com.example.Application.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Application.Models.Prestamo;
import com.example.Application.Repository.PrestamoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/prestamos")
@Tag(name = "prestamo-controller", description = "Operaciones sobre préstamos")
public class PrestamoController {

    private final PrestamoRepository prestamoRepository;

    public PrestamoController(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los préstamos", responses = {
            @ApiResponse(responseCode = "200", description = "Listado de préstamos")})
    public List<Prestamo> getAllPrestamos() {
        return prestamoRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener préstamo por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Préstamo encontrado"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")})
    public ResponseEntity<Prestamo> getPrestamoById(@Parameter(description = "ID del préstamo") @PathVariable Long id) {
        return prestamoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un préstamo", responses = {
            @ApiResponse(responseCode = "200", description = "Préstamo creado")})
    public Prestamo createPrestamo(@RequestBody Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar préstamo", responses = {
            @ApiResponse(responseCode = "200", description = "Préstamo actualizado"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")})
    public ResponseEntity<Prestamo> updatePrestamo(
            @Parameter(description = "ID del préstamo") @PathVariable Long id,
            @RequestBody Prestamo prestamoDetails) {

        return prestamoRepository.findById(id).map(prestamo -> {
            prestamo.setSocio(prestamoDetails.getSocio());
            prestamo.setLibro(prestamoDetails.getLibro());
            prestamo.setFechaPrestamo(prestamoDetails.getFechaPrestamo());
            prestamo.setFechaLimite(prestamoDetails.getFechaLimite());
            prestamo.setFechaDevolucion(prestamoDetails.getFechaDevolucion());
            return ResponseEntity.ok(prestamoRepository.save(prestamo));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar préstamo", responses = {
            @ApiResponse(responseCode = "204", description = "Préstamo eliminado"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")})
    public ResponseEntity<Void> deletePrestamo(@Parameter(description = "ID del préstamo") @PathVariable Long id) {
        if (!prestamoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        prestamoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}




