package com.curso.springboot.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.curso.springboot.domain.Cliente;
import com.curso.springboot.domain.ItemPedido;
import com.curso.springboot.domain.Pedido;
import com.curso.springboot.dto.emailDTO;
import com.curso.springboot.dto.emailDTOAux;

@Service
public class PrepareAndSendEmailService  implements EmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PrepareAndSendEmailService.class);
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	
	
	@Autowired
	private JavaMailSender javaMailSender;
	

	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
		sendEmail(sm);
		
	}
	
	public SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom("spotifyemersondelfino@gmail.com");
		sm.setSubject("Pedido confirmado! Código: " + obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm;
	}


	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm  = prepareNewPasswordEmail(cliente, newPass);
		this.sendEmail(sm);
		
	}
	
	public SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(cliente.getEmail());
		sm.setFrom("");
		sm.setSubject("Solicitação de nova senha");
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText("Nova senha: " + newPass);
		return sm;
	}

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("envio de email...");
		LOG.info(msg.toString());
		this.mailSender.send(msg);
		LOG.info("Email enviado");
		
	}
	
	
	/**
	 * foi criado um DTO para transporte dos dados pois o framework não
	 * suporta os relacionamentos complexos para o envio de email utilizando o
	 * framework thymeleaf
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public String htmlFromTemplatePedidos(Pedido obj) throws Exception {
		String template = null;
		Context context = new Context();
		emailDTO emailDTO = null;
		
		emailDTO = new emailDTO();
		emailDTO = this.pedidoToEmailDTO(obj);
		
		context.setVariable("emailDTO",emailDTO);
		template = this.templateEngine.process("email/confirmacaoPedido",context);
		
		return template;
	}
	

	/**
	 * encapsulando os valores do objeto pedido
	 * ao emailDTO
	 * @param obj
	 * @return emailDTO
	 */
	private emailDTO pedidoToEmailDTO(Pedido obj) {
		emailDTO emailDTO = new emailDTO();
		emailDTO.setIdPedido(obj.getId().toString());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		emailDTO.setInstante(sdf.format(obj.getInstante()));
		emailDTO.setNomeCliente(obj.getCliente().getNome());
		emailDTO.setValortotal(obj.getCliente().getPedidos().get(0).getValorTotal().toString());
		emailDTO.setSituacaoDoPagamento(obj.getPagamento().getEstadoPagamento().getDescricao());
		
		List<emailDTOAux> emailDTOAuxs  =  new ArrayList<emailDTOAux>();
		
		for (ItemPedido item : obj.getItens()) {
			emailDTOAux emailDTOAux =  new emailDTOAux();
			
			emailDTOAux.setNome(item.getProduto().getNome());
			emailDTOAux.setQuantidade(item.getQuantidade().toString());
			emailDTOAux.setPreco(item.getPreco().toString());
			emailDTOAux.setSubtotal(item.getSubtotal().toString());
			emailDTOAuxs.add(emailDTOAux);
		}
		
		emailDTO.setItens(emailDTOAuxs);
		
		return emailDTO;
	}

	public MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws Exception {
		MimeMessage mm = this.javaMailSender.createMimeMessage();
		
		MimeMessageHelper mmh = new MimeMessageHelper(mm, true);
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom("spotifyemersondelfino@gmail.com");
		mmh.setSubject(" Pedido Comfirmado Código: "+ obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(this.htmlFromTemplatePedidos(obj),true);
		
		return mm;
	}

	/**
	 * preparo para envio de email utilizando o
	 * framework thymeleaf
	 */
	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("envio de email...");
		LOG.info(msg.toString());
		this.javaMailSender.send(msg);
		LOG.info("Email enviado");
		
	}

}
