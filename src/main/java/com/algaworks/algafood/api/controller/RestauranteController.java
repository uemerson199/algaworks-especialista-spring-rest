 package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.disassembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {
	
	@Autowired
	private CadastroRestauranteService restauranteService;	
	
	@Autowired
	private RestauranteRepository restaurauranteRepository;
	
	@Autowired
	private SmartValidator validator;
	
	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	
	@GetMapping
	public List<RestauranteModel> listar() {
		return restauranteModelAssembler.toCollectionModel(restaurauranteRepository.findAll());
		
	}
	 
	@GetMapping(value = "/{id}")
	public RestauranteModel buscar(@PathVariable Long id) {
		Restaurante restaurante = restauranteService.buscarOuFalhar(id);
		
		return restauranteModelAssembler.toModel(restaurante);
	}

	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {	
		try {
			Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
			
			return restauranteModelAssembler.toModel(restauranteService.salvar(restaurante));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}		
		
	}
	
	@PutMapping("/{id}")
	public RestauranteModel atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteInput restauranteInput) {
		 try {
		 
// 		 Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);	 
	     Restaurante restauranteAtual = restauranteService.buscarOuFalhar(id);		
	     
	     restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
			
//		 BeanUtils.copyProperties(restaurante, restauranteAtual, 
//				        "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
		 
		
			 return restauranteModelAssembler.toModel(restauranteService.salvar(restauranteAtual));
		 } catch (CozinhaNaoEncontradaException e) {
				throw new NegocioException(e.getMessage());
	 	 }	
		 	
	}
	
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable long id) {
		restauranteService.excluir(id);
	}
	
//	@PatchMapping("/{id}")
//	public RestauranteModel atualizarParcial(@PathVariable Long id,
//			@RequestBody Map<String, Object> campos, HttpServletRequest request) {
//		
//		Restaurante restauranteAtual = restauranteService.buscarOuFalhar(id);
//		
//		
//		merge(campos, restauranteAtual, request);
//		validate(restauranteAtual, "restaurante");
//		
//		return atualizar(id, restauranteAtual);
//		
//	}

	private void validate(Restaurante restauranteAtual, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restauranteAtual, objectName);
		
		validator.validate(restauranteAtual, bindingResult);
		
		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult); 
		}
		
	}

	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDetino,
			HttpServletRequest request) {
		
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
			Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);
			
			System.out.println(restauranteOrigem);
			
			
			camposOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
				
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
				
			//	System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
				
		    	ReflectionUtils.setField(field, restauranteDetino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
			
	}
	
	
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		restauranteService.ativar(restauranteId);
	}
		
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desativar(@PathVariable Long restauranteId) {
		restauranteService.inativar(restauranteId);
	}
		


}
