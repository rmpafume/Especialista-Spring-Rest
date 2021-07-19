package com.algaworks.algafood.core.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

	private List<String> tiposArquivo;
	
	@Override
	public void initialize(FileContentType constraintAnnotation) {
		this.tiposArquivo = Arrays.asList(constraintAnnotation.allowed());
	}
	
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		String valor = value.getContentType().toString();
		
		return value == null || this.tiposArquivo.contains(valor);
	}
	
	
}
