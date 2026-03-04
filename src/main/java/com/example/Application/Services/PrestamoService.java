package com.example.Application.Services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.example.Application.Exceptions.ReglaNegocioException;
import com.example.Application.Models.EstadoLibro;
import com.example.Application.Models.Libro;
import com.example.Application.Models.Prestamo;
import com.example.Application.Models.Socio;
import com.example.Application.Models.Socio.EstadoSocio;
import com.example.Application.Repository.LibroRepository;
import com.example.Application.Repository.PrestamoRepository;
import com.example.Application.Repository.SocioRepository;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final SocioRepository socioRepository;

    public PrestamoService(PrestamoRepository prestamoRepository,
            LibroRepository libroRepository,
            SocioRepository socioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.socioRepository = socioRepository;
    }

    // Crear préstamo con reglas de negocio
    public Prestamo crearPrestamo(Socio socio, Libro libro) {

    if (socio.getEstado() == EstadoSocio.SANCIONADO && socio.getFechaFinPenalizacion() == null) {
            socio.setFechaFinPenalizacion(LocalDate.now().plusDays(15));
        }

        if (socio.getEstado() == EstadoSocio.SANCIONADO
                && socio.getFechaFinPenalizacion() != null
                && socio.getFechaFinPenalizacion().isAfter(LocalDate.now())) {

            throw new ReglaNegocioException(
                    "El socio está sancionado hasta " + socio.getFechaFinPenalizacion()
            );
        }

        long activos = prestamoRepository
                .countBySocio_SocioIdAndFechaDevolucionIsNull(socio.getSocioId());

        if (activos >= 3) {
            throw new ReglaNegocioException(
                    "El socio ya tiene 3 préstamos activos"
            );
        }

        if (libro.getEstado() != EstadoLibro.DISPONIBLE) {
            throw new ReglaNegocioException(
                    "El libro no está disponible"
            );
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setSocio(socio);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaLimite(LocalDate.now().plusDays(2));

        libro.setEstado(EstadoLibro.PRESTADO);
        libroRepository.save(libro);

        return prestamoRepository.save(prestamo);
    }

    // Devolver libro con cálculo de penalización
    public Prestamo devolverPrestamo(Long prestamoId) throws Exception {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new Exception("Préstamo no encontrado"));

        if (prestamo.getFechaDevolucion() != null) {
            throw new Exception("Préstamo ya devuelto");
        }

        prestamo.setFechaDevolucion(LocalDate.now());

        Libro libro = prestamo.getLibro();
        libro.setEstado(EstadoLibro.DISPONIBLE);
        libroRepository.save(libro);

        // Calcular penalización si devuelve tarde
        if (prestamo.getFechaDevolucion().isAfter(prestamo.getFechaLimite())) {
            long diasRetraso = ChronoUnit.DAYS.between(prestamo.getFechaLimite(), prestamo.getFechaDevolucion());
            Socio socio = prestamo.getSocio();
            socio.setEstado(EstadoSocio.SANCIONADO);
            LocalDate fechaFinPenalizacion = LocalDate.now().plusDays(diasRetraso * 2);
            socio.setFechaFinPenalizacion(fechaFinPenalizacion);
            socioRepository.save(socio);
        }

        return prestamoRepository.save(prestamo);
    }

}
