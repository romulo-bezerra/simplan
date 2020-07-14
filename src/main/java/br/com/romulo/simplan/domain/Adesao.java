package br.com.romulo.simplan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "T_ADESAO")
@GenericGenerator(
        name = "ADESAO_SEQUENCIA",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
                @Parameter(name = "sequence_name", value = "S_ADESAO"),
                @Parameter(name = "increment_size", value = "1")
        }
)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Adesao implements Cloneable {

    @Id
    @GeneratedValue(generator = "ADESAO_SEQUENCIA")
    @JsonIgnore
    private Long id;

    private String codigoEmissor;
    private String codigoSeguro;
    private String codigoPortador;
    private String cpfPortador;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date cadastro;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancelamento;

    private String usuarioAdesao;

    private String meioAdesao;

    private String meioCancelamento;

    private String usuarioCancelamento;

    private String codigoAdesao;

    private String numeroDaSorte;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sorteio;

    private String codigoOperacao;

    private Long codigoCiclo;

    private String codigoLoja;

    private LocalDateTime entradaCreliq;
    @Column(name = "ENVIADO_CRELIQ", columnDefinition = "BOOLEAN")
    private Boolean enviadoCreliq;

    @Column(name = "NSU_VENDA")
    private Long nsuVenda;

    @Column(name = "LOJA_CANCELAMENTO")
    private String lojaCancelamento;

    @Transient
    private List<AdesaoDependente> adesoesDependentes;

    @JsonIgnore
    private String comentario;

}
