package com.curso.springboot.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.springboot.domain.Pedido;
import com.curso.springboot.repositories.PedidoRepository;
import com.curso.springboot.services.exception.ObjectNotFoundException;



@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	
	public Pedido find(Integer id){
		
		Optional<Pedido> obj = this.repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id +", Tipo "+ Pedido.class.getName()));
		
	}

}
