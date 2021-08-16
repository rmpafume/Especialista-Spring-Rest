package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FotoProdutoModel {
	
	private String descricao;
	private String nomeArquivo;
	private String contentType;
	private Long tamanho;
}
