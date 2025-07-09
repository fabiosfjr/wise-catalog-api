package com.first.wisecatalogapi.adapters.input;

import com.first.wisecatalogapi.application.dto.LivroDTO;
import com.first.wisecatalogapi.application.usecases.BuscarLivroUseCase;
import com.first.wisecatalogapi.application.usecases.ImportarLivrosCSVUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final ImportarLivrosCSVUseCase importarLivrosCsvUseCase;
    private final BuscarLivroUseCase buscarLivroUseCase;

    public LivroController(ImportarLivrosCSVUseCase importarLivrosCsvUseCase,
                           BuscarLivroUseCase buscarLivroUseCase) {
        this.importarLivrosCsvUseCase = importarLivrosCsvUseCase;
        this.buscarLivroUseCase = buscarLivroUseCase;
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
    public ResponseEntity<LivroDTO> buscarLivroPorId(@PathVariable Long id) {
        var livro = buscarLivroUseCase.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LivroDTO>> buscarLivroPorAutor(@PathVariable String autor) {
        var livros = buscarLivroUseCase.buscarPorAutor(autor);
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<LivroDTO>> buscarLivroPorGenero(@PathVariable String genero) {
        var livros = buscarLivroUseCase.buscarPorGenero(genero);
        return ResponseEntity.ok(livros);
    }

    @PostMapping("/importar")
    public String importarLivrosCsv(@RequestParam String caminhoArquivoCsv) {
        try {
            importarLivrosCsvUseCase.importarLivrosCsv(caminhoArquivoCsv);
            return "Importação concluída com sucesso!";
        } catch (Exception e) {
            return "Erro durante a importação: " + e.getMessage();
        }
    }
}
