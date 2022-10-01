package br.org.ifsuldeminas.acaocidadania.service.impl;

import br.org.ifsuldeminas.acaocidadania.domain.Unidade;
import br.org.ifsuldeminas.acaocidadania.repository.UnidadeRepository;
import br.org.ifsuldeminas.acaocidadania.service.UnidadeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Unidade}.
 */
@Service
@Transactional
public class UnidadeServiceImpl implements UnidadeService {

    private final Logger log = LoggerFactory.getLogger(UnidadeServiceImpl.class);

    private final UnidadeRepository unidadeRepository;

    public UnidadeServiceImpl(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    @Override
    public Unidade save(Unidade unidade) {
        log.debug("Request to save Unidade : {}", unidade);
        return unidadeRepository.save(unidade);
    }

    @Override
    public Unidade update(Unidade unidade) {
        log.debug("Request to update Unidade : {}", unidade);
        return unidadeRepository.save(unidade);
    }

    @Override
    public Optional<Unidade> partialUpdate(Unidade unidade) {
        log.debug("Request to partially update Unidade : {}", unidade);

        return unidadeRepository
            .findById(unidade.getId())
            .map(existingUnidade -> {
                if (unidade.getNome() != null) {
                    existingUnidade.setNome(unidade.getNome());
                }
                if (unidade.getCep() != null) {
                    existingUnidade.setCep(unidade.getCep());
                }
                if (unidade.getCidade() != null) {
                    existingUnidade.setCidade(unidade.getCidade());
                }
                if (unidade.getLogradouro() != null) {
                    existingUnidade.setLogradouro(unidade.getLogradouro());
                }
                if (unidade.getCodigoIbge() != null) {
                    existingUnidade.setCodigoIbge(unidade.getCodigoIbge());
                }

                return existingUnidade;
            })
            .map(unidadeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Unidade> findAll(Pageable pageable) {
        log.debug("Request to get all Unidades");
        return unidadeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Unidade> findOne(Long id) {
        log.debug("Request to get Unidade : {}", id);
        return unidadeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Unidade : {}", id);
        unidadeRepository.deleteById(id);
    }
}
