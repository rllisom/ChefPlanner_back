package com.salesianostriana.chefplanner.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.chefplanner.files.shared.exception.StorageException;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandleException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)  // ← Específico para credenciales
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("success", "false");
        error.put("error", "bad_credentials");
        error.put("message", "Usuario o contraseña incorrectos");  // ← Mensaje amigable
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)  // ← Usuario no existe
    public ResponseEntity<Map<String, String>> handleUserNotFound(UsernameNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("success", "false");
        error.put("error", "user_not_found");
        error.put("message", "Usuario no encontrado");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse handleAuthenticationException(AuthenticationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, "Error de autenticación"
        );
        return ErrorResponse.builder(ex, problemDetail)
                .header("WWW-Authenticate", "Bearer")
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException ex) {
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("Entidad no encontrada");
        problemDetail.setType(URI.create("chefplanner.com/error"));

        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());

        problemDetail.setTitle("Error en los argumentos");
        problemDetail.setType(URI.create("chefplanner.com/error/illegal-argument"));

        return problemDetail;
    }

    @ExceptionHandler(StorageException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleStorageException(StorageException ex) {
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("Error en el almacenamiento de ficheros");
        problemDetail.setType(URI.create("chefplanner.com/error/storage-error"));

        return problemDetail;
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemDetail result =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());

        result.setDetail("Error al validar los campos del formulario");

        List<ApiValidationSubError> subErrors =
                ex.getAllErrors().stream()
                        .map(ApiValidationSubError::from)
                        .toList();

        result.setProperty("invalid-params",subErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(result);


    }

    @Builder
    record ApiValidationSubError(
            String object,
            String message,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            String field,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            Object rejectedValue
    ){
        public ApiValidationSubError(String object,String message){
            this(object,message,null,null);
        }

        public static ApiValidationSubError from (ObjectError error){
            ApiValidationSubError resul = null;

            if(error instanceof FieldError fieldError){
                resul = ApiValidationSubError.builder()
                        .object(fieldError.getObjectName())
                        .message(fieldError.getDefaultMessage())
                        .field(fieldError.getField())
                        .rejectedValue(fieldError.getRejectedValue())
                        .build();
            }else{
                resul = ApiValidationSubError.builder()
                        .object(error.getObjectName())
                        .message(error.getDefaultMessage())
                        .build();
            }

            return resul;
        }
    }
}
