# 📚 LiterTech: Catálogo de Livros Interativo

**LiterTech** é um catálogo de livros e autores desenvolvido em **Java com Spring Boot**, que consome dados da [API Gutendex](https://gutendex.com/), armazena-os em um banco de dados **PostgreSQL** e permite ao usuário consultar, filtrar e remover registros via **console interativo**.

---

## ✨ Funcionalidades

O projeto oferece as seguintes opções através de um **menu no console**:

* 🔍 **Buscar Livro por Título**
  Busca um livro na API e salva (se ainda não estiver registrado) junto com seu autor principal.

* 📚 **Listar Livros Registrados**
  Exibe todos os livros salvos no banco de dados.

* 👨‍💼 **Listar Autores Registrados**
  Mostra todos os autores salvos.

* 🦳 **Listar Autores Vivos em Determinado Ano**
  Filtra autores que estavam vivos em um ano informado.

* 🌍 **Listar Livros por Idioma**
  Filtra livros por código de idioma (ex: `en`, `fr`, `pt`).

* 📈 **TOP 10 Livros por Downloads**
  Exibe os dez livros mais populares segundo o número de downloads.

* 🧑‍💻 **Buscar Autor por Nome**
  Permite buscar autores por parte do nome.

* 📘 **Listar Livros de um Autor Específico**
  Exibe todos os livros de um autor já registrado.

* 📂 **Estatísticas de Idiomas**
  Mostra a quantidade de livros registrados por idioma.

* ❌ **Remover Livro por Título**
  Remove livros com base no título, com confirmação.

---

## 💠 Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3.x**
* **Spring Data JPA**
* **Hibernate**
* **PostgreSQL**
* **Jackson Databind** (para conversão de JSON)
* **java.net.http.HttpClient** (requisições HTTP nativas)
* **API [Gutendex](https://gutendex.com/)**

---

## ⚙️ Como Rodar o Projeto

### ✅ Pré-requisitos

* Java 17+
* IDE Java (IntelliJ IDEA, VS Code etc.)
* Maven
* PostgreSQL

### 📦 Configuração do Banco de Dados

1. Crie o banco:

```bash
psql -U seu_usuario_postgres
CREATE DATABASE litertech_db;
\q
```

Ou use uma ferramenta gráfica como **pgAdmin** ou **DBeaver**.

2. Configure o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/litertech_db
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 🚀 Executando a Aplicação

1. Clone ou baixe o repositório.
2. No terminal:

```bash
mvn clean install
mvn spring-boot:run
```

Ou simplesmente execute a classe `Principal.java` via IDE.

---

## 👨‍💼 Autor

**Geovani Galdino**
🔗 [LinkedIn](https://www.linkedin.com/in/geovanigaldino/)
