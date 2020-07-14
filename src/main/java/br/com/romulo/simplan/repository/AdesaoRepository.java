package br.com.romulo.simplan.repository;

import br.com.romulo.simplan.domain.Adesao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdesaoRepository extends JpaRepository<Adesao, Long> {

    Optional<Adesao> findByCodigoAdesao(String codigoAdesao);

}