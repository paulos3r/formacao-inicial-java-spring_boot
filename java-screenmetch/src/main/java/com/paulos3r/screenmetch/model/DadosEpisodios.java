package com.paulos3r.screenmetch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodios(@JsonAlias("Title") String titulo,
                             @JsonAlias("Episode") Integer episodio,
                             @JsonAlias("imdbRating") String avaliacao,
                             @JsonAlias("Released") String dataDeLancamento) {
}