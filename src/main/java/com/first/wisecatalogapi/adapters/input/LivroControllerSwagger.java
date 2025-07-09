package com.first.wisecatalogapi.adapters.input;

import com.first.wisecatalogapi.application.dto.LivroDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "LivroController", description = "Gerenciamento e consultas dos livros importados da Amazon")
public interface LivroControllerSwagger {

    @Operation(summary = "Listar livros paginados")
    ResponseEntity<Page<LivroDTO>> listarTodosLivros(
            @Parameter(description = "Número da página") int page,
            @Parameter(description = "Quantidade de elementos por página") int size);

    @Operation(summary = "Buscar livro por ID e registrar visualização")
    ResponseEntity<LivroDTO> buscarLivroPorId(Long id, String sessionId);

    @Operation(summary = "Buscar livros por autor")
    ResponseEntity<List<LivroDTO>> buscarLivroPorAutor(String author);

    @Operation(summary = "Buscar livros por gênero")
    ResponseEntity<List<LivroDTO>> buscarLivroPorGenero(String genre);

    @Operation(summary = "Buscar os últimos 5 livros visualizados recentemente")
    ResponseEntity<List<LivroDTO>> buscarLivrosVisualizadosRecentemente(String sessionId);

    @Operation(summary = "Importar livros via arquivo CSV")
    ResponseEntity<String> importarLivrosCsv(MultipartFile arquivo);
}
