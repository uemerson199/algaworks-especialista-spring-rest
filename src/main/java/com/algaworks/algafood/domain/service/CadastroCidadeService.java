package com.algaworks.algafood.domain.service;

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
	
	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoRepository estadoRepository;
	
	
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		Estado estado = estadoRepository.buscar(estadoId);
		
		if (estado == null) {
			throw new EntidadeNaoEncontradaException( 
					String.format("Não existe cadastro de estado com esse código %d", estadoId));
		}
		
		cidade.setEstado(estado);
		
		return cidadeRepository.salvar(cidade);
		
	}
	
	public void excluir(Long id) {
		try {
			cidadeRepository.remover(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("O Restaurante de id %d, não foi encontrado", id));
		}
	
	}
	
	
	
}
