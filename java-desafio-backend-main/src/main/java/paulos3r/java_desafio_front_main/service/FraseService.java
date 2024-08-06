package paulos3r.java_desafio_front_main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paulos3r.java_desafio_front_main.dto.FraseDTO;
import paulos3r.java_desafio_front_main.model.Frase;
import paulos3r.java_desafio_front_main.repository.fraseRepository;

@Service
public class FraseService {

  @Autowired
  private fraseRepository repository;

  public FraseDTO obterFraseAleatorio() {
    Frase frase = repository.buscaFraseAleatorio();
    return new FraseDTO(frase.getTitulo(), frase.getFrase(), frase.getPersonagem(), frase.getPoster());
  }

}
