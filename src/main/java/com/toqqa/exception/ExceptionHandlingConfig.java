package com.toqqa.exception;

import com.toqqa.bo.ErrorBo;
import com.toqqa.payload.Response;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlingConfig extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
                                                         WebRequest request) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + " -> " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + " -> " + error.getDefaultMessage());
        }

        ErrorBo apiError = new ErrorBo(HttpStatus.BAD_REQUEST, "", errors);

        return this.handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            org.springframework.web.bind.MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + " -> " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + " -> " + error.getDefaultMessage());
        }

        ErrorBo apiError = new ErrorBo(HttpStatus.BAD_REQUEST, "", errors);

        return this.handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);

    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }
        return new ResponseEntity<>(new Response<>(body, ""), new HttpHeaders(), status.value());
    }

    @ExceptionHandler(ResourceCreateUpdateException.class)
    public ResponseEntity<Response<ErrorBo>> handleResourceCreateUpdateException(ResourceCreateUpdateException ex,
                                                                                 HttpServletRequest request, HttpServletResponse response) {
        ErrorBo apiError = new ErrorBo(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Response<ErrorBo>>(new Response<>(apiError, ""), new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Response<ErrorBo>> handleInternalServerException(InternalServerException ex,
                                                                           HttpServletRequest request, HttpServletResponse response) {
        ErrorBo apiError = new ErrorBo(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Response<ErrorBo>>(new Response<>(apiError, ""), new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response<ErrorBo>> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                             HttpServletRequest request, HttpServletResponse response) {
        ErrorBo apiError = new ErrorBo(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Response<ErrorBo>>(new Response<>(apiError, ""), new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Response<ErrorBo>> authenticationException(AuthenticationException ex,
                                                                     HttpServletRequest request, HttpServletResponse response) {
        ErrorBo apiError = new ErrorBo(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Response<ErrorBo>>(new Response<>(apiError, ""), new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response<ErrorBo>> badCredException(BadCredentialsException ex, HttpServletRequest request,
                                                              HttpServletResponse response) {
        ErrorBo apiError = new ErrorBo(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Response<ErrorBo>>(new Response<>(apiError, ""), new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler(InvalidAccessException.class)
    public ResponseEntity<Response<ErrorBo>> invalidAccessException(InvalidAccessException ex,
                                                                    HttpServletRequest request, HttpServletResponse response) {
        ErrorBo apiError = new ErrorBo(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Response<ErrorBo>>(new Response<>(apiError, ""), new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response<ErrorBo>> accessDeniedException(AccessDeniedException ex, HttpServletRequest request,
                                                                   HttpServletResponse response) {
        ErrorBo apiError = new ErrorBo(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Response<ErrorBo>>(new Response<>(apiError, ""), new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response<ErrorBo>> badReqException(BadRequestException ex, HttpServletRequest request,
                                                             HttpServletResponse response) {
        ErrorBo apiError = new ErrorBo(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Response<ErrorBo>>(new Response<>(apiError, ""), new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<Response<ErrorBo>> userAlreadyExistsException(UserAlreadyExists ex,
                                                                        HttpServletRequest request, HttpServletResponse response) {
        ErrorBo apiError = new ErrorBo(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Response<ErrorBo>>(new Response<>(apiError, ""), new HttpHeaders(),
                apiError.getStatus());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Response<ErrorBo>> invalidTokenException(InvalidTokenException ex, HttpServletRequest request,
                                                                   HttpServletResponse response) {
        ErrorBo apiError = new ErrorBo(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Response<ErrorBo>>(new Response<>(apiError, ""), new HttpHeaders(),
                apiError.getStatus());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<ErrorBo>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                          HttpServletRequest request, HttpServletResponse response) {
        ErrorBo apiError = new ErrorBo(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Response<ErrorBo>>(new Response<>(apiError, ""), new HttpHeaders(),
                apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {
        String error = "Invalid date " + ex.getValue() + ".. Please enter date in yyyy-MM-dd.";
        ErrorBo apiError = new ErrorBo(HttpStatus.BAD_REQUEST, error, ex.getMessage());
        return this.handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity urlNotFoundException(UrlNotFoundException ex, HttpServletRequest request,
                                               HttpServletResponse response) {
        return new ResponseEntity(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

}
