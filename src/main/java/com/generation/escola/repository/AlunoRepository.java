package com.generation.escola.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.escola.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

}