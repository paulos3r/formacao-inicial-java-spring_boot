package com.paulos3r.screenmetch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodios {
  private int temporada;
  private String titulo;
  private Integer numero;
  private Double avaliacao;
  private LocalDate dataLancamento;

  public Episodios(Integer numeroTempoada, DadosEpisodios dadosEpisodios) {
    this.temporada = numeroTempoada;
    this.titulo = dadosEpisodios.titulo();
    this.numero = dadosEpisodios.episodio();
    try {
      this.avaliacao = Double.valueOf( dadosEpisodios.avaliacao() );
    }catch (NumberFormatException e){
      this.avaliacao = 0.0;
    }
    try {
      this.dataLancamento = LocalDate.parse( dadosEpisodios.dataDeLancamento() );
    }catch (DateTimeParseException e){
      this.dataLancamento = null;
    }
  }
  public int getTemporada() {
    return temporada;
  }

  public void setTemporada(int temporada) {
    this.temporada = temporada;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Integer getNumero() {
    return numero;
  }

  public void setNumero(Integer numero) {
    this.numero = numero;
  }

  public Double getAvaliacao() {
    return avaliacao;
  }

  public void setAvaliacao(Double avaliacao) {
    this.avaliacao = avaliacao;
  }

  public LocalDate getDataLancamento() {
    return dataLancamento;
  }

  public void setDataLancamento(LocalDate dataLancamento) {
    this.dataLancamento = dataLancamento;
  }

  @Override
  public String toString() {
    return "temporada=" + temporada +
            ", titulo='" + titulo + '\'' +
            ", numero=" + numero +
            ", avaliacao=" + avaliacao +
            ", dataLancamento=" + dataLancamento ;
  }
}
