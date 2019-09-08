package com.curso.springboot.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.curso.springboot.domain.Cliente;
import com.curso.springboot.repositories.ClienteRepository;
import com.curso.springboot.services.exception.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private PrepareAndSendEmailService prepareAndSendEmailService;
	
	private Random rand = new Random();
	
	
	
	public void sendNewPassword(String email) {
		Cliente cliente = null;
		String newPass = null;
		
		cliente = this.clienteRepository.findByEmail(email);
		
		if(cliente == null)
			throw new ObjectNotFoundException("Cliente não encontrado!");
		
		newPass = newPassword();
		
		cliente.setSenha(this.bCryptPasswordEncoder.encode(newPass));
		
		this.clienteRepository.save(cliente);
		
		this.prepareAndSendEmailService.sendNewPasswordEmail(cliente, newPass);
	}



	private String newPassword() {
		char[] vet = new char[10];
		
		for(int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}


	private char randomChar() {
		int opt = this.rand.nextInt(3);
		if(opt == 0) {
			return (char) (this.rand.nextInt(10) + 48);
		}else if(opt == 1) {
			return (char) (this.rand.nextInt(26) + 65);
		}else {
			return (char) (this.rand.nextInt(10) + 97);
		}
	}

}
