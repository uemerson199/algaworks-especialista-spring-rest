package com.algaworks.algafood.domain.exception;

public class FormaPagamentoNaoEncontradaException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;

	public FormaPagamentoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public FormaPagamentoNaoEncontradaException(Long formaPgamendoId) {
		this(String.format("Não existe um cadastro de Forma de Pagamento com o código %d", formaPgamendoId));
	}
	
}
