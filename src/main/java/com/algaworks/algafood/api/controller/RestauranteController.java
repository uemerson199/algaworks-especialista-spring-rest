package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {
	
	@Autowired
	private CadastroRestauranteService restauranteService;	
	@Autowired
	private RestauranteRepository restaurauranteRepository;
	
	@GetMapping
	public List<Restaurante> listar() {
		return restaurauranteRepository.findAll();
		
	}
	 
	@GetMapping(value = "/{id}")
	public Restaurante buscar(@PathVariable Long id) {
		return restauranteService.buscarOuFalhar(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Restaurante adicionar(@RequestBody Restaurante restaurante) {	
		try {
			return restauranteService.salvar(restaurante);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}		
		
	}
	
	@PutMapping("/{id}")
	public Restaurante atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
		
		 Restaurante restauranteAtual = restauranteService.buscarOuFalhar(id);		
			
		 BeanUtils.copyProperties(restaurante, restauranteAtual, 
				        "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
		 
		 try {
			 return restauranteService.salvar(restauranteAtual);
		 } catch (EntidadeNaoEncontradaException e) {
				throw new NegocioException(e.getMessage());
	 	 }	
		 	
	}
	
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable long id) {
		restauranteService.excluir(id);
	}
	
	@PatchMapping("/{id}")
	public Restaurante atualizarParcial(@PathVariable Long id,
			@RequestBody Map<String, Object> campos) {
		
		Restaurante restauranteAtual = restauranteService.buscarOuFalhar(id);
		
		merge(campos, restauranteAtual);
		
		return atualizar(id, restauranteAtual);
		
	}

	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDetino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);
		
		System.out.println(restauranteOrigem);
		
		
		camposOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			field.setAccessible(true);
			
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
			
		//	System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
			
	    	ReflectionUtils.setField(field, restauranteDetino, novoValor);
		});
	}
	
	
	

}
