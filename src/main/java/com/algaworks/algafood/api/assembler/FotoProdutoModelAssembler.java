package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.FotoProdutoModel;
import com.algaworks.algafood.domain.model.FotoProduto;

@Component
public class FotoProdutoModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public FotoProdutoModel toModel(FotoProduto fotoProduto) {
		return modelMapper.map(fotoProduto, FotoProdutoModel.class);
	}
	
	public List<FotoProdutoModel> toCollectionModel(Collection<FotoProduto> fotoProdutos){
		return fotoProdutos.stream()
				.map(fotoProduto -> toModel(fotoProduto))
				.collect(Collectors.toList());
	}
}
