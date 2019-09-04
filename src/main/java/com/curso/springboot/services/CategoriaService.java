package com.curso.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.curso.springboot.domain.Categoria;
import com.curso.springboot.repositories.CategoriaRepository;
import com.curso.springboot.services.exception.DataIntegrityException;
import com.curso.springboot.services.exception.ObjectNotFoundException;



@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	
	public Categoria find(Integer id) {
		
		Optional<Categoria> obj = this.repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id +", Tipo "+ Categoria.class.getName()));
		
	}


	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}


	public Categoria update(Categoria obj) {
		//metodo find verifica a existencia do objeto
		this.find(obj.getId());
		
		//mesmo sem o find o spring data verifica a existencia do id
		//existindo ele atualiza o objeto
		//nao existindo ele cria o objeto
		return this.repo.save(obj);
	}


	public void delete(Integer id) {
		//caso nao exista o objeto utiliza a execao criada no metodo find
		this.find(id);
		
		try {
			
			this.repo.deleteById(id);
			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos!");
		}
		
	}


	public List<Categoria> findAll() {
		
		return this.repo.findAll();
	}


	


	
}
