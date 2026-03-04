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

import com.example.Application.Models.Socio;
import com.example.Application.Repository.SocioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/socios")
@Tag(name = "socio-controller", description = "Operaciones sobre socios")
public class SocioController {

    private final SocioRepository socioRepository;

    public SocioController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los socios", responses = {
            @ApiResponse(responseCode = "200", description = "Listado de socios")})
    public List<Socio> getAllSocios() {
        return socioRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener socio por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Socio encontrado"),
            @ApiResponse(responseCode = "404", description = "Socio no encontrado")})
    public ResponseEntity<Socio> getSocioById(@Parameter(description = "ID del socio") @PathVariable Long id) {
        return socioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un socio", responses = {
            @ApiResponse(responseCode = "200", description = "Socio creado")})
    public Socio createSocio(@RequestBody Socio socio) {
        return socioRepository.save(socio);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar socio", responses = {
            @ApiResponse(responseCode = "200", description = "Socio actualizado"),
            @ApiResponse(responseCode = "404", description = "Socio no encontrado")})
    public ResponseEntity<Socio> updateSocio(
            @Parameter(description = "ID del socio") @PathVariable Long id,
            @RequestBody Socio socioDetails) {

        return socioRepository.findById(id).map(socio -> {
            socio.setNombre(socioDetails.getNombre());
            socio.setApellidos(socioDetails.getApellidos());
            socio.setEmail(socioDetails.getEmail());
            socio.setFechaNacimiento(socioDetails.getFechaNacimiento());
            socio.setFechaAlta(socioDetails.getFechaAlta());
            socio.setEstado(socioDetails.getEstado());
            socio.setFechaFinPenalizacion(socioDetails.getFechaFinPenalizacion());
            return ResponseEntity.ok(socioRepository.save(socio));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar socio", responses = {
            @ApiResponse(responseCode = "204", description = "Socio eliminado"),
            @ApiResponse(responseCode = "404", description = "Socio no encontrado")})
    public ResponseEntity<Void> deleteSocio(@Parameter(description = "ID del socio") @PathVariable Long id) {
        if (!socioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        socioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}



