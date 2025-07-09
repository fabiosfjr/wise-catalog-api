package com.first.wisecatalogapi.application.dto;

public record LivroDTO(
        Long id,
        String titulo,
        String autor,
        String genero,
        String subGenero,
        String tipo,
        Double preco,
        Double avaliacao,
        Integer numeroAvaliacoes,
        String url
) {
}
