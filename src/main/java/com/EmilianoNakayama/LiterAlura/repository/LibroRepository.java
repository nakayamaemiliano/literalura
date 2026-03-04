package com.EmilianoNakayama.LiterAlura.repository;

import com.EmilianoNakayama.LiterAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByGutendexId(Long gutendexId);
    List<Libro> findByIdiomaIgnoreCase(String idioma);
    List<Libro> findTop10ByOrderByNumeroDescargasDesc();
}
