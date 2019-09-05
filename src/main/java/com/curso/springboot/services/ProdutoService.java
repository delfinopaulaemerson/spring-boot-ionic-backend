package com.curso.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.curso.springboot.domain.Categoria;
import com.curso.springboot.domain.Produto;
import com.curso.springboot.repositories.CategoriaRepository;
import com.curso.springboot.repositories.ProdutoRepository;
import com.curso.springboot.services.exception.ObjectNotFoundException;



@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	
	public Produto find(Integer id) {
		
		Optional<Produto> obj = this.repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id +", Tipo "+ Produto.class.getName()));
		
	}
	
	public Page<Produto> search(String nome, List<Integer> ids,Integer page, Integer linesPorPage, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPorPage, Direction.valueOf(direction),orderBy);
		
		List<Categoria> categorias = this.categoriaRepository.findAllById(ids);
		
		return this.repo.findDistinctByNomeContainingAndCategoriasIn(nome,categorias,pageRequest);
	}

	
}
