# LiterTech: Catálogo de Livros

Um catálogo interativo de livros e autores, desenvolvido em Java com Spring Boot, que busca dados de uma API externa (Gutendex), armazena-os em um banco de dados PostgreSQL e permite ao usuário consultar, filtrar e remover registros via console.

---

## Funcionalidades

O LiterTech oferece as seguintes opções de interação via menu no console:

* **Buscar Livro por Título:** Realiza uma busca na API Gutendex e, se o livro não existir no banco de dados local, o salva junto com seu autor principal.
* **Listar Livros Registrados:** Exibe todos os livros que foram salvos no banco de dados.
* **Listar Autores Registrados:** Exibe todos os autores que foram salvos no banco de dados.
* **Listar Autores Vivos em Determinado Ano:** Filtra e exibe autores que estavam vivos em um ano específico, com base nos dados registrados.
* **Listar Livros por Idioma:** Filtra e exibe livros por um idioma específico (ex: "en", "fr", "pt").
* **Listar TOP 10 Livros por Downloads:** Mostra os dez livros mais baixados (com base no dado de downloads da API) registrados no seu catálogo.
* **Buscar Autor por Nome:** Permite buscar autores já salvos no banco de dados por uma parte do nome.
* **Listar Livros de um Autor Específico:** Exibe todos os livros de um autor que já estejam registrados no banco de dados.
* **Exibir Estatísticas de Idiomas:** Apresenta a quantidade de livros registrados para idiomas específicos (Inglês, Francês, Português).
* **Remover Livro por Título:** Permite remover um ou mais livros do banco de dados com base no título, com uma confirmação antes da exclusão.

---

## Tecnologias Utilizadas

* **Java 17 (ou superior)**
* **Spring Boot 3.x:** Para rápida configuração e execução da aplicação.
* **Spring Data JPA:** Para persistência de dados e interações com o banco de dados.
* **Hibernate:** Implementação padrão do JPA no Spring Boot.
* **PostgreSQL:** Banco de dados relacional para armazenamento dos livros e autores.
* **Jackson Databind:** Para desserialização de JSON (conversão de dados da API para objetos Java).
* **java.net.http.HttpClient:** API nativa do Java para realizar requisições HTTP.
* **Gutendex API:** API pública utilizada para buscar informações sobre livros.

---

## Como Rodar o Projeto

Siga estas instruções para configurar e executar o LiterTech em sua máquina:

### Pré-requisitos

Ter instalado em sua máquina:

* **Java Development Kit (JDK) 17** ou superior.
* Um **ambiente de desenvolvimento (IDE)** como IntelliJ IDEA (Community Edition) ou VS Code com as extensões Java.
* **Maven** (gerenciador de dependências, já vem integrado na maioria das IDEs).
* **PostgreSQL:** Servidor de banco de dados rodando localmente (ou em um container Docker, por exemplo).

### Configuração do Banco de Dados

1.  **Crie um banco de dados PostgreSQL** chamado `litertech_db`.
    * Via `psql` (terminal):
        ```bash
        psql -U seu_usuario_postgres
        CREATE DATABASE litertech_db;
        \q
        ```
    * Via ferramenta gráfica (DBeaver, pgAdmin): Crie um novo banco de dados com o nome `litertech_db`.
2.  **Configure as credenciais** no arquivo `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/litertech_db
    spring.datasource.username=seu_usuario_do_banco
    spring.datasource.password=sua_senha_do_banco
    spring.datasource.driver-class-name=org.postgresql.Driver

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```
    **Lembre-se de substituir `seu_usuario_do_banco` e `sua_senha_do_banco` pelas suas credenciais reais do PostgreSQL.**

### Executando a Aplicação

1.  **Clone este repositório** (se estiver em um repositório Git) ou abra o projeto em sua IDE.
2.  **Navegue até o diretório raiz do projeto** no terminal, onde o arquivo `pom.xml` está localizado.
3.  **Compile e execute o projeto** usando Maven:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    Ou, se estiver usando uma IDE como IntelliJ IDEA, basta abrir o projeto e **executar a classe `Principal.java`** (clicando no botão `Run` ou no menu `Run`).

4.  O menu interativo do LiterTech será exibido no console. Siga as opções para interagir com o catálogo.

---

## Autor

* **Geovani Galdino**
* [LinkedIn: https://www.linkedin.com/in/geovanigaldino/](https://www.linkedin.com/in/geovanigaldino/)
