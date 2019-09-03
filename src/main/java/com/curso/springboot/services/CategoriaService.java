package com.curso.springboot.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.springboot.domain.Categoria;
import com.curso.springboot.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	
	public Categoria buscar(Integer id) throws Exception {
		
		Optional<Categoria> obj = this.repo.findById(id);
		
		return obj.orElse(null);
		
	}

}
