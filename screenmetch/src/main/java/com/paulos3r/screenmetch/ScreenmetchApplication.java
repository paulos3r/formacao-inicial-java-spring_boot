package com.paulos3r.screenmetch;

import com.paulos3r.screenmetch.model.DadosSerie;
import com.paulos3r.screenmetch.service.ConsimoAPI;
import com.paulos3r.screenmetch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmetchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmetchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		ConsimoAPI consimoAPI = new ConsimoAPI();

		var json = consimoAPI.obterDados("http://www.omdbapi.com/?t=gilmore%20girls&apikey=1801fb5f");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();

		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
