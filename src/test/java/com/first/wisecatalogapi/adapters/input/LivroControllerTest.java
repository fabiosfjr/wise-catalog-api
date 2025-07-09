package com.first.wisecatalogapi.adapters.input;

import com.first.wisecatalogapi.application.dto.LivroDTO;
import com.first.wisecatalogapi.application.usecases.BuscarLivroUseCase;
import com.first.wisecatalogapi.application.usecases.ImportarLivrosCsvUseCase;
import com.first.wisecatalogapi.application.usecases.VisualizacaoLivroCacheUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroControllerTest {

    @Mock
    private ImportarLivrosCsvUseCase importarLivrosCsvUseCase;

    @Mock
    private BuscarLivroUseCase buscarLivroUseCase;

    @Mock
    private VisualizacaoLivroCacheUseCase visualizacaoLivroCacheUseCase;

    @InjectMocks
    private LivroController livroController;

    @Test
    void deveListarTodosLivrosComResultado() {
        Pageable pageable = PageRequest.of(0, 10);
        var livroDTO = new LivroDTO(1L, "Título", "Autor", "Gênero", "SubGênero", "Tipo", 20.0, 4.0, 10, "url");
        var page = new PageImpl<>(List.of(livroDTO), pageable, 1);

        when(buscarLivroUseCase.listarTodosLivros(pageable)).thenReturn(page);

        var response = livroController.listarTodosLivros(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().getTotalElements());
        verify(buscarLivroUseCase).listarTodosLivros(pageable);
    }

    @Test
    void deveRetornarNoContentAoListarTodosLivrosVazio() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<LivroDTO> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(buscarLivroUseCase.listarTodosLivros(pageable)).thenReturn(emptyPage);

        var response = livroController.listarTodosLivros(0, 10);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(buscarLivroUseCase).listarTodosLivros(pageable);
    }

    @Test
    void deveBuscarLivroPorIdERegistrarVisualizacao() {
        Long id = 1L;
        String sessionId = "session-123";
        var livroDTO = new LivroDTO(id, "Título", "Autor", "Gênero", "SubGênero", "Tipo", 20.0, 4.0, 10, "url");

        when(buscarLivroUseCase.buscarPorId(id)).thenReturn(livroDTO);

        var response = livroController.buscarLivroPorId(id, sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(livroDTO, response.getBody());
        verify(buscarLivroUseCase).buscarPorId(id);
        verify(visualizacaoLivroCacheUseCase).registrarVisualizacao(sessionId, id);
    }

    @Test
    void deveBuscarLivrosPorAutor() {
        String autor = "Autor Teste";
        var livroDTO = new LivroDTO(1L, "Título", autor, "Gênero", "SubGênero", "Tipo", 20.0, 4.0, 10, "url");
        var lista = List.of(livroDTO);

        when(buscarLivroUseCase.buscarPorAutor(autor)).thenReturn(lista);

        var response = livroController.buscarLivroPorAutor(autor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
        verify(buscarLivroUseCase).buscarPorAutor(autor);
    }

    @Test
    void deveBuscarLivrosPorGenero() {
        String genero = "Gênero Teste";
        var livroDTO = new LivroDTO(1L, "Título", "Autor", genero, "SubGênero", "Tipo", 20.0, 4.0, 10, "url");
        var lista = List.of(livroDTO);

        when(buscarLivroUseCase.buscarPorGenero(genero)).thenReturn(lista);

        var response = livroController.buscarLivroPorGenero(genero);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
        verify(buscarLivroUseCase).buscarPorGenero(genero);
    }

    @Test
    void deveBuscarLivrosVisualizadosRecentementeComResultado() {
        String sessionId = "session-123";
        var livroDTO = new LivroDTO(1L, "Título", "Autor", "Gênero", "SubGênero", "Tipo", 20.0, 4.0, 10, "url");
        var lista = List.of(livroDTO);

        when(visualizacaoLivroCacheUseCase.buscarLivrosVisualizadosRecentemente(sessionId)).thenReturn(lista);

        var response = livroController.buscarLivrosVisualizadosRecentemente(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
        verify(visualizacaoLivroCacheUseCase).buscarLivrosVisualizadosRecentemente(sessionId);
    }

    @Test
    void deveRetornarNoContentAoBuscarLivrosVisualizadosRecentementeVazio() {
        String sessionId = "session-123";

        when(visualizacaoLivroCacheUseCase.buscarLivrosVisualizadosRecentemente(sessionId)).thenReturn(List.of());

        var response = livroController.buscarLivrosVisualizadosRecentemente(sessionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(visualizacaoLivroCacheUseCase).buscarLivrosVisualizadosRecentemente(sessionId);
    }

    @Test
    void deveImportarLivrosCsvComSucesso() throws Exception {
        MultipartFile arquivo = mock(MultipartFile.class);

        doNothing().when(importarLivrosCsvUseCase).importarLivrosCsv(arquivo);

        var response = livroController.importarLivrosCsv(arquivo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Importação concluída com sucesso!", response.getBody());
        verify(importarLivrosCsvUseCase).importarLivrosCsv(arquivo);
    }
}