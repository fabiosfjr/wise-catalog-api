CREATE TABLE livros_amazon (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(1000) NOT NULL,
    autor VARCHAR(255),
    genero VARCHAR(255) NOT NULL,
    sub_genero VARCHAR(255),
    tipo VARCHAR(255),
    preco NUMERIC(10, 2) CHECK (preco >= 0),
    avaliacao NUMERIC(3, 2) CHECK (avaliacao >= 0 AND avaliacao <= 5),
    numero_avaliacoes INT CHECK (numero_avaliacoes >= 0),
    url TEXT
);

CREATE INDEX idx_livros_autor ON livros_amazon (autor);
CREATE INDEX idx_livros_genero ON livros_amazon (genero);