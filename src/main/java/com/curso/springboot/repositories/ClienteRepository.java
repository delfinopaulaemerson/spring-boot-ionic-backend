package com.curso.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.curso.springboot.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	//so em declara a sintaxe findByEmail
	//o spring data na reconhece que a busca sera pelo email
	Cliente findByEmail(String email);

}
