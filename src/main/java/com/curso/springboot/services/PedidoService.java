package com.curso.springboot.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curso.springboot.domain.ItemPedido;
import com.curso.springboot.domain.PagamentoComBoleto;
import com.curso.springboot.domain.Pedido;
import com.curso.springboot.domain.enums.EstadoPagamento;
import com.curso.springboot.repositories.ClienteRepository;
import com.curso.springboot.repositories.ItemPedidoRepository;
import com.curso.springboot.repositories.PagamentoRepository;
import com.curso.springboot.repositories.PedidoRepository;
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
	
	
	public Pedido find(Integer id){
		
		Optional<Pedido> obj = this.repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id +", Tipo "+ Pedido.class.getName()));
		
	}

	@Transactional
	public Pedido insert( Pedido obj) {
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
		
		System.out.println(obj);
		
		return obj;
	}

}
