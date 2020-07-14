package br.com.romulo.simplan.service.tarefa;

import br.com.romulo.simplan.domain.Tarefa;

public interface TarefaService {

    Tarefa buscarTarefaPorDescricao(String descricao);

}
