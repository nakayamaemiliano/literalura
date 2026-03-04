package com.EmilianoNakayama.LiterAlura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosRespuesta(Integer count,
                             String next,
                             String previous,
                             @JsonAlias("results") List<DatosLibro> libros) {
}
