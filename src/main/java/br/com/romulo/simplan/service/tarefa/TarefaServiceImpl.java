package br.com.romulo.simplan.service.tarefa;

import br.com.romulo.simplan.domain.Tarefa;
import br.com.romulo.simplan.repository.TarefaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TarefaServiceImpl implements TarefaService {

    @Autowired TarefaRepository tarefaRepository;

    @Override
    public Tarefa buscarTarefaPorDescricao(String descricao) {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findByDescricao(descricao);
        if (tarefaOptional.isPresent()) return tarefaOptional.get();
        else log.error("FALHA NA BUSCA DA TAREFA DE DESCRICAO {}", descricao);
        return new Tarefa();
    }

}