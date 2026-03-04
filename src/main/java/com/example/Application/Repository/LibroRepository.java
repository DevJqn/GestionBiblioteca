package com.example.Application.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Application.Models.EstadoLibro;
import com.example.Application.Models.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Buscar libros por título
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    // Buscar por ISBN (único)
    Optional<Libro> findByIsbn(String isbn);

    // Filtrar por estado
    List<Libro> findByEstado(EstadoLibro estado);

    // Buscar por autor
    List<Libro> findByAutorContainingIgnoreCase(String autor);
}

