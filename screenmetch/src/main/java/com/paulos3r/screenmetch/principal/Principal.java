package com.paulos3r.screenmetch.principal;

import com.paulos3r.screenmetch.model.DadosEpisodios;
import com.paulos3r.screenmetch.model.DadosSerie;
import com.paulos3r.screenmetch.model.DadosTemporadas;
import com.paulos3r.screenmetch.service.ConsimoAPI;
import com.paulos3r.screenmetch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
//
//    for (int i = 0; i < dados.totalTemporadas(); i++) {
//      List<DadosEpisodios> episodios = temporadas.get(i).episodios();
//      for (int j = 0; j < episodios.size(); j++) {
//        System.out.println(episodios.get(j).titulo());
//      }
//    }
    //lambda ->  funções anônimas
    temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

  }
}
