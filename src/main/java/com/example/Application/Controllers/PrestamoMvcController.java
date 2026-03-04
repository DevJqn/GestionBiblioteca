package com.example.Application.Controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Application.Exceptions.ReglaNegocioException;
import com.example.Application.Models.EstadoLibro;
import com.example.Application.Models.Libro;
import com.example.Application.Models.Prestamo;
import com.example.Application.Models.Socio;
import com.example.Application.Repository.LibroRepository;
import com.example.Application.Repository.PrestamoRepository;
import com.example.Application.Repository.SocioRepository;
import com.example.Application.Services.PrestamoService;

@Controller
@RequestMapping("/prestamos")
public class PrestamoMvcController {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final SocioRepository socioRepository;
    private final PrestamoService prestamoService;

    public PrestamoMvcController(PrestamoRepository prestamoRepository,
            LibroRepository libroRepository,
            SocioRepository socioRepository,
            PrestamoService prestamoService) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.socioRepository = socioRepository;
        this.prestamoService = prestamoService;
    }

    // LISTADO + FILTROS
    @GetMapping
    public String listarPrestamos(
            @RequestParam(required = false) Long socioId,
            @RequestParam(required = false) Long libroId,
            @RequestParam(required = false) Boolean activos,
            Model model) {

        List<Prestamo> prestamos;

        if (Boolean.TRUE.equals(activos)) {
            prestamos = prestamoRepository.findByFechaDevolucionIsNull();
        } else if (socioId != null) {
            prestamos = prestamoRepository.findBySocio_SocioId(socioId);
        } else if (libroId != null) {
            prestamos = prestamoRepository.findByLibro_LibroId(libroId);
        } else {
            prestamos = prestamoRepository.findAll();
        }

        model.addAttribute("prestamos", prestamos);
        model.addAttribute("socios", socioRepository.findAll());
        model.addAttribute("libros", libroRepository.findAll());

        return "prestamos_list";
    }

    // FORMULARIO NUEVO
    @GetMapping("/new")
    public String nuevoPrestamo(Model model) {
        model.addAttribute("prestamo", new Prestamo());
        model.addAttribute("socios", socioRepository.findAll());
        model.addAttribute("libros",
                libroRepository.findByEstado(EstadoLibro.DISPONIBLE));
        return "prestamos_new";
    }

    // GUARDAR
    @PostMapping
    public String guardarPrestamo(
            @ModelAttribute Prestamo prestamo,
            BindingResult result,
            Model model) {

        try {
            // Obtener objetos a partir de los IDs
            Socio socio = socioRepository.findById(prestamo.getSocioId())
                    .orElseThrow(() -> new ReglaNegocioException("Socio no encontrado"));
            Libro libro = libroRepository.findById(prestamo.getLibroId())
                    .orElseThrow(() -> new ReglaNegocioException("Libro no encontrado"));

            prestamoService.crearPrestamo(socio, libro);

        } catch (ReglaNegocioException e) {
            result.reject("prestamo", e.getMessage());

            model.addAttribute("socios", socioRepository.findAll());
            model.addAttribute("libros", libroRepository.findByEstado(EstadoLibro.DISPONIBLE));

            return "prestamos_new";
        }

        return "redirect:/prestamos";
    }

    // DEVOLVER
    @PostMapping("/devolver")
    public String devolverPrestamo(@RequestParam Long id, Model model) {
        try {
            prestamoService.devolverPrestamo(id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "prestamos_list"; // o la página de listado con mensaje de error
        }
        return "redirect:/prestamos";
    }

}
