package com.curso.springboot.dto;

import java.io.Serializable;

public class emailDTOAux implements Serializable{

	private static final long serialVersionUID = 1L;

	private String nome;
	
	private String quantidade;
	
	private String preco;
	
	private String subtotal;
	
	
	
	public emailDTOAux() {
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public String getPreco() {
		return preco;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}
	
}
