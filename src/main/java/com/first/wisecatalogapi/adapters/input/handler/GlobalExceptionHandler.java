package com.first.wisecatalogapi.adapters.input.handler;

import com.first.wisecatalogapi.domain.exceptions.LivroNaoEncontradoPeloAutorException;
import com.first.wisecatalogapi.domain.exceptions.LivroNaoEncontradoPeloGeneroException;
import com.first.wisecatalogapi.domain.exceptions.LivroNaoEncontradoPeloIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LivroNaoEncontradoPeloIdException.class)
    public ResponseEntity<?> handleLivroNaoEncontradoPorId(LivroNaoEncontradoPeloIdException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LivroNaoEncontradoPeloAutorException.class)
    public ResponseEntity<?> handleLivroNaoEncontradoPorAutor(LivroNaoEncontradoPeloAutorException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LivroNaoEncontradoPeloGeneroException.class)
    public ResponseEntity<?> handleLivroNaoEncontradoPorGenero(LivroNaoEncontradoPeloGeneroException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return new ResponseEntity<>(buildResponse("Erro interno no servidor: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> buildResponse(String mensagem) {
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        var dataFormatada = LocalDateTime.now().format(formatter);

        return Map.of(
                "mensagem", mensagem,
                "dataHora", dataFormatada
        );
    }
}
