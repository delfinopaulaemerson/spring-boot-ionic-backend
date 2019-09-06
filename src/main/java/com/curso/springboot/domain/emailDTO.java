package com.curso.springboot.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class emailDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String idPedido;
	
	private String instante;
	
	private String nomeCliente;
	
	
	private String situacaoDoPagamento;
	
	private String valortotal;
	
	
	private List<emailDTOAux> itens = new ArrayList<emailDTOAux>();
	

	public String getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(String idPedido) {
		this.idPedido = idPedido;
	}

	public String getInstante() {
		return instante;
	}

	public void setInstante(String instante) {
		this.instante = instante;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getSituacaoDoPagamento() {
		return situacaoDoPagamento;
	}

	public void setSituacaoDoPagamento(String situacaoDoPagamento) {
		this.situacaoDoPagamento = situacaoDoPagamento;
	}

	public List<emailDTOAux> getItens() {
		return itens;
	}

	public void setItens(List<emailDTOAux> itens) {
		this.itens = itens;
	}

	public String getValortotal() {
		return valortotal;
	}

	public void setValortotal(String valortotal) {
		this.valortotal = valortotal;
	}
	

}
