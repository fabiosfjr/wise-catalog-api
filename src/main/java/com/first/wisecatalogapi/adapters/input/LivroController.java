package com.first.wisecatalogapi.adapters.input;

import com.first.wisecatalogapi.application.dto.LivroDTO;
import com.first.wisecatalogapi.application.usecases.BuscarLivroUseCase;
import com.first.wisecatalogapi.application.usecases.ImportarLivrosCsvUseCase;
import com.first.wisecatalogapi.application.usecases.VisualizacaoLivroCacheUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class LivroController {

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
    public ResponseEntity<List<LivroDTO>> buscarLivroPorAutor(@PathVariable String autor) {
        var livros = buscarLivroUseCase.buscarPorAutor(autor);
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<LivroDTO>> buscarLivroPorGenero(@PathVariable String genero) {
        var livros = buscarLivroUseCase.buscarPorGenero(genero);
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

    @PostMapping("/import")
    public ResponseEntity<String> importarLivrosCsv(@RequestParam String caminhoArquivoCsv) throws FileNotFoundException {
        importarLivrosCsvUseCase.importarLivrosCsv(caminhoArquivoCsv);
        return ResponseEntity.ok("Importação concluída com sucesso!");
    }
}
