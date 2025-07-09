package com.first.wisecatalogapi.application.usecases;

import com.first.wisecatalogapi.application.dto.LivroDTO;

import java.util.List;

public interface VisualizacaoLivroCacheUseCase {
    void registrarVisualizacao(String sessionId, Long livroId);
    List<LivroDTO> buscarLivrosVisualizadosRecentemente(String sessionId);
}
