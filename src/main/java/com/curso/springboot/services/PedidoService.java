package com.curso.springboot.services;

import java.util.Date;
import java.util.Optional;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curso.springboot.domain.Categoria;
import com.curso.springboot.domain.Cliente;
import com.curso.springboot.domain.ItemPedido;
import com.curso.springboot.domain.PagamentoComBoleto;
import com.curso.springboot.domain.Pedido;
import com.curso.springboot.domain.enums.EstadoPagamento;
import com.curso.springboot.repositories.ItemPedidoRepository;
import com.curso.springboot.repositories.PagamentoRepository;
import com.curso.springboot.repositories.PedidoRepository;
import com.curso.springboot.security.UserSS;
import com.curso.springboot.services.exception.AuthorizationException;
import com.curso.springboot.services.exception.ObjectNotFoundException;



@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private boletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private PrepareAndSendEmailService prepareAndSendEmailService;
	
	
	public Pedido find(Integer id){
		
		Optional<Pedido> obj = this.repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id +", Tipo "+ Pedido.class.getName()));
		
	}

	@Transactional
	public Pedido insert( Pedido obj) throws Exception {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(this.clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			this.boletoService.preencherPagamentoComBoleto(pagto,obj.getInstante());
		}
			
		//salvando o pedido
		this.repo.save(obj);
		
		//salvando o pagamento
		this.pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			//TUDO verificar o encapsulamento do produto
			ip.setProduto(this.produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
			//salvando o itens de pedidos
		this.itemPedidoRepository.saveAll(obj.getItens());
		
		//SimpleMailMessage msg = this.prepareAndSendEmailService.prepareSimpleMailMessageFromPedido(obj);
		
		//this.prepareAndSendEmailService.sendEmail(msg);
		
		
		//MimeMessage mm = this.prepareAndSendEmailService.prepareMimeMessageFromPedido(obj);
		
		//this.prepareAndSendEmailService.sendHtmlEmail(mm);
		
		
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPorPage, String orderBy, String direction){
		UserSS userSS = null;
		PageRequest pageRequest = null;
		Cliente cliente = null;
		
		userSS = UserService.authenticated();
		
		if(userSS == null)
			throw new AuthorizationException("Acesso Negado!");
		
		 pageRequest = PageRequest.of(page, linesPorPage, Direction.valueOf(direction),orderBy);
		 cliente = this.clienteService.find(userSS.getId());
		 
		return this.repo.findByCliente(cliente, pageRequest);
	}
	
	

}
