package com.curso.springboot.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.springboot.domain.Cliente;
import com.curso.springboot.repositories.ClienteRepository;
import com.curso.springboot.services.exception.ObjectNotFoundException;



@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	
	public Cliente find(Integer id) {
		
		Optional<Cliente> obj = this.repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(" Cliente não encontrado! ID: " + id +", Tipo "+ Cliente.class.getName()));
		
	}

}
