package com.algaworks.algafood.domain.exception;

public class GrupoNaoEncontradaoException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;

	public GrupoNaoEncontradaoException(String mensagem) {
		super(mensagem);
	}
	
	public GrupoNaoEncontradaoException(Long formaPgamendoId) {
		this(String.format("Não existe um cadastro de Forma de Pagamento com o código %d", formaPgamendoId));
	}
	
}
