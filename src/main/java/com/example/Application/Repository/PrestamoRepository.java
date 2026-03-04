package com.example.Application.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Application.Models.Prestamo;
import com.example.Application.Models.Socio;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // Encuentra todos los préstamos de un socio por su socioId
    List<Prestamo> findBySocio_SocioId(Long socioId);

    // Encuentra préstamos activos (sin fecha de devolución) de un socio
    @Query("SELECT p FROM Prestamo p WHERE p.socio.socioId = :socioId AND p.fechaDevolucion IS NULL")
    List<Prestamo> findPrestamosActivosBySocioId(Long socioId);

    // Para el método de devolución
    @Query("SELECT p FROM Prestamo p WHERE p.libro.libroId = :libroId AND p.fechaDevolucion IS NULL")
    java.util.Optional<Prestamo> findActivePrestamoByLibroId(Long libroId);

    // Préstamos de un libro
    List<Prestamo> findByLibro_LibroId(Long libroId);

    // Préstamos activos (no devueltos)
    List<Prestamo> findByFechaDevolucionIsNull();

    List<Prestamo> findBySocioAndFechaDevolucionIsNull(Socio socio);

    long countBySocio_SocioIdAndFechaDevolucionIsNull(Long socioId);

}



