package com.first.wisecatalogapi.application.services;

import com.first.wisecatalogapi.application.dto.LivroDTO;
import com.first.wisecatalogapi.application.ports.VisualizacaoLivroOutputPort;
import com.first.wisecatalogapi.domain.entities.LivroEntity;
import com.first.wisecatalogapi.domain.mapper.LivroMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisualizacaoLivroServiceTest {

    @Mock
    private VisualizacaoLivroOutputPort visualizacaoLivroOutputPort;

    @Mock
    private LivroMapper livroMapper;

    @InjectMocks
    private VisualizacaoLivroService visualizacaoLivroService;

    private final String sessionId = "session-123";

    @Test
    void deveRegistrarVisualizacaoComSucesso() {
        String sessionId = "sessao123";
        Long livroId = 42L;

        visualizacaoLivroService.registrarVisualizacao(sessionId, livroId);

        verify(visualizacaoLivroOutputPort, times(1)).registrarVisualizacao(sessionId, livroId);
    }

    @Test
    void deveBuscarLivrosVisualizadosRecentementeComSucesso() {
        String sessionId = "sessao456";
        List<Long> ids = List.of(1L, 2L);
        List<LivroEntity> livros = List.of(
                new LivroEntity(1L, "Livro 1", "Autor 1", "Genero 1", "Sub", "Tipo", 10.0, 4.5, 100, "url"),
                new LivroEntity(2L, "Livro 2", "Autor 2", "Genero 2", "Sub", "Tipo", 15.0, 4.0, 50, "url2")
        );
        List<LivroDTO> livrosDTO = List.of(
                new LivroDTO(1L, "Livro 1", "Autor 1", "Genero 1", "Sub", "Tipo", 10.0, 4.5, 100, "url"),
                new LivroDTO(2L, "Livro 2", "Autor 2", "Genero 2", "Sub", "Tipo", 15.0, 4.0, 50, "url2")
        );

        when(visualizacaoLivroOutputPort.buscarVisualizadosRecentemente(sessionId)).thenReturn(ids);
        when(visualizacaoLivroOutputPort.buscarLivrosPorIds(ids)).thenReturn(livros);
        when(livroMapper.toDTO(livros.get(0))).thenReturn(livrosDTO.get(0));
        when(livroMapper.toDTO(livros.get(1))).thenReturn(livrosDTO.get(1));

        var resultado = visualizacaoLivroService.buscarLivrosVisualizadosRecentemente(sessionId);

        assertEquals(livrosDTO, resultado);
        verify(visualizacaoLivroOutputPort, times(1)).buscarVisualizadosRecentemente(sessionId);
        verify(visualizacaoLivroOutputPort, times(1)).buscarLivrosPorIds(ids);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverVisualizacoes() {
        when(visualizacaoLivroOutputPort.buscarVisualizadosRecentemente(sessionId)).thenReturn(Collections.emptyList());

        var resultado = visualizacaoLivroService.buscarLivrosVisualizadosRecentemente(sessionId);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveTratarExcecaoAoBuscarVisualizacoes() {
        when(visualizacaoLivroOutputPort.buscarVisualizadosRecentemente(sessionId))
                .thenThrow(new RuntimeException("Erro ao acessar Redis"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                visualizacaoLivroService.buscarLivrosVisualizadosRecentemente(sessionId));

        assertEquals("Erro ao acessar Redis", exception.getMessage());
    }
}