package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseBody

    public ResponseEntity<ErrorResponse> handleRecursoNoEncontradoException(RecursoNoEncontradoException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Recurso no encontrado");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DatosInvalidosException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDatosInvalidosException(DatosInvalidosException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Los datos proporcionados no son correctos");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}



