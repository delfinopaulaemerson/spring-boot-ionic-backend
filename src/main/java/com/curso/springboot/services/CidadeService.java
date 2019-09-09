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
import com.curso.springboot.domain.Cidade;
import com.curso.springboot.domain.Estado;
import com.curso.springboot.dto.CategoriaDTO;
import com.curso.springboot.repositories.CategoriaRepository;
import com.curso.springboot.repositories.CidadeRepository;
import com.curso.springboot.repositories.EstadoRepository;
import com.curso.springboot.services.exception.DataIntegrityException;
import com.curso.springboot.services.exception.ObjectNotFoundException;



@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repo;
	
	
	public Cidade find(Integer id) {
		
		Optional<Cidade> obj = this.repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id +", Tipo "+ Cidade.class.getName()));
		
	}
	
	public List<Cidade> findByEstado(Integer estadoId){
		List<Cidade> objs = new ArrayList<Cidade>();
		
		objs = this.repo.findCidades(estadoId);
		
		return objs;
	}


	
}
