package com.first.wisecatalogapi.application.services;

import com.first.wisecatalogapi.application.dto.LivroDTO;
import com.first.wisecatalogapi.application.ports.VisualizacaoLivroOutputPort;
import com.first.wisecatalogapi.application.usecases.VisualizacaoLivroCacheUseCase;
import com.first.wisecatalogapi.domain.mapper.LivroMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisualizacaoLivroService implements VisualizacaoLivroCacheUseCase {

    private final VisualizacaoLivroOutputPort visualizacaoLivroOutputPort;
    private final LivroMapper livroMapper;

    public VisualizacaoLivroService(VisualizacaoLivroOutputPort visualizacaoLivroOutputPort,
                                    LivroMapper livroMapper) {
        this.visualizacaoLivroOutputPort = visualizacaoLivroOutputPort;
        this.livroMapper = livroMapper;
    }

    @Override
    public void registrarVisualizacao(String sessionId,
                                      Long livroId) {
        visualizacaoLivroOutputPort.registrarVisualizacao(sessionId, livroId);
    }

    @Override
    public List<LivroDTO> buscarLivrosVisualizadosRecentemente(String sessionId) {
        List<Long> ids = visualizacaoLivroOutputPort.buscarVisualizadosRecentemente(sessionId);

        return visualizacaoLivroOutputPort.buscarLivrosPorIds(ids)
                .stream()
                .map(livroMapper::toDTO)
                .collect(Collectors.toList());
    }
}
