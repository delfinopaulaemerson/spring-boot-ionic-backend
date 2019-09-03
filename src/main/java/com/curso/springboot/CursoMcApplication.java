package com.curso.springboot;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.curso.springboot.domain.Categoria;
import com.curso.springboot.repositories.CategoriaRepository;

@SpringBootApplication
public class CursoMcApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CursoMcApplication.class, args);
	}

	/**
	 * metodo implemantado pelo comandlineruner 
	 * para inicializacao do springboot e execucao
	 */
	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1  = new Categoria(null, "informatica");
		Categoria cat2  = new Categoria(null, "escritorio");
		
		this.categoriaRepository.saveAll(Arrays.asList(cat1,cat2));
		
	}

}
