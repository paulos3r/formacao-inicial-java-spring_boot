package paulos3r.java_desafio_front_main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import paulos3r.java_desafio_front_main.dto.FraseDTO;
import paulos3r.java_desafio_front_main.service.FraseService;

@RestController
public class FraseController {

  @Autowired
  private FraseService service;

  @GetMapping("/series/frases")
  public FraseDTO obterFraseAleatorio(){
    return service.obterFraseAleatorio();
  }
}
