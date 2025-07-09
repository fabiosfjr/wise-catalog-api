package com.first.wisecatalogapi.application.services;

import com.first.wisecatalogapi.domain.entities.LivroEntity;
import com.first.wisecatalogapi.domain.exceptions.ErroImportacaoCsvException;
import com.first.wisecatalogapi.infrastructure.databse.LivroJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ImportarLivrosServiceTest {

    @Mock
    private LivroJpaRepository livroJpaRepository;

    @InjectMocks
    private ImportarLivrosService importarLivrosService;

    private MultipartFile arquivoCsvValido;

    @BeforeEach
    void setUp() {
        String csv = "id,titulo,autor,genero,subGenero,tipo,preco,avaliacao,numeroAvaliacoes,url\n" +
                "1,Título Teste,Autor Teste,Genero Teste,SubGenero Teste,Tipo Teste,\"29.90\",4.5,100,http://url.teste";

        arquivoCsvValido = new MockMultipartFile(
                "arquivo",
                "livros.csv",
                "text/csv",
                csv.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    void deveImportarLivrosCsvComSucesso() {
        importarLivrosService.importarLivrosCsv(arquivoCsvValido);

        ArgumentCaptor<LivroEntity> captor = ArgumentCaptor.forClass(LivroEntity.class);
        verify(livroJpaRepository, times(1)).save(captor.capture());

        LivroEntity livroSalvo = captor.getValue();

        assertEquals(1L, livroSalvo.getId());
        assertEquals("Título Teste", livroSalvo.getTitulo());
        assertEquals("Autor Teste", livroSalvo.getAutor());
        assertEquals("Genero Teste", livroSalvo.getGenero());
        assertEquals("SubGenero Teste", livroSalvo.getSubGenero());
        assertEquals("Tipo Teste", livroSalvo.getTipo());
        assertEquals(29.90, livroSalvo.getPreco());
        assertEquals(4.5, livroSalvo.getAvaliacao());
        assertEquals(100, livroSalvo.getNumeroAvaliacoes());
        assertEquals("http://url.teste", livroSalvo.getUrl());
    }

    @Test
    void deveLancarExcecaoQuandoArquivoInvalido() throws IOException {
        MultipartFile arquivoInvalido = mock(MultipartFile.class);
        when(arquivoInvalido.getInputStream()).thenThrow(new IOException("Erro de IO"));

        ErroImportacaoCsvException exception = assertThrows(ErroImportacaoCsvException.class,
                () -> importarLivrosService.importarLivrosCsv(arquivoInvalido));

        assertEquals("Erro ao importar o arquivo CSV", exception.getMessage());
        verifyNoInteractions(livroJpaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoCsvInvalido() throws IOException {
        MultipartFile arquivoInvalido = mock(MultipartFile.class);
        when(arquivoInvalido.getInputStream()).thenThrow(new IOException("Erro ao ler arquivo"));

        ErroImportacaoCsvException exception = assertThrows(ErroImportacaoCsvException.class,
                () -> importarLivrosService.importarLivrosCsv(arquivoInvalido));

        assertEquals("Erro ao importar o arquivo CSV", exception.getMessage());
        verifyNoInteractions(livroJpaRepository);
    }
}