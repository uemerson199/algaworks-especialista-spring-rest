package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {
	
	@Autowired
	private CadastroRestauranteService restauranteService;	
	
	@Autowired
	private RestauranteRepository restaurauranteRepository;
	
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
	
	
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		restauranteService.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		restauranteService.inativar(restauranteId);
	}
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			restauranteService.ativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			restauranteService.inativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}	
	
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
	    restauranteService.abrir(restauranteId);
	}

	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
	    restauranteService.fechar(restauranteId);
	} 
	
}
