package com.curso.springboot.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curso.springboot.domain.Produto;
import com.curso.springboot.dto.ProdutoDTO;
import com.curso.springboot.resources.utils.URL;
import com.curso.springboot.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findAllPage(

			@RequestParam(value = "nome", defaultValue = "") String nome,

			@RequestParam(value = "categorias", defaultValue = "") String categorias,

			@RequestParam(value = "page", defaultValue = "0") Integer page,

			@RequestParam(value = "linesPorPage", defaultValue = "24") Integer linesPorPage,

			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,

			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		Page<Produto> lista = null;
		List<Integer> ids = new ArrayList<Integer>();
		Page<ProdutoDTO> listaDTO = null;
		String nomeDecode = null;

		nomeDecode = URL.decodeParam(nome);
		
		//metodo que recupera os ids de categoria passsado por parametros na URL
		ids = URL.decodeIntList(categorias);
		
		lista = this.service.search(nomeDecode, ids, page, linesPorPage, orderBy, direction);

		listaDTO = lista.map(obj -> new ProdutoDTO(obj));

		return ResponseEntity.ok(listaDTO);
	}

}
