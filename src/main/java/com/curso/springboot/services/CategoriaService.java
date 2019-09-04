package com.curso.springboot.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.springboot.domain.Categoria;
import com.curso.springboot.repositories.CategoriaRepository;
import com.curso.springboot.services.exception.ObjectNotFoundException;



@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	
	public Categoria find(Integer id) throws Exception {
		
		Optional<Categoria> obj = this.repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id +", Tipo "+ Categoria.class.getName()));
		
	}


	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}

}
