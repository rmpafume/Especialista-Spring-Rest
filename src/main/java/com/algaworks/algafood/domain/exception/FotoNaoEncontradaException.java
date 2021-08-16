package com.algaworks.algafood.domain.exception;

public class FotoNaoEncontradaException extends EntidadeNaoEncontradaException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FotoNaoEncontradaException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}
	
	public FotoNaoEncontradaException(Long produtoId, Long restauranteId) {
		this(String.format("Não existe um cadastro de foto para produto de código %d para o restaurante de código %d", 
                produtoId, restauranteId));
	}
	
	

}
