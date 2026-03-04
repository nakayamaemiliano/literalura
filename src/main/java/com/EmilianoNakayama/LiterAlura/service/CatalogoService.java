package com.EmilianoNakayama.LiterAlura.service;

import com.EmilianoNakayama.LiterAlura.dto.DatosAutor;
import com.EmilianoNakayama.LiterAlura.dto.DatosLibro;
import com.EmilianoNakayama.LiterAlura.dto.DatosRespuesta;
import com.EmilianoNakayama.LiterAlura.model.Autor;
import com.EmilianoNakayama.LiterAlura.model.Libro;
import com.EmilianoNakayama.LiterAlura.repository.AutorRepository;
import com.EmilianoNakayama.LiterAlura.repository.LibroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatalogoService {
    private static final String URL_BASE = "https://gutendex.com/books/?search=";

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final ConsumoApi consumoApi;
    private final ConvierteDatos convierteDatos;

    public CatalogoService(LibroRepository libroRepository,
                           AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;

        // Si ConsumoApi y ConvierteDatos no tienen @Component, se instancian acá
        this.consumoApi = new ConsumoApi();
        this.convierteDatos = new ConvierteDatos();
    }

    // =========================================================
    // 1) Buscar libro por título (API + guardar en DB)
    // =========================================================
    @Transactional
    public void buscarLibroPorTitulo(String tituloBuscado) {
        if (tituloBuscado == null || tituloBuscado.isBlank()) {
            System.out.println("Debes ingresar un título válido.");
            return;
        }

        try {
            String tituloCodificado = URLEncoder.encode(tituloBuscado.trim(), StandardCharsets.UTF_8);
            String json = consumoApi.obtenerDatos(URL_BASE + tituloCodificado);

            DatosRespuesta respuesta = convierteDatos.obtenerDatos(json, DatosRespuesta.class);

            if (respuesta == null || respuesta.libros() == null || respuesta.libros().isEmpty()) {
                System.out.println("No se encontraron libros con ese título.");
                return;
            }

            // MVP: tomar el primer resultado
            DatosLibro datosLibro = respuesta.libros().get(0);

            // Evitar duplicados por id de Gutendex
            Optional<Libro> existente = libroRepository.findByGutendexId(datosLibro.id());
            if (existente.isPresent()) {
                System.out.println("El libro ya está registrado:");
                imprimirLibro(existente.get());
                return;
            }

            // Crear libro desde DTO
            Libro libro = new Libro(datosLibro);

            // Asociar autores (reutilizando si ya existen)
            if (datosLibro.autores() != null && !datosLibro.autores().isEmpty()) {
                for (DatosAutor datosAutor : datosLibro.autores()) {
                    if (datosAutor == null || datosAutor.nombre() == null || datosAutor.nombre().isBlank()) {
                        continue;
                    }

                    Autor autor = autorRepository.findByNombreIgnoreCase(datosAutor.nombre().trim())
                            .orElseGet(() -> new Autor(datosAutor));

                    libro.getAutores().add(autor);
                }
            }

            Libro guardado = libroRepository.save(libro);

            System.out.println("\nLibro guardado correctamente:");
            System.out.println("------------------------------------");
            imprimirLibro(guardado);

        } catch (Exception e) {
            System.out.println("Error al buscar/guardar libro: " + e.getMessage());
        }
    }

    // =========================================================
    // 2) Listar libros registrados
    // =========================================================
    @Transactional
    public void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("\n=== LIBROS REGISTRADOS ===");
        libros.forEach(libro -> {
            System.out.println("------------------------------------");
            imprimirLibro(libro);
        });
    }

    // =========================================================
    // 3) Listar autores registrados
    // =========================================================
    @Transactional
    public void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
            return;
        }

        System.out.println("\n=== AUTORES REGISTRADOS ===");
        autores.forEach(autor -> {
            System.out.println("------------------------------------");
            imprimirAutor(autor);
        });
    }

    // =========================================================
    // 4) Listar autores vivos en un año
    // =========================================================
    @Transactional
    public void listarAutoresVivosEnAnio(int anio) {
        List<Autor> autores = autorRepository.autoresVivosEn(anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio);
            return;
        }

        System.out.println("\n=== AUTORES VIVOS EN " + anio + " ===");
        autores.forEach(autor -> {
            System.out.println("------------------------------------");
            imprimirAutor(autor);
        });
    }

    // =========================================================
    // 5) Listar libros por idioma
    // =========================================================
    @Transactional
    public void listarLibrosPorIdioma(String idiomaIngresado) {
        String idioma = normalizarIdioma(idiomaIngresado);

        if (idioma == null) {
            System.out.println("Idioma inválido. Usa por ejemplo: es, en, fr, pt.");
            return;
        }

        List<Libro> libros = libroRepository.findByIdiomaIgnoreCase(idioma);

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en idioma: " + idioma);
            return;
        }

        System.out.println("\n=== LIBROS EN IDIOMA: " + idioma + " ===");
        libros.forEach(libro -> {
            System.out.println("------------------------------------");
            imprimirLibro(libro);
        });
    }


    private void imprimirLibro(Libro libro) {
        String autores = (libro.getAutores() == null || libro.getAutores().isEmpty())
                ? "Sin autores"
                : libro.getAutores().stream()
                .map(Autor::getNombre)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));

        System.out.println("Título       : " + valorSeguro(libro.getTitulo()));
        System.out.println("Autores      : " + autores);
        System.out.println("Idioma       : " + valorSeguro(libro.getIdioma()));
        System.out.println("Descargas    : " + (libro.getNumeroDescargas() != null ? libro.getNumeroDescargas().intValue() : 0));
        System.out.println("Gutendex ID  : " + (libro.getGutendexId() != null ? libro.getGutendexId() : "-"));
    }

    private void imprimirAutor(Autor autor) {
        String libros = (autor.getLibros() == null || autor.getLibros().isEmpty())
                ? "Sin libros asociados"
                : autor.getLibros().stream()
                .map(Libro::getTitulo)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));

        System.out.println("Nombre             : " + valorSeguro(autor.getNombre()));
        System.out.println("Año nacimiento     : " + (autor.getAnioNacimiento() != null ? autor.getAnioNacimiento() : "desconocido"));
        System.out.println("Año fallecimiento  : " + (autor.getAnioFallecimiento() != null ? autor.getAnioFallecimiento() : "N/A"));
        System.out.println("Libros             : " + libros);
    }

    private String valorSeguro(String valor) {
        return (valor == null || valor.isBlank()) ? "desconocido" : valor;
    }

    private String normalizarIdioma(String entrada) {
        if (entrada == null || entrada.isBlank()) return null;

        String valor = entrada.trim().toLowerCase();

        return switch (valor) {
            case "es", "español", "espanol", "spanish" -> "es";
            case "en", "ingles", "inglés", "english" -> "en";
            case "fr", "frances", "francés", "french" -> "fr";
            case "pt", "portugues", "portugués", "portuguese" -> "pt";
            case "de", "aleman", "alemán", "german" -> "de";
            case "it", "italiano", "italian" -> "it";
            default -> null;
        };
    }
}
