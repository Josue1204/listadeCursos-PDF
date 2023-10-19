package com.listacursos.pdf.repository;

import com.listacursos.pdf.entity.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso,Integer> {
    Page<Curso> findByTituloContainingIgnoreCase(String keyword, Pageable pageable);
}
