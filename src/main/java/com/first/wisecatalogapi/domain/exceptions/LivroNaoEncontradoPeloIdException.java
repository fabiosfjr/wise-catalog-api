package com.first.wisecatalogapi.domain.exceptions;

public class LivroNaoEncontradoPeloIdException extends RuntimeException {

    public LivroNaoEncontradoPeloIdException(Long id) {
        super("Livro com id " + id + " não encontrado.");
    }
}
