package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.PermissaoInput;
import com.algaworks.algafood.domain.model.Permissao;

@Component
public class PermissaoInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Permissao toDomainObject(PermissaoInput permissaoInput) {
		return modelMapper.map(permissaoInput, Permissao.class);
	}
	
	public void copyToDomainObject(PermissaoInput permissaoInput, Permissao permissao) {
		modelMapper.map(permissaoInput, permissao);
	}
}
