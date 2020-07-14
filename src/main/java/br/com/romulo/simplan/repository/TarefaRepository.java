package br.com.romulo.simplan.repository;

import br.com.romulo.simplan.domain.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    Optional<Tarefa> findByDescricao(String descricao);

}