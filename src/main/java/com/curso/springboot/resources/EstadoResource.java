package com.curso.springboot.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.curso.springboot.domain.Cidade;
import com.curso.springboot.domain.Estado;
import com.curso.springboot.dto.CidadeDTO;
import com.curso.springboot.dto.EstadoDTO;
import com.curso.springboot.services.CidadeService;
import com.curso.springboot.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {
	
	@Autowired
	private EstadoService service;
	
	@Autowired
	private CidadeService CidadeService;
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Estado> find(@PathVariable Integer id) {
		Estado obj = null;
			
		obj = this.service.find(id);
		
		return ResponseEntity.ok(obj);
	}
	
	@RequestMapping( method = RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> find() {
		List<Estado> list = new ArrayList<Estado>();
			
		list = this.service.findByEstado();
		
		List<EstadoDTO> listDTO = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok(listDTO);
	}
	
	@RequestMapping(value = "/{estadoId}/cidades", method = RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> findByCidades(@PathVariable Integer estadoId) {
		List<Cidade> list = new ArrayList<Cidade>();
			
		list = this.CidadeService.findByEstado(estadoId);
		
		List<CidadeDTO> listDTO = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok(listDTO);
	}
	
	
	
	
	
	

}
