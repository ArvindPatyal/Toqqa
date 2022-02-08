package com.toqqa.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

import com.toqqa.bo.ErrorBo;

@ControllerAdvice
public class ExceptionHandlingConfig {

	@ExceptionHandler(ResourceCreateUpdateException.class)
	public ResponseEntity<ErrorBo> handleResourceCreateUpdateException(ResourceCreateUpdateException ex,
			HttpServletRequest request, HttpServletResponse response) {
		ErrorBo apiError = new ErrorBo(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<ErrorBo> handleInternalServerException(InternalServerException ex, HttpServletRequest request,
			HttpServletResponse response) {
		ErrorBo apiError = new ErrorBo(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorBo> handleResourceNotFoundException(ResourceNotFoundException ex,
			HttpServletRequest request, HttpServletResponse response) {
		ErrorBo apiError = new ErrorBo(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorBo> authenticationException(AuthenticationException ex, HttpServletRequest request,
			HttpServletResponse response) {
		ErrorBo apiError = new ErrorBo(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorBo> badCredException(BadCredentialsException ex, HttpServletRequest request,
			HttpServletResponse response) {
		ErrorBo apiError = new ErrorBo(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(ResourceAccessException.class)
	public ResponseEntity<ErrorBo> resourceAccessExceptionException(ResourceAccessException ex,
			HttpServletRequest request, HttpServletResponse response) {
		ErrorBo apiError = new ErrorBo(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorBo> accessDeniedException(AccessDeniedException ex, HttpServletRequest request,
			HttpServletResponse response) {
		ErrorBo apiError = new ErrorBo(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorBo> badReqException(BadRequestException ex, HttpServletRequest request,
			HttpServletResponse response) {
		ErrorBo apiError = new ErrorBo(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(UserAlreadyExists.class)
	public ResponseEntity<ErrorBo> userAlreadyExistsException(UserAlreadyExists ex, HttpServletRequest request,
			HttpServletResponse response) {
		ErrorBo apiError = new ErrorBo(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<ErrorBo>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorBo> invalidTokenException(InvalidTokenException ex, HttpServletRequest request,
			HttpServletResponse response) {
		ErrorBo apiError = new ErrorBo(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

}
