package com.EmilianoNakayama.LiterAlura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro (   Long id,
                             @JsonAlias("title") String titulo,
                             @JsonAlias("languages") List<String> idiomas,
                             @JsonAlias("download_count") Double numeroDescargas,
                             @JsonAlias("authors") List<DatosAutor> autores,
                             @JsonAlias("formats") Map<String, String> formatos){
}
