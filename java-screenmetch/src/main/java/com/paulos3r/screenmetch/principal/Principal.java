package com.paulos3r.screenmetch.principal;

import com.paulos3r.screenmetch.model.DadosEpisodios;
import com.paulos3r.screenmetch.model.DadosSerie;
import com.paulos3r.screenmetch.model.DadosTemporadas;
import com.paulos3r.screenmetch.model.Episodios;
import com.paulos3r.screenmetch.service.ConsimoAPI;
import com.paulos3r.screenmetch.service.ConverteDados;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

  private final String ENDERECO = "http://www.omdbapi.com/?t=";
  private final String API_KEY = "&apikey=1801fb5f";

  private ConsimoAPI consimoAPI = new ConsimoAPI();

  private ConverteDados conversor = new ConverteDados();

  private

  Scanner scan = new Scanner(System.in);

  public void exibeMenu(){

    System.out.println("Digite o nome da sua serie: ");
    var nomeSerie = scan.nextLine();
    var json = consimoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
    System.out.println(json);

    DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
    System.out.println(dados);

    List<DadosTemporadas> temporadas = new ArrayList<>();
    for (int i = 1; i <= dados.totalTemporadas(); i++) {
      json = consimoAPI.obterDados( ENDERECO + nomeSerie.replace(" ", "+") + "&season=" +i+ API_KEY );
      DadosTemporadas dadosTemporadas = conversor.obterDados(json, DadosTemporadas.class);
      temporadas.add(dadosTemporadas);
    }
    temporadas.forEach(System.out::println);

    //lambda ->  funções anônimas
    temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

    List<DadosEpisodios> dadosEpisodios = temporadas.stream()
            .flatMap(t -> t.episodios().stream())
            .collect(Collectors.toList());

    System.out.println("#### top 5 episodios");
    dadosEpisodios.stream()
            .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A "))
            .peek(e -> System.out.println("Primeiro Filtro (N/A) "))
            .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
            .peek(e -> System.out.println("Ordenacao "))
            .limit(5)
            .peek(e -> System.out.println("limite "))
            .map(e -> e.titulo().toUpperCase())
            .peek(e -> System.out.println("Mapeamento "))
            .forEach(System.out::println);

    List<Episodios> episodios = temporadas.stream()
            .flatMap(t -> t.episodios().stream()
                    .map(d ->new Episodios(t.numero(),d))
            ).collect(Collectors.toList());

    episodios.forEach(System.out::println);

    System.out.println("Informe um trecho do titulo do episodio: ");

    var trechoTitulo = scan.nextLine();

    Optional<Episodios> episodioBuscado = episodios.stream()
            .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
            .findFirst();

    if (episodioBuscado.isPresent()){
      System.out.println("Episodio encontrado!");
      System.out.println("Temporada " + episodioBuscado.get().getTemporada());
    }else{
      System.out.println("Episodio nao encontrado!");
    }

    System.out.println("Apartir de que anos vocë quer ver os episodios");

    var ano = scan.nextLine();
    scan.nextLine();

    LocalDate dataBusca = LocalDate.of( Integer.parseInt(ano),1,1);

    DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    episodios.stream()
            .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
            .forEach(e -> System.out.println(
                    "Temporada : " + e.getTemporada() +
                    " Episodio: " + e.getTitulo() +
                    " Data Lancamento: " + e.getDataLancamento().format(formatar)
            ));

    Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
            .filter(e->e.getAvaliacao() > 0.0)
            .collect(Collectors.groupingBy(Episodios::getTemporada,
                    Collectors.averagingDouble(Episodios::getAvaliacao)));

    System.out.println(avaliacoesPorTemporada);

    DoubleSummaryStatistics est = episodios.stream()
            .filter(e->e.getAvaliacao() >0.0)
            .collect(Collectors.summarizingDouble(Episodios::getAvaliacao));

    System.out.println( "Média: " + est.getAverage() + " Melhor Episodio: " + est.getMax() +  " Pior episodio: " + est.getMin() );

  }
}