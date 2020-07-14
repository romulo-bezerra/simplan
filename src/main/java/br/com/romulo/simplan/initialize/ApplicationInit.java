package br.com.romulo.simplan.initialize;

import br.com.romulo.simplan.domain.Adesao;
import br.com.romulo.simplan.service.adesao.AdesaoService;
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
    @Autowired AdesaoService adesaoService;

    //arquivo csv para analise
    private final String PATH_CSV_FILE = "src/main/resources/retroativos_quitacao.csv";

    //arquivos de saida / escrita
    private final String UPDATE_RETROATIVOS_QUITACAO_FILE = "src/main/resources/updates_retroativos_quitacao.sql";
    private final String ROLLBACK_UPDATE_RETROATIVOS_QUITACAO_FILE = "src/main/resources/rollback_updates_retroativos_quitacao.sql";
    private final String INSERT_OCORRENCIAS_QUITACAO_FILE = "src/main/resources/insert_ocorrencias_quitacao.sql";
    private final String ROLLBACK_INSERTS_OCORRENCIAS_QUITACAO_FILE = "src/main/resources/rollback_inserts_ocorrencias_quitacao.sql";
    private final String ANALISE_EXISTENCIA_ADESAO_SEGURADORA_FILE = "src/main/resources/quitacoes_codigos_portadores_null.csv";

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
            String codigoAdesao = linha[0];
            String codigoSeguro = linha[1];
            String dataQuitacao = formatarData(linha[2]);

            Adesao adesao = adesaoService.buscarAdesaoPorCodigoAdesao(codigoAdesao);
            String codigoPortador = adesao.getCodigoPortador();

            //Monta arquivo sql com scripts de update e rollback para quitação de adesoes
            gerarUpdatesQuitacao(UPDATE_RETROATIVOS_QUITACAO_FILE, codigoAdesao, codigoSeguro, dataQuitacao);
            gerarRollbackUpdatesQuitacao(ROLLBACK_UPDATE_RETROATIVOS_QUITACAO_FILE, codigoAdesao, codigoSeguro);

            //Montar arquivo sql com scripts de insert e rollback de ocorrencias para as quitação de adesoes
            gerarInsertsOcorrenciasQuitacao(INSERT_OCORRENCIAS_QUITACAO_FILE, codigoAdesao, dataQuitacao, codigoPortador);
            gerarRollbackInsertsOcorrenciasQuitacao(ROLLBACK_INSERTS_OCORRENCIAS_QUITACAO_FILE, codigoAdesao, dataQuitacao, codigoPortador);

            //Monta arquivo no formato CSV com as adesoes que nao existem do lado da seguradora
            analisadorExistenciaAdesaoSeguradora(ANALISE_EXISTENCIA_ADESAO_SEGURADORA_FILE, linha);
        });
    }

    private void analisadorExistenciaAdesaoSeguradora(String pathFileOutput, String[] data) {
        String codigoAdesao = data[0];
        String codigoSeguro = data[1];
        String dataQuitacao = data[2];

        Adesao adesao = adesaoService.buscarAdesaoPorCodigoAdesao(codigoAdesao);

        if (adesao.getCodigoAdesao() == null) {
            String csv = codigoAdesao.concat(";").concat(codigoSeguro).concat(";").concat(formatarData(dataQuitacao));
            manipuladorArquivoService.escreverConteudoArquivo(pathFileOutput, csv);
        }
    }

    private void gerarUpdatesQuitacao(String pathFileOutput, String codigoAdesao, String codigoSeguro, String dataQuitacao) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE t_adesao ");
        sql.append("SET status = 'QUITADO', ");
        sql.append(String.format("cancelamento = TIMESTAMP '%s 00:00:00.000000', ", dataQuitacao));
        sql.append("meio_cancelamento = 'MIGRACAO' ");
        sql.append(String.format("WHERE codigo_adesao = '%s' ", codigoAdesao));
        sql.append(String.format("AND codigo_seguro = '%s';", codigoSeguro));

        manipuladorArquivoService.escreverConteudoArquivo(pathFileOutput, sql.toString());
    }

    private void gerarRollbackUpdatesQuitacao(String pathFileOutput, String codigoAdesao, String codigoSeguro) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE t_adesao ");
        sql.append("SET status = 'ATIVO', ");
        sql.append("cancelamento = null, ");
        sql.append("meio_cancelamento = null ");
        sql.append(String.format("WHERE codigo_adesao = '%s' ", codigoAdesao));
        sql.append(String.format("AND codigo_seguro = '%s';", codigoSeguro));

        manipuladorArquivoService.escreverConteudoArquivo(pathFileOutput, sql.toString());
    }

    private void gerarInsertsOcorrenciasQuitacao(String pathFileOutput, String codigoAdesao, String dataQuitacao, String codigoPortador) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO T_OCORRENCIA ");
        sql.append("(IDOCORRENCIA, DATA, DESCRICAO, IDUSUARIO, IDCLIENTECOBRANCA, IDTIPOOCORRENCIA, ALERTADATA, ");
        sql.append("ALERTASTATUS, ALERTAATIVO, STATUS, IDUSUARIOCANCELAMENTO, DATACANCELAMENTO, DATAPREVISTAPAGAMENTO, ");
        sql.append("IDCLIENTETITULARPESSOAFISICA, IDUSUARIOCONCLUSAOAGENDAMENTO, DATACONCLUSAOAGENDAMENTO, ");
        sql.append("IDATENDIMENTOCLIENTE, VERSION, IDORIGEM, IDORIGEMALTERACAO, CODIGOEXTERNO)");
        sql.append(String.format("VALUES (S_OCORRENCIA.NEXTVAL,TIMESTAMP '%s 00:00:00.000000',", dataQuitacao));
        sql.append(String.format("'[SEGURO BACKSEG] QUITAÇÃO DE SEGURO: %s - COMPRA DA SORTE',-5,NULL,", codigoAdesao));
        sql.append(String.format("-5,NULL,'N','N','A',NULL,NULL,NULL,%s,NULL,NULL,NULL,NULL,0,0,NULL);", codigoPortador));

        manipuladorArquivoService.escreverConteudoArquivo(pathFileOutput, sql.toString());
    }

    private void gerarRollbackInsertsOcorrenciasQuitacao(String pathFileOutput, String codigoAdesao, String dataQuitacao, String codigoPortador) {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("DELETE FROM T_OCORRENCIA WHERE DATA = TIMESTAMP '%s 00:00:00.000000' ", dataQuitacao));
        sql.append(String.format("AND DESCRICAO = '[SEGURO BACKSEG] QUITAÇÃO DE SEGURO: %s - COMPRA DA SORTE' ", codigoAdesao));
        sql.append(String.format("AND IDCLIENTETITULARPESSOAFISICA = %s;", codigoPortador));

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
