# 📚 Wise Catalog API (O Catálogo do Sábio)

API REST para gerenciamento e busca de livros da Amazon, com suporte a cache, upload de CSV, visualizações recentes e execução via Docker.

---

Organizada em camadas bem definidas:

- `application`: Casos de uso da aplicação
- `domain`: Entidades e regras de negócio
- `adapters`: Controllers e DTOs
- `infrastructure`: Banco de dados, Redis e configurações

### 🚀 Tecnologias Utilizadas:

| Tecnologia      | Descrição                        |
|-----------------|----------------------------------|
| ![Java](https://img.shields.io/badge/Java-17-blue) | Linguagem principal |
| ![Spring](https://img.shields.io/badge/SpringBoot-3.0.0-brightgreen) | Framework principal |
| ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue) | Banco de dados |
| ![Redis](https://img.shields.io/badge/Redis-cache-red) | Cache e armazenamento de sessões |
| ![Docker](https://img.shields.io/badge/Docker-container-blue) | Containerização |
| ![Flyway](https://img.shields.io/badge/Flyway-migrations-orange) | Controle de versionamento de banco |
| ![Swagger](https://img.shields.io/badge/Swagger-API--Docs-yellow) | Documentação da API |
| ![OpenCSV](https://img.shields.io/badge/OpenCSV-csv-lightgrey) | Leitura de arquivos |

---
## 🧠 I. Arquitetura de Solução e Arquitetura Técnica

### 🏗️ Arquitetura: Clean Architecture
## 💡 Por que Arquitetura Limpa?

A **Arquitetura Limpa** foi escolhida para manter uma separação clara entre camadas da aplicação, facilitando:

- Testabilidade: camada de domínio pura, sem dependências de frameworks
- Manutenção e extensibilidade com menos acoplamento
- Independência da tecnologia, tornando a aplicação mais flexível para futuras mudanças

O uso de **princípios de Código Limpo** como SOLID, SRP e coesão garante código legível, com baixo acoplamento e fácil de evoluir.

---

## 📄 II. Explicação sobre o Case Desenvolvido (Plano de Implementação)

### ✅ Funcionalidades implementadas

- 📥 **Importação de livros via CSV**: `POST /api/books/import`
- 🔎 **Busca paginada de livros**: `GET /api/books`
- 📚 **Busca por autor e gênero**: `GET /api/books/author/{autor}`, `genre/{genero}`
- 📖 **Detalhes do livro por ID**, registrando visualizações: `GET /api/books/{id}`
- 🕵️‍♂️ **Livros visualizados recentemente** por sessão: `GET /api/books/recentsly-viewed`

### 🗃️ Modelagem do Banco de Dados

Tabela: `livros_amazon`  
Com índices otimizados para busca por `autor` e `genero`.

📸 **Imagem ilustrativa da estrutura da tabela:**

![modelagem-livros-amazon.png.png](src%2Fmain%2Fresources%2Fdocs%2Fmodelagem-livros-amazon.png.png)
## 📊 Estrutura SQL

```sql
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
```
---

## 📂 Dataset Utilizado

O conjunto de dados foi obtido do [Amazon Books Dataset no Kaggle](https://www.kaggle.com/datasets/chhavidhankhar11/amazon-books-dataset?resource=download).

📎 Download direto do CSV usado no projeto:  
[📥 Baixar Books_df.csv](./Books_df.csv)

---

## 🐘 Por que usar o Flyway?

- Permite versionar o schema do banco de dados com segurança
- Traz rastreabilidade das alterações no banco
- Automatiza o setup do banco em ambientes diferentes
- Permite rollback seguro e evita divergência entre dev/prod

As migrations estão localizadas no diretório:  
`src/main/resources/db/migration`

---

## 🚢 III. Melhorias e Considerações Finais

### 🔧 Melhorias Futuras

- Autenticação com JWT e controle por usuário
- Armazenar visualizações recentes por login
- Adicionar filtros avançados de busca (faixa de preço, avaliação etc.)
- Implementar CI/CD para builds automáticos

### 🤔 Considerações

- Docker garantiu portabilidade e reprodutibilidade
- Redis melhorou performance e UX
- Arquitetura Limpa facilitou testes e modularidade

---

## 🧩 Diagrama de Arquitetura (C4-Container)

![diagrama-arquitetura-Wise_Catalog_API.png](src%2Fmain%2Fresources%2Fdocs%2Fdiagrama-arquitetura-Wise_Catalog_API.png)

---

## ⚙️ Execução com Docker

### 📌 Pré-requisitos

- Docker
- Docker Compose

### 🔧 Build do projeto

```bash
git clone https://github.com/seu-usuario/wise-catalog-api.git
cd wise-catalog-api
./mvnw clean package -DskipTests

### 🚀 Rodar via Docker Compose

```bash
docker-compose up --build
```

Acesse em: [http://localhost:8080](http://localhost:8080)

### 📑 Swagger

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🧪 Instruções para Teste da API

1. Faça o upload do arquivo CSV no endpoint:

   ```http
   POST /api/books/import
   Content-Type: multipart/form-data
   Body: arquivo Books_df.csv
   ```

2. Após a importação ser concluída com sucesso, acesse os endpoints:

    - `GET /api/books/{id}` → Detalhe de um livro (exige header `session-id`)
    - `GET /api/books/author/{author}` → Livros por autor
    - `GET /api/books/genre/{genre}` → Livros por gênero

3. A cada chamada ao `GET /api/books/{id}` com `session-id`, o livro será armazenado no Redis.

    - Use `GET /api/books/recentsly-viewed` com o mesmo `session-id` para ver os últimos 5 livros acessados.

---

## 🧑‍💻 Autor

Desenvolvido por Fábio Ferreira para o desafio técnico proposto.

Para dúvidas, sugestões ou contribuições, abra uma issue ou envie um PR. 🚀


