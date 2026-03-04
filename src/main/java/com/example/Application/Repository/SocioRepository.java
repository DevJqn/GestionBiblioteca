package com.example.Application.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Application.Models.Socio;
import com.example.Application.Models.Socio.EstadoSocio;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    // Buscar por email (único)
    Optional<Socio> findByEmail(String email);

    // Buscar socios por nombre
    List<Socio> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por nombre o apellidos
    List<Socio> findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            String nombre, String apellidos);

    // Filtrar por estado
    List<Socio> findByEstado(EstadoSocio estado);
}
