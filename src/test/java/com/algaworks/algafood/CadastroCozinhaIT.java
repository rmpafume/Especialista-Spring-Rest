package com.algaworks.algafood;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class CadastroCozinhaIT {
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;	
	
	@Test
	public void testarCadastroCozinhaComSucesso() {
		//cenario
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("chinesa");
		
		//acao
		novaCozinha = cadastroCozinha.salvar(novaCozinha);
		
		//validacao
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test
	public void testarCadastroCozinhaSemNome() {
		Assertions.assertThrows(ConstraintViolationException.class,
			() -> { 
				Cozinha novaCozinha = new Cozinha();
				novaCozinha.setNome(null);
				novaCozinha = cadastroCozinha.salvar(novaCozinha);
			}
		);
	}
	
	@Test
	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
		Assertions.assertThrows(EntidadeEmUsoException.class, 
			() -> cadastroCozinha.excluir(1L));
	}
	
	@Test
	public void deveFalhar_QuandoExcluirCozinhaInexistente() {
		Assertions.assertThrows(CozinhaNaoEncontradaException.class,
			() -> cadastroCozinha.excluir(100L));
	}
	
	
}
