package br.com.romulo.simplan.initialize;

import br.com.romulo.simplan.domain.Tarefa;
import br.com.romulo.simplan.service.tarefa.TarefaService;
import br.com.romulo.simplan.service.io.file.ManipuladorArquivoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ApplicationInit implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired ManipuladorArquivoService manipuladorArquivoService;
    @Autowired TarefaService tarefaService;

    //arquivo csv para analise
    private final String PATH_CSV_FILE = "src/main/resources/arquivo_para_analise.csv";

    //arquivos de saida / escrita
    private final String UPDATE_TAREFAS_FILE = "src/main/resources/updates_tarefa.sql";
    private final String ROLLBACK_UPDATE_TAREFAS_FILE = "src/main/resources/rollback_updates_tarefas.sql";
    private final String INSERT_OCORRENCIAS_TAREFAS_FILE = "src/main/resources/insert_ocorrencias_tarefas.sql";
    private final String ROLLBACK_INSERTS_OCORRENCIAS_TAREFAS_FILE = "src/main/resources/rollback_inserts_ocorrencias_tarefa.sql";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        List<String[]> arquivoCsvLido = manipuladorArquivoService.lerDadosCsvComSeparadorCustomizado(";", PATH_CSV_FILE);

        arquivoCsvLido.parallelStream().forEach(linha -> {
            /**
             * ------------------------------
             * Métodos para execução aqui
             * ------------------------------
             */


            /**
             * Exemplo de procedimento abaixo
             */
            String campo01 = linha[0];
            String campo02 = linha[1];
            String campo03 = formatarData(linha[2]);

            Tarefa tarefa = tarefaService.buscarTarefaPorDescricao(campo01);
            Long tarefaId = tarefa.getId();

            //Monta arquivo sql com scripts de update e rollback para tarefas
            gerarUpdatesTarefas(UPDATE_TAREFAS_FILE, campo01, campo02);
            gerarRollbackUpdatesTarefas(ROLLBACK_UPDATE_TAREFAS_FILE, campo01, campo03);

            //Montar arquivo sql com scripts de insert e rollback de ocorrencias para as tarefas
            gerarInsertsOcorrenciasTarefas(INSERT_OCORRENCIAS_TAREFAS_FILE, campo01, campo02, campo03);
            gerarRollbackInsertsOcorrenciasTarefas(ROLLBACK_INSERTS_OCORRENCIAS_TAREFAS_FILE, tarefaId, campo02);
        });
    }

    private void gerarUpdatesTarefas(String pathFileOutput, String ...parametros) {
        StringBuilder sql = new StringBuilder();
        sql.append("script sql aqui");

        manipuladorArquivoService.escreverConteudoArquivo(pathFileOutput, sql.toString());
    }

    private void gerarRollbackUpdatesTarefas(String pathFileOutput, String ...parametros) {
        StringBuilder sql = new StringBuilder();
        sql.append("script sql aqui");

        manipuladorArquivoService.escreverConteudoArquivo(pathFileOutput, sql.toString());
    }

    private void gerarInsertsOcorrenciasTarefas(String pathFileOutput, String ...parametros) {
        StringBuilder sql = new StringBuilder();
        sql.append("script sql aqui");

        manipuladorArquivoService.escreverConteudoArquivo(pathFileOutput, sql.toString());
    }

    private void gerarRollbackInsertsOcorrenciasTarefas(String pathFileOutput, Long tarefaId, String ...parametros) {
        StringBuilder sql = new StringBuilder();
        sql.append("script sql aqui");

        manipuladorArquivoService.escreverConteudoArquivo(pathFileOutput, sql.toString());
    }

    // formata como yyyy-MM-dd
    private String formatarData(String dataDiaMesAno) {
        String[] partesData = dataDiaMesAno.split("/");

        String ano = partesData[2];
        String mes = (partesData[1].length()==1) ? "0".concat(partesData[1]) : partesData[1];
        String dia = (partesData[0].length()==1) ? "0".concat(partesData[0]) : partesData[0];

        return ano.concat("-").concat(mes).concat("-").concat(dia);
    }

}
