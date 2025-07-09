package com.first.wisecatalogapi.application.ports;

import com.first.wisecatalogapi.domain.entities.LivroEntity;

import java.util.List;

public interface VisualizacaoLivroOutputPort {
    void registrarVisualizacao(String sessionId, Long livroId);
    List<Long> buscarVisualizadosRecentemente(String sessionId);
    List<LivroEntity> buscarLivrosPorIds(List<Long> ids);
}
