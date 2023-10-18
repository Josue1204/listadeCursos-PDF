package com.listacursos.pdf.reports;

import com.listacursos.pdf.entity.Curso;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CursosExporterPDF {
    private List<Curso>listaCursos;
    public CursosExporterPDF(List<Curso>listaCursos){
        this.listaCursos=listaCursos;
    }
    private void writeTableHeader(PdfPTable table){
        PdfPCell cell=new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
    cell.setPadding(5);

    Font font= FontFactory.getFont(FontFactory.HELVETICA);
    font.setColor(Color.WHITE);

    cell.setPhrase(new Phrase("ID",font));
    table.addCell(cell);
        cell.setPhrase(new Phrase("Titulo",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Descripción",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Nivel",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Publicado",font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        for (Curso curso:listaCursos){
            table.addCell(String.valueOf(curso.getId()));
            table.addCell(curso.getTitulo());
            table.addCell(curso.getDescripcion());
            table.addCell(String.valueOf(curso.getNivel()));
            table.addCell(String.valueOf(curso.isPublicado()));
        }
    }
    public void export(HttpServletResponse response)throws IOException{
        Document document=new Document(PageSize.LETTER);
        PdfWriter.getInstance(document,response.getOutputStream());
    document.open();
    Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    font.setSize(18);
    font.setColor(Color.BLUE);

    Paragraph p=new Paragraph("Lista de Cursos",font);
    p.setAlignment(Paragraph.ALIGN_CENTER);
    document.add(p);

    PdfPTable table=new PdfPTable(5);
    table.setWidthPercentage(100f);
    table.setWidths(new float[]{1.3f,3.5f,3.5f,2.0f,1.5f});
    table.setSpacingBefore(10);

    writeTableHeader(table);
    writeTableData(table);

    document.add(table);
    document.close();
    }
}
