package com.algaworks.algafood.api.exceptionhandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
	
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe";
		
		Problema problema = createProblemaBuilder(status, problemType, detail).build();				
				
		return handleExceptionInternal(ex, problema, new HttpHeaders(),
				status, request);	
	}

	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handletratarEntidadeNaoEncontradaException(
			EntidadeNaoEncontradaException ex, WebRequest request) { 
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
		String detail = ex.getMessage();
		
		Problema problema = createProblemaBuilder(status, problemType, detail).build();				
				
		return handleExceptionInternal(ex, problema, new HttpHeaders(),
				status, request);
	
	}
	

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {

	    HttpStatus status = HttpStatus.BAD_REQUEST;
	    ProblemType problemType = ProblemType.ERRO_NEGOCIO;
	    String detail = ex.getMessage();
	    
	    Problema problem = createProblemaBuilder(status, problemType, detail).build();
	    
	    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(
	        EntidadeEmUsoException ex, WebRequest request) {
	    
	    HttpStatus status = HttpStatus.CONFLICT;
	    ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
	    String detail = ex.getMessage();
	    
	    Problema problem = createProblemaBuilder(status, problemType, detail).build();
	    
	    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = Problema.builder()
					.title(status.getReasonPhrase())
					.status(status.value())
					.build();
		} else if (body instanceof String) {
			body = Problema.builder()
					.title((String) body)
					.status(status.value())
					.build();
		}
				
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	
	private Problema.ProblemaBuilder createProblemaBuilder(HttpStatus status,
			ProblemType problemType, String detail) {
		
		return Problema.builder()
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(detail);
		
	}
	
}
