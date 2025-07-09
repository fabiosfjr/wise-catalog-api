package com.first.wisecatalogapi.domain.exceptions;

public class LivroNaoEncontradoPeloAutorException extends RuntimeException {

    public LivroNaoEncontradoPeloAutorException(String autor) {
        super("Livros do autor " + autor + " não encontrado.");
    }
}
