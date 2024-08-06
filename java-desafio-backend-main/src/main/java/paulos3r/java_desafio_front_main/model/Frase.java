package paulos3r.java_desafio_front_main.model;

import jakarta.persistence.*;

@Entity
@Table(name="frases")
public class Frase {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String titulo;
  private String personagem;
  private String poster;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getPersonagem() {
    return personagem;
  }

  public void setPersonagem(String personagem) {
    this.personagem = personagem;
  }

  public String getPoster() {
    return poster;
  }

  public void setPoster(String poster) {
    this.poster = poster;
  }

  @Override
  public String toString() {
    return
            "id=" + id +
            ", titulo='" + titulo + '\'' +
            ", personagem='" + personagem + '\'' +
            ", poster='" + poster + '\'';
  }
}
