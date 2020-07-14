package br.com.romulo.simplan.service.io.file;

import java.util.List;

public interface ManipuladorArquivoService {

    List<String[]> lerDadosCsvComSeparadorCustomizado(String separator, String file);
    void escreverConteudoArquivo(String pathFile, String text);

}
