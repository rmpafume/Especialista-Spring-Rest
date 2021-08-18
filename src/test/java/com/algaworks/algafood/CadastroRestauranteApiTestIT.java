package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteApiTestIT { 
	
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";

    private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
    
	private static final int RESTAURANTE_ID_INEXISTENTE = 100;
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	
	private int quantidadeRestaurantesCadastrados;
	private String jsonRestauranteCorreto;
	private String jsonRestauranteCozinhaInexistente;
	private String jsonRestauranteFreteNegativo;
	private Restaurante restauranteComidaManeira;
	
	@BeforeEach //antes de cada teste rodar aqui.
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/restaurantes";
		jsonRestauranteCorreto = ResourceUtils.getContentFromResource(
				"/json/correto/restaurante-correto.json");
		jsonRestauranteCozinhaInexistente = ResourceUtils.getContentFromResource(
				"/json/incorreto/restaurante-cozinha-inexistente.json");
		
		jsonRestauranteFreteNegativo = ResourceUtils.getContentFromResource(
				"/json/incorreto/restaurante-frete-negativo.json");
		
		databaseCleaner.clearTables();
		prepararDados();
	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarRestaurantes() {
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarQuantidadeCorretaDeRestaurantes_QuandoConsultarRestaurantes() {
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeRestaurantesCadastrados));
			//.body("nome", hasItems("Indiana","Tailandesa"));
			
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarRestaurante() {
		given()
			.body(jsonRestauranteCorreto)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteSemTaxaFrete() {
		given()
			.body(jsonRestauranteFreteNegativo)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
	    .when()
	        .post()
	    .then()
	        .statusCode(HttpStatus.BAD_REQUEST.value())
	        .body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente() {
		given()
			.body(jsonRestauranteCozinhaInexistente)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}
	
	
	// GET /restaurantes/{restauranteId}
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestauranteExistente() {
		given()
			.pathParam("restauranteId", restauranteComidaManeira.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{restauranteId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(restauranteComidaManeira.getNome()));
		
	}
	
	// GET /restaurantes/{restauranteId}
		@Test
		public void deveRetornarStatus404_QuandoConsultarRestauranteInexistente() {
			given()
				.pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
				.accept(ContentType.JSON)
			.when()
				.get("/{restauranteId}")
			.then()
				.statusCode(HttpStatus.NOT_FOUND.value());
		}
		
		
	
	private void prepararDados() {

		Cozinha cozinhaTailandesa = new Cozinha();
		cozinhaTailandesa.setNome("tailandesa");
		cozinhaRepository.save(cozinhaTailandesa);
		
		Cozinha cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("americana");
		cozinhaRepository.save(cozinhaAmericana);
		
		restauranteComidaManeira = new Restaurante();
		restauranteComidaManeira.setNome("Comida maneira");
		restauranteComidaManeira.setTaxaFrete(new BigDecimal(7.5));
		restauranteComidaManeira.setCozinha(cozinhaTailandesa);
		restauranteRepository.save(restauranteComidaManeira);
		
		Restaurante restauranteBomGosto = new Restaurante();
		restauranteBomGosto.setNome("Bom gosto");
		restauranteBomGosto.setTaxaFrete(new BigDecimal(5.8));
		restauranteBomGosto.setCozinha(cozinhaAmericana);
		restauranteRepository.save(restauranteBomGosto);
		
		quantidadeRestaurantesCadastrados = (int) restauranteRepository.count();
	}
}
