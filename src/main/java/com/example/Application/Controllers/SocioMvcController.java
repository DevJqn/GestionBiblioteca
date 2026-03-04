package com.example.Application.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Application.Models.Socio;
import com.example.Application.Repository.SocioRepository;

@Controller
@RequestMapping("/socios")
public class SocioMvcController {

    private final SocioRepository socioRepository;

    public SocioMvcController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @GetMapping
    public String listarSocios(Model model) {
        model.addAttribute("socios", socioRepository.findAll());
        return "socios_list";
    }

    @GetMapping("/new")
    public String nuevoSocio(Model model) {
        model.addAttribute("socio", new Socio());
        return "socios_new";
    }

    @PostMapping
    public String guardarSocio(@ModelAttribute Socio socio) {
        // ⭐ GENERAR numeroSocio automáticamente si está vacío
        if (socio.getNumeroSocio() == null || socio.getNumeroSocio().isEmpty()) {
            // Generar número de socio único (ej: SOC001, SOC002...)
            long count = socioRepository.count();
            String numeroSocio = String.format("SOC%03d", count + 1);
            socio.setNumeroSocio(numeroSocio);
        }
        
        socioRepository.save(socio);
        return "redirect:/socios";
    }


    @GetMapping("/edit")
    public String editarSocio(@RequestParam Long id, Model model) {
        model.addAttribute("socio", socioRepository.findById(id).orElseThrow());
        return "socios_new";
    }

    @PostMapping("/delete")
    public String borrarSocio(@RequestParam Long id) {
        socioRepository.deleteById(id);
        return "redirect:/socios";
    }
}


