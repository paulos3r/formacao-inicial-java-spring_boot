package paulos3r.java_desafio_front_main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paulos3r.java_desafio_front_main.model.Frase;

public interface fraseRepository extends JpaRepository<Frase, Long> {
}
