 package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CadastroCozinhaService {
	
	private static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";
	private static final String MSG_COZINHA_NAO_ENCONTRADA
	   = "A cozinha de id %d, não foi encontrada";
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			cozinhaRepository.deleteById(id);
			cozinhaRepository.flush();
			 
		} catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncontradaException(String.format(MSG_COZINHA_NAO_ENCONTRADA, id));
		}
		catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_COZINHA_EM_USO, id));
		}
	}
	
	public Cozinha buscarOuFalhar(Long id) {
		return cozinhaRepository.findById(id)
				.orElseThrow(() -> new CozinhaNaoEncontradaException(
						String.format(MSG_COZINHA_NAO_ENCONTRADA, id)));
	}
	
}
