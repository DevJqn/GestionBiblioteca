package com.example.Application.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Application.Models.Libro;
import com.example.Application.Repository.LibroRepository;

@Controller
@RequestMapping("/libros")
public class LibroMvcController {

    private final LibroRepository libroRepository;

    public LibroMvcController(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @GetMapping
    public String listarLibros(Model model) {
        model.addAttribute("libros", libroRepository.findAll());
        return "libros_list";
    }

    @GetMapping("/new")
    public String nuevoLibro(Model model) {
        model.addAttribute("libro", new Libro());
        return "libros_new";
    }

    @PostMapping
    public String guardarLibro(@ModelAttribute Libro libro) {
        libroRepository.save(libro);
        return "redirect:/libros";
    }

    @GetMapping("/edit")
    public String editarLibro(@RequestParam Long id, Model model) {
        model.addAttribute("libro", libroRepository.findById(id).orElseThrow());
        return "libros_new";
    }

    @PostMapping("/delete")
    public String borrarLibro(@RequestParam Long id) {
        libroRepository.deleteById(id);
        return "redirect:/libros";
    }
}


