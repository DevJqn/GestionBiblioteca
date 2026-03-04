package com.example.Application.Controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Application.Models.Libro;
import com.example.Application.Models.Prestamo;
import com.example.Application.Models.Socio;
import com.example.Application.Repository.LibroRepository;
import com.example.Application.Repository.PrestamoRepository;
import com.example.Application.Repository.SocioRepository;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticaController {

    private final SocioRepository socioRepository;
    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;

    public EstadisticaController(SocioRepository socioRepository,
                                 PrestamoRepository prestamoRepository,
                                 LibroRepository libroRepository) {
        this.socioRepository = socioRepository;
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
    }

    @GetMapping
    public String mostrarEstadisticas(Model model) {
        List<Socio> socios = socioRepository.findAll();
        List<Libro> libros = libroRepository.findAll();
        List<Prestamo> prestamos = prestamoRepository.findAll();

        long prestamosActivos = prestamos.stream().filter(p -> p.getFechaDevolucion() == null).count();
        long sociosSancionados = socios.stream()
                .filter(s -> s.getEstado() != null && s.getEstado().name().equals("SANCIONADO"))
                .count();

        model.addAttribute("totalSocios", socios.size());
        model.addAttribute("totalLibros", libros.size());
        model.addAttribute("totalPrestamos", prestamos.size());
        model.addAttribute("prestamosActivos", prestamosActivos);
        model.addAttribute("sociosSancionados", sociosSancionados);

        return "estadisticas"; // Thymeleaf template: estadisticas.html
    }
}


