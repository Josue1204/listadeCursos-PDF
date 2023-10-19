package com.listacursos.pdf.controller;

import com.listacursos.pdf.entity.Curso;
import com.listacursos.pdf.reports.CursosExporterExcel;
import com.listacursos.pdf.reports.CursosExporterPDF;
import com.listacursos.pdf.repository.CursoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CursoController {
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public String home(){
        return "redirect:/cursos";
    }

    @GetMapping("/cursos")
    public String listarCursos(Model model, @Param("keyword") String keyword, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size){
        try{
            List<Curso> cursos = new ArrayList<>();
            Pageable paging = PageRequest.of(page-1,size);

            Page<Curso> pageCursos = null;

            if(keyword == null){
                pageCursos = cursoRepository.findAll(paging);
            }
            else{
                pageCursos = cursoRepository.findByTituloContainingIgnoreCase(keyword,paging);
                model.addAttribute("keyword",keyword);
            }

            cursos = pageCursos.getContent();

            model.addAttribute("cursos",cursos);

            model.addAttribute("currentPage",pageCursos.getNumber() + 1);
            model.addAttribute("totalItems",pageCursos.getTotalElements());
            model.addAttribute("totalPages",pageCursos.getTotalPages());
            model.addAttribute("pageSize",size);
        }catch (Exception exception){
            model.addAttribute("message",exception.getMessage());
        }

        return "cursos";
    }

    @GetMapping("/cursos/nuevo")
    public String mostrarFormularioGuardarCurso(Model model){
        Curso curso = new Curso();
        curso.setPublicado(true);

        model.addAttribute("curso",curso);
        model.addAttribute("pageTitle","Nuevo curso");

        return "curso_form";
    }

    @PostMapping("/cursos/save")
    public String guardarCurso(Curso curso, RedirectAttributes redirectAttributes){
        try{
            cursoRepository.save(curso);
            redirectAttributes.addFlashAttribute("message","El curso ha sido guardado con éxito");
        }catch (Exception e){
            redirectAttributes.addAttribute("message",e.getMessage());
        }
        return "redirect:/cursos";
    }

    @GetMapping("/cursos/{id}")
    public String editarCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            Curso curso = cursoRepository.findById(id).get();

            model.addAttribute("curso",curso);
            model.addAttribute("pageTitle","Editar curso : " + id);

            return "curso_form";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/cursos";
        }
    }

    @GetMapping("/cursos/delete/{id}")
    public String eliminarCurso(@PathVariable Integer id,Model model,RedirectAttributes redirectAttributes){
        try{
            cursoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message","El curso ha sido eliminado");
        }catch (Exception exception){
            redirectAttributes.addFlashAttribute("message",exception.getMessage());
        }
        return "redirect:/cursos";
    }
    @GetMapping("/export/pdf")
    public void generarReportePDF(HttpServletResponse response)throws IOException{
        response.setContentType("application/pdf");
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime= dateFormat.format(new Date());

        String headerKey="Content-Disposition";
        String headerValue="attachment; filename=cursos"+currentDateTime+".pdf";
        response.setHeader(headerKey,headerValue);

        List<Curso>cursos=cursoRepository.findAll();

        CursosExporterPDF exporterPDF=new CursosExporterPDF(cursos);
        exporterPDF.export(response);
    }
    @GetMapping("/export/excel")
    private void generarReporteExcel(HttpServletResponse response)throws IOException{
        response.setContentType("application/octet-stream");
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime= dateFormat.format(new Date());

        String headerKey="content-Disposition";
        String headerValue="attachment; filename=cursos"+currentDateTime+".xlsx";
        response.setHeader(headerKey,headerValue);

        List<Curso>cursos=cursoRepository.findAll();

        CursosExporterExcel exporterExcel=new CursosExporterExcel(cursos);
        exporterExcel.export(response);
    }
}

