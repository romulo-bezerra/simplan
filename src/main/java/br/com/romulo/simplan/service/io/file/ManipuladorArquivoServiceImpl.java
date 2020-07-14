package br.com.romulo.simplan.service.io.file;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Slf4j
@Service
public class ManipuladorArquivoServiceImpl implements ManipuladorArquivoService {

    @Override
    public List<String[]> lerDadosCsvComSeparadorCustomizado(String separator, String file) {
        try {
            FileReader filereader = new FileReader(file);

            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(separator.charAt(0))
                    .build();

            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .withSkipLines(1) //ignora a primeira linha de cabecalho
                    .build();

            return csvReader.readAll();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void escreverConteudoArquivo(String pathFile, String text) {
        BufferedWriter writer = null;
        try {
            File file = new File(pathFile);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            log.error("Erro: Não foi possível escrever no arquivo. Mensagem: {}", e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("Erro: Não foi possivel fechar conexão com o arquivo. Mensagem: {}", e.getMessage());
                }
            }
        }
    }

}
