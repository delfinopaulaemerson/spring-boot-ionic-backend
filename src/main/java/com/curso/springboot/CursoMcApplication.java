package com.curso.springboot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.curso.springboot.domain.Categoria;
import com.curso.springboot.domain.Produto;
import com.curso.springboot.repositories.CategoriaRepository;
import com.curso.springboot.repositories.ProdutoRepository;

@SpringBootApplication
public class CursoMcApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
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
		
		Produto p1 = new Produto(null, "computador", 2000.00);
		Produto p2 = new Produto(null, "impressora", 800.00);
		Produto p3 = new Produto(null, "mause", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		
		this.categoriaRepository.saveAll(Arrays.asList(cat1,cat2));
		
		this.produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
	}

}
