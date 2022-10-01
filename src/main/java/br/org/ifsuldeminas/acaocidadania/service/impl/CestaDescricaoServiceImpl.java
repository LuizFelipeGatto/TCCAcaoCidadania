package br.org.ifsuldeminas.acaocidadania.service.impl;

import br.org.ifsuldeminas.acaocidadania.domain.CestaDescricao;
import br.org.ifsuldeminas.acaocidadania.repository.CestaDescricaoRepository;
import br.org.ifsuldeminas.acaocidadania.service.CestaDescricaoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CestaDescricao}.
 */
@Service
@Transactional
public class CestaDescricaoServiceImpl implements CestaDescricaoService {

    private final Logger log = LoggerFactory.getLogger(CestaDescricaoServiceImpl.class);

    private final CestaDescricaoRepository cestaDescricaoRepository;

    public CestaDescricaoServiceImpl(CestaDescricaoRepository cestaDescricaoRepository) {
        this.cestaDescricaoRepository = cestaDescricaoRepository;
    }

    @Override
    public CestaDescricao save(CestaDescricao cestaDescricao) {
        log.debug("Request to save CestaDescricao : {}", cestaDescricao);
        return cestaDescricaoRepository.save(cestaDescricao);
    }

    @Override
    public CestaDescricao update(CestaDescricao cestaDescricao) {
        log.debug("Request to update CestaDescricao : {}", cestaDescricao);
        return cestaDescricaoRepository.save(cestaDescricao);
    }

    @Override
    public Optional<CestaDescricao> partialUpdate(CestaDescricao cestaDescricao) {
        log.debug("Request to partially update CestaDescricao : {}", cestaDescricao);

        return cestaDescricaoRepository
            .findById(cestaDescricao.getId())
            .map(existingCestaDescricao -> {
                if (cestaDescricao.getDescricao() != null) {
                    existingCestaDescricao.setDescricao(cestaDescricao.getDescricao());
                }

                return existingCestaDescricao;
            })
            .map(cestaDescricaoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CestaDescricao> findAll(Pageable pageable) {
        log.debug("Request to get all CestaDescricaos");
        return cestaDescricaoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CestaDescricao> findOne(Long id) {
        log.debug("Request to get CestaDescricao : {}", id);
        return cestaDescricaoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CestaDescricao : {}", id);
        cestaDescricaoRepository.deleteById(id);
    }
}
