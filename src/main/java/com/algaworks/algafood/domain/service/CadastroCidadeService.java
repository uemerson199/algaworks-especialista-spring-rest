package com.algaworks.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroCidadeService {
	
	private static final String MSG_ESTADO_NAO_ENCONTRADO = "Não existe cadastro de estado com esse código %d";

	private static final String MSG_CIDADE_NAO_ENCONTRADA = "A cidade de id %d, não foi encontrada.";

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoRepository estadoRepository;
	
	
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		Optional<Estado> estado = estadoRepository.findById(estadoId);
		
		if (estado.isEmpty()) {
			throw new EntidadeNaoEncontradaException( 
					String.format(MSG_ESTADO_NAO_ENCONTRADO, estadoId));
		}
		
		cidade.setEstado(estado.get());
		
		return cidadeRepository.save(cidade);
		
	}
	
	public void excluir(Long id) {
		try {
			cidadeRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format(MSG_CIDADE_NAO_ENCONTRADA, id));
		}
	
	}
	
	
	public Cidade buscarOuFalhar(Long id) {
		return cidadeRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException
						(String.format(MSG_CIDADE_NAO_ENCONTRADA, id)));
	}
	
	
}
