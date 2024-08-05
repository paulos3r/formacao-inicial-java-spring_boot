package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

  Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

  List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor,Double avaliacao);

  List<Serie> findTop5ByOrderByAvaliacaoDesc();

  List<Serie> findByGenero(Categoria categoria);

  // List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int totalTemporadas, double avaliacao);

  // List<Serie> findTop5ByOrderByEpisodiosDataLancamentoDesc();

  /**
   * jpql = Java Persistence Query Language
   */
  @Query("select s1_0 from Serie s1_0 where s1_0.totalTemporadas<=:totalTemporadas and s1_0.avaliacao>=:avaliacao")
  List<Serie> seriePorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);

  @Query(
          "select e1_0 " +
          "from Serie s1_0 " +
          "join s1_0.episodios e1_0 " +
          "where e1_0.titulo ilike %:trechoEpisodio%"
  )
  List<Episodio> episodioPorTrecho(String trechoEpisodio);

  @Query(
          "select e1_0 " +
                  "from Serie s1_0 " +
                  "join s1_0.episodios e1_0 " +
                  "where s1_0 = :serie order by e1_0.avaliacao desc LIMIT 5"
  )
  List<Episodio> topEpsodiosPorSerie(Serie serie);

  @Query(
          "select e1_0 " +
                  "from Serie s1_0 " +
                  "join s1_0.episodios e1_0 " +
                  "where s1_0 = :serie and year( e1_0.dataLancamento )>= :anoLancamento"
  )
  List<Episodio> episodioPorSerieEAno(Serie serie, int anoLancamento);

  @Query("SELECT s FROM Serie s " +
          "JOIN s.episodios e " +
          "GROUP BY s " +
          "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
  List<Serie> encontrarEpisodiosMaisRecentes();  // retorna a top 5 também devido a um ajuste no findTop5ByOrderByEpisodiosDataLancamentoDesc();

  @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
  List<Episodio> obterEpisodiosPorTemporadas(Long id, Long numero);
}