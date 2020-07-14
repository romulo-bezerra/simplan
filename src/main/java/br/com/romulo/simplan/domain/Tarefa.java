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
@Table(name = "T_TAREFA")
@GenericGenerator(
        name = "TAREFA_SEQUENCIA",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
                @Parameter(name = "sequence_name", value = "S_TAREFA"),
                @Parameter(name = "increment_size", value = "1")
        }
)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Tarefa implements Cloneable {

    @Id
    @GeneratedValue(generator = "TAREFA_SEQUENCIA")
    @JsonIgnore
    private Long id;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date cadastro;

}
