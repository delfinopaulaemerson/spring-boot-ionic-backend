package com.curso.springboot.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.curso.springboot.domain.Cliente;
import com.curso.springboot.domain.enums.TipoCliente;
import com.curso.springboot.dto.ClienteNewDTO;
import com.curso.springboot.repositories.ClienteRepository;
import com.curso.springboot.resources.exception.FieldMessage;
import com.curso.springboot.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
		
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		Cliente aux = null;
		
		if(objDto.getTipo() == TipoCliente.PESSOAFISICA.getCod() && !BR.isValidCPF(objDto.getCpfOuCnpj())) 
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido!"));
		
	
		if(objDto.getTipo() == TipoCliente.PESSOAJURIDICA.getCod() && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) 
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido!"));
		
		
		 aux = this.repo.findByEmail(objDto.getEmail());
		 
		 if(aux != null)
			 list.add(new FieldMessage("email", "email já existente!"));
		
		
		
		// inclua os testes aqui, inserindo erros na lista
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}
}