package br.com.romulo.simplan.service.adesao;

import br.com.romulo.simplan.domain.Adesao;
import br.com.romulo.simplan.repository.AdesaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AdesaoServiceImpl implements AdesaoService {

    @Autowired AdesaoRepository adesaoRepository;

    @Override
    public Adesao buscarAdesaoPorCodigoAdesao(String codigoAdesao) {
        Optional<Adesao> adesaoOptional = adesaoRepository.findByCodigoAdesao(codigoAdesao);
        if (adesaoOptional.isPresent()) return adesaoOptional.get();
        else log.error("FALHA NA BUSCA DA ADESAO DE CODIGO {}", codigoAdesao);
        return new Adesao();
    }

}