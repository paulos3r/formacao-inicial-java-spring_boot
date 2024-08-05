package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
  @Autowired
  private SerieRepository repository;

  // baixo aclopamento alta coesao
  public List<SerieDTO> obterTodasAsSeries(){
    return converteDadosSerie( repository.findAll() );
  }

  public List<SerieDTO> obterTop5Series() {
    return converteDadosSerie( repository.findTop5ByOrderByAvaliacaoDesc() );
  }

  public List<SerieDTO> obterLancamentos() {
    return converteDadosSerie(repository.encontrarEpisodiosMaisRecentes());
  }

  public List<SerieDTO> obterSeriesPorCategoria(String nomeGenero) {
    Categoria categoria = Categoria.fromPortugues(nomeGenero);

    return converteDadosSerie( repository.findByGenero(categoria) );
  }

  public SerieDTO obterPorId(Long id) {
    Optional<Serie> serie = repository.findById(id);

    if (serie.isPresent()){
      Serie s = serie.get();
      return new SerieDTO(s.getId(), s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),s.getGenero(),s.getAtores(),s.getPoster(),s.getSinopse());
    }

    return null;
  }

  public List<EpisodioDTO> obterTodasTemporadas(Long id) {
    Optional<Serie> serie = repository.findById(id);

    if (serie.isPresent()){
      Serie s = serie.get();
      return converteDadosEpisodios( s.getEpisodios() );
    }
    return null;
  }

  public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
    return converteDadosEpisodios( repository.obterEpisodiosPorTemporadas(id,numero) );
  }

  private List<SerieDTO> converteDadosSerie(List<Serie> series){
    return series.stream()
            .map(s -> new SerieDTO(s.getId(), s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),s.getGenero(),s.getAtores(),s.getPoster(),s.getSinopse()))
            .collect(Collectors.toList());
  }

  private List<EpisodioDTO> converteDadosEpisodios(List<Episodio> episodios){
    return episodios.stream()
            .map(e->new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio()))
            .collect(Collectors.toList());
  }
}
