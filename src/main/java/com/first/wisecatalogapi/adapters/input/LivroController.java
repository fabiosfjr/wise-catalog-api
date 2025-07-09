package com.first.wisecatalogapi.adapters.input;

import com.first.wisecatalogapi.application.dto.LivroDTO;
import com.first.wisecatalogapi.application.usecases.BuscarLivroUseCase;
import com.first.wisecatalogapi.application.usecases.ImportarLivrosCsvUseCase;
import com.first.wisecatalogapi.application.usecases.VisualizacaoLivroCacheUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class LivroController implements LivroControllerSwagger {

    private final ImportarLivrosCsvUseCase importarLivrosCsvUseCase;
    private final BuscarLivroUseCase buscarLivroUseCase;
    private final VisualizacaoLivroCacheUseCase visualizacaoLivroCacheUseCase;

    public LivroController(ImportarLivrosCsvUseCase importarLivrosCsvUseCase,
                           BuscarLivroUseCase buscarLivroUseCase,
                           VisualizacaoLivroCacheUseCase visualizacaoLivroCacheUseCase) {
        this.importarLivrosCsvUseCase = importarLivrosCsvUseCase;
        this.buscarLivroUseCase = buscarLivroUseCase;
        this.visualizacaoLivroCacheUseCase = visualizacaoLivroCacheUseCase;
    }

    @GetMapping
    public ResponseEntity<Page<LivroDTO>> listarTodosLivros(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        var livros = buscarLivroUseCase.listarTodosLivros(pageable);

        if (livros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroDTO> buscarLivroPorId(@PathVariable Long id,
                                                     @RequestHeader("session-id") String sessionId) {
        var livro = buscarLivroUseCase.buscarPorId(id);
        visualizacaoLivroCacheUseCase.registrarVisualizacao(sessionId, id);
        return ResponseEntity.ok(livro);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<LivroDTO>> buscarLivroPorAutor(@PathVariable String author) {
        var livros = buscarLivroUseCase.buscarPorAutor(author);
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<LivroDTO>> buscarLivroPorGenero(@PathVariable String genre) {
        var livros = buscarLivroUseCase.buscarPorGenero(genre);
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/recentsly-viewed")
    public ResponseEntity<List<LivroDTO>> buscarLivrosVisualizadosRecentemente(@RequestHeader("session-id") String sessionId) {
        var livros = visualizacaoLivroCacheUseCase.buscarLivrosVisualizadosRecentemente(sessionId);

        if (livros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(livros);
    }

    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<String> importarLivrosCsv(@RequestParam("arquivo") MultipartFile arquivo) {
        importarLivrosCsvUseCase.importarLivrosCsv(arquivo);
        return ResponseEntity.ok("Importação concluída com sucesso!");
    }
}
