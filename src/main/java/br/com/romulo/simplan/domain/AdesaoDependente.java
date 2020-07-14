package br.com.romulo.simplan.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AdesaoDependente {

    private String codigoEmissor;
    private String codigoSeguro;
    private String codigoPortador;
    private String cpfPortador;
    private String status;
    private String cadastro;
    private String cancelamento;
    private String usuarioAdesao;
    private String meioAdesao;
    private String meioCancelamento;
    private String usuarioCancelamento;
    private String codigoOperacao;
    private String codigoDependente;
    private String codigoAdesaoTitular;
    private String codigoAdesaoDependente;
    private String cpfDependente;

}
