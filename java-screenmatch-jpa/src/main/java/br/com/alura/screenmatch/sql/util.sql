-- zerar a chave primaria para manter a ordem
SET  @num := 0;
UPDATE episodios SET id = @num := (@num+1);
ALTER TABLE episodios AUTO_INCREMENT = 1;