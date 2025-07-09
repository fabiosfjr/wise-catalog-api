package com.first.wisecatalogapi.adapters.integration;

import com.first.wisecatalogapi.adapters.input.LivroController;
import com.first.wisecatalogapi.application.dto.LivroDTO;
import com.first.wisecatalogapi.application.usecases.BuscarLivroUseCase;
import com.first.wisecatalogapi.application.usecases.ImportarLivrosCsvUseCase;
import com.first.wisecatalogapi.application.usecases.VisualizacaoLivroCacheUseCase;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LivroController.class)
class LivroControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImportarLivrosCsvUseCase importarLivrosCsvUseCase;

    @MockBean
    private BuscarLivroUseCase buscarLivroUseCase;

    @MockBean
    private VisualizacaoLivroCacheUseCase visualizacaoLivroCacheUseCase;

    private LivroDTO livroDTO;

    @BeforeEach
    public void setup() {
        livroDTO = new LivroDTO(
                1L,
                "Livro Teste",
                "Autor Teste",
                "Ficção",
                "Fantasia",
                "Ebook",
                29.90,
                4.5,
                150,
                "http://url.com/livro"
        );
    }

    @Test
    void listarTodosLivros_retornaOk_comConteudo() throws Exception {
        Page<LivroDTO> page = new PageImpl<>(List.of(livroDTO));
        when(buscarLivroUseCase.listarTodosLivros(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(livroDTO.id()))
                .andExpect(jsonPath("$.content[0].titulo").value(livroDTO.titulo()));
    }

    @Test
    void listarTodosLivros_retornaNoContent_quandoVazio() throws Exception {
        Page<LivroDTO> pageVazia = Page.empty();
        when(buscarLivroUseCase.listarTodosLivros(any(PageRequest.class))).thenReturn(pageVazia);

        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarLivroPorId_retornaOk() throws Exception {
        when(buscarLivroUseCase.buscarPorId(1L)).thenReturn(livroDTO);
        doNothing().when(visualizacaoLivroCacheUseCase).registrarVisualizacao(anyString(), anyLong());

        mockMvc.perform(get("/api/books/1")
                        .header("session-id", "sessao123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(livroDTO.id()))
                .andExpect(jsonPath("$.titulo").value(livroDTO.titulo()));

        Mockito.verify(visualizacaoLivroCacheUseCase).registrarVisualizacao("sessao123", 1L);
    }

    @Test
    void buscarLivroPorAutor_retornaOk() throws Exception {
        when(buscarLivroUseCase.buscarPorAutor("Autor Teste")).thenReturn(List.of(livroDTO));

        mockMvc.perform(get("/api/books/author/Autor Teste")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].autor").value("Autor Teste"));
    }

    @Test
    void buscarLivroPorGenero_retornaOk() throws Exception {
        when(buscarLivroUseCase.buscarPorGenero("Ficção")).thenReturn(List.of(livroDTO));

        mockMvc.perform(get("/api/books/genre/Ficção")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].genero").value("Ficção"));
    }

    @Test
    void buscarLivrosVisualizadosRecentemente_retornaOk() throws Exception {
        when(visualizacaoLivroCacheUseCase.buscarLivrosVisualizadosRecentemente("sessao123"))
                .thenReturn(List.of(livroDTO));

        mockMvc.perform(get("/api/books/recentsly-viewed")
                        .header("session-id", "sessao123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(livroDTO.id()));
    }

    @Test
    void buscarLivrosVisualizadosRecentemente_retornaNoContent_quandoVazio() throws Exception {
        when(visualizacaoLivroCacheUseCase.buscarLivrosVisualizadosRecentemente("sessao123"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/books/recentsly-viewed")
                        .header("session-id", "sessao123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void importarLivrosCsv_retornaOk() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo",
                "livros.csv",
                "text/csv",
                "id,titulo,autor\n1,Livro Teste,Autor Teste".getBytes()
        );

        doNothing().when(importarLivrosCsvUseCase).importarLivrosCsv(any());

        mockMvc.perform(multipart("/api/books/import")
                        .file(arquivo)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Importação concluída com sucesso!"));
    }

}
