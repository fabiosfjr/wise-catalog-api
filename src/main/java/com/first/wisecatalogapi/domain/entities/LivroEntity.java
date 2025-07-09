package com.first.wisecatalogapi.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "livros_amazon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;

    @Column(name = "genero")
    private String genero;

    @Column(name = "sub_genero")
    private String subGenero;

    private String tipo;
    private Double preco;
    private Double avaliacao;

    @Column(name = "numero_avaliacoes")
    private Integer numeroAvaliacoes;

    private String url;
}
