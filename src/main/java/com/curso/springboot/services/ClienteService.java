package com.curso.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.curso.springboot.domain.Categoria;
import com.curso.springboot.domain.Cidade;
import com.curso.springboot.domain.Cliente;
import com.curso.springboot.domain.Endereco;
import com.curso.springboot.domain.enums.Perfil;
import com.curso.springboot.domain.enums.TipoCliente;
import com.curso.springboot.dto.ClienteDTO;
import com.curso.springboot.dto.ClienteNewDTO;
import com.curso.springboot.repositories.CidadeRepository;
import com.curso.springboot.repositories.ClienteRepository;
import com.curso.springboot.repositories.EnderecoRepository;
import com.curso.springboot.security.UserSS;
import com.curso.springboot.services.exception.AuthorizationException;
import com.curso.springboot.services.exception.DataIntegrityException;
import com.curso.springboot.services.exception.ObjectNotFoundException;



@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	public Cliente find(Integer id) {
		
		UserSS user = null;
		
		user = UserService.authenticated();
		
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId()) ) {
			throw new AuthorizationException("Acesso Negado!");
		}
		
		Optional<Cliente> obj = this.repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(" Cliente não encontrado! ID: " + id +", Tipo "+ Cliente.class.getName()));
		
	}
	
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		
		obj =  repo.save(obj);
		
		this.enderecoRepository.saveAll(obj.getEnderecos());
		
		return obj;
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
	
	public Cliente findByEmail(String email) {
		Cliente cliente = null;
		UserSS user = null;
		
		user = UserService.authenticated();
		
		if(user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername()))
			throw new AuthorizationException(" Acesso Negado!");
		
		cliente = this.repo.findByEmail(email);
		
		if(cliente == null)
			throw new ObjectNotFoundException("Cliente não encontrado! id:"+ user.getId());
		
		return cliente;
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
		c = new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
		return c;
	}
	
	public Cliente fromDTO(ClienteNewDTO objetoDTO) {
		Cliente cli = null;
		
		Cidade cid  = null;
		
		Endereco end = null;
		
		cli =  new Cliente(null, objetoDTO.getNome(), objetoDTO.getEmail(),objetoDTO.getCpfOuCnpj(), TipoCliente.toEnum(objetoDTO.getTipo()),this.bCryptPasswordEncoder.encode(objetoDTO.getSenha()));
		
		cid  = this.findById(objetoDTO.getCidadeId());
		
		end = new Endereco(null, objetoDTO.getLogradouro(), objetoDTO.getNumero(),objetoDTO.getComplemento(), null, objetoDTO.getCep(), cli, cid);
		
		cli.getEnderecos().add(end);
		
		cli.getTelefones().add(objetoDTO.getTelefone1());
		
		if(objetoDTO.getTelefone2() != null) 
			cli.getTelefones().add(objetoDTO.getTelefone2());
		
		if(objetoDTO.getTelefone3() != null) 
			cli.getTelefones().add(objetoDTO.getTelefone3());
		
		
		return cli;
		
	}
	
	private Cidade findById(Integer cidadeId) {
		
		Optional<Cidade> cidade  = this.cidadeRepository.findById(cidadeId);
		
		
		return cidade.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + cidadeId +", Tipo "+ Categoria.class.getName()));
	}

}
