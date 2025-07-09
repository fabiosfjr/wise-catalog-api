package com.first.wisecatalogapi.domain.exceptions;

public class LivroNaoEncontradoPeloGeneroException extends RuntimeException {
    public LivroNaoEncontradoPeloGeneroException(String genero) {
        super("Livros com o genero " + genero + " não encontrado.");
    }
}
