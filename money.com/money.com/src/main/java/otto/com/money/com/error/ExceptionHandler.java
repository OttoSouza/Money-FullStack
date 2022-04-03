package otto.com.money.com.error;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
	
		String mesangemDesenvolvedor = ex.getCause().toString();
		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		List<Erro> errors = Arrays.asList(new Erro(mesangemDesenvolvedor, mensagemUsuario));
		
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<Erro> errors = criarListaDeError(ex.getBindingResult());
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private List<Erro> criarListaDeError(BindingResult bindingResult) {
		
		List<Erro> errors = new ArrayList<>();
		
		for(FieldError fe : bindingResult.getFieldErrors()) {
			String mensagemUsuario = messageSource.getMessage(fe, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = fe.getField().toString();
			errors.add(new Erro(mensagemDesenvolvedor, mensagemUsuario));	
		}
		return errors;
	}
	
	public static class Erro {
		String mesangemDesenvolvedor;
		String mensagemUsuario;
		
		public Erro(String mesangemDesenvolvedor, String mensagemUsuario) {
			super();
			this.mesangemDesenvolvedor = mesangemDesenvolvedor;
			this.mensagemUsuario = mensagemUsuario;
		}
		
		public String getMesangemDesenvolvedor() {
			return mesangemDesenvolvedor;
		}
		public void setMesangemDesenvolvedor(String mesangemDesenvolvedor) {
			this.mesangemDesenvolvedor = mesangemDesenvolvedor;
		}
		public String getMensagemUsuario() {
			return mensagemUsuario;
		}
		public void setMensagemUsuario(String mensagemUsuario) {
			this.mensagemUsuario = mensagemUsuario;
		}
		
	}
	
}

