package com.curso.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.curso.springboot.domain.Cliente;
import com.curso.springboot.dto.ClienteDTO;
import com.curso.springboot.repositories.ClienteRepository;
import com.curso.springboot.services.exception.DataIntegrityException;
import com.curso.springboot.services.exception.ObjectNotFoundException;



@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	
	public Cliente find(Integer id) {
		
		Optional<Cliente> obj = this.repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(" Cliente não encontrado! ID: " + id +", Tipo "+ Cliente.class.getName()));
		
	}
	
	public Cliente update(Cliente obj) {
		//metodo find verifica a existencia do objeto
		Cliente newObj = this.find(obj.getId());
		
		this.updateData(newObj,obj);
		
		//mesmo sem o find o spring data verifica a existencia do id
		//existindo ele atualiza o objeto
		//nao existindo ele cria o objeto
		return this.repo.save(newObj);
	}


	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}

	public void delete(Integer id) {
		//caso nao exista o objeto utiliza a execao criada no metodo find
		this.find(id);
		
		try {
			
			this.repo.deleteById(id);
			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um Cliente pois a entidades relacionadas ");
		}
		
	}


	public List<Cliente> findAll() {
		
		return this.repo.findAll();
	}
	
	/**
	 * Paginacao com spring data
	 * @param page
	 * @param linesPorPage
	 * @param orderBy
	 * @param direction
	 * @return Page<Cliente>
	 */
	public Page<Cliente> findPage(Integer page, Integer linesPorPage, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPorPage, Direction.valueOf(direction),orderBy);
		
		return this.repo.findAll(pageRequest);
	}
	
	/**
	 * convertendo de DTO para objeto
	 * @param objDTO
	 * @return Cliente
	 */
	public Cliente fromDTO(ClienteDTO objDTO) {
		Cliente c = null;
		c = new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
		return c;
	}

	public Cliente insert(Cliente obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
