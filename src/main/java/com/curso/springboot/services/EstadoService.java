package com.curso.springboot.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.curso.springboot.domain.Categoria;
import com.curso.springboot.domain.Estado;
import com.curso.springboot.dto.CategoriaDTO;
import com.curso.springboot.repositories.CategoriaRepository;
import com.curso.springboot.repositories.EstadoRepository;
import com.curso.springboot.services.exception.DataIntegrityException;
import com.curso.springboot.services.exception.ObjectNotFoundException;



@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repo;
	
	
	public Estado find(Integer id) {
		
		Optional<Estado> obj = this.repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id +", Tipo "+ Estado.class.getName()));
		
	}
	
	public List<Estado> findByEstado(){
		List<Estado> objs = new ArrayList<Estado>();
		
		objs = this.repo.findAllByOrderByNome();
		
		return objs;
	}


	
}
