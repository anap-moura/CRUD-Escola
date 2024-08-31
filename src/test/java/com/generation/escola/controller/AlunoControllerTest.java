package com.generation.escola.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.escola.model.Aluno;
import com.generation.escola.repository.AlunoRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AlunoControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AlunoRepository alunoRepository;

    @BeforeAll
    void start() {
        alunoRepository.deleteAll();
    }

    @Test
    @DisplayName("Cadastrar Um Aluno")
    public void deveCriarUmAluno() {
        Aluno novoAluno = new Aluno(null, "Maria Santos", 17, 6.5, 7.0, "Professor Ana", "102");

        HttpEntity<Aluno> corpoRequisicao = new HttpEntity<>(novoAluno);

        ResponseEntity<Aluno> corpoResposta = testRestTemplate
                .exchange("/alunos/cadastrar", HttpMethod.POST, corpoRequisicao, Aluno.class);

        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());

        Aluno alunoCriado = corpoResposta.getBody();
        assertNotNull(alunoCriado);
        assertNotNull(alunoCriado.getId()); 
    }

    @Test
    @DisplayName("Não deve permitir duplicação do Aluno")
    public void naoDeveDuplicarAluno() {
        Aluno alunoExistente = new Aluno(null, "Pedro Oliveira", 16, 7.0, 6.5, "Professor José", "103");
        alunoRepository.save(alunoExistente);

        HttpEntity<Aluno> corpoRequisicao = new HttpEntity<>(alunoExistente);

        ResponseEntity<Aluno> corpoResposta = testRestTemplate
                .exchange("/alunos/cadastrar", HttpMethod.POST, corpoRequisicao, Aluno.class);

        assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Atualizar um Aluno")
    public void deveAtualizarUmAluno() {
        Aluno alunoExistente = alunoRepository.save(new Aluno(null, "Ana Souza", 19, 8.0, 8.5, "Professor Lucas", "104"));

        Aluno alunoUpdate = new Aluno(alunoExistente.getId(), "Ana Souza Mendes", 19, 8.0, 8.5, "Professor Lucas", "104");

        HttpEntity<Aluno> corpoRequisicao = new HttpEntity<>(alunoUpdate);

        ResponseEntity<Aluno> corpoResposta = testRestTemplate
                .exchange("/alunos/atualizar", HttpMethod.PUT, corpoRequisicao, Aluno.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());

        Aluno alunoAtualizado = corpoResposta.getBody();
        assertNotNull(alunoAtualizado);
        assertEquals("Ana Souza Mendes", alunoAtualizado.getNome());
    }

    @Test
    @DisplayName("Listar todos os Alunos")
    public void deveMostrarTodosAlunos() {
        alunoRepository.save(new Aluno(null, "Carlos Mendes", 20, 9.0, 8.8, "Professor Paulo", "105"));
        alunoRepository.save(new Aluno(null, "Fernanda Almeida", 21, 7.8, 9.2, "Professor Clara", "106"));

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/alunos/all", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
           }
}
