package br.com.LiterTech.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosRespostaAPI(@JsonAlias("results") List<DadosLivro> resultados) {
}
