package com.first.wisecatalogapi.application.services;

import com.first.wisecatalogapi.application.dto.LivroDTO;
import com.first.wisecatalogapi.domain.entities.LivroEntity;
import com.first.wisecatalogapi.domain.exceptions.LivroNaoEncontradoPeloAutorException;
import com.first.wisecatalogapi.domain.exceptions.LivroNaoEncontradoPeloGeneroException;
import com.first.wisecatalogapi.domain.exceptions.LivroNaoEncontradoPeloIdException;
import com.first.wisecatalogapi.domain.mapper.LivroMapper;
import com.first.wisecatalogapi.domain.repositories.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarLivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private LivroMapper livroMapper;

    @InjectMocks
    private BuscarLivroService buscarLivroService;

    @Test
    void deveBuscarLivroPorIdComSucesso() {
        var livroEntity = new LivroEntity(
                1L,
                "Título Teste",
                "Autor Teste",
                "Genero Teste",
                "SubGenero Teste",
                "Tipo Teste",
                29.90,
                4.7,
                100,
                "http://url.teste"
        );

        var livroDTO = new LivroDTO(
                1L,
                "Título Teste",
                "Autor Teste",
                "Genero Teste",
                "SubGenero Teste",
                "Tipo Teste",
                29.90,
                4.7,
                100,
                "http://url.teste"
        );

        when(livroRepository.buscarLivroPorId(1L)).thenReturn(Optional.of(livroEntity));
        when(livroMapper.toDTO(livroEntity)).thenReturn(livroDTO);

        var resultado = buscarLivroService.buscarPorId(1L);

        assertEquals(livroDTO, resultado);
        verify(livroRepository).buscarLivroPorId(1L);
        verify(livroMapper).toDTO(livroEntity);
    }

    @Test
    void deveLancarExcecaoQuandoLivroPorIdNaoExistir() {
        when(livroRepository.buscarLivroPorId(1L)).thenReturn(Optional.empty());

        var excecao = assertThrows(LivroNaoEncontradoPeloIdException.class, () -> buscarLivroService.buscarPorId(1L));

        assertEquals("Livro com id 1 não encontrado.", excecao.getMessage());
        verify(livroRepository).buscarLivroPorId(1L);
        verifyNoInteractions(livroMapper);
    }

    @Test
    void deveListarTodosLivrosComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);

        var livroEntity = new LivroEntity(
                1L,
                "Título Teste",
                "Autor Teste",
                "Genero Teste",
                "SubGenero Teste",
                "Tipo Teste",
                29.90,
                4.7,
                100,
                "http://url.teste"
        );

        var livroDTO = new LivroDTO(
                1L,
                "Título Teste",
                "Autor Teste",
                "Genero Teste",
                "SubGenero Teste",
                "Tipo Teste",
                29.90,
                4.7,
                100,
                "http://url.teste"
        );

        Page<LivroEntity> paginaLivros = new PageImpl<>(List.of(livroEntity));

        when(livroRepository.listarTodosLivros(pageable)).thenReturn(paginaLivros);
        when(livroMapper.toDTO(livroEntity)).thenReturn(livroDTO);

        var resultado = buscarLivroService.listarTodosLivros(pageable);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals(livroDTO, resultado.getContent().get(0));
        verify(livroRepository).listarTodosLivros(pageable);
        verify(livroMapper).toDTO(livroEntity);
    }

    @Test
    void deveBuscarLivrosPorGeneroComSucesso() {
        var livroEntity = new LivroEntity(
                1L,
                "Título Teste",
                "Autor Teste",
                "Genero Teste",
                "SubGenero Teste",
                "Tipo Teste",
                29.90,
                4.7,
                100,
                "http://url.teste"
        );

        var livroDTO = new LivroDTO(
                1L,
                "Título Teste",
                "Autor Teste",
                "Genero Teste",
                "SubGenero Teste",
                "Tipo Teste",
                29.90,
                4.7,
                100,
                "http://url.teste"
        );

        List<LivroEntity> livros = List.of(livroEntity);

        when(livroRepository.buscarLivrosPorGenero("Genero Teste")).thenReturn(livros);
        when(livroMapper.toDTO(livroEntity)).thenReturn(livroDTO);

        var resultado = buscarLivroService.buscarPorGenero("Genero Teste");

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(livroDTO));
        verify(livroRepository).buscarLivrosPorGenero("Genero Teste");
        verify(livroMapper).toDTO(livroEntity);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarLivrosPorGenero() {
        when(livroRepository.buscarLivrosPorGenero("Genero Inexistente")).thenReturn(Collections.emptyList());

        var excecao = assertThrows(LivroNaoEncontradoPeloGeneroException.class, () -> buscarLivroService.buscarPorGenero("Genero Inexistente"));

        assertEquals("Livros com o genero Genero Inexistente não encontrado.", excecao.getMessage());
        verify(livroRepository).buscarLivrosPorGenero("Genero Inexistente");
        verifyNoInteractions(livroMapper);
    }

    @Test
    void deveBuscarLivrosPorAutorComSucesso() {
        var livroEntity = new LivroEntity(
                1L,
                "Título Teste",
                "Autor Teste",
                "Genero Teste",
                "SubGenero Teste",
                "Tipo Teste",
                29.90,
                4.7,
                100,
                "http://url.teste"
        );

        var livroDTO = new LivroDTO(
                1L,
                "Título Teste",
                "Autor Teste",
                "Genero Teste",
                "SubGenero Teste",
                "Tipo Teste",
                29.90,
                4.7,
                100,
                "http://url.teste"
        );

        List<LivroEntity> livros = List.of(livroEntity);

        when(livroRepository.buscarLivrosPorAutor("Autor Teste")).thenReturn(livros);
        when(livroMapper.toDTO(livroEntity)).thenReturn(livroDTO);

        var resultado = buscarLivroService.buscarPorAutor("Autor Teste");

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(livroDTO));
        verify(livroRepository).buscarLivrosPorAutor("Autor Teste");
        verify(livroMapper).toDTO(livroEntity);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarLivrosPorAutor() {
        when(livroRepository.buscarLivrosPorAutor("Autor Inexistente")).thenReturn(Collections.emptyList());

        var excecao = assertThrows(LivroNaoEncontradoPeloAutorException.class, () -> buscarLivroService.buscarPorAutor("Autor Inexistente"));

        assertEquals("Livros do autor Autor Inexistente não encontrado.", excecao.getMessage());
        verify(livroRepository).buscarLivrosPorAutor("Autor Inexistente");
        verifyNoInteractions(livroMapper);
    }
}