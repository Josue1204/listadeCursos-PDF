package com.listacursos.pdf.controller;

import com.listacursos.pdf.entity.Curso;
import com.listacursos.pdf.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CursoController {
    @Autowired
    CursoRepository cursoRepository;

    @GetMapping
    public String home() {
        return "redirect:/cursos";
    }

    @GetMapping("/cursos")
    public String listarCursos(Model model) {
        List<Curso> cursos = cursoRepository.findAll();
        model.addAttribute("cursos", cursos);
        return "cursos";
    }
    @GetMapping("/cursos/nuevo")//metodo para agregar un nuevo curso
    public String agregarCurso(Model model){
        Curso curso=new Curso();
        curso.setPublicado(true);
        model.addAttribute("curso",curso);
        model.addAttribute("pageTitle","Nuevo Curso");
        return "curso_form";
    }
    @PostMapping("/cursos/save")//metodo para guardar el curso creado
    public String guardarCurso(Curso curso, RedirectAttributes redirectAttributes){
        try {
            cursoRepository.save(curso);
            redirectAttributes.addFlashAttribute("message","El curso ha sido guardado con éxito");
        }catch(Exception e){
          redirectAttributes.addAttribute("message",e.getMessage());
        }
return "redirect:/cursos";
    }

}

