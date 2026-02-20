package com.salesianostriana.chefplanner.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.chefplanner.files.shared.exception.StorageException;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalHandleException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException ex) {
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("Entidad no encontrada");
        problemDetail.setType(URI.create("chefplanner.com/error"));

        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());

        problemDetail.setTitle("Error en los argumentos");
        problemDetail.setType(URI.create("chefplanner.com/error/illegal-argument"));

        return problemDetail;
    }

    @ExceptionHandler(StorageException.class)
    public ProblemDetail handleStorageException(StorageException ex) {
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("Error en el almacenamiento de ficheros");
        problemDetail.setType(URI.create("chefplanner.com/error/storage-error"));

        return problemDetail;
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());

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
