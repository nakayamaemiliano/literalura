package com.EmilianoNakayama.LiterAlura.model;

import com.EmilianoNakayama.LiterAlura.dto.DatosLibro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "libros", uniqueConstraints = {
        @UniqueConstraint(columnNames = "gutendexId")
})
@Setter
@Getter
@AllArgsConstructor
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long gutendexId;
    private String titulo;
    private String idioma;
    private Double numeroDescargas;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "libros_autores",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    public Libro() {}

    public Libro(DatosLibro datos) {
        this.gutendexId = datos.id();
        this.titulo = datos.titulo();
        this.idioma = (datos.idiomas() != null && !datos.idiomas().isEmpty())
                ? datos.idiomas().get(0)
                : "desconocido";
        this.numeroDescargas = datos.numeroDescargas() != null ? datos.numeroDescargas() : 0.0;
    }

}
