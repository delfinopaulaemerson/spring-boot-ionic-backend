package com.curso.springboot.services;

import org.springframework.mail.SimpleMailMessage;

import com.curso.springboot.domain.Cliente;
import com.curso.springboot.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);

}
