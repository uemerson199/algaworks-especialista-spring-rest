package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	private static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "O Restaurante de id %d, não foi encontrado";

	@Autowired
	private RestauranteRepository restauranteRepository;

	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		
//		Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
//				.orElseThrow(() -> new EntidadeNaoEncontradaException(
//						String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, cozinhaId)));

		restaurante.setCozinha(cozinha);

		return restauranteRepository.save(restaurante);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			restauranteRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncontradaException(String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, id));
		}
	
	}
	
	
	public Restaurante buscarOuFalhar(Long id) {
		return restauranteRepository.findById(id)
				.orElseThrow(() -> new CozinhaNaoEncontradaException(
						String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, id)));
	}

	

}
