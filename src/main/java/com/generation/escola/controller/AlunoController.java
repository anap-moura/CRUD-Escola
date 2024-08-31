package com.generation.escola.controller;

import com.generation.escola.model.Aluno;
import com.generation.escola.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Aluno>> getAllAlunos() {
        List<Aluno> alunos = alunoRepository.findAll();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> getAlunoById(@PathVariable Long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        return aluno.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Aluno> createAluno(@RequestBody Aluno aluno) {
        try {
            if (alunoRepository.existsByNomeAndIdadeAndNotaPrimeiroSemestreAndNotaSegundoSemestreAndNomeProfessorAndNumeroSala(
                    aluno.getNome(), aluno.getIdade(), aluno.getNotaPrimeiroSemestre(), aluno.getNotaSegundoSemestre(), aluno.getNomeProfessor(), aluno.getNumeroSala())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Aluno novoAluno = alunoRepository.save(aluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAluno);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Aluno> updateAluno(@RequestBody Aluno alunoAtualizado) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(alunoAtualizado.getId());
        if (alunoOptional.isPresent()) {
            Aluno aluno = alunoOptional.get();
            aluno.setNome(alunoAtualizado.getNome());
            aluno.setIdade(alunoAtualizado.getIdade());
            aluno.setNotaPrimeiroSemestre(alunoAtualizado.getNotaPrimeiroSemestre());
            aluno.setNotaSegundoSemestre(alunoAtualizado.getNotaSegundoSemestre());
            aluno.setNomeProfessor(alunoAtualizado.getNomeProfessor());
            aluno.setNumeroSala(alunoAtualizado.getNumeroSala());

            alunoRepository.save(aluno);
            return ResponseEntity.ok(aluno);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluno(@PathVariable Long id) {
        if (alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
