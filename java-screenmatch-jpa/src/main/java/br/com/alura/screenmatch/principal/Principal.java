package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

  private Scanner leitura = new Scanner(System.in);
  private ConsumoApi consumo = new ConsumoApi();
  private ConverteDados conversor = new ConverteDados();
  private final String ENDERECO = "https://www.omdbapi.com/?t=";
  private final String API_KEY = "&apikey=1801fb5f";
  private List<DadosSerie> dadosSeries = new ArrayList<>();

  private SerieRepository repository;

  private List<Serie> series = new ArrayList<>();

  public Principal(SerieRepository repository) {
    this.repository = repository;
  }

  public void exibeMenu() {
    var opcao = -1;
    while (opcao != 0) {
      var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar series listadas
                    4 - Buscar serie por tipo
                    5 - Buscar serie por ator
                    6 - Top 5 series
                    7 - Buscar por categoria
                    0 - Sair
                    """;
      System.out.println(menu);
      opcao = leitura.nextInt();
      leitura.nextLine();

      switch (opcao) {
        case 1:
          buscarSerieWeb();
          break;
        case 2:
          buscarEpisodioPorSerie();
          break;
        case 3:
          listarSeriesBuscadas();
          break;
        case 4:
          buscarSeriePorTipo();
          break;
        case 5:
          buscarSeriesPorAtor();
          break;
        case 6:
          buscarTopSeries();
          break;
        case 7:
          buscarSeriesCategoria();
          break;
        case 0:
          System.out.println("Saindo...");
          break;
        default:
          System.out.println("Opção inválida");
      }
    }
  }

  private void buscarSerieWeb() {
    DadosSerie dados = getDadosSerie();
    // dadosSeries.add(dados);
    Serie serie = new Serie(dados);

    repository.save(serie);
    System.out.println(dados);
  }

  private DadosSerie getDadosSerie() {
    System.out.println("Digite o nome da série para busca");
    var nomeSerie = leitura.nextLine();
    var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
    DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
    return dados;
  }

  private void listarSeriesBuscadas(){
    this.series = repository.findAll();

    series.stream()
            .sorted(Comparator.comparing(Serie::getGenero))
            .forEach(System.out::println);

    dadosSeries.forEach(System.out::println);
  }

  private void buscarEpisodioPorSerie(){

    listarSeriesBuscadas();

    System.out.println("Escolha uma serie pelo nome: ");
    var nomeSerie = leitura.nextLine();

    Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

    if(serie.isPresent()){
      var serieEncontrada = serie.get();
      List<DadosTemporada> temporadas = new ArrayList<>();

      for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
        var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
        DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
        temporadas.add(dadosTemporada);
      }
      temporadas.forEach(System.out::println);

      List<Episodio> episodios =  temporadas.stream()
              .flatMap(d->d.episodios().stream()
                      .map(e->new Episodio(d.numero(),e)))
              .collect(Collectors.toList());

      serieEncontrada.setEpisodios(episodios);

      repository.save(serieEncontrada);
    }else {
      System.out.println("Serie não encontrada!");
    }

  }
  private void buscarSeriePorTipo() {

    System.out.println("Escolha uma serie pelo nome: ");
    var nomeSerie = leitura.nextLine();

    Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

    if (serieBuscada.isPresent()){
      System.out.println("Dados da serie: " + serieBuscada.get());
    }else{
      System.out.println("Serie não encontrada!");
    }
  }
  private void buscarSeriesPorAtor() {
    System.out.println("Escolha uma nome do ator da serie: ");
    var nomeAtor = leitura.nextLine();

    System.out.println("Escolha uma avaliacao que deseja: ");
    var avaliacao = leitura.nextDouble();

    List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor,avaliacao);
    System.out.println("Serie em que " + nomeAtor + " trabalhou: ");
    seriesEncontradas.forEach(s-> System.out.println(s.getTitulo() + " Avaliacao: " + s.getAvaliacao()));
    System.out.println(seriesEncontradas);
  }

  private void buscarTopSeries() {
    List<Serie> serieTop = repository.findTop5ByOrderByAvaliacaoDesc();

    serieTop.forEach(s->
            System.out.println(s.getTitulo() + " Avaliações: " + s.getAvaliacao()));
  }

  private void buscarSeriesCategoria() {
    System.out.println("Deseja buscar series de que categoria ou genero: ");
    var nomeGenero = leitura.nextLine().toUpperCase();
    Categoria categoria = Categoria.fromPortugues(nomeGenero);
    List<Serie> seriesPorCategoria = repository.findByGenero(categoria);

    System.out.println("Series da categoria" + categoria);

    seriesPorCategoria.forEach(System.out::println);
  }
}