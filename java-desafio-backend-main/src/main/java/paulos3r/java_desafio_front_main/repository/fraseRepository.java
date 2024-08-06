package paulos3r.java_desafio_front_main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import paulos3r.java_desafio_front_main.model.Frase;

public interface fraseRepository extends JpaRepository<Frase, Long> {

  @Query("SELECT f FROM Frase f order by function('RANDOM') LIMIT 1")
  Frase buscaFraseAleatorio();

}
