package com.generation.escola.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.generation.escola.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
           "FROM Aluno a WHERE a.nome = :nome " +
           "AND a.idade = :idade " +
           "AND a.notaPrimeiroSemestre = :notaPrimeiroSemestre " +
           "AND a.notaSegundoSemestre = :notaSegundoSemestre " +
           "AND a.nomeProfessor = :nomeProfessor " +
           "AND a.numeroSala = :numeroSala")
    boolean existsByNomeAndIdadeAndNotaPrimeiroSemestreAndNotaSegundoSemestreAndNomeProfessorAndNumeroSala(
        @Param("nome") String nome,
        @Param("idade") Integer idade,
        @Param("notaPrimeiroSemestre") Double notaPrimeiroSemestre,
        @Param("notaSegundoSemestre") Double notaSegundoSemestre,
        @Param("nomeProfessor") String nomeProfessor,
        @Param("numeroSala") String numeroSala
    );
}
