package com.curso.springboot.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.curso.springboot.domain.Cliente;
import com.curso.springboot.domain.enums.TipoCliente;
import com.curso.springboot.dto.ClienteDTO;
import com.curso.springboot.repositories.ClienteRepository;
import com.curso.springboot.resources.exception.FieldMessage;
import com.curso.springboot.services.validation.utils.BR;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public void initialize(ClienteUpdate ann) {
		
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		Cliente aux = null;
		Integer uriId = 0;
		
		//recupera o id da URI ou do enpoint rest
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		
		uriId = Integer.parseInt(map.get("id"));
		
		 aux = this.repo.findByEmail(objDto.getEmail());
		 
		 if(aux != null && !aux.getId().equals(uriId));
			 list.add(new FieldMessage("email", "email já existente!"));
		
		
		
		// inclua os testes aqui, inserindo erros na lista
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}
}