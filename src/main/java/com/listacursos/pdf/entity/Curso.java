package com.listacursos.pdf.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128,nullable = false)//maximo de caracteres sera 128 y no puede estar vacio
    private String titulo;
    @Column(length = 256)
    private String descripcion;
    @Column(nullable = false)
    private  int nivel;
    @Column(name = "estado_publicacion")
    private boolean isPublicado;
}
