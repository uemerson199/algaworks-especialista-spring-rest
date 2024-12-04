package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;


@RestController
@RequestMapping(value = "/cozinhas")
public class CozinhaController {
	
	
	@Autowired
	private CozinhaRepository repository;
	
	@GetMapping
	public List<Cozinha> listar() {
		return repository.listar();
	}
	
	@GetMapping(value = "/{id}")
	public Cozinha buscar(@PathVariable Long id) {
		return repository.buscar(id);
	}
	

}
