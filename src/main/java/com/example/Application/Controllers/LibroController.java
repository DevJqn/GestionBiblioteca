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

import com.example.Application.Models.Libro;
import com.example.Application.Repository.LibroRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/libros")
@Tag(name = "libro-controller", description = "Operaciones sobre libros")
public class LibroController {

    private final LibroRepository libroRepository;

    public LibroController(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los libros", responses = {
            @ApiResponse(responseCode = "200", description = "Listado de libros")})
    public List<Libro> getAllLibros() {
        return libroRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener libro por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")})
    public ResponseEntity<Libro> getLibroById(@Parameter(description = "ID del libro") @PathVariable Long id) {
        return libroRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un libro", responses = {
            @ApiResponse(responseCode = "200", description = "Libro creado")})
    public Libro createLibro(@RequestBody Libro libro) {
        return libroRepository.save(libro);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar libro", responses = {
            @ApiResponse(responseCode = "200", description = "Libro actualizado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")})
    public ResponseEntity<Libro> updateLibro(
            @Parameter(description = "ID del libro") @PathVariable Long id,
            @RequestBody Libro libroDetails) {

        return libroRepository.findById(id).map(libro -> {
            libro.setTitulo(libroDetails.getTitulo());
            libro.setAutor(libroDetails.getAutor());
            libro.setIsbn(libroDetails.getIsbn());
            libro.setCategoria(libroDetails.getCategoria());
            libro.setEstado(libroDetails.getEstado());
            return ResponseEntity.ok(libroRepository.save(libro));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar libro", responses = {
            @ApiResponse(responseCode = "204", description = "Libro eliminado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")})
    public ResponseEntity<Void> deleteLibro(@Parameter(description = "ID del libro") @PathVariable Long id) {
        if (!libroRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        libroRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


